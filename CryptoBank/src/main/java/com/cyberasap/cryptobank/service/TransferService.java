package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.transfer.TransferRequest;
import com.cyberasap.cryptobank.domain.user.User;
import com.cyberasap.cryptobank.repository.IBankAccountRepository;
import com.cyberasap.cryptobank.repository.ITransferRepository;
import com.cyberasap.cryptobank.repository.IUserRepository;
import com.cyberasap.cryptobank.util.CryptoUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TransferService implements ITransferService {
    @Autowired
    ITransferRepository transferRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IBankAccountRepository bankAccountRepository;

    @Override
    public void transfer(String userEmail, TransferRequest transferRequest, String signature) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + userEmail + " not found");
        }

        User user = optionalUser.get();
        String publicKey = user.getPublicKey();

        try {
            String transferRequestHash = hashTransferRequest(transferRequest);

            String senderHash = CryptoUtil.decryptMessage(signature, publicKey);

            if (!senderHash.equals(transferRequestHash)) {
                throw new RuntimeException("Invalid signature");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String hashTransferRequest(TransferRequest transferRequest) throws Exception {
        ObjectMapper Obj = new ObjectMapper();
        String transferRequestJson = Obj.writeValueAsString(transferRequest);

        return CryptoUtil.hashMessage(transferRequestJson);
    }
}
