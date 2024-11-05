package com.example.pravo.services;

import com.example.pravo.dto.*;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Reward;
import com.example.pravo.models.User;
import com.example.pravo.repository.AuthRepository;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverterImpl;
import com.turkraft.springfilter.parser.node.FilterNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private FilterBuilder fb;
    @Autowired
    private MapStructMapper mapper;
    @Autowired
    private FilterSpecificationConverterImpl filterService;

    private static Specification<User> allUsersSearch(String search, String userId) {
        return (root, cq, cb) -> cb.and(
                cb.or(cb.like(root.get("id"), "%" + search.toUpperCase() + "%"), cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%")),
                cb.notEqual(root.get("id"), userId)
                );
    }

    private Specification<User> specificationConverter(FilterNode filterNode) {
        return filterService.convert(filterNode);
    }

    private User findUser(String userId) {
        User user = authRepository.findById(userId).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with provided ID!");

        return user;
    }

    public UserDto login(LoginDto credentials) {
        FilterNode emailFilterNode = fb.field("email").equal(fb.input(credentials.getEmail().toLowerCase())).get();
        FilterNode authSpec = fb.field("email").equal(fb.input(credentials.getEmail().toLowerCase())).and(fb.field("password").equal(fb.input(credentials.getPassword()))).get();
        //search if there is corresponding email
        List<User> userExist = authRepository.findAll(specificationConverter(emailFilterNode));

        if (userExist.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is not found registered! Please contact your Admin for registration or check the spelling!");
        }

        //find and return if the email and password match, else throw error
        User auth = authRepository.findOne(specificationConverter(authSpec)).orElse(null);
        if (auth == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are not match!");
        }

        //check if deactivated
        if(auth.isActive() == false){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your account is deactivated. Kindly contact your administrator for more details.");
        }

        return mapper.toUserDto(auth);
    }

    public UserDto getUser(String userId) {
        return mapper.toUserDto(findUser(userId));
    }

    public List<UserDto> getAllUsers(String search, String userId) {
        List<User> allUsers = authRepository.findAll(allUsersSearch(search, userId));

        return allUsers.stream().map(user -> mapper.toUserDto(user)).toList();
    }

    public Page<User> getUsers(Pageable pageable, String userId) {
        return authRepository.findByTypeAndIdNot("user", userId, pageable);
    }

    public Page<User> getAdmins(Pageable pageable, String userId) {
        return authRepository.findByTypeAndIdNot("admin", userId, pageable);
    }

    public UserDto postUser(UserEntryDto user) {
        //duplicate user check with ID
        User duplicateId = authRepository.findById(user.getId().toUpperCase()).orElse(null);
        if (duplicateId != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this ID already existed!");

        //duplicate user check with email
        FilterNode duplicateEmailFilterNode = fb.field("email").equal(fb.input(user.getEmail().toLowerCase())).get();
        User duplicateUserEmail = authRepository.findOne(specificationConverter(duplicateEmailFilterNode)).orElse(null);
        if (duplicateUserEmail != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already existed!");

        User newUser = new User();
        newUser.setId(user.getId().toUpperCase());
        newUser.setEmail(user.getEmail().toLowerCase());
        newUser.setPassword(user.getPassword().trim());
        newUser.setName(user.getName().trim());
        newUser.setType(user.getType());
        newUser.setPosition(user.getPosition().trim());
        newUser.setDepartment(user.getDepartment().trim());
        newUser.setContact(user.getContact());
        newUser.setAddress(user.getAddress().trim());
        newUser.setActive(true);

        return mapper.toUserDto(authRepository.save(newUser));
    }

    public UserDto putUser(UserUpdateDto user, String userId) {
        User updateUser = findUser(userId);

        updateUser.setName(user.getName().trim());
        updateUser.setPosition(user.getPosition());
        updateUser.setDepartment(user.getDepartment());
        updateUser.setContact(user.getContact());
        updateUser.setAddress(user.getAddress().trim());
        updateUser.setType(user.getType());

        return mapper.toUserDto(authRepository.save(updateUser));
    }

    public UserDto updatePassword(PasswordUpdateDto updatePassword, String userId) {
        User updateUser = findUser(userId);

        if (!Objects.equals(updateUser.getPassword(), updatePassword.getOldPassword().trim()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password provided are not the same!");
        updateUser.setPassword(updatePassword.getNewPassword().trim());

        return mapper.toUserDto(authRepository.save(updateUser));
    }

    public UserDto updatePoints(Integer points, String userId) {
        User updateUser = findUser(userId);

        updateUser.setPoints(points);

        return mapper.toUserDto(authRepository.save(updateUser));
    }

    public UserDto reactivateUser(String userId) {
        User user = authRepository.findById(userId).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");
        else {
            user.setActive(true);
            return mapper.toUserDto(authRepository.save(user));

        }
    }

    public boolean deleteUser(String userId) {
        User user = authRepository.findById(userId).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");
        else {
            user.setActive(false);
            authRepository.save(user);

            return true;
        }

    }
}
