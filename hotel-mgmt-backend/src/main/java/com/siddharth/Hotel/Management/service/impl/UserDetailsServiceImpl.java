package com.siddharth.Hotel.Management.service.impl;


import com.siddharth.Hotel.Management.model.Users;
import com.siddharth.Hotel.Management.repository.UserRepository;
import com.siddharth.Hotel.Management.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isValidAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 18;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByName(username);
        if(user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        return new CustomUserDetails(user);
    }

    public Users addUser(Users user) {
        if(!isValidAge(user.getDateOfBirth())) {
            throw new RuntimeException("Users must be of the age 18 years");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
