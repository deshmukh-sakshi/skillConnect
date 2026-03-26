package com.skillconnect.backend.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ContractResponse;
import com.skillconnect.backend.Service.contract.ContractService;

import lombok.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts")
public class ContractController {
	private final ContractService contractService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<ContractResponse>>> getAllContracts(){
		return ResponseEntity.ok(contractService.getAllContracts());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ContractResponse>> getContractById(@PathVariable Long id){
		return ResponseEntity.ok(contractService.getContractById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ContractResponse>> updateContract(
	        @PathVariable Long id,
	        @RequestBody String contractStatus) {
	    return ResponseEntity.ok(contractService.updateContract(id, contractStatus));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteContract(@PathVariable Long id) {
	    return ResponseEntity.ok(contractService.deleteContract(id));
	}
	
}
