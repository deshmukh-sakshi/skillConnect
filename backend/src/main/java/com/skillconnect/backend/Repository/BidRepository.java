package com.skillconnect.backend.Repository;

import com.skillconnect.backend.Entity.Bids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bids, Long> {
    List<Bids> findByProject_Id(Long projectId);

    List<Bids> findByFreelancer_Id(Long freelancerId);

    boolean existsByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
}
