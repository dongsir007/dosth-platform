package com.dosth.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Gateway
 * @author THINKPAD
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class DosthGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(DosthGatewayApplication.class, args);
	}
}