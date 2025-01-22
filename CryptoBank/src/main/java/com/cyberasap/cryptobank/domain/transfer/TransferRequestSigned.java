package com.cyberasap.cryptobank.domain.transfer;

import jakarta.validation.Valid;
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
public class TransferRequestSigned {
    @NotNull
    @Valid
    private TransferRequest transferRequest;

    @NotBlank
    private String signature;
}
