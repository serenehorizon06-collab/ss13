package com.example.ss13.auth;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/test")
	public Map<String, String> test() {
		return Map.of("message", "Public auth endpoint is working");
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
		Authentication authentication = authService.login(request);
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
		httpRequest.getSession(true).setAttribute(
				HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				securityContext);
		return authService.toLoginResponse(authentication);
	}

	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public ResponseEntity<Map<String, String>> handleUsernameAlreadyExists(UsernameAlreadyExistsException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", exception.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException exception) {
		return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Map<String, String>> handleAuthenticationException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Username or password is incorrect"));
	}
}
