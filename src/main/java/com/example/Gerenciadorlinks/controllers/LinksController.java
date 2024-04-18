package com.example.Gerenciadorlinks.controllers;

import com.example.Gerenciadorlinks.domain.user.User;
import com.example.Gerenciadorlinks.model.Link;
import com.example.Gerenciadorlinks.repositories.LinkRepository;
import com.example.Gerenciadorlinks.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/links")
public class LinksController {


    private LinkRepository linkRepository;
    private UserRepository userRepository;

    @Autowired
    public LinksController(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Link> list() {
        return linkRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Link> create(){

        Link link = new Link();

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("Email: " + userEmail);

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if(userOptional.isPresent()){
            User user = userOptional.get();


            link.setUser(user);
            link.setSufixo(gerarSufixo(10));
            link.setDataCriada(LocalDateTime.now().format(formatter));
            linkRepository.saveAndFlush(link);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(link);
        }else{
            throw new UsernameNotFoundException("User not found");
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
