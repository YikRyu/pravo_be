package com.example.pravo.controller;

import com.example.pravo.dto.UserDto;
import com.example.pravo.dto.LoginDto;
import com.example.pravo.dto.UserEntryDto;
import com.example.pravo.dto.UserUpdateDto;
import com.example.pravo.dto.PasswordUpdateDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.User;
import com.example.pravo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private MapStructMapper mapper;

    @PostMapping(path = "/auth")
    public UserDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    //for searching for peer and referee
    @GetMapping(path = "/auth/allUsers")
    public List<UserDto> getAllUsers(
            @RequestParam(value = "search") String search,
            @RequestParam(value = "userId") String userId
    ){
        return authService.getAllUsers(search, userId);
    }

    @GetMapping(path = "/auth/user/{userId}")
    public UserDto getUser(@PathVariable(value = "userId") String userId) {
        return authService.getUser(userId);
    }

    @GetMapping(path = "/auth/users")
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "userId") String userId
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = authService.getUsers(pageable, userId);

        List<User> data = users.getContent();
        long numberOfElements = users.getTotalElements();

        //convert total number of items as int if it doesnt reach long amount worth of amount...
        if (numberOfElements < Integer.MAX_VALUE) {
            totalItems = (int) numberOfElements;
            response.put("totalItems", totalItems);
        } else {
            response.put("totalItems", numberOfElements);
        }
        //reformat data
        List<UserDto> userData = data.stream().map(user -> mapper.toUserDto(user)).toList();
        response.put("data", userData);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/auth/admins")
    public ResponseEntity<Map<String, Object>> getAdmins(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "userId") String userId
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = authService.getAdmins(pageable, userId);

        List<User> data = users.getContent();
        long numberOfElements = users.getTotalElements();

        //convert total number of items as int if it doesnt reach long amount worth of amount...
        if (numberOfElements < Integer.MAX_VALUE) {
            totalItems = (int) numberOfElements;
            response.put("totalItems", totalItems);
        } else {
            response.put("totalItems", numberOfElements);
        }
        //reformat data
        List<UserDto> userData = data.stream().map(user -> mapper.toUserDto(user)).toList();
        response.put("data", userData);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/auth/newUser")
    public UserDto postUser(@RequestBody UserEntryDto newUser) {
        return authService.postUser(newUser);
    }

    @PutMapping(path = "/auth/updateUser/{userId}")
    public UserDto putUser(@RequestBody UserUpdateDto updateUser, @PathVariable(value = "userId") String userId) {
        return authService.putUser(updateUser, userId);
    }

    @PutMapping(path = "/auth/updatePassword/{userId}")
    public UserDto updatePassword(@RequestBody PasswordUpdateDto newPassword, @PathVariable(value = "userId") String userId) {
        return authService.updatePassword(newPassword, userId);
    }

    @PutMapping(path = "/auth/updatePoints/{userId}")
    public UserDto updatePoints(@RequestParam(value = "points") int points, @PathVariable(value = "userId") String userId) {
        return authService.updatePoints(points, userId);
    }

    @PutMapping(path = "/auth/reactivateUser/{userId}")
    public UserDto reactivateUser(@PathVariable(value = "userId") String userId){
        return authService.reactivateUser(userId);
    }

    @DeleteMapping(path = "/auth/{userId}")
    public boolean deleteUser(@PathVariable(value = "userId") String userId){
        return authService.deleteUser(userId);
    }

}
