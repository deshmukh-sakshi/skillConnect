package com.skillconnect.backend.Wallet.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Data
@Schema(description = "Request DTO for wallet operations including money addition and wallet creation")
public class WalletRequestDTO {
    @Schema(
            description = "Unique identifier of the user who owns the wallet",
            example = "1",
            requiredMode = RequiredMode.REQUIRED
    )
    @NotNull(message = "User ID is required")
    private Long userId;

    @Schema(
            description = "Role of the user (CLIENT or FREELANCER)",
            example = "CLIENT",
            requiredMode = RequiredMode.REQUIRED,
            allowableValues = {"CLIENT", "FREELANCER"}
    )
    @NotBlank(message = "Role is required")
    private String role;

    @Schema(
            description = "Amount of money to add to the wallet (must be positive)",
            example = "100.50",
            requiredMode = RequiredMode.REQUIRED,
            minimum = "0.01"
    )
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
