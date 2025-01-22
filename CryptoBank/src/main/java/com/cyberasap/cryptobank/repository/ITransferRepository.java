package com.cyberasap.cryptobank.repository;

import com.cyberasap.cryptobank.domain.transfer.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllBySenderBankAccount_User_Id(Long userId);
}
