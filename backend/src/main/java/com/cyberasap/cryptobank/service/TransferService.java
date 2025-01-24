package com.cyberasap.cryptobank.service;

import com.cyberasap.cryptobank.domain.bankaccount.BankAccount;
import com.cyberasap.cryptobank.domain.transfer.Transfer;
import com.cyberasap.cryptobank.domain.transfer.TransferRequest;
import com.cyberasap.cryptobank.domain.user.User;
import com.cyberasap.cryptobank.repository.IBankAccountRepository;
import com.cyberasap.cryptobank.repository.ITransferRepository;
import com.cyberasap.cryptobank.repository.IUserRepository;
import com.cyberasap.cryptobank.util.CryptoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        try {
            String transferRequestHash = hashTransferRequest(transferRequest);

            String publicKey = user.getPublicKey();
            String senderHash = CryptoUtil.decryptMessage(signature, publicKey);

            if (!senderHash.equals(transferRequestHash)) {
                throw new RuntimeException("Invalid digital signature");
            }

            String senderIban = transferRequest.getSenderIban();
            String receiverIban = transferRequest.getReceiverIban();

            if (senderIban.equals(receiverIban)) {
                throw new RuntimeException("Sender and receiver account cannot be the same");
            }

            Optional<BankAccount> optionalSenderBankAccount = bankAccountRepository.findByIban(senderIban);
            if (optionalSenderBankAccount.isEmpty()) {
                throw new RuntimeException("Bank account with iban " + senderIban + " not found");
            }

            BankAccount senderBankAccount = optionalSenderBankAccount.get();
            if (!senderBankAccount.getUser().getEmail().equals(userEmail)) {
                throw new RuntimeException("Bank account with iban " + senderIban + "does not belong to user");
            }

            Integer transferAmount = transferRequest.getAmount();

            if (transferAmount < 1) {
                throw new RuntimeException("Transfer amount cannot be less than 1");
            }

            Integer senderAccountAmount = senderBankAccount.getAmount();

            if (senderAccountAmount <= 0) {
                throw new RuntimeException("Sender account amount is not positive");
            }

            if (transferAmount > senderBankAccount.getAmount()) {
                throw new RuntimeException("Transfer amount is greater than sender bank account");
            }

            Optional<BankAccount> optionalReceiverBankAccount = bankAccountRepository.findByIban(receiverIban);
            if (optionalReceiverBankAccount.isEmpty()) {
                throw new RuntimeException("Bank account with iban " + receiverIban + " not found");
            }

            BankAccount receiverBankAccount = optionalReceiverBankAccount.get();

            Transfer transfer = Transfer.builder()
                    .amount(transferAmount)
                    .isApproved(false)
                    .senderBankAccount(senderBankAccount)
                    .receiverBankAccount(receiverBankAccount)
                    .timestamp(System.currentTimeMillis())
                    .build();

            transferRepository.save(transfer);

            senderBankAccount.setAmount(senderAccountAmount - transferAmount);
            receiverBankAccount.setAmount(receiverBankAccount.getAmount() + transferAmount);

            bankAccountRepository.save(senderBankAccount);
            bankAccountRepository.save(receiverBankAccount);

            transfer.setIsApproved(true);

            transferRepository.save(transfer);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Transfer> getAllTransfers(String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + userEmail + " not found");
        }

        User user = optionalUser.get();

        return transferRepository.findAllBySenderBankAccount_User_Id(user.getId());
    }

    private String hashTransferRequest(TransferRequest transferRequest) throws Exception {
        ObjectMapper Obj = new ObjectMapper();
        String transferRequestJson = Obj.writeValueAsString(transferRequest);

        return CryptoUtil.hashMessage(transferRequestJson);
    }
}
