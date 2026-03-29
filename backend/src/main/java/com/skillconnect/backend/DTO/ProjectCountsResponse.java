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
@Schema(
        description = "Response containing project counts grouped by category",
        example = """
                {
                  "counts": [
                    {
                      "category": "Web Development",
                      "activeProjectCount": 15,
                      "lastUpdated": "2025-01-08T10:30:00"
                    },
                    {
                      "category": "Mobile Development",
                      "activeProjectCount": 8,
                      "lastUpdated": "2025-01-08T10:30:00"
                    }
                  ],
                  "totalActiveProjects": 23
                }
                """
)
public class ProjectCountsResponse {

    @Schema(
            description = "List of project counts grouped by category",
            requiredMode = RequiredMode.REQUIRED
    )
    private List<ProjectCountResponse> counts;

    @Schema(
            description = "Total number of active projects across all categories",
            example = "23",
            minimum = "0",
            requiredMode = RequiredMode.REQUIRED
    )
    private Long totalActiveProjects;
}