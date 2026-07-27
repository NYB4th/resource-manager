package com.enterprise.resourcemanager.service;

import com.enterprise.resourcemanager.domain.Role;
import com.enterprise.resourcemanager.domain.User;
import com.enterprise.resourcemanager.dto.UserRegistrationRequest;
import com.enterprise.resourcemanager.dto.UserResponse;
import com.enterprise.resourcemanager.exception.ResourceAlreadyExistsException;
import com.enterprise.resourcemanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request)
    {
        if(userRepository.existsByEmail(request.email()))
        {
            throw new ResourceAlreadyExistsException("User already exists with email: "+request.email());
        }

        Set<Role> roles = (request.roles()==null || request.roles().isEmpty())
                ?Set.of(Role.ROLE_USER)
                :request.roles().stream()
                .map(r->Role.valueOf(r.startsWith("ROLE_")?r:"ROLE_"+r.toUpperCase()))
                .collect(Collectors.toSet());

        User user=User.builder().email(request.email()).password(request.password())
                .firstName(request.firstName()).lastName(request.lastName())
                .roles(roles).build();

        User savedUser=userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email)
    {
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("\"User not found with email: \" + email"));
        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user)
    {
        Set<String> roleStrings=user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        return new UserResponse(user.getId(),user.getEmail(),user.getFirstName(),
                user.getLastName(),roleStrings,user.getCreatedAt());
    }



}
