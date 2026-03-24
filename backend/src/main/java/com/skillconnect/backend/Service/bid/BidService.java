package com.skillconnect.backend.Service.bid;

import com.skillconnect.backend.DTO.BidDTO;
import com.skillconnect.backend.DTO.BidResponseDTO;
import com.skillconnect.backend.Entity.Bids;

import java.util.List;

public interface BidService {
    Bids placeBid(BidDTO dto);
    List<BidResponseDTO> getBidsByFreelancerId(Long freelancerId);
    void deleteBid(Long bidId, Long freelancerId);
}
