package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.bankaccount.BankAccount;
import org.springframework.data.util.Pair;

import java.security.NoSuchAlgorithmException;
import java.util.List;


public interface IBankAccountService {
    Pair<BankAccount, String> create(String userEmail) throws NoSuchAlgorithmException;
    BankAccount getBankAccount(String userEmail, String iban);
    List<BankAccount> getAllBankAccounts(String userEmail);
    String generateBankAccountKeys(String userEmail, String iban) throws NoSuchAlgorithmException;
}
