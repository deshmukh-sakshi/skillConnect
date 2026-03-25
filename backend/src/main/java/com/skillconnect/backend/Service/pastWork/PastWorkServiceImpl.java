package com.skillconnect.backend.Service.pastWork;

import com.skillconnect.backend.DTO.PastWorkDTO;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.PastWork;
import com.skillconnect.backend.Repository.FreelancerRepository;
import com.skillconnect.backend.Repository.PastWorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PastWorkServiceImpl implements PastWorkService {

    private final PastWorkRepository pastWorkRepo;
    private final FreelancerRepository freelancerRepo;

    @Override
    public PastWorkDTO addPastWork(PastWorkDTO dto) {
        // Find the freelancer by ID for whom the past work is being added
        log.info("Adding past work for freelancer ID: {}", dto.getFreelancerId());
        Freelancer freelancer = freelancerRepo.findById(dto.getFreelancerId())
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));
        log.info("Freelancer found: {}", freelancer.getName());

        // Create and populate the PastWork entity
        PastWork work = new PastWork();
        work.setFreelancer(freelancer);
        work.setTitle(dto.getTitle());
        work.setLink(dto.getLink());
        work.setDescription(dto.getDescription());
        log.info("Past work created: {}", work.getTitle());

        PastWork saved = pastWorkRepo.save(work);
        log.info("Past work saved: {}", saved.getTitle());

        // Map the saved entity to a DTO for the response
        PastWorkDTO result = new PastWorkDTO();
        result.setTitle(saved.getTitle());
        result.setLink(saved.getLink());
        result.setDescription(saved.getDescription());
        result.setFreelancerId(saved.getFreelancer().getId());
        log.info("Past work added: {}", saved.getTitle());

        return result;
    }

    @Override
    public List<PastWork> getPastWorkByFreelancerId(Long freelancerId) {
        // Fetch all past works for a freelancer
        log.info("Fetching past work for freelancer ID: {}", freelancerId);
        return pastWorkRepo.findByFreelancerId(freelancerId);
    }

    @Override
    public PastWork updatePastWork(Long id, PastWorkDTO dto) {
        // Find the past work entry by ID and update its fields
        log.info("Updating past work by ID: {}", id);
        PastWork work = pastWorkRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Past work not found"));

        work.setTitle(dto.getTitle());
        work.setLink(dto.getLink());
        work.setDescription(dto.getDescription());
        log.info("Past work updated: {}", work.getTitle());
        return pastWorkRepo.save(work);
    }

    @Override
    public void deletePastWork(Long id) {
        // Delete the past work entry by its ID
        log.info("Deleting past work by ID: {}", id);
        pastWorkRepo.deleteById(id);
    }

}
