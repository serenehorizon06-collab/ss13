package com.example.ss13.auth;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ss13.user.Role;
import com.example.ss13.user.User;
import com.example.ss13.user.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public AuthService(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}

	public UserResponse register(RegisterRequest request) {
		validateRegisterRequest(request);
		String username = request.username().trim();

		if (userRepository.existsByUsername(username)) {
			throw new UsernameAlreadyExistsException(username);
		}

		User user = new User();
		user.setUsername(username);
		user.setFullName(request.fullName().trim());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setRole(Role.USER);
		user.setEnabled(true);

		return UserResponse.from(userRepository.save(user));
	}

	public Authentication login(LoginRequest request) {
		validateLoginRequest(request);
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.username().trim(),
						request.password()));
	}

	public LoginResponse toLoginResponse(Authentication authentication) {
		List<String> authorities = authentication.getAuthorities().stream()
				.map(authority -> authority.getAuthority())
				.toList();

		return new LoginResponse(
				"Login successful",
				authentication.getName(),
				authorities);
	}

	private void validateRegisterRequest(RegisterRequest request) {
		if (request == null
				|| isBlank(request.username())
				|| isBlank(request.password())
				|| isBlank(request.fullName())) {
			throw new IllegalArgumentException("Username, password and fullName are required");
		}
	}

	private void validateLoginRequest(LoginRequest request) {
		if (request == null || isBlank(request.username()) || isBlank(request.password())) {
			throw new IllegalArgumentException("Username and password are required");
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
