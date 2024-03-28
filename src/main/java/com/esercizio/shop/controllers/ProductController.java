package com.esercizio.shop.controllers;

import com.esercizio.shop.entities.Category;
import com.esercizio.shop.entities.Product;
import com.esercizio.shop.entities.SubCategory;
import com.esercizio.shop.entities.dto.ProductDTO;
import com.esercizio.shop.repositories.CategoryRepository;
import com.esercizio.shop.repositories.ProductRepository;
import com.esercizio.shop.repositories.SubCategoryRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.swing.text.html.Option;
import java.util.*;

@RestController
@RequestMapping(value = "/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping(value = "/all")
    public Optional<List<Product>> getProducts() {
        return Optional.of(productRepository.findAll());
    }

    @GetMapping(value = "/search")
    public List<Product> getProductByName(@RequestParam String text) {
        return productRepository.findAllByNameContaining(text);
    }

    @GetMapping(value = "/findById", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProductById(@RequestParam int idProduct) {
        Optional<Product> optionalProduct = productRepository.findById(idProduct);
        if (optionalProduct.isPresent()) {
            Product prodotto = optionalProduct.get();
            return prodotto;
        }
        return null;
    }


    @GetMapping(value = "/byPriceRange")
    public List<Product> getProductsByPriceRange(@RequestParam int prezzoMin, int prezzoMax) {
        return productRepository.findAllByPriceBetween(prezzoMin, prezzoMax);
    }

    @GetMapping(value = "/byBrandName")
    public List<Product> getProductsByPriceRange(@RequestParam String brandName) {
        return productRepository.findAllByBrandName(brandName);
    }

    @GetMapping(value = "/byCategory")
    public List<Product> getProductsByCategory(@RequestParam String categoryName){
        Category category = categoryRepository.findByName(categoryName);
        return productRepository.findAllBySubCategory_Category(category);
    }

    @GetMapping(value ="/bySubCategory")
    public List<Product> getProductsBySubCategory(@RequestParam String subCategoryName){
        return productRepository.findAllBySubCategory_Name(subCategoryName);
    }

    @GetMapping(value = "/byPriceRangeAndSubcategory")
    public List<Product> getProductsByPriceRangeAndSubcategory(@RequestParam int prezzoMin, int prezzoMax,
                                                               String subCategoryName) {
        SubCategory subCategory = subCategoryRepository.findByName(subCategoryName);
        return productRepository.findAllByPriceBetweenAndSubCategory(prezzoMin, prezzoMax, subCategory);
    }

    @PostMapping(value = "/addProduct")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping(value ="/modifyProduct")
    public ResponseEntity<Map<String, String>> modifyProduct(@RequestBody ProductDTO productDTO, @RequestParam int idProduct) {
        Map<String, String> response = new HashMap<>();
        Optional<SubCategory> categoryOptional = subCategoryRepository.findById(productDTO.getSubCategoryId());
        if (categoryOptional.isPresent()) {
            SubCategory subCategory = categoryOptional.get();
            Product product = new Product();
            product.setId(idProduct);
            product.setName(productDTO.getName());
            product.setBrandName(productDTO.getBrandName());
            product.setDescription(productDTO.getDescription());
            product.setImgUrl(productDTO.getImgUrl());
            product.setPrice(productDTO.getPrice());
            product.setSubCategory(subCategory);
            productRepository.save(product);
            response.put("message", "The product has been modified correctly!");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Error occured when trying to update this product.");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/addProducts")
    public ResponseEntity<Map<String, String>> addProducts(@RequestBody List<ProductDTO> productDTOList) {
        List<Product> productList = new ArrayList<>();
        Map<String, String> response = new HashMap<>();
        for (ProductDTO productDTO : productDTOList) {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setBrandName(productDTO.getBrandName());
            product.setPrice(productDTO.getPrice());
            product.setImgUrl(productDTO.getImgUrl());

            Optional<SubCategory> subCategoryOptional= subCategoryRepository.findById(productDTO.getSubCategoryId());
            if (subCategoryOptional.isPresent()) {
                SubCategory subCategory = subCategoryOptional.get();
                product.setSubCategory(subCategory);
                productList.add(product);
            }
        }
        if (productList.size() > 0) {
            productRepository.saveAll(productList);
            response.put("message", "I prodotti sono stati salvati correttamente!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("message", "Non sono presenti prodotti da salvare!");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteProductById(@RequestParam Integer idProduct) {
        Map<String, String> response = new HashMap<>();
        productRepository.deleteById(idProduct);
        if (productRepository.existsById(idProduct)) {
            response.put("message", "The product hasn't been deleted!");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        response.put("message", "The product has been deleted!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
