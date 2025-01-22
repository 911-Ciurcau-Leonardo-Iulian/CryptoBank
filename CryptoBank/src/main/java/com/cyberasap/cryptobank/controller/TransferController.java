package com.cyberasap.cryptobank.controller;

import com.cyberasap.cryptobank.service.ITransferService;
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
                                            @RequestBody String signedTransferRequest) {
        try {
            return ResponseEntity.ok().body(signedTransferRequest);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Object() {
                public String getMessage() {
                    return exception.getMessage();
                }
            });
        }
    }
}
