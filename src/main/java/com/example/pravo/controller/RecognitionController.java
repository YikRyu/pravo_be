package com.example.pravo.controller;

import com.example.pravo.dto.RecognitionEntryDto;
import com.example.pravo.dto.RecognitionUpdateEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Recognition;
import com.example.pravo.services.RecognitionService;
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
public class RecognitionController {
    @Autowired
    private RecognitionService recognitionService;
    @Autowired
    private MapStructMapper mapper;

    @GetMapping(path = "/recognitions")
    public ResponseEntity<Map<String, Object>> getRecognitions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Recognition> recognitions = recognitionService.getRecognitions(pageable);

        List<Recognition> data = recognitions.getContent();
        long numberOfElements = recognitions.getTotalElements();

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

    @GetMapping(path = "/recognitions/transactions")
    public List<Recognition> getRecognitionsForTransaction(
            @RequestParam(value = "recognitionId") List<Long> recognitionIds
    ) {
        return recognitionService.getRecognitionForTransaction(recognitionIds);
    }

    @GetMapping(path = "/recognitions/{userId}")
    public ResponseEntity<Map<String, Object>> getMyRecognitions(
            @PathVariable(value = "userId") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Recognition> recognitions = recognitionService.getMyRecognitions(userId, pageable);

        List<Recognition> data = recognitions.getContent();
        long numberOfElements = recognitions.getTotalElements();

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

    @GetMapping(path = "/recognitions/approval/{userId}")
    public ResponseEntity<Map<String, Object>> getMyApprovalRecognitions(
            @PathVariable(value = "userId") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Recognition> recognitions = recognitionService.getMyApprovalRecognitions(userId, pageable);

        List<Recognition> data = recognitions.getContent();
        long numberOfElements = recognitions.getTotalElements();

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

    @GetMapping(path = "/recognitions/peer/{userId}")
    public ResponseEntity<Map<String, Object>> getPeerRecognitions(
            @PathVariable(value = "userId") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Recognition> recognitions = recognitionService.getPeerRecognitions(userId, pageable);

        List<Recognition> data = recognitions.getContent();
        long numberOfElements = recognitions.getTotalElements();

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

    @PostMapping(path = "/recognitions")
    public Recognition postRecognition(
            @RequestBody RecognitionEntryDto newRecognition
            ){
        return recognitionService.newRecognition(newRecognition);
    }

    @PutMapping(path = "/recognitions/{recognitionId}")
    public Recognition putRecognition(
            @PathVariable(value = "recognitionId") Long recognitionId,
            @RequestBody RecognitionUpdateEntryDto updateRecognition
            ){
        return recognitionService.updateRecognition(updateRecognition, recognitionId);
    }
}
