package com.enterprise.resourcemanager.service;
import com.enterprise.resourcemanager.dto.UserRegistrationRequest;
import com.enterprise.resourcemanager.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest request);
    UserResponse getUserByEmail(String email);
}
