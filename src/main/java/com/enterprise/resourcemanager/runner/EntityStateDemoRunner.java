package com.enterprise.resourcemanager.runner;

import com.enterprise.resourcemanager.domain.Role;
import com.enterprise.resourcemanager.domain.User;
import com.enterprise.resourcemanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class EntityStateDemoRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    public EntityStateDemoRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // Establishes a JPA PersistenceContext boundary for this method thread
    public void run(String... args) {
        if (userRepository.existsByEmail("admin@enterprise.com")) {
            return;
        }

        // 1. TRANSIENT STATE: Object exists in JVM heap memory, unknown to Hibernate/Database.
        User user = User.builder()
                .email("admin@enterprise.com")
                .password("$2a$10$e8Z...encodedPassword") // Example bcrypt payload
                .firstName("Admin")
                .lastName("User")
                .roles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER))
                .build();

        // 2. PERSISTENT STATE: Attached to the PersistenceContext (First-Level Cache).
        userRepository.save(user);

        // 3. DIRTY CHECKING DEMO:
        // We do NOT call userRepository.save(user) again here.
        user.setFirstName("System-Admin");

        // When the method completes, @Transactional commits.
        // Hibernate detects the field mutation during flush and fires an SQL UPDATE automatically!
    }
}