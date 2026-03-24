package com.skillconnect.backend.Service.contract;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ContractResponse;
import com.skillconnect.backend.Entity.Bids;
import com.skillconnect.backend.Entity.Contract;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.BidRepository;
import com.skillconnect.backend.Repository.ContractRepository;
import lombok.*;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
	private final ContractRepository contractRepository;
	private final BidRepository bidRepository;
	
	@Override
	public ContractResponse createContract(Bids bid) {

	    // Fetch the associated project from the bid
	    Project project = bid.getProject();
	    if (project == null) {
	        throw new RuntimeException("Project associated with the bid not found.");
	    }

	    Contract contract = new Contract();
	    contract.setProject(project);
	    contract.setBid(bid);

	    Contract savedContract = contractRepository.save(contract);

	    return toDTO(savedContract);
	}
	
	@Override
	public ApiResponse<List<ContractResponse>> getAllContracts(){
		List<Contract> contracts = contractRepository.findAll();

		List<ContractResponse> responseList = contracts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ApiResponse.success(responseList);
	}
	
	public ApiResponse<ContractResponse> getContractById(Long id) {
		Contract contract = contractRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Contract not found"));
		
		return ApiResponse.success(toDTO(contract));
	}
	
	private ContractResponse toDTO(Contract contract) {
        return ContractResponse.builder()
                .contractId(contract.getContractId())
                .projectId(contract.getProject().getId())
                .bidId(contract.getBid().getId())
                .contractStatus(contract.getContractStatus().name())
                .createdAt(contract.getCreatedAt())
                .updatedAt(contract.getUpdatedAt())
                .build();
    }
}
