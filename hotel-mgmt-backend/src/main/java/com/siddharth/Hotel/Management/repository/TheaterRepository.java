package com.siddharth.Hotel.Management.repository;


import com.fasterxml.jackson.databind.node.IntNode;
import com.siddharth.Hotel.Management.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    long countByName(String name);
}
