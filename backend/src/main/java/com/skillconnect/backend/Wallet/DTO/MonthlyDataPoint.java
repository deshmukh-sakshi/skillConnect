package com.skillconnect.backend.Wallet.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Monthly data point containing earnings and project statistics")
@Data
@Builder
public class MonthlyDataPoint {

    @Schema(
            description = "Month identifier in YYYY-MM format",
            example = "2024-01",
            requiredMode = RequiredMode.REQUIRED
    )
    private String month;

    @Schema(
            description = "Total earnings for the specified month",
            example = "2875.50",
            requiredMode = RequiredMode.REQUIRED
    )
    private Double earnings;

    @Schema(
            description = "Number of projects completed in the specified month",
            example = "3",
            requiredMode = RequiredMode.REQUIRED
    )
    private Integer projects;
}