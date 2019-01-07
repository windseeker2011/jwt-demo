package com.windseeker2011.jwt;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.windseeker2011.jwt.core.JwtAuthenticationConfig;
import com.windseeker2011.jwt.core.JwtTokenAuthenticationFilter;

/**
 * Spring Security Configuration
 * 
 * @author Weihai Li
 *
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationConfig config;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().logout().disable().formLogin().disable()
				// session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
				.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				// add filter
				.addFilterAfter(new JwtTokenAuthenticationFilter(config), UsernamePasswordAuthenticationFilter.class)
				// permit config url
				.authorizeRequests().antMatchers(config.getUrl()).permitAll()
				// some service role
				.antMatchers("/service/admin").hasRole("ADMIN").antMatchers("/service/user").hasRole("USER")
				.antMatchers("/service/guest").permitAll();
	}

}
