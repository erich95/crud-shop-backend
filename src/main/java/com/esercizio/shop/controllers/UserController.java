package com.esercizio.shop.controllers;

import com.esercizio.shop.entities.User;
import com.esercizio.shop.entities.dto.RequestClass;
import com.esercizio.shop.entities.dto.Roles;
import com.esercizio.shop.repositories.ProductRepository;
import com.esercizio.shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value ="/login")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping(value = "/login")
    public User doLogin(@RequestParam String email, String password) {
        Optional<User> userOptional = userRepository.findByEmailAndPassword(email, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return  user;
        }
        return null;
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> doSignUp(@RequestParam String email, String password, String name, String surname) {
        Map<String, String> response = new HashMap<>();
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setSurname(surname);
            user.setPassword(password);
            /*user.setRole(Roles.ADMIN);*/
            userRepository.save(user);
            response.put("message", "Sign Up completed!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("message", "The user having email: " + userOptional.get().getEmail() + " is already present in our system.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value="/userSetting")
    public RequestClass userSetting(@RequestBody User user){
        RequestClass requestClass = new RequestClass();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            User userToBeModified = userOptional.get();
            userToBeModified.setName(user.getName());
            userToBeModified.setSurname(user.getSurname());
            userToBeModified.setRole(user.getRole());
            userToBeModified.setEmail(user.getEmail());
            userToBeModified = userRepository.save(userToBeModified);
            requestClass.setUser(userToBeModified);
            requestClass.setStatusCode(200);
            requestClass.setMessage("The user's info has been updated!");
            return requestClass;
        }
        requestClass.setMessage("No user found.");
        requestClass.setStatusCode(500);
        return requestClass;
    }

}
