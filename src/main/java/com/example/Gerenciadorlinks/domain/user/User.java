package com.example.Gerenciadorlinks.domain.user;

import com.example.Gerenciadorlinks.model.Link;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
    private List<Link> links;

    @Override
    public String toString() {
        return email;
    }

}