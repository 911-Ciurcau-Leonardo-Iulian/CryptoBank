package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.user.RegisterRequest;
import com.cyberasap.cryptobank.domain.user.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.NoSuchAlgorithmException;

public interface IUserService extends UserDetailsService {
    void register(RegisterRequest request);
    boolean existsByEmail(String email);
    UserDetails getByEmail(String email);
    String generateUserKeys(String email) throws NoSuchAlgorithmException;
}
