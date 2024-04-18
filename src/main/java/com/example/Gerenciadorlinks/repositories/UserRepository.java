package com.example.Gerenciadorlinks.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Gerenciadorlinks.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);


}
