package com.example.Gerenciadorlinks.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.Gerenciadorlinks.model.Link;
import com.example.Gerenciadorlinks.repositories.LinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/links")
public class LinksController {


    private LinkRepository linkRepository;

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

        System.out.println(link.getTitulo());
        System.out.println(link.getId());
        System.out.println(link.getUrl());
        System.out.println(link.getSufixo());
        System.out.println(link.getDataCriada());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(linkRepository.save(link));
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

}
