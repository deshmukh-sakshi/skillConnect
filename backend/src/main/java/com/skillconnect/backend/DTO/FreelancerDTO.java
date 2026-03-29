package com.skillconnect.backend.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Freelancer profile information")
public class FreelancerDTO {

    @Schema(
            description = "Full name of the freelancer",
            example = "John Smith",
            requiredMode = RequiredMode.REQUIRED
    )
    private String name;

    @Schema(
            description = "Email address of the freelancer",
            example = "john.smith@example.com",
            requiredMode = RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "Average rating of the freelancer based on client feedback",
            example = "4.5",
            minimum = "0.0",
            maximum = "5.0"
    )
    private Double rating;

    @Schema(
            description = "List of skills and technologies the freelancer specializes in",
            example = "[\"Java\", \"Spring Boot\", \"React\", \"PostgreSQL\"]"
    )
    private List<String> skills;

    @Schema(
            description = "Portfolio of past work and projects completed by the freelancer"
    )
    private List<PastWorkDTO> pastWorks;

}

