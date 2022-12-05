package com.cybage.emsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages="com.cybage.emsys.model")
@EnableJpaRepositories(basePackages="com.cybage.emsys.repository")
@SpringBootApplication(scanBasePackages="com.cybage.emsys")
public class EmsysApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmsysApplication.class, args);
	}

}
