package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.user.RegisterRequest;
import com.cyberasap.cryptobank.domain.user.User;
import com.cyberasap.cryptobank.domain.user.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User register(RegisterRequest request);
    boolean existsByEmail(String email);
    UserDetails getByEmail(String email);
}
