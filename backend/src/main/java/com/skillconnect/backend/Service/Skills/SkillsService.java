package com.skillconnect.backend.Service.Skills;

import com.skillconnect.backend.DTO.SkillDTO;

public interface SkillsService {
    SkillDTO addSkill(SkillDTO dto);
    void removeSkillFromFreelancer(Long freelancerId, String skillName);
}

