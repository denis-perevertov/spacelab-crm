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

@OpenAPIDefinition(
		servers = {
				@Server(url = "https://slj.avada-media-dev2.od.ua/spacelab/admin", description = "Avada Deploy"),
				@Server(url = "www.denis-perevertov.com/spacelab/admin", description = "AWS Deploy"),
				@Server(url = "http://localhost:1489/spacelab/admin", description = "Local")
		}
)
@SpringBootApplication
public class SpacelabApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpacelabApplication.class, args);
	}

}
