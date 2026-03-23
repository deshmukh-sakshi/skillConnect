package com.skillconnect.backend.Service.client;

import com.skillconnect.backend.DTO.ClientCreateDTO;
import com.skillconnect.backend.DTO.ClientDTO;

public interface ClientService {
    void createClient(ClientCreateDTO dto);
    ClientDTO getClientDTOById(Long clientId);
    boolean deleteClient(Long clientId);
}
