package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ProjectDTO;
import com.skillconnect.backend.DTO.ReportResponse;
import com.skillconnect.backend.Service.project.ProjectService;
import com.skillconnect.backend.Service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final ReportService reportService;
    private final ProjectService projectService;

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getPendingReports() {
        List<ReportResponse> reports = reportService.getAllPendingReports();
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ApiResponse<ProjectDTO>> getProjectDetails(@PathVariable Long projectId) {
        ProjectDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @DeleteMapping("/projects/{projectId}/reports/{reportId}")
    public ResponseEntity<ApiResponse<String>> deleteProject(
            @PathVariable Long projectId,
            @PathVariable Long reportId
    ) {
        reportService.deleteProjectByAdmin(projectId, reportId);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully"));
    }

    @PutMapping("/reports/{reportId}/dismiss")
    public ResponseEntity<ApiResponse<String>> dismissReport(@PathVariable Long reportId) {
        reportService.dismissReport(reportId);
        return ResponseEntity.ok(ApiResponse.success("Report dismissed"));
    }
}
