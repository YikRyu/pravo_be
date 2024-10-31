package com.example.pravo.services;

import com.example.pravo.dto.RecognitionEntryDto;
import com.example.pravo.dto.RecognitionUpdateEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Recognition;
import com.example.pravo.models.User;
import com.example.pravo.repository.AuthRepository;
import com.example.pravo.repository.RecognitionRepository;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverterImpl;
import com.turkraft.springfilter.parser.node.FilterNode;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RecognitionService {
    @Autowired
    private RecognitionRepository recognitionRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private FilterBuilder fb;
    @Autowired
    private MapStructMapper mapper;
    @Autowired
    private FilterSpecificationConverterImpl filterService;

    private Specification<Recognition> specificationConverter(FilterNode filterNode){
        return filterService.convert(filterNode);
    }

    private static Specification<Recognition> createdBy( String userId) {
        return (root, cq, cb) -> {
            Join<Recognition, User> createdBy =root.join("createdBy");
            return cb.equal(createdBy.get("id"), userId);
        };
    }

    private static Specification<Recognition> getReferee( String userId) {
        return (root, cq, cb) -> {
            Join<Recognition, User> referee =root.join("referee");
            return cb.equal(referee.get("id"), userId);
        };
    }

    private static Specification<Recognition> getPeer( String userId) {
        return (root, cq, cb) -> {
            Join<Recognition, User> peer =root.join("peer");
            return cb.equal(peer.get("id"), userId);
        };
    }

    private User getUser(String userId){
        User user = authRepository.findById(userId).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");

        return user;
    }

    public Page<Recognition> getRecognitions(Pageable pageable){
        return recognitionRepository.findAll(pageable);
    }

    public List<Recognition> getRecognitionForTransaction(List<Long> recognitionIds) { return recognitionRepository.findByIdIn(recognitionIds);}

    public Page<Recognition> getMyRecognitions(String userId, Pageable pageable){
        return recognitionRepository.findAll(createdBy(userId), pageable);
    }

    public Page<Recognition> getMyApprovalRecognitions(String userId, Pageable pageable){
        return recognitionRepository.findAll(getReferee(userId), pageable);
    }

    public Page<Recognition> getPeerRecognitions(String userId, Pageable pageable){
        return recognitionRepository.findAll(getPeer(userId), pageable);
    }

    public Recognition newRecognition(RecognitionEntryDto recognition){
        String referee = recognition.getReferee();
        String peer = recognition.getPeer();

        Recognition newRecognition = new Recognition();
        newRecognition.setTitle(recognition.getTitle().trim());
        newRecognition.setDescription(recognition.getDescription().trim());
        newRecognition.setType(recognition.getType());
        newRecognition.setPoints(recognition.getPoints());
        newRecognition.setStatus(recognition.getStatus());
        newRecognition.setCreatedBy(getUser(recognition.getCreatedBy()));
        newRecognition.setCreatedDate(LocalDateTime.now());

        if (referee != null) newRecognition.setReferee(getUser(referee));
        if (peer != null) newRecognition.setPeer(getUser(peer));

        return recognitionRepository.save(newRecognition);
    }

    public Recognition updateRecognition(RecognitionUpdateEntryDto recognition, Long id){
        Integer points = recognition.getPoints();
        String status= recognition.getStatus();
        Boolean refereeApproval = recognition.getRefereeApproval();
        String admin = recognition.getAdmin();
        Boolean adminApproval = recognition.getAdminApproval();

        Recognition updateRecognition = recognitionRepository.findById(id).orElse(null);
        if (updateRecognition == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recognition is not found!");

        if (points != null) updateRecognition.setPoints(points);
        if (status != null) updateRecognition.setStatus(status);
        if (refereeApproval != null) updateRecognition.setRefereeApproval(refereeApproval);
        if (admin != null) updateRecognition.setAdmin(getUser(admin));
        if (adminApproval != null) updateRecognition.setAdminApproval(adminApproval);
        updateRecognition.setModifiedBy(getUser(recognition.getModifiedBy()));
        updateRecognition.setModifiedDate(LocalDateTime.now());

        return recognitionRepository.save(updateRecognition);
    }
}
