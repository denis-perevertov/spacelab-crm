package com.example.spacelab;

import com.example.spacelab.config.Tttt;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@OpenAPIDefinition(servers = {@Server(url = "www.denis-perevertov.com", description = "AWS Deploy")})
@SpringBootApplication
@Controller
@Slf4j
@RequiredArgsConstructor
public class SpacelabApplication {

	private final Tttt config;

	public static void main(String[] args) {
		SpringApplication.run(SpacelabApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	void lul() {
		log.info("CONFIG");
		log.info(config.toString());
	}

}
