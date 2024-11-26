package com.example.pravo.controller;

import com.example.pravo.dto.CategoryDto;
import com.example.pravo.dto.CategoryEntryDto;
import com.example.pravo.dto.CreatedModifiedByDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Category;
import com.example.pravo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    private CreatedModifiedByDto mapCreatedModifiedBy(Category category, String type) {
        if (type.equals("createdBy")) return mapper.toCreatedModifiedByDto(category.getCreatedBy());
        else return mapper.toCreatedModifiedByDto(category.getModifiedBy());
    }

    private CategoryDto mapCategory(Category category) {
        CategoryDto mappedCategory = new CategoryDto();

        mappedCategory.setId(category.getId());
        mappedCategory.setName(category.getName());
        mappedCategory.setCreatedBy(mapCreatedModifiedBy(category, "createdBy"));
        if (category.getModifiedBy() != null) {
            mappedCategory.setModifiedBy(mapCreatedModifiedBy(category, "modifiedBy"));
            mappedCategory.setModifiedDate(category.getModifiedDate());
        } else {
            mappedCategory.setModifiedBy(null);
            mappedCategory.setModifiedDate(null);
        }

        return mappedCategory;
    }

    @GetMapping(path = "/category")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping(path = "/category/pageable")
    public ResponseEntity<Map<String, Object>> getCategoriesPageable(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryService.getCategoriesPageable(pageable);

        List<Category> fetchedData = categories.getContent();
        List<CategoryDto> data = new ArrayList<CategoryDto>();
        long numberOfElements = categories.getTotalElements();

        //convert total number of items as int if it doesnt reach long amount worth of amount...
        if (numberOfElements < Integer.MAX_VALUE) {
            totalItems = (int) numberOfElements;
            response.put("totalItems", totalItems);
        } else {
            response.put("totalItems", numberOfElements);
        }

        if (!fetchedData.isEmpty()) {
            for (Category category : fetchedData) {
                data.add(mapCategory(category));
            }
        }
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/category")
    public CategoryDto postCategory(
            @RequestBody CategoryEntryDto category
    ) {
        return mapCategory(categoryService.postCategory(category));
    }

    @PutMapping(path = "/category/{categoryId}")
    public CategoryDto putCategory(
            @RequestBody CategoryEntryDto category, @PathVariable(value = "categoryId") Long categoryId
    ) {
        return mapCategory(categoryService.putCategory(category, categoryId));
    }

    @DeleteMapping(path = "/category/{categoryId}")
    public boolean deleteCategory(
            @PathVariable(value = "categoryId") Long categoryId
    ) {
        return categoryService.deleteCategory(categoryId);
    }
}
