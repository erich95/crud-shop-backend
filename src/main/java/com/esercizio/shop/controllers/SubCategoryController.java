package com.esercizio.shop.controllers;

import com.esercizio.shop.entities.Category;
import com.esercizio.shop.entities.SubCategory;
import com.esercizio.shop.repositories.CategoryRepository;
import com.esercizio.shop.repositories.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subcategory")
@CrossOrigin(origins = "http://localhost:4200")
public class SubCategoryController {

    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping(value = "/all")
    public Optional<List<SubCategory>> getSubCategories() {
        return Optional.of(subCategoryRepository.findAll());
    }

    @PostMapping(value = "/add")
    public SubCategory addSubCategory(@RequestParam String name, int idCategory) {
        SubCategory existingSubCategory = subCategoryRepository.findByName(name);
        if (existingSubCategory == null) {
            SubCategory subCategoryToSave = new SubCategory();
            Optional<Category> categoryOptional = categoryRepository.findById(idCategory);
            if (categoryOptional.isPresent()) {
                Category category = categoryOptional.get();
                subCategoryToSave.setName(name);
                subCategoryToSave.setCategory(category);
                return subCategoryRepository.save(subCategoryToSave);
            }
        }
        return existingSubCategory;
    }
}
