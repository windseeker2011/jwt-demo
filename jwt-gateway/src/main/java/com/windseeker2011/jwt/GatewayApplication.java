package com.windseeker2011.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
