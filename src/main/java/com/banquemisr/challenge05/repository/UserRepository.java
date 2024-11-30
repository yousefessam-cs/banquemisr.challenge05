package com.banquemisr.challenge05.repository;

import com.banquemisr.challenge05.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional <User> findByEmail(String email);


}
