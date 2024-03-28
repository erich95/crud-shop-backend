package com.esercizio.shop.controllers;

import com.esercizio.shop.entities.User;
import com.esercizio.shop.entities.dto.RequestClass;
import com.esercizio.shop.repositories.UserRepository;
import com.esercizio.shop.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signUp")
    public ResponseEntity<RequestClass> signUp(@RequestBody RequestClass signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/signIn")
    public ResponseEntity<RequestClass> signIn(@RequestBody RequestClass signInRequest) {
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<RequestClass> refreshToken(@RequestBody RequestClass refreshTokenRequest) {
        return ResponseEntity.ok(authService.signIn(refreshTokenRequest));
    }

    @PostMapping(value="/userSetting")
    public RequestClass userSetting(@RequestBody RequestClass req){
        RequestClass requestClass = new RequestClass();
        Optional<User> userOptional = userRepository.findById(req.getUser().getId());
        if (userOptional.isPresent()) {
            User userToBeModified = userOptional.get();
            userToBeModified.setName(req.getUser().getName());
            userToBeModified.setSurname(req.getUser().getSurname());
            userToBeModified.setRole(req.getUser().getRole());
            userToBeModified.setEmail(req.getUser().getEmail());
            if (req.getNewPassword() != null && !req.getNewPassword().isEmpty()) {
                try {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUser().getEmail(), req.getPassword()));
                    userToBeModified.setPassword(passwordEncoder.encode(req.getNewPassword()));
                } catch (Exception e) {

                }
            }
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

    @GetMapping(value="/getUserById")
    public User getUserById(@RequestParam int idUser) {
        Optional<User> userOpt = userRepository.findById(idUser);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user;
        }
        return null;
    }

}
