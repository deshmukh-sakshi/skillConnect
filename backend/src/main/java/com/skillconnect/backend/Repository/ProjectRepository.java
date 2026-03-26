package com.skillconnect.backend.Repository;

import com.skillconnect.backend.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String titlePart, String categoryPart);
}
