package com.windseeker2011.jwt.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

/**
 * JWT Username Password Authentication Filter
 * 
 * @author Weihai Li
 *
 */
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final JwtAuthenticationConfig config;
	private final ObjectMapper mapper;

	public JwtUsernamePasswordAuthenticationFilter(JwtAuthenticationConfig config, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(config.getUrl(), "POST"));
		setAuthenticationManager(authManager);
		this.config = config;
		this.mapper = new ObjectMapper();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse rsp)
			throws AuthenticationException, IOException {
		User u = mapper.readValue(req.getInputStream(), User.class);
		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), Collections.emptyList()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse rsp, FilterChain chain,
			Authentication auth) {
		Instant now = Instant.now();
		String token = Jwts.builder().setSubject(auth.getName())
				.claim("authorities",
						auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(Date.from(now)).setExpiration(Date.from(now.plusSeconds(config.getExpiration())))
				.signWith(SignatureAlgorithm.HS256, config.getSecret().getBytes()).compact();
		rsp.setCharacterEncoding("UTF-8");
		rsp.setContentType("application/json; charset=utf-8");
		String str = "{\"access_token\":\"" + token + "\"}";
		PrintWriter out;
		try {
			out = rsp.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Data
	private static class User {
		private String username, password;
	}
}
