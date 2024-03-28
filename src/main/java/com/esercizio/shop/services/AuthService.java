package com.esercizio.shop.services;

import com.esercizio.shop.entities.User;
import com.esercizio.shop.entities.dto.RequestClass;
import com.esercizio.shop.entities.dto.Roles;
import com.esercizio.shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public RequestClass signUp(RequestClass registrationRequest) {
        RequestClass resp = new RequestClass();
        try {
            User user = new User();
            user.setName(registrationRequest.getName());
            user.setSurname(registrationRequest.getSurname());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setRole(registrationRequest.getRole());
            if (!userRepository.existsByEmail(registrationRequest.getEmail())) {
                User userResult = userRepository.save(user);
                if (userResult != null && userResult.getId() > 0) {
                    resp.setUser(userResult);
                    resp.setMessage("User saved succesfully!");
                    resp.setStatusCode(200);
                }
            } else {
                resp.setUser(null);
                resp.setMessage("This email is already present in our system!");
                resp.setStatusCode(500);
            }
        } catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public RequestClass signIn(RequestClass signInRequest) {
        RequestClass resp = new RequestClass();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow();
            System.out.println("USER IS: " + user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            resp.setUser(user);
            resp.setRole(user.getRole());
            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24hr");
            resp.setMessage("Succesfully Signed In");
        }catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }
        return resp;
    }

    public RequestClass refreshToken(RequestClass refreshTokenRequest) {
        RequestClass resp = new RequestClass();
        String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                var jwt = jwtUtils.generateToken(user);
                resp.setStatusCode(200);
                resp.setToken(jwt);
                resp.setRefreshToken(refreshTokenRequest.getRefreshToken());
                resp.setExpirationTime("24hr");
                resp.setMessage("Succesfully Refreshed Token");
            }
            resp.setStatusCode(500);
            return resp;
        }
        return resp;
    }

    /*@PostMapping(value="/userSetting")
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
    }*/


}
