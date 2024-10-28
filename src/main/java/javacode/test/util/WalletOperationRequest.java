package javacode.test.util;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WalletOperationRequest {

    @NotNull
    private UUID walletId;

    @NotNull
    private String operationType;  // "DEPOSIT" или "WITHDRAW"

    @NotNull
    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

}


