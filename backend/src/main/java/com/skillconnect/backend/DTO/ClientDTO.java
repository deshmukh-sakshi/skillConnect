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
@Schema(description = "Client data transfer object containing client profile information and associated projects")
public class ClientDTO {

    @Schema(description = "Full name of the client", example = "Jane Doe", requiredMode = RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Email address of the client", example = "jane.doe@example.com", requiredMode = RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "List of projects posted by this client")
    private List<ProjectDTO> projects;
}
