package com.skillconnect.backend.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    description = "Request payload for submitting a contact form",
    example = """
        {
          "email": "user@example.com",
          "subject": "Bug Report",
          "message": "I found an issue with the project submission form...",
          "userType": "LOGGED_IN"
        }
        """
)
public class ContactRequestDTO {
    
    @Schema(
        description = "Email address of the person submitting the contact form",
        example = "user@example.com",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @Schema(
        description = "Subject line for the contact request",
        example = "Bug Report",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject must not exceed 200 characters")
    private String subject;
    
    @Schema(
        description = "Detailed message describing the inquiry or issue",
        example = "I found an issue with the project submission form...",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "Message is required")
    @Size(max = 5000, message = "Message must not exceed 5000 characters")
    private String message;
    
    @Schema(
        description = "Type of user submitting the form",
        example = "LOGGED_IN",
        allowableValues = {"LOGGED_IN", "GUEST"},
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "User type is required")
    private String userType;
}