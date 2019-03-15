package org.freelesson.sendsms;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;

import org.freelesson.sendsms.domain.Role;
import org.freelesson.sendsms.domain.User;
import org.freelesson.sendsms.repository.RoleRepository;
import org.freelesson.sendsms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SendSMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendSMSApplication.class, args);
	}
	@Bean
	CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository) {
		return (evt) -> {
			createAdminAccount(userRepository, roleRepository);

		};
	}

	private void createAdminAccount(UserRepository userRepository, RoleRepository roleRepository) {
		Optional<User> accountOptional = userRepository.findByUsername("example@domain.com");
		if (!accountOptional.isPresent()) {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			User user = new User("example@domain.com", bCryptPasswordEncoder.encode("example@123"),true);
			Set<Role> roles = new HashSet<>();
			roles.add(getRole(roleRepository, Role.ROLE_ADMIN));
			roles.add(getRole(roleRepository, Role.ROLE_USER));
			user.roles = roles;
			userRepository.save(user);
		}
	}

	private Role getRole(RoleRepository roleRepository, String name) {
		Role role;
		Optional<Role> roleOptional = roleRepository.findByName(name);
		role = roleOptional.orElseGet(() -> roleRepository.save(new Role(name)));
		return role;
	}
	
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("SendSMS-");
		executor.initialize();
		return executor;
	}

}
