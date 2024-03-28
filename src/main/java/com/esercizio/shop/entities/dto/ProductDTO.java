package com.esercizio.shop.entities.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    String name;
    String brandName;
    String description;
    double price;
    String imgUrl;
    int subCategoryId;
}
