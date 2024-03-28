package com.esercizio.shop.repositories;

import com.esercizio.shop.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {

    Optional<CartDetail> findByCarts_User_Id(int idUser);

    Optional<List<CartDetail>> findAllByCarts_User_Id(int idUser);

    Optional<CartDetail> findByProducts_Id(int idProduct);



}
