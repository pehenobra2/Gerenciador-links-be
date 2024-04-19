package com.example.Gerenciadorlinks.controllers;

import com.example.Gerenciadorlinks.dto.LinkRegisterDTO;
import com.example.Gerenciadorlinks.dto.mapper.LinkMapper;
import com.example.Gerenciadorlinks.model.User;
import com.example.Gerenciadorlinks.model.Link;
import com.example.Gerenciadorlinks.repositories.LinkRepository;
import com.example.Gerenciadorlinks.repositories.UserRepository;
import com.example.Gerenciadorlinks.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    private UserRepository userRepository;
    private final LinkService linkService;
    private final LinkMapper linkMapper;

    @Autowired
    public LinksController(LinkMapper linkMapper, UserRepository userRepository, LinkService linkService) {

        this.userRepository = userRepository;
        this.linkService  = linkService;
        this.linkMapper = linkMapper;
    }

    @GetMapping
    public List<LinkRegisterDTO> list() {
        return linkService.list();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public LinkRegisterDTO create(@RequestBody LinkRegisterDTO body){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Email: " + userEmail);

        Optional<User> userOptional = userRepository.findByEmail(userEmail);


        if(userOptional.isPresent()){

            User user = userOptional.get();

            System.out.println("O user é :" + user);

            Link link = new Link();
            link.setTitulo(body.titulo());
            link.setUrl(body.url());
            link.setSufixo(gerarSufixo(10));
            link.setDataCriada(LocalDateTime.now().format(formatter));

            link.setUser(user);

            System.out.println("O titulo é :" + link.getTitulo());
            System.out.println("O url é :" + link.getUrl());
            System.out.println("O sufixo é :" + link.getSufixo());
            System.out.println("O Data é :" + link.getDataCriada());
            System.out.println("O user é :" + link.getUser());

            LinkRegisterDTO createdLink = linkService.create(linkMapper.toDTO(link));

            System.out.println("O createdLink é : " + createdLink.url());

            return createdLink;
        } else {
            return null; // Or any other appropriate error response
        }
        }



    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    public static String gerarSufixo(int tamanho) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(tamanho * 5, random).toString(32).substring(0, tamanho);
    }

    @GetMapping("/{id}")
    public LinkRegisterDTO findById(@PathVariable Long id){
        return linkService.findById(id);
    }
    @PutMapping("/{id}")
    public LinkRegisterDTO update(@PathVariable Long id, @RequestBody Link link){
        return linkService.update(id, link);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        linkService.delete(id);

    }

}
