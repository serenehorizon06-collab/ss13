package com.example.ss13.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ss13.user.Role;
import com.example.ss13.user.User;
import com.example.ss13.user.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
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

	private void validateRegisterRequest(RegisterRequest request) {
		if (request == null
				|| isBlank(request.username())
				|| isBlank(request.password())
				|| isBlank(request.fullName())) {
			throw new IllegalArgumentException("Username, password and fullName are required");
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
