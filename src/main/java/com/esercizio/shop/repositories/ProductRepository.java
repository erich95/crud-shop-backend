package com.esercizio.shop.repositories;

import com.esercizio.shop.entities.Category;
import com.esercizio.shop.entities.Product;
import com.esercizio.shop.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByNameContaining(String name);

    List<Product> findAllByPriceBetweenAndSubCategory_Name(int prezzoMin, int prezzoMax, String subCategoryName);

    List<Product> findAllByPriceBetween(int prezzoMin, int prezzoMax);

    List<Product> findAllByPriceBetweenAndSubCategory(int prezzoMin, int prezzoMax, SubCategory subCategoryName);

    List<Product> findAllBySubCategory_Category(Category category);

    List<Product> findAllBySubCategory_Name(String subcategoryName);

    List<Product> findAllByBrandName(String brandName);
}
