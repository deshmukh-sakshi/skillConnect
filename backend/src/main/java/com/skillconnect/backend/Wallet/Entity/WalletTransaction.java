package com.skillconnect.backend.Wallet.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long userId;
    private String userRole;
    private String transactionType; // CREDIT, DEBIT, FREEZE, RELEASE
    private Double amount;
    private Double balanceBefore;
    private Double balanceAfter;
    private Long projectId;
    private String description;
    // ADD these fields to your existing WalletTransaction entity

    @Schema(description = "Razorpay payment ID for tracking")
    private String razorpayPaymentId;

    @Schema(description = "Razorpay order ID for tracking")
    private String razorpayOrderId;

    @Schema(description = "Payment method used")
    private String paymentMethod; // "RAZORPAY", "MANUAL", "BANK_TRANSFER", etc.
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
