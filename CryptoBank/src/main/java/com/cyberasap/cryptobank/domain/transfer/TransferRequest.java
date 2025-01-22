package com.cyberasap.cryptobank.domain.transfer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    @NotBlank
    private String senderIban;

    @NotBlank
    private String receiverIban;

    @NotNull
    @Min(1)
    private Integer amount;
}
