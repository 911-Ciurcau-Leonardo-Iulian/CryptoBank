package com.cyberasap.cryptobank.controller;

import com.cyberasap.cryptobank.domain.user.RegisterRequest;
import com.cyberasap.cryptobank.jwt.JwtTokenUtil;
import com.cyberasap.cryptobank.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    IUserService userService;

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
}
