package com.skillconnect.backend.Repository;
import com.skillconnect.backend.Entity.Bids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bids, Long> {
}
