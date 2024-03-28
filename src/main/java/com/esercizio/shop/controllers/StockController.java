package com.esercizio.shop.controllers;

import com.esercizio.shop.entities.Stock;
import com.esercizio.shop.repositories.ProductRepository;
import com.esercizio.shop.repositories.StockRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/stock")
@CrossOrigin(origins = "http://localhost:4200")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/getAllStock")
    public ResponseEntity<Map<String,List<Stock>>> getAllStock() {
        Map<String,List<Stock>> response = new HashMap<>();
        List<Stock> stockList = stockRepository.findAll();
        response.put("stocks", stockList);
        return ResponseEntity.ok(response);
    }
}
