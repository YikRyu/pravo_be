package com.example.pravo.controller;

import com.example.pravo.dto.CategoryEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Category;
import com.example.pravo.services.CategoryService;
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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MapStructMapper mapper;

    @GetMapping(path = "/category")
    public ResponseEntity<Map<String, Object>> getCategories(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryService.getCategories(pageable);

        List<Category> data = categories.getContent();
        long numberOfElements = categories.getTotalElements();

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

    @PostMapping(path = "/category")
    public Category postCategory(
            @RequestBody CategoryEntryDto category
            ){
        return categoryService.postCategory(category);
    }

    @PutMapping(path = "/category/{categoryId}")
    public Category putCategory(
            @RequestBody CategoryEntryDto category, @PathVariable(value = "categoryId") Long categoryId
    ){
        return categoryService.putCategory(category, categoryId);
    }

    @DeleteMapping(path = "/category/{categoryId}")
    public boolean deleteCategory(
            @PathVariable(value = "categoryId") Long categoryId
    ){
        return categoryService.deleteCategory(categoryId);
    }
}
