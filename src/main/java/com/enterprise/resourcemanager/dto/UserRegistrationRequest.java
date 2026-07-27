package com.enterprise.resourcemanager.dto;
import java.util.Set;

public record UserRegistrationRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        Set <String> roles){}