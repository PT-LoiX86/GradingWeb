package com.grd.gradingbe.utilities;

import com.grd.gradingbe.dto.enums.AuthenticationType;
import com.grd.gradingbe.dto.enums.Role;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .password_hash(passwordEncoder.encode("admin"))
                    .full_name("Admin User")
                    .email("admin@gmail.com")
                    .role(Role.ADMIN)
                    .authType(AuthenticationType.LOCAL)
                    .updated_at(LocalDateTime.now())
                    .created_at(LocalDateTime.now())
                    .verified(true)
                    .is_active(true)
                    .build());
            log.info("Initialized default admin user in the database.");
        }
    }
}
