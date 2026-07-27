package com.enterprise.resourcemanager.dto;
import java.time.Instant;
import java.util.Set;

public record UserResponse (
  Long id,
  String email,
  String firstName,
  String lastName,
  Set<String> roles,
  Instant createdAt
){}
