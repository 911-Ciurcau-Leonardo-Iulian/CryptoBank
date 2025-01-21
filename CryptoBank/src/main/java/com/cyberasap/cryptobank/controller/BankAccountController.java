package com.cyberasap.cryptobank.controller;

import com.cyberasap.cryptobank.domain.bankaccount.BankAccount;
import com.cyberasap.cryptobank.domain.bankaccount.BankAccountCreationResponse;
import com.cyberasap.cryptobank.domain.bankaccount.BankAccountDetails;
import com.cyberasap.cryptobank.domain.bankaccount.BankAccountSimpleDetails;
import com.cyberasap.cryptobank.jwt.JwtTokenUtil;
import com.cyberasap.cryptobank.service.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/bank-account")
public class BankAccountController {
    @Autowired
    private IBankAccountService bankAccountService;

    @PostMapping("/create")
    public ResponseEntity<?> createBankAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            String email = JwtTokenUtil.getEmailFromAuthorizationHeader(authHeader);
            if (email == null) {
                throw new Exception("Bad credentials");
            }

            Pair<BankAccount, String> result = bankAccountService.create(email);
            BankAccount bankAccount = result.getFirst();
            String privateKey = result.getSecond();

            BankAccountCreationResponse response = BankAccountCreationResponse.builder()
                    .iban(bankAccount.getIban())
                    .privateKey(privateKey)
                    .build();

            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public String getMessage() {
                            return exception.getMessage();
                        }
                    });
        }
    }

    @GetMapping("/get/{iban}")
    public ResponseEntity<?> getBankAccount(@PathVariable("iban") String iban,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            String email = JwtTokenUtil.getEmailFromAuthorizationHeader(authHeader);
            if (email == null) {
                throw new Exception("Bad credentials");
            }

            BankAccount bankAccount = bankAccountService.getBankAccount(email, iban);
            BankAccountSimpleDetails response = BankAccountSimpleDetails.builder()
                    .creationTime(bankAccount.getCreationTime())
                    .amount(bankAccount.getAmount())
                    .build();

            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public String getMessage() {
                            return exception.getMessage();
                        }
                    });
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listBankAccounts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            String email = JwtTokenUtil.getEmailFromAuthorizationHeader(authHeader);
            if (email == null) {
                throw new Exception("Bad credentials");
            }

            List<BankAccount> result = bankAccountService.getAllBankAccounts(email);
            List<BankAccountDetails> response = result.stream()
                    .map((bankAccount) -> BankAccountDetails.builder()
                            .iban(bankAccount.getIban())
                            .creationTime(bankAccount.getCreationTime())
                            .amount(bankAccount.getAmount())
                            .build())
                    .toList();

            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public String getMessage() {
                            return exception.getMessage();
                        }
                    });
        }
    }

    @GetMapping("/keys/{iban}")
    public ResponseEntity<?> generateBankAccountKeys(@PathVariable("iban") String iban,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            String email = JwtTokenUtil.getEmailFromAuthorizationHeader(authHeader);
            if (email == null) {
                throw new Exception("Bad credentials");
            }

            String response = bankAccountService.generateBankAccountKeys(email, iban);

            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public String getMessage() {
                            return exception.getMessage();
                        }
                    });
        }
    }
}
