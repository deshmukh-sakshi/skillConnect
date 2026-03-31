package com.skillconnect.backend.Service.report;

import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.Auth.Repository.AppUserRepository;
import com.skillconnect.backend.DTO.ReportRequest;
import com.skillconnect.backend.DTO.ReportResponse;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Entity.Report;
import com.skillconnect.backend.Repository.ProjectRepository;
import com.skillconnect.backend.Repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public void reportProject(Long projectId, String reporterEmail, ReportRequest request) {
        log.info("Reporting project ID: {} by user: {}", projectId, reporterEmail);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        AppUser reporter = appUserRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (reportRepository.existsByReportedBy_IdAndProject_Id(reporter.getId(), projectId)) {
            throw new IllegalStateException("You have already reported this project");
        }

        String resolvedName = reporter.getEmail();
        if (reporter.getFreelancerProfile() != null) {
            resolvedName = reporter.getFreelancerProfile().getName();
        } else if (reporter.getClientProfile() != null) {
            resolvedName = reporter.getClientProfile().getName();
        }

        Report report = new Report();
        report.setProject(project);
        report.setReportedBy(reporter);
        report.setReporterName(resolvedName);
        report.setReporterEmail(reporter.getEmail());
        report.setReason(request.getReason());
        report.setDescription(request.getDescription());
        report.setStatus(Report.ReportStatus.PENDING);

        reportRepository.save(report);
        log.info("Report saved for project ID: {}", projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponse> getAllPendingReports() {
        log.info("Fetching all pending reports");
        return reportRepository.findByStatusOrderByCreatedAtDesc(Report.ReportStatus.PENDING)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void dismissReport(Long reportId) {
        log.info("Dismissing report ID: {}", reportId);
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(Report.ReportStatus.DISMISSED);
        reportRepository.save(report);
    }

    @Override
    public void deleteProjectByAdmin(Long projectId, Long reportId) {
        log.info("Admin deleting project ID: {} via report ID: {}", projectId, reportId);

        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found");
        }

        // Delete all reports for this project first to avoid FK constraint violation
        List<Report> projectReports = reportRepository.findByProject_Id(projectId);
        reportRepository.deleteAll(projectReports);

        projectRepository.deleteById(projectId);
        log.info("Project ID: {} deleted by admin", projectId);
    }

    private ReportResponse toResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getProject().getId(),
                report.getProject().getTitle(),
                report.getProject().getCategory(),
                report.getReportedBy().getId(),
                report.getReporterName(),
                report.getReporterEmail(),
                report.getReason(),
                report.getDescription(),
                report.getStatus().name(),
                report.getCreatedAt()
        );
    }
}
