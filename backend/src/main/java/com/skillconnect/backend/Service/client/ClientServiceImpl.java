package com.skillconnect.backend.Service.client;

import com.skillconnect.backend.DTO.ClientDTO;
import com.skillconnect.backend.DTO.ProjectDTO;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDTO getClientDTOById(Long id) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) return null;

        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for (Project p : client.getProjects()) {
            Long clientId = (p.getClient() != null) ? p.getClient().getId() : null;

            ProjectDTO dto = new ProjectDTO(
                    p.getId(),
                    p.getTitle(),
                    p.getDescription(),
                    p.getCategory(),
                    p.getDeadline(),
                    p.getBudget(),
                    p.getStatus(),
                    clientId
            );

            projectDTOs.add(dto);
        }

        String email = client.getAppUser() != null ? client.getAppUser().getEmail() : null;
        return new ClientDTO(client.getName(), email, projectDTOs);
    }

    @Override
    public boolean deleteClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) return false;
        clientRepository.deleteById(clientId);
        return true;
    }
}
