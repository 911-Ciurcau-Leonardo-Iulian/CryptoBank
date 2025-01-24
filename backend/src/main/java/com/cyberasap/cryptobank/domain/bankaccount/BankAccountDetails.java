package com.cyberasap.cryptobank.domain.bankaccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountDetails {
    public String iban;

    public Long creationTime;

    public Integer amount;
}
