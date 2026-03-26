package com.skillconnect.backend.Service.project;

import com.skillconnect.backend.DTO.BidResponseDTO;
import com.skillconnect.backend.DTO.ProjectDTO;

import java.util.List;

public interface ProjectService {
     ProjectDTO createProject(ProjectDTO dto);
     List<ProjectDTO> getAllProjects(String titleQuery);
     ProjectDTO getProjectById(Long id);
     List<BidResponseDTO> getBidsByProjectId(Long projectId);
     ProjectDTO updateProject(Long id,ProjectDTO dto);
     boolean deleteProjectById(Long id);
     void acceptBid(Long projectId, Long bidId);
     void rejectBid(Long projectId, Long bidId);
}