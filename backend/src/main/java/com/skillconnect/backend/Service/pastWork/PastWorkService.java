package com.skillconnect.backend.Service.pastWork;

import com.skillconnect.backend.DTO.PastWorkDTO;
import com.skillconnect.backend.Entity.PastWork;

import java.util.List;

public interface PastWorkService {
    PastWorkDTO addPastWork(PastWorkDTO dto);
    List<PastWork> getPastWorkByFreelancerId(Long freelancerId);
    PastWork updatePastWork(Long id, PastWorkDTO dto);
    void deletePastWork(Long id);

}
