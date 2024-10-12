package com.example.pravo.controller;

import com.example.pravo.dto.PointTransactionEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.PointsTransaction;
import com.example.pravo.services.PointsTransactionServices;
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
public class PointsTransactionController {
    @Autowired
    private PointsTransactionServices pointsTransactionServices;
    @Autowired
    private MapStructMapper mapper;

    @GetMapping(path = "/recognitionTransactions/{userId}")
    public ResponseEntity<Map<String, Object>> getTransactions(
            @PathVariable(value = "userId") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<PointsTransaction> recognitionTransactions = pointsTransactionServices.getRecognitionTransactions(userId, pageable);

        List<PointsTransaction> data = recognitionTransactions.getContent();
        long numberOfElements = recognitionTransactions.getTotalElements();

        //convert total number of items as int if it doesnt reach long amount worth of amount...
        if (numberOfElements < Integer.MAX_VALUE) {
            totalItems = (int) numberOfElements;
            response.put("totalItems", totalItems);
        } else {
            response.put("totalItems", numberOfElements);
        }

        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/recognitionTransactions")
    public PointsTransaction postTransaction(
            @RequestBody PointTransactionEntryDto transaction
    ) {
        return pointsTransactionServices.postRecognitionTransaction(transaction);
    }
}
