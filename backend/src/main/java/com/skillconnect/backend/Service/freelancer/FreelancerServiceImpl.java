package com.skillconnect.backend.Service.freelancer;

import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.Auth.Repository.AppUserRepository;
import com.skillconnect.backend.DTO.FreelancerDTO;
import com.skillconnect.backend.DTO.PastWorkDTO;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.PastWork;
import com.skillconnect.backend.Entity.Skills;
import com.skillconnect.backend.Repository.FreelancerRepository;
import com.skillconnect.backend.Repository.PastWorkRepository;
import com.skillconnect.backend.Repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FreelancerServiceImpl implements FreelancerService {

    @Autowired
    private FreelancerRepository freelancerRepo;

    @Autowired
    private AppUserRepository appUserRepo;

    @Autowired
    private SkillRepository skillRepo;

    @Autowired
    private PastWorkRepository pastWorkRepo;

    @Override
    public FreelancerDTO getFreelancerProfile(Long freelancerId) {
        // Fetch the freelancer entity by ID
        Freelancer freelancer = freelancerRepo.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        AppUser appUser = freelancer.getAppUser();

        // Fetch skills and past works for the freelancer
        List<Skills> skills = skillRepo.findByFreelancers_Id(freelancerId);
        List<PastWork> pastWorks = pastWorkRepo.findByFreelancerId(freelancerId);

        // Build the FreelancerDTO response
        FreelancerDTO profile = new FreelancerDTO();
        profile.setName(freelancer.getName());
        profile.setEmail(appUser.getEmail());
        profile.setRating(freelancer.getRating());

        // Map skills to a list of skill names
        profile.setSkills(
                skills.stream()
                        .map(Skills::getName)
                        .collect(Collectors.toList())
        );

        // Map past works to DTOs (excluding freelancerId for profile response)
        profile.setPastWorks(
            pastWorks.stream().map(p -> {
                PastWorkDTO dto = new PastWorkDTO();
                dto.setTitle(p.getTitle());
                dto.setLink(p.getLink());
                dto.setDescription(p.getDescription());
                return dto;
            }).toList()
        );
        return profile;
    }

    @Override
    public void deleteFreelancer(Long id) {
        freelancerRepo.deleteById(id);
    }
}
