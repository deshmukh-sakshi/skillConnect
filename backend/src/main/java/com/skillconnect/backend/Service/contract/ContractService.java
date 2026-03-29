package com.skillconnect.backend.Service.contract;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ContractResponse;
import com.skillconnect.backend.Entity.Bids;

import java.util.List;

public interface ContractService {
    Long createContract(Bids bid);

    ApiResponse<List<ContractResponse>> getAllContracts();

    ApiResponse<ContractResponse> getContractById(Long id);

    ApiResponse<ContractResponse> updateContract(Long id, String contractStatus);

    ApiResponse<String> deleteContract(Long id);
}
