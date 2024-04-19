package com.example.Gerenciadorlinks.dto;

import com.example.Gerenciadorlinks.model.User;

public record LinkRegisterDTO (String id, String titulo, String url, String sufixo, String dataCriada, User user ){
}
