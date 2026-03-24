package com.skillconnect.backend.Service.freelancer;
import com.skillconnect.backend.DTO.FreelancerDTO;

public interface FreelancerService {
    FreelancerDTO getFreelancerProfile(Long freelancerId);
    void deleteFreelancer(Long id);
}