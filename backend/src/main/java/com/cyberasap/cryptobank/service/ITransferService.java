package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.transfer.Transfer;
import com.cyberasap.cryptobank.domain.transfer.TransferRequest;

import java.util.List;

public interface ITransferService {
    void transfer(String userEmail, TransferRequest transferRequest, String signature);
    List<Transfer> getAllTransfers(String userEmail);
}
