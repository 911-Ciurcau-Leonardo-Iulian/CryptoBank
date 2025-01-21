package com.cyberasap.cryptobank.repository;

import com.cyberasap.cryptobank.domain.bankaccount.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IBankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByIban(String iban);
    List<BankAccount> findAllByUser_Id(Long userId);
}
