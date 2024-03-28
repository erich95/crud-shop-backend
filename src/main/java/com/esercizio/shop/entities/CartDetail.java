package com.esercizio.shop.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToMany
    @JsonBackReference
    private List<Cart> carts;
    @ManyToOne
    @JsonManagedReference
    private Product products;

    private int productQuantity;

    private double totalPrice;


}
