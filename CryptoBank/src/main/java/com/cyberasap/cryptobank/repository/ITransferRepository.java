package com.cyberasap.cryptobank.repository;

import com.cyberasap.cryptobank.domain.transfer.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransferRepository extends JpaRepository<Transfer, Long> {
}
