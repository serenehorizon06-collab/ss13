package com.example.ss13.auth;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@GetMapping("/test")
	public Map<String, String> test() {
		return Map.of("message", "Public auth endpoint is working");
	}
}
