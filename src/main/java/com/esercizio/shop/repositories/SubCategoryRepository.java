package com.esercizio.shop.repositories;

import com.esercizio.shop.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {

    SubCategory findByName(String subCategory);
}
