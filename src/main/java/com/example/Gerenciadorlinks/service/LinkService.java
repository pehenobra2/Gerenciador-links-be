package com.example.Gerenciadorlinks.service;

import com.example.Gerenciadorlinks.controllers.UserController;
import com.example.Gerenciadorlinks.dto.LinkRegisterDTO;
import com.example.Gerenciadorlinks.dto.mapper.LinkMapper;
import com.example.Gerenciadorlinks.model.Link;
import com.example.Gerenciadorlinks.model.User;
import com.example.Gerenciadorlinks.repositories.LinkRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final LinkMapper linkMapper;
    private final UserController userController;


    public LinkService(LinkRepository linkRepository, LinkMapper linkMapper, UserController userController) {
        this.linkRepository = linkRepository;
        this.linkMapper = linkMapper;
        this.userController = userController;
    }

    public List<LinkRegisterDTO> list() {
        return linkRepository.findAll()
                .stream()
                .map(linkMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LinkRegisterDTO findById(@PathVariable Long id) {
        return linkRepository.findById(id).map(linkMapper::toDTO)
                .orElseThrow();
    }

    public LinkRegisterDTO create(LinkRegisterDTO linkDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Link link = linkMapper.toEntity(linkDTO);

        link.setUser(user);

        Link savedLink = linkRepository.save(link);

        return linkMapper.toDTO(savedLink);
    }

    // Outros métodos do serviço


    public LinkRegisterDTO update(@PathVariable Long id, Link link) {
        return linkRepository.findById(id)
                .map(record -> {
                    record.setTitulo(link.getTitulo());
                    record.setUrl(link.getUrl());
                    return linkRepository.save(record);
                }).map(linkMapper::toDTO).orElseThrow();

    }
    public boolean delete(@PathVariable Long id){

        return linkRepository.findById(id)
                .map(record -> {
                    linkRepository.deleteById(id);
                    return true;
                })
                .orElse(false);



    }
}