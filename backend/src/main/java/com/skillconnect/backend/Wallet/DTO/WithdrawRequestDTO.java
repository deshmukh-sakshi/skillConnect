package com.skillconnect.backend.Wallet.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Request DTO for withdrawing money from a user's wallet")
public class WithdrawRequestDTO {
    @Schema(
        description = "Unique identifier of the user requesting the withdrawal",
        example = "1",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotNull(message = "User ID is required")
    private Long userId;

    @Schema(
        description = "Amount of money to withdraw from the wallet (must be positive and not exceed available balance)",
        example = "75.25",
        requiredMode = RequiredMode.REQUIRED,
        minimum = "0.01"
    )
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
