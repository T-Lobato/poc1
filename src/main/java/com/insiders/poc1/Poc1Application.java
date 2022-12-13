package com.insiders.poc1;

import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Poc1Application {
	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		SpringApplication.run(Poc1Application.class, args);
	}
}