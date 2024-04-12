package com.example.Gerenciadorlinks;

import com.example.Gerenciadorlinks.model.Link;
import com.example.Gerenciadorlinks.repositories.LinkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GerenciadorLinksApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerenciadorLinksApplication.class, args);
	}

	@Bean
	CommandLineRunner initDataBase(LinkRepository linkRepository){
		return args -> {
		};
	}
}
