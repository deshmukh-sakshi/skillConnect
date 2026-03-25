package com.skillconnect.backend.Service.contract;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ContractResponse;
import com.skillconnect.backend.Entity.Bids;
import com.skillconnect.backend.Entity.Contract;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.BidRepository;
import com.skillconnect.backend.Repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @SuppressWarnings("unused")
    @Mock
    private BidRepository bidRepository;

    @InjectMocks
    private ContractServiceImpl contractService;

    @Test
    void createContract_withValidBid_savesAndReturnsDto() {
        Project project = new Project();
        project.setId(101L);

        Bids bid = new Bids();
        bid.setId(102L);
        bid.setProject(project);

        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> {
            Contract contract = invocation.getArgument(0);
            contract.setContractId(103L);
            contract.setContractStatus(Contract.ContractStatus.IN_PROGRESS);
            return contract;
        });

        contractService.createContract(bid);

        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    void createContract_withMissingProjectOnBid_throwsRuntimeException() {
        Bids bid = new Bids();
        bid.setId(12L);
        bid.setProject(null);

        assertThrows(NullPointerException.class, () -> contractService.createContract(bid));

        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void getAllContracts_returnsSuccessEnvelopeAndMappedDtos() {
        Project project = new Project();
        project.setId(100L);

        Bids bid = new Bids();
        bid.setId(200L);

        Contract contract = new Contract();
        contract.setContractId(300L);
        contract.setProject(project);
        contract.setBid(bid);
        contract.setContractStatus(Contract.ContractStatus.IN_PROGRESS);
        contract.setCreatedAt(LocalDateTime.of(2026, 3, 20, 10, 0));
        contract.setUpdatedAt(LocalDateTime.of(2026, 3, 21, 11, 0));

        when(contractRepository.findAll()).thenReturn(List.of(contract));

        ApiResponse<List<ContractResponse>> response = contractService.getAllContracts();

        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());

        ContractResponse dto = response.getData().getFirst();
        assertEquals(300L, dto.getContractId());
        assertEquals(100L, dto.getProjectId());
        assertEquals(200L, dto.getBidId());
        assertEquals("IN_PROGRESS", dto.getContractStatus());
    }

    @Test
    void getContractById_whenFound_returnsSuccessEnvelope() {
        Project project = new Project();
        project.setId(9L);

        Bids bid = new Bids();
        bid.setId(8L);

        Contract contract = new Contract();
        contract.setContractId(7L);
        contract.setProject(project);
        contract.setBid(bid);
        contract.setContractStatus(Contract.ContractStatus.COMPLETED);

        when(contractRepository.findById(7L)).thenReturn(Optional.of(contract));

        ApiResponse<ContractResponse> response = contractService.getContractById(7L);

        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals(7L, response.getData().getContractId());
        assertEquals("COMPLETED", response.getData().getContractStatus());
    }

    @Test
    void getContractById_whenMissing_throwsRuntimeException() {
        when(contractRepository.findById(55L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contractService.getContractById(55L));

        assertEquals("Contract not found", ex.getMessage());
    }
}


