package com.siddharth.Hotel.Management.repository;


import com.siddharth.Hotel.Management.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    public Users findByName(String name);
}
