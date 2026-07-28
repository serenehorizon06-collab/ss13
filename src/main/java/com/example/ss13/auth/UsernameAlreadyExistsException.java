package com.example.ss13.auth;

public class UsernameAlreadyExistsException extends RuntimeException {

	public UsernameAlreadyExistsException(String username) {
		super("Username already exists: " + username);
	}
}
