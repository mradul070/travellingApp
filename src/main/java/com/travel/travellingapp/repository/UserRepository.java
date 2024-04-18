package com.travel.travellingapp.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.travellingapp.model.Users;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
