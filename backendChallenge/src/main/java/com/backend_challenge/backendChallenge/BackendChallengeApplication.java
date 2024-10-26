package com.backend_challenge.backendChallenge;

import com.backend_challenge.backendChallenge.entites.Role;
import com.backend_challenge.backendChallenge.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendChallengeApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {

		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						new Role("USER")
				);
			} else {
				System.out.println("Role USER was already initialized");
			}
		};
	}
}
