package com.example.Gerenciadorlinks.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class Link implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(length = 100, nullable = false)
    private String titulo;

    @NotNull
    @Column(length = 200, nullable = false)
    private String url;

    @Column(unique = true)
    private String sufixo;

    private String dataCriada;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id")
    private User user;


}
