package com.example.Gerenciadorlinks.controllers;

import com.example.Gerenciadorlinks.domain.user.User;
import com.example.Gerenciadorlinks.dto.LoginRequestDTO;
import com.example.Gerenciadorlinks.dto.RegisterRequestDTO;
import com.example.Gerenciadorlinks.dto.ResponseDTO;
import com.example.Gerenciadorlinks.infra.security.TokenService;
import com.example.Gerenciadorlinks.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public AuthController(UserRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

}
