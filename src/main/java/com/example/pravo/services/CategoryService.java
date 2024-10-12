package com.example.pravo.services;

import com.example.pravo.dto.CategoryEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Category;
import com.example.pravo.models.Reward;
import com.example.pravo.models.User;
import com.example.pravo.repository.AuthRepository;
import com.example.pravo.repository.CategoryRepository;
import com.example.pravo.repository.RewardRepository;
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
import java.util.List;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private FilterBuilder fb;
    @Autowired
    private MapStructMapper mapper;
    @Autowired
    private FilterSpecificationConverterImpl filterService;

    private Specification<Category> specificationConverter(FilterNode filterNode){
        return filterService.convert(filterNode);
    }

    private Specification<Reward> rewardSpecificationConverter(FilterNode filterNode){
        return filterService.convert(filterNode);
    }

    private User getUser(String userId){
        User user = authRepository.findById(userId).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");

        return user;
    }

    public Page<Category> getCategories(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }

    public Category postCategory (CategoryEntryDto category) {
        FilterNode duplicateCategoryFilterNode = fb.field("name").equal(fb.input(category.getName().trim())).get();
        Category duplicateCategory = categoryRepository.findOne(specificationConverter(duplicateCategoryFilterNode)).orElse(null);
        if (duplicateCategory != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already existed!");

        Category newCategory = new Category();
        newCategory.setName(category.getName().trim());
        newCategory.setCreatedBy(getUser(category.getCreatedBy()));
        newCategory.setCreatedDate(LocalDateTime.now());

        return categoryRepository.save(newCategory);
    }

    public Category putCategory (CategoryEntryDto category, Long categoryId) {
        FilterNode duplicateCategoryFilterNode = fb.field("name").equal(fb.input(category.getName().trim())).get();
        Category duplicateCategory = categoryRepository.findOne(specificationConverter(duplicateCategoryFilterNode)).orElse(null);
        if (duplicateCategory != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already existed!");

        Category newCategory = new Category();

        newCategory.setName(category.getName().trim());
        newCategory.setModifiedBy(getUser(category.getModifiedBy()));
        newCategory.setModifiedDate(LocalDateTime.now());

        return categoryRepository.save(newCategory);
    }

    public boolean deleteCategory (Long categoryId) {
        FilterNode categoryFilterNode = fb.field("id").equal(fb.input(categoryId)).get();
        Category category = categoryRepository.findOne(specificationConverter(categoryFilterNode)).orElse(null);
        if (category == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category with such ID does not exist!");

        FilterNode inUseCategoryFilterNode = fb.field("category").equal(fb.input(categoryId)).get();
        List<Reward> categoryInUse = rewardRepository.findAll(rewardSpecificationConverter(inUseCategoryFilterNode));
        if(!categoryInUse.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category is in use!");
        else {
            categoryRepository.delete(category);
            return true;
        }
    }
}
