package com.example.pravo.services;

import com.example.pravo.dto.PointTransactionEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Recognition;
import com.example.pravo.models.PointsTransaction;
import com.example.pravo.models.User;
import com.example.pravo.repository.AuthRepository;
import com.example.pravo.repository.RecognitionRepository;
import com.example.pravo.repository.PointsTransactionRepository;
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
public class PointsTransactionServices {
    @Autowired
    private PointsTransactionRepository pointsTransactionRepository;
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

    private Specification<PointsTransaction> specificationConverter(FilterNode filterNode){
        return filterService.convert(filterNode);
    }

    private User getUser(String userId){
        User user = authRepository.findById(userId).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");

        return user;
    }

    public Page<PointsTransaction> getRecognitionTransactions(String userId, Pageable pageable){
        FilterNode filterNode = fb.field("user_id").equal(fb.input(userId)).get();
        return pointsTransactionRepository.findAll(specificationConverter(filterNode), pageable);
    }

    public PointsTransaction postRecognitionTransaction(PointTransactionEntryDto transaction){
        PointsTransaction pointsTransaction = new PointsTransaction();

        Recognition recognition = recognitionRepository.findById(transaction.getRecognitionId()).orElse(null);
        if(recognition == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recognition does not exist!");
        pointsTransaction.setRecognitionId(recognition);
        pointsTransaction.setUserId(getUser(transaction.getUserId()));
        pointsTransaction.setCreatedDate(LocalDateTime.now());

        return pointsTransactionRepository.save(pointsTransaction);
    }
}
