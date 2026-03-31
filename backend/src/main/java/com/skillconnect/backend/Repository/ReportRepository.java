package com.skillconnect.backend.Repository;

import com.skillconnect.backend.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByStatusOrderByCreatedAtDesc(Report.ReportStatus status);

    boolean existsByReportedBy_IdAndProject_Id(Long userId, Long projectId);

    List<Report> findByProject_Id(Long projectId);
}
