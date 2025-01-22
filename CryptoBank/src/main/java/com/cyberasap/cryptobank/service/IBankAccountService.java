package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.bankaccount.BankAccount;

import java.util.List;


public interface IBankAccountService {
    BankAccount create(String userEmail);
    BankAccount getBankAccount(String userEmail, String iban);
    List<BankAccount> getAllBankAccounts(String userEmail);
}
