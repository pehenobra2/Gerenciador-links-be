package com.example.Gerenciadorlinks.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Gerenciadorlinks.domain.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User as u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

}
