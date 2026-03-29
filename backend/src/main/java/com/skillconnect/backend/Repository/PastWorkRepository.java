package com.skillconnect.backend.Repository;

import com.skillconnect.backend.Entity.PastWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PastWorkRepository extends JpaRepository<PastWork, Long> {
    List<PastWork> findByFreelancerId(Long freelancerId);
}
