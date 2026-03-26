package com.skillconnect.backend.Service.contract;

import java.util.List;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ContractResponse;
import com.skillconnect.backend.Entity.Bids;

public interface ContractService {
	void createContract(Bids bid);
	ApiResponse<List<ContractResponse>> getAllContracts();
	ApiResponse<ContractResponse> getContractById(Long id);
	ApiResponse<ContractResponse> updateContract(Long id, String contractStatus);
	ApiResponse<String> deleteContract(Long id);
}
