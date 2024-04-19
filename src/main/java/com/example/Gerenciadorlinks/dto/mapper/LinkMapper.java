package com.example.Gerenciadorlinks.dto.mapper;

import com.example.Gerenciadorlinks.dto.LinkRegisterDTO;
import com.example.Gerenciadorlinks.model.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {

    public LinkRegisterDTO toDTO(Link link){
        if (link == null){
            return null;
        }
        return new LinkRegisterDTO(link.getId(),link.getTitulo(), link.getUrl(), link.getSufixo(), link.getDataCriada(), link.getUser());
    }

    public Link toEntity(LinkRegisterDTO linkRegisterDTO){

        if(linkRegisterDTO == null){
            return null;
        }
        Link link = new Link();
        if(linkRegisterDTO.id() != null){
            link.setId(linkRegisterDTO.id());
        }
        link.setTitulo(linkRegisterDTO.titulo());
        link.setUrl(linkRegisterDTO.url());
        return link;
    }
}
