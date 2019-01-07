package com.windseeker2011.jwt;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.windseeker2011.jwt.core.JwtAuthenticationConfig;
import com.windseeker2011.jwt.core.JwtUsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration
 * 
 * @author Weihai Li
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private JwtAuthenticationConfig config;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				// memory
				.inMemoryAuthentication().passwordEncoder(passwordEncoder())
				// admin
				.withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER").and()
				// user
				.withUser("user").password(passwordEncoder().encode("user")).roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().logout().disable().formLogin().disable()
				// session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
				.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				// add filter
				.addFilterAfter(new JwtUsernamePasswordAuthenticationFilter(config, authenticationManager()),
						UsernamePasswordAuthenticationFilter.class)
				// permit config url
				.authorizeRequests().antMatchers(config.getUrl()).permitAll().anyRequest().authenticated();
	}

}
