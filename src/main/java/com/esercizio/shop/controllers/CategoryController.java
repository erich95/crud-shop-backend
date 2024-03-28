package com.esercizio.shop.controllers;

import com.esercizio.shop.entities.Category;
import com.esercizio.shop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/category")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping(value = "/all")
    public Optional<List<Category>> getAllCategories() {
        return Optional.of(categoryRepository.findAll());
    }

    @GetMapping(value = "/{id}")
    public Optional<Category> findCategory(@PathVariable Integer id) {
        return categoryRepository.findById(id);
    }

    @PostMapping("/admin/addCategory")
    public Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }


}
