package com.skillconnect.backend.Service.contract;

import java.util.List;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ContractResponse;
import com.skillconnect.backend.Entity.Bids;

public interface ContractService {
	ContractResponse createContract(Bids bid);
	ApiResponse<List<ContractResponse>> getAllContracts();
	ApiResponse<ContractResponse> getContractById(Long id);
}
