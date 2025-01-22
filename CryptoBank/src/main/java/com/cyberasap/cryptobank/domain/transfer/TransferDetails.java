package com.cyberasap.cryptobank.domain.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDetails {
    private String senderIban;

    private String receiverIban;

    private Integer amount;

    private Long time;

    private Boolean isApproved;
}
