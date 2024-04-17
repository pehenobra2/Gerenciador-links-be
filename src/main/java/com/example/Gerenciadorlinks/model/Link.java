package com.example.Gerenciadorlinks.model;


import com.example.Gerenciadorlinks.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titulo;

    private String url;

    @Column(unique = true)
    private String sufixo;

    private String dataCriada;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
