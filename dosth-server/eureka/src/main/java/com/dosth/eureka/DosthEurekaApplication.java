package com.dosth.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka server
 * 
 * @author THINKPAD
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class DosthEurekaApplication {
	public static void main(String[] args) {
		SpringApplication.run(DosthEurekaApplication.class, args);
	}
}