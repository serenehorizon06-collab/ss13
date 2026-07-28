package com.example.ss13.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.ss13.user.Role;
import com.example.ss13.user.User;
import com.example.ss13.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	@Test
	void loadUserByUsernameReturnsUserPrincipalWhenUserExists() {
		User user = new User();
		user.setUsername("admin");
		user.setPassword("$2a$10$encoded-password");
		user.setRole(Role.ADMIN);
		user.setEnabled(true);

		when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

		UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

		assertThat(userDetails).isInstanceOf(UserPrincipal.class);
		assertThat(userDetails.getUsername()).isEqualTo("admin");
		assertThat(userDetails.getPassword()).isEqualTo("$2a$10$encoded-password");
		assertThat(userDetails.isEnabled()).isTrue();
		assertThat(userDetails.getAuthorities())
				.extracting("authority")
				.containsExactly("ROLE_ADMIN");
	}

	@Test
	void loadUserByUsernameThrowsExceptionWhenUserDoesNotExist() {
		when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("missing"))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessage("User not found: missing");
	}
}
