package com.example.ss13.auth;

import java.util.List;

public record LoginResponse(String message, String username, List<String> authorities) {
}
