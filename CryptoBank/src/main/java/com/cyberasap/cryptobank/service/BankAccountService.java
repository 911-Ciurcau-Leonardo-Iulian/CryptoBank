package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.bankaccount.BankAccount;
import com.cyberasap.cryptobank.domain.user.User;
import com.cyberasap.cryptobank.repository.IBankAccountRepository;
import com.cyberasap.cryptobank.repository.IUserRepository;
import fr.marcwrobel.jbanking.IsoCountry;
import fr.marcwrobel.jbanking.iban.Iban;
import fr.marcwrobel.jbanking.iban.RandomIban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BankAccountService implements IBankAccountService {
    @Autowired
    private IBankAccountRepository bankAccountRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public BankAccount create(String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + userEmail + " not found");
        }

        User user = optionalUser.get();
        Iban iban = new RandomIban().next(IsoCountry.RO);
        BankAccount bankAccount = BankAccount.builder()
                .user(user)
                .iban(iban.toString())
                .amount(0)
                .creationTime(System.currentTimeMillis())
                .build();

        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount getBankAccount(String userEmail, String iban) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByIban(iban);
        if (optionalBankAccount.isEmpty()) {
            throw new RuntimeException("Bank account not found");
        }

        BankAccount bankAccount = optionalBankAccount.get();
        if (!bankAccount.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Bad credentials");
        }

        return bankAccount;
    }

    @Override
    public List<BankAccount> getAllBankAccounts(String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + userEmail + " not found");
        }

        User user = optionalUser.get();

        return bankAccountRepository.findAllByUser_Id(user.getId());
    }
}
