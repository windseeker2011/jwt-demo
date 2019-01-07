package com.windseeker2011.jwt.core;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth JWT Configuartion
 * 
 * @author Weihai Li
 *
 */
@Component
@ConfigurationProperties(prefix = JwtAuthenticationConfig.PREFIX)
@Data
@Slf4j
public class JwtAuthenticationConfig {

	public static final String PREFIX = "auth.jwt";

	private String url;

	private String header = "Authorization";

	private String prefix = "Bearer";

	private long expiration = 2 * 60 * 60 * 1000; // default 2 hours

	private String secret;

	@PostConstruct
	public void init() {
		log.info("oauth jwt configuration init: {}", toString());
	}

}
