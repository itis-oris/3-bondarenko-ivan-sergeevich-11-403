package com.itis.cryptotracker.service;

import com.itis.cryptotracker.model.Role;
import com.itis.cryptotracker.model.enums.RoleName;
import com.itis.cryptotracker.model.User;
import com.itis.cryptotracker.dto.form.RegisterForm;
import com.itis.cryptotracker.exception.BadRequestException;
import com.itis.cryptotracker.exception.ConflictException;
import com.itis.cryptotracker.exception.NotFoundException;
import com.itis.cryptotracker.repository.RoleRepository;
import com.itis.cryptotracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(RegisterForm form) {
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            throw new BadRequestException("Пароли не совпадают");
        }
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new ConflictException("Имя пользователя уже занято");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new ConflictException("Email уже используется");
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Роль ROLE_USER не найдена"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .passwordHash(passwordEncoder.encode(form.getPassword()))
                .enabled(true)
                .emailNotificationsEnabled(form.isEmailNotificationsEnabled())
                .roles(roles)
                .build();

        User saved = userRepository.save(user);
        log.info("Registered user id={} username={}", saved.getId(), saved.getUsername());
        return saved;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: %d".formatted(id)));
    }
}
