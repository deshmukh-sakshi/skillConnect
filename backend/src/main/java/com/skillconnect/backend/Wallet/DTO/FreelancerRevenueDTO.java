package com.skillconnect.backend.Wallet.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Comprehensive revenue information for a freelancer")
@Data
@Builder
public class FreelancerRevenueDTO {

    @Schema(
            description = "Unique identifier of the freelancer",
            example = "1",
            requiredMode = RequiredMode.REQUIRED
    )
    private Long freelancerId;

    @Schema(
            description = "Full name of the freelancer",
            example = "John Doe",
            requiredMode = RequiredMode.REQUIRED
    )
    private String freelancerName;

    @Schema(
            description = "Total amount earned by the freelancer across all completed projects",
            example = "15750.50",
            requiredMode = RequiredMode.REQUIRED
    )
    private Double totalEarnings;

    @Schema(
            description = "Current available balance in the freelancer's wallet",
            example = "2500.75",
            requiredMode = RequiredMode.REQUIRED
    )
    private Double currentBalance;

    @Schema(
            description = "Total amount withdrawn by the freelancer from their wallet",
            example = "13249.75",
            requiredMode = RequiredMode.REQUIRED
    )
    private Double totalWithdrawn;

    @Schema(
            description = "Number of projects completed by the freelancer",
            example = "12",
            requiredMode = RequiredMode.REQUIRED
    )
    private Integer completedProjects;

    @Schema(
            description = "List of recent revenue transactions for the freelancer",
            requiredMode = RequiredMode.REQUIRED
    )
    private List<RevenueTransactionDTO> recentTransactions;

    @Schema(
            description = "Monthly revenue breakdown and analytics",
            requiredMode = RequiredMode.REQUIRED
    )
    private MonthlyRevenueDTO monthlyBreakdown;
}