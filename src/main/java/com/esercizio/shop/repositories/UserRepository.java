package com.esercizio.shop.repositories;

import com.esercizio.shop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);


    Optional<User> findByEmail(String email);
}
