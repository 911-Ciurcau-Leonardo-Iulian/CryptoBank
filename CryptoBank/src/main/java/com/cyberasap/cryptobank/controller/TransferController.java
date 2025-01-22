package com.cyberasap.cryptobank.controller;

import com.cyberasap.cryptobank.domain.transfer.TransferRequest;
import com.cyberasap.cryptobank.domain.transfer.TransferRequestSigned;
import com.cyberasap.cryptobank.jwt.JwtTokenUtil;
import com.cyberasap.cryptobank.service.ITransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    @Autowired
    private ITransferService transferService;

    @PostMapping("/transfer-amount")
    public ResponseEntity<?> transferAmount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                            @RequestBody @Valid TransferRequestSigned transferRequestSigned) {
        try {
            String email = JwtTokenUtil.getEmailFromAuthorizationHeader(authHeader);
            if (email == null) {
                throw new Exception("Bad credentials");
            }

            TransferRequest transferRequest = transferRequestSigned.getTransferRequest();
            String signature = transferRequestSigned.getSignature();

            transferService.transfer(email, transferRequest, signature);

            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Object() {
                public String getMessage() {
                    return exception.getMessage();
                }
            });
        }
    }
}
