package com.example.Gerenciadorlinks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public ResponseEntity<String> getUser() {
        // Obtém o objeto de autenticação do contexto de segurança
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se o usuário está autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            // Obtém o nome do usuário autenticado
            String username = authentication.getName();

            return ResponseEntity.ok("Usuário logado: " + username);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nenhum usuário autenticado");
        }
    }
}
