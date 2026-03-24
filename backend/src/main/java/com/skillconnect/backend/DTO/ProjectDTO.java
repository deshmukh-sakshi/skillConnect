package com.skillconnect.backend.DTO;

import com.skillconnect.backend.Entity.Project;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime deadline;
    private Long budget;
    private Project.ProjectStatus status;
    private Long clientId;
}
