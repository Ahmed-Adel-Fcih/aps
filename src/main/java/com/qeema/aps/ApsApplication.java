package com.qeema.aps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.qeema.aps" })

public class ApsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApsApplication.class, args);
	}

}
