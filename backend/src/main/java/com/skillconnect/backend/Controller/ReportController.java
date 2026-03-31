package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ReportRequest;
import com.skillconnect.backend.Service.report.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{projectId}")
    public ResponseEntity<ApiResponse<String>> reportProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ReportRequest request,
            Authentication authentication
    ) {
        String reporterEmail = authentication.getName();
        reportService.reportProject(projectId, reporterEmail, request);
        return ResponseEntity.ok(ApiResponse.success("Report submitted successfully"));
    }
}
