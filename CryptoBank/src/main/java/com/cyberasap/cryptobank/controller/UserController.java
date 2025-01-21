package com.cyberasap.cryptobank.controller;

import com.cyberasap.cryptobank.domain.user.LoginRequest;
import com.cyberasap.cryptobank.domain.user.LoginResponse;
import com.cyberasap.cryptobank.domain.user.RegisterRequest;
import com.cyberasap.cryptobank.domain.user.User;
import com.cyberasap.cryptobank.jwt.JwtTokenUtil;
import com.cyberasap.cryptobank.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            System.out.println(JwtTokenUtil.SECRET_KEY);
            userService.register(request);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Object() {
                public String getMessage() {
                    return exception.getMessage();
                }
            });
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            if (!userService.existsByEmail(request.getEmail())) {
                throw new Exception("Bad credentials");
            }

            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = (User)authentication.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            LoginResponse response = new LoginResponse(accessToken);

            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public String getMessage() {
                            return exception.getMessage();
                        }
                    });
        }
    }

}
