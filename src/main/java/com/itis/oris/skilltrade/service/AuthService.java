package com.itis.oris.skilltrade.service;

import com.itis.oris.skilltrade.entity.User;
import com.itis.oris.skilltrade.entity.enums.Role;
import com.itis.oris.skilltrade.exception.UserAlreadyExistsException;
import com.itis.oris.skilltrade.form.RegisterForm;
import com.itis.oris.skilltrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(RegisterForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new UserAlreadyExistsException("Логин уже занят");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new UserAlreadyExistsException("Email уже занят");
        }

        User user = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .passwordHash(passwordEncoder.encode(form.getPassword()))
                .bio(form.getBio())
                .city(form.getCity())
                .role(Role.USER)
                .blocked(false)
                .build();

        return userRepository.save(user);
    }
}
