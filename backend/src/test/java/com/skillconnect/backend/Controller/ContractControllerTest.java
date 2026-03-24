package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ContractResponse;
import com.skillconnect.backend.Service.contract.ContractService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractController contractController;

    @Test
    void getAllContracts_returnsOkEnvelope() {
        ContractResponse dto = ContractResponse.builder().contractId(1L).build();
        ApiResponse<List<ContractResponse>> payload = ApiResponse.success(List.of(dto));

        when(contractService.getAllContracts()).thenReturn(payload);

        ResponseEntity<ApiResponse<List<ContractResponse>>> response = contractController.getAllContracts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getContractById_returnsOkEnvelope() {
        ContractResponse dto = ContractResponse.builder().contractId(3L).build();
        ApiResponse<ContractResponse> payload = ApiResponse.success(dto);

        when(contractService.getContractById(3L)).thenReturn(payload);

        ResponseEntity<ApiResponse<ContractResponse>> response = contractController.getContractById(3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3L, response.getBody().getData().getContractId());
    }
}

