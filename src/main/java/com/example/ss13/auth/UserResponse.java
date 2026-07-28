package com.example.ss13.auth;

import com.example.ss13.user.Role;
import com.example.ss13.user.User;

public record UserResponse(Long id, String username, String fullName, Role role, boolean enabled) {

	public static UserResponse from(User user) {
		return new UserResponse(
				user.getId(),
				user.getUsername(),
				user.getFullName(),
				user.getRole(),
				user.isEnabled());
	}
}
