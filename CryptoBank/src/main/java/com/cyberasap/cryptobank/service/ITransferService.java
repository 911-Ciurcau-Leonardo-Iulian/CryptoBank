package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.transfer.TransferRequest;

public interface ITransferService {
    void transfer(String userEmail, TransferRequest transferRequest, String signature);
}
