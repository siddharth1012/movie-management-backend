package com.siddharth.Hotel.Management.controller;

import com.siddharth.Hotel.Management.model.Users;
import com.siddharth.Hotel.Management.security.JwtRequest;
import com.siddharth.Hotel.Management.security.JwtResponse;
import com.siddharth.Hotel.Management.security.JwtUtil;
import com.siddharth.Hotel.Management.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private JwtUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        logger.info("Inside the login endpoint");

        this.authenticate(request.getName(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getName());

        String token = this.jwtUtil.generateToken(userDetails);
        logger.info("Generating token");

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .name(userDetails.getUsername()).build();
        logger.info("Token Generated");

        return new ResponseEntity<JwtResponse>(response, HttpStatus.OK);
    }

    private void authenticate(String name, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(name, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            logger.info("Wrong Credentials: {}", e.getMessage());
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        logger.error("Invalid Credentials: BadCredentialsException in AuthController");
        return "Credentials Invalid";
    }

    @PostMapping(value = "/register")
    public Users createUser(@RequestBody Users user) {
        logger.info("Endpoint for registration is called");
        return userDetailsServiceImpl.addUser(user);
    }
}
