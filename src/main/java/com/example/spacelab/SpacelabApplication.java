package com.example.spacelab;

import com.example.spacelab.controller.AdminController;
import com.example.spacelab.controller.CourseController;
import com.example.spacelab.integration.teamwork.TeamworkService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@OpenAPIDefinition(servers = {@Server(url = "/spacelab", description = "Default Server URL (w/ https??)")})
@SpringBootApplication
@Controller
public class SpacelabApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpacelabApplication.class, args);
	}

}
