package com.windseeker2011.jwt.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A Simple Controller
 * 
 * @author Weihai Li
 *
 */
@RestController
public class ServiceController {

	@GetMapping("/admin")
	public String admin() {
		return "Hello Admin!";
	}

	@GetMapping("/user")
	public String user() {
		return "Hello User!";
	}

	@GetMapping("/guest")
	public String guest() {
		return "Hello Guest!";
	}

}
