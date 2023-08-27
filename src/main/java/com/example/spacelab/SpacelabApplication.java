package com.example.spacelab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class SpacelabApplication {

//	@Autowired private DefaultInitializer defaultInitializer;

	public static void main(String[] args) {
		SpringApplication.run(SpacelabApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void setDefaultValues() throws InterruptedException {
//		defaultInitializer.init();
//	}

}
