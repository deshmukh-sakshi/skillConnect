package com.skillconnect.backend.Service.project;

import com.skillconnect.backend.DTO.BidResponseDTO;
import com.skillconnect.backend.DTO.ProjectDTO;
import com.skillconnect.backend.Entity.Bids;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.BidRepository;
import com.skillconnect.backend.Repository.ClientRepository;
import com.skillconnect.backend.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;
    private final BidRepository bidRepo;

    @Override
    public ProjectDTO createProject(ProjectDTO dto) {
        Optional<Client> optionalClient = clientRepository.findById(dto.getClientId());
        if (optionalClient.isEmpty()) {
            throw new RuntimeException("Client not found");
        }
        Client client = optionalClient.get();


        Project project = new Project();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setDeadline(dto.getDeadline());
        project.setBudget(dto.getBudget());
        project.setClient(client);


        Project saved = projectRepository.save(project);
        return toDTO(saved);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> dtoList = new ArrayList<>();

        for (Project project : projects) {
            dtoList.add(toDTO(project));
        }

        return dtoList;
    }


    @Override
    public ProjectDTO getProjectById(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            return toDTO(project);
        } else {
            throw new RuntimeException("Project not found");
        }
    }

    @Override
    public boolean deleteProjectById(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<BidResponseDTO> getBidsByProjectId(Long projectId) {
        // Fetch and map all bids for a project to DTOs
        return bidRepo.findByProject_Id(projectId).stream().map(this::mapToDTO).toList();
    }

    private ProjectDTO toDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getDeadline(),
                project.getBudget(),
                project.getStatus(),
                project.getClient() != null ? project.getClient().getId() : null
        );
    }

    private BidResponseDTO mapToDTO(Bids bid) {
        BidResponseDTO dto = new BidResponseDTO();
        dto.setBidId(bid.getId());
        dto.setFreelancerId(bid.getFreelancer().getId());
        dto.setProjectId(bid.getProject().getId());
        dto.setProposal(bid.getProposal());
        dto.setBidAmount(bid.getBidAmount());
        dto.setDurationDays(bid.getDurationDays());
        dto.setStatus(bid.getStatus().toString());
        dto.setCreatedAt(bid.getCreatedAt());
        return dto;
    }
}
