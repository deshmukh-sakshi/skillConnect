package com.skillconnect.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReportResponse {

    private Long id;
    private Long projectId;
    private String projectTitle;
    private String projectCategory;
    private Long reportedByUserId;
    private String reportedByName;
    private String reportedByEmail;
    private String reason;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}
