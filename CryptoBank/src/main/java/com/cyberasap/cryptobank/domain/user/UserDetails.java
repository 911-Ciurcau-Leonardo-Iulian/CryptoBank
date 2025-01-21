package com.cyberasap.cryptobank.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetails {
    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;
}
