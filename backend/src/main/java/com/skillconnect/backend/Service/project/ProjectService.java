package com.skillconnect.backend.Service.project;

import com.skillconnect.backend.DTO.ProjectDTO;

import java.util.List;

public interface ProjectService {
     ProjectDTO createProject(ProjectDTO dto);
     List<ProjectDTO> getAllProjects();
     ProjectDTO getProjectById(Long id);
     boolean deleteProjectById(Long id);
}
