package com.skillconnect.backend.Service.report;

import com.skillconnect.backend.DTO.ReportRequest;
import com.skillconnect.backend.DTO.ReportResponse;

import java.util.List;

public interface ReportService {

    void reportProject(Long projectId, String reporterEmail, ReportRequest request);

    List<ReportResponse> getAllPendingReports();

    void dismissReport(Long reportId);

    void deleteProjectByAdmin(Long projectId, Long reportId);
}
