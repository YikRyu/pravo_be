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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

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

    private User getUser(String userId){
        User user = authRepository.findById(userId).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");

        return user;
    }

    public Page<Recognition> getRecognitions(String userId, Pageable pageable){
        FilterNode filterNode = fb.field("user_id").equal(fb.input(userId)).get();
        return recognitionRepository.findAll(specificationConverter(filterNode), pageable);
    }

    public Recognition newRecognition(RecognitionEntryDto recognition){
        String referee = recognition.getReferee();
        String peer = recognition.getPeer();

        Recognition newRecognition = new Recognition();
        newRecognition.setTitle(recognition.getTitle().trim());
        newRecognition.setDescription(recognition.getDescription().trim());
        newRecognition.setType(recognition.getType());
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
