package com.skillconnect.backend.Wallet.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Data
@Schema(description = "Request DTO for creating Razorpay order for wallet top-up")
public class AddMoneyOrderRequest {

    @Schema(
            description = "Unique identifier of the user who wants to add money",
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
            example = "500.00",
            requiredMode = RequiredMode.REQUIRED,
            minimum = "1.00"
    )
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Minimum amount is ₹1")
    @DecimalMax(value = "100000.0", message = "Maximum amount is ₹1,00,000")
    private Double amount;
}
