package com.skillconnect.backend.Wallet.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Monthly revenue breakdown and analytics for a freelancer")
@Data
@Builder
public class MonthlyRevenueDTO {

    @Schema(
            description = "Total revenue earned in the current month",
            example = "3250.00",
            requiredMode = RequiredMode.REQUIRED
    )
    private Double currentMonth;

    @Schema(
            description = "Total revenue earned in the previous month",
            example = "2875.50",
            requiredMode = RequiredMode.REQUIRED
    )
    private Double lastMonth;

    @Schema(
            description = "Total revenue earned in the current year",
            example = "15750.50",
            requiredMode = RequiredMode.REQUIRED
    )
    private Double currentYear;

    @Schema(
            description = "Monthly revenue data points for trend analysis",
            requiredMode = RequiredMode.REQUIRED
    )
    private List<MonthlyDataPoint> monthlyData;
}