package com.skillconnect.backend.Service.contact;

import com.skillconnect.backend.DTO.ContactRequestDTO;
import com.skillconnect.backend.DTO.ContactResponseDTO;

public interface ContactService {
    ContactResponseDTO submitContactRequest(ContactRequestDTO contactRequestDTO, Long userId);
}