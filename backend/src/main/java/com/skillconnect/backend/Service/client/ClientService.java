package com.skillconnect.backend.Service.client;

import com.skillconnect.backend.DTO.ClientDTO;

public interface ClientService {
    ClientDTO getClientDTOById(Long clientId);

    boolean deleteClient(Long clientId);
}
