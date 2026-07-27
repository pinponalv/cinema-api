package com.example.cinema_api.repository;

import com.example.cinema_api.entity.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserSec, Long> {
    Optional<UserSec> findByEmail(String email);
}
