package com.cyberasap.cryptobank.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    @Email
    @Length(min = 6, max = 255)
    private String email;

    @NotBlank
    @Length(min = 10, max = 255)
    private String password;

    @NotBlank
    @Length(min = 10, max = 255)
    private String confirmedPassword;

    @NotBlank
    @Length(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Length(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Length(min = 3, max = 20)
    private String phoneNumber;

    @NotBlank
    @Length(min = 5, max = 255)
    private String address;
}
