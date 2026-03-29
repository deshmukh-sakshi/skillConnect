package com.skillconnect.backend.Wallet.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Revenue transaction details for a specific project payment")
@Data
@Builder
public class RevenueTransactionDTO {

    @Schema(
            description = "Unique identifier of the project associated with this transaction",
            example = "101",
            requiredMode = RequiredMode.REQUIRED
    )
    private Long projectId;

    @Schema(
            description = "Title of the project for which payment was received",
            example = "E-commerce Website Development",
            requiredMode = RequiredMode.REQUIRED
    )
    private String projectTitle;

    @Schema(
            description = "Name of the client who made the payment",
            example = "Tech Solutions Inc.",
            requiredMode = RequiredMode.REQUIRED
    )
    private String clientName;

    @Schema(
            description = "Amount received for this transaction",
            example = "1250.00",
            requiredMode = RequiredMode.REQUIRED
    )
    private Double amount;

    @Schema(
            description = "Date and time when the payment was received",
            example = "2024-01-15T14:30:00",
            requiredMode = RequiredMode.REQUIRED
    )
    private LocalDateTime receivedAt;

    @Schema(
            description = "Current status of the transaction",
            example = "COMPLETED",
            allowableValues = {"PENDING", "COMPLETED", "FAILED", "REFUNDED"},
            requiredMode = RequiredMode.REQUIRED
    )
    private String status;
}