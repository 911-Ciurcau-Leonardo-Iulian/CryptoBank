package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.config.SecurityConfiguration;
import com.cyberasap.cryptobank.domain.user.RegisterRequest;
import com.cyberasap.cryptobank.domain.user.User;
import com.cyberasap.cryptobank.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + username + " not found"));
    }

    @Override
    public User register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmedPassword())) {
            throw new RuntimeException("Password does not match confirmed password");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email " + request.getEmail() + " is already used");
        }

        String encryptedPassword = SecurityConfiguration.passwordEncoder().encode(request.getPassword());

        User newUser = User.builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();

        return userRepository.save(newUser);
    }
}
