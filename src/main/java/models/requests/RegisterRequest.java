package models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password,
        @JsonProperty("confirm_password") String confirmPassword
) {}