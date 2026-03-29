package com.skillconnect.backend.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Data
@Schema(description = "Request object for updating freelancer profile information")
public class FreelancerUpdateDTO {

    @Schema(
            description = "Updated full name of the freelancer",
            example = "John Smith",
            requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(
            description = "Updated list of past work and portfolio items. Existing items can be modified or marked for deletion, new items can be added"
    )
    @Valid // Cascade validation to past work objects
    private List<PastWorkUpdateDTO> pastWorks;

    @Schema(
            description = "Updated list of skills and technologies the freelancer specializes in",
            example = "[\"Java\", \"Spring Boot\", \"React\", \"PostgreSQL\", \"Docker\"]"
    )
    private List<String> skills;
}