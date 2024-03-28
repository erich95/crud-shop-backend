package com.esercizio.shop.repositories;

import com.esercizio.shop.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser_Id(Integer idUser);

    Optional<Cart> findByUser_IdAndCartDetail_Products_Id(int idUser, int idProduct);

}
