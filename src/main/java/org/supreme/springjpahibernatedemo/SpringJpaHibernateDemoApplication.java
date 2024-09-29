package org.supreme.springjpahibernatedemo;

import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Transactional
public class SpringJpaHibernateDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaHibernateDemoApplication.class, args);
	}

}
