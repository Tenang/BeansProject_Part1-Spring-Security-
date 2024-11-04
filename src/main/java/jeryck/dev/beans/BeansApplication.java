package jeryck.dev.beans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class BeansApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeansApplication.class, args);
	}

}
