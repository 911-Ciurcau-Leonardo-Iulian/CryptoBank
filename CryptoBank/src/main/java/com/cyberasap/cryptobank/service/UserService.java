package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.config.SecurityConfiguration;
import com.cyberasap.cryptobank.domain.user.RegisterRequest;
import com.cyberasap.cryptobank.domain.user.User;
import com.cyberasap.cryptobank.repository.IUserRepository;
import com.cyberasap.cryptobank.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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
    public void register(RegisterRequest request) {
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

        userRepository.save(newUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public com.cyberasap.cryptobank.domain.user.UserDetails getByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + email + " not found");
        }

        User user = optionalUser.get();

        return com.cyberasap.cryptobank.domain.user.UserDetails.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

    @Override
    public String generateUserKeys(String email) throws NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + email + " not found");
        }

        User user = optionalUser.get();

        KeyPair keyPair = RSAUtil.generateKeyPair();
        String publicKeyString = RSAUtil.keyToString(keyPair.getPublic());
        String privateKeyString = RSAUtil.keyToString(keyPair.getPrivate());

        user.setPublicKey(publicKeyString);
        userRepository.save(user);

        return privateKeyString;
    }
}
