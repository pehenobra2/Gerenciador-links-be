package com.example.Gerenciadorlinks.controllers;

import com.example.Gerenciadorlinks.domain.user.User;
import com.example.Gerenciadorlinks.model.Link;
import com.example.Gerenciadorlinks.repositories.LinkRepository;
import com.example.Gerenciadorlinks.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/links")
public class LinksController {


    private LinkRepository linkRepository;
    private UserRepository userRepository;

    @Autowired
    public LinksController(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @GetMapping
    public List<Link> list() {
        return linkRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Link> create(@RequestBody Link link){



        link.setSufixo(gerarSufixo(8));
        link.setDataCriada(LocalDateTime.now().format(formatter));

        String userId = getUserIdFromContext();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        link.setUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(linkRepository.save(link));

    }

    private String getUserIdFromContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userRepository.findByName(userDetails.getUsername())
                    .map(User::getId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userDetails.getUsername()));
        } else {
            throw new IllegalStateException("Cannot get user ID from non-UserDetails principal");
        }
    }


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    public static String gerarSufixo(int tamanho) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(tamanho * 5, random).toString(32).substring(0, tamanho);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Link> findById(@PathVariable Long id){
        return linkRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Link> update(@PathVariable Long id, @RequestBody Link link){
        return linkRepository.findById(id)
                .map(record -> {
                    record.setTitulo(link.getTitulo());
                    record.setUrl(link.getUrl());

                    Link updated = linkRepository.save(record);
                    return ResponseEntity.ok().body(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        return linkRepository.findById(id)
                .map(record -> {
                    linkRepository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());



    }

}
