package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.bankaccount.BankAccount;
import com.cyberasap.cryptobank.domain.user.User;
import com.cyberasap.cryptobank.repository.IBankAccountRepository;
import com.cyberasap.cryptobank.repository.IUserRepository;
import com.cyberasap.cryptobank.util.RSAUtil;
import fr.marcwrobel.jbanking.IsoCountry;
import fr.marcwrobel.jbanking.iban.Iban;
import fr.marcwrobel.jbanking.iban.RandomIban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


@Service
public class BankAccountService implements IBankAccountService {
    @Autowired
    private IBankAccountRepository bankAccountRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Pair<BankAccount, String> create(String userEmail) throws NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + userEmail + " not found");
        }

        User user = optionalUser.get();

        Iban iban = new RandomIban().next(IsoCountry.RO);
        KeyPair keyPair = RSAUtil.generateKeyPair();
        String publicKeyString = RSAUtil.keyToString(keyPair.getPublic());
        String privateKeyString = RSAUtil.keyToString(keyPair.getPrivate());
        BankAccount bankAccount = BankAccount.builder()
                .user(user)
                .iban(iban.toString())
                .amount(0)
                .publicKey(publicKeyString)
                .creationTime(System.currentTimeMillis())
                .build();

        bankAccountRepository.save(bankAccount);

        return Pair.of(bankAccount, privateKeyString);
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

    @Override
    public String generateBankAccountKeys(String userEmail, String iban) throws NoSuchAlgorithmException {
        BankAccount bankAccount = getBankAccount(userEmail, iban);

        KeyPair keyPair = RSAUtil.generateKeyPair();
        String publicKeyString = RSAUtil.keyToString(keyPair.getPublic());
        String privateKeyString = RSAUtil.keyToString(keyPair.getPrivate());

        bankAccount.setPublicKey(publicKeyString);

        bankAccountRepository.save(bankAccount);

        return privateKeyString;
    }
}
