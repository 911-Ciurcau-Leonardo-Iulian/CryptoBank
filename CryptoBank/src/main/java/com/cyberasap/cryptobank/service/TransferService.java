package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService implements ITransferService {
    @Autowired
    ITransferRepository transferRepository;
}
