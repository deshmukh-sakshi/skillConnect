package com.skillconnect.backend.Wallet.Service;

import com.skillconnect.backend.Repository.ProjectRepository;
import com.skillconnect.backend.Wallet.DTO.WalletRequestDTO;
import com.skillconnect.backend.Wallet.DTO.WalletResponseDTO;
import com.skillconnect.backend.Wallet.DTO.WithdrawRequestDTO;
import com.skillconnect.backend.Wallet.Entity.Wallet;
import com.skillconnect.backend.Wallet.Entity.WalletFreeze;
import com.skillconnect.backend.Wallet.Entity.WalletTransaction;
import com.skillconnect.backend.Wallet.Repository.WalletFreezeRepository;
import com.skillconnect.backend.Wallet.Repository.WalletRepository;
import com.skillconnect.backend.Wallet.Repository.WalletTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletTransactionRepository transactionRepository;

    @Mock
    private WalletFreezeRepository walletFreezeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(walletService, "transactionRepository", transactionRepository);
        ReflectionTestUtils.setField(walletService, "projectRepository", projectRepository);
    }

    @Test
    void createWallet_success_savesAndReturnsDTO() {
        when(walletRepository.findByUserIdAndRole(1L, "CLIENT")).thenReturn(Optional.empty());
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> {
            Wallet w = i.getArgument(0);
            w.setWalletId(100L);
            return w;
        });

        WalletResponseDTO result = walletService.createWallet(1L, "ROLE_CLIENT");

        assertNotNull(result);
        assertEquals(100L, result.getWalletId());
        assertEquals(1L, result.getUserId());
        assertEquals("CLIENT", result.getRole());
        assertEquals(0.0, result.getAvailableBalance());
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void createWallet_alreadyExists_throwsException() {
        when(walletRepository.findByUserIdAndRole(2L, "FREELANCER"))
                .thenReturn(Optional.of(new Wallet()));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                walletService.createWallet(2L, "ROLE_FREELANCER")
        );
        assertEquals("Wallet already exists for this user and role", ex.getMessage());
    }

    @Test
    void addMoney_success_increasesBalanceAndLogsTransaction() {
        Wallet wallet = new Wallet();
        wallet.setWalletId(10L);
        wallet.setUserId(1L);
        wallet.setRole("CLIENT");
        wallet.setAvailableBalance(500.0);

        WalletRequestDTO request = new WalletRequestDTO();
        request.setUserId(1L);
        request.setRole("CLIENT");
        request.setAmount(200.0);

        when(walletRepository.findByUserIdAndRole(1L, "CLIENT")).thenReturn(Optional.of(wallet));

        WalletResponseDTO response = walletService.addMoney(request);

        assertEquals(700.0, wallet.getAvailableBalance());
        assertEquals(700.0, response.getAvailableBalance());

        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(WalletTransaction.class));
    }

    @Test
    void withdraw_success_decreasesBalanceAndLogsTransaction() {
        Wallet wallet = new Wallet();
        wallet.setWalletId(20L);
        wallet.setUserId(2L);
        wallet.setRole("FREELANCER");
        wallet.setAvailableBalance(1000.0);

        WithdrawRequestDTO request = new WithdrawRequestDTO();
        request.setUserId(2L);
        request.setAmount(300.0);

        when(walletRepository.findByUserIdAndRole(2L, "FREELANCER")).thenReturn(Optional.of(wallet));

        WalletResponseDTO response = walletService.withdraw(request);

        assertEquals(700.0, wallet.getAvailableBalance());
        assertEquals(700.0, response.getAvailableBalance());

        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(WalletTransaction.class));
    }

    @Test
    void withdraw_insufficientFunds_throwsException() {
        Wallet wallet = new Wallet();
        wallet.setAvailableBalance(100.0);

        WithdrawRequestDTO request = new WithdrawRequestDTO();
        request.setUserId(2L);
        request.setAmount(300.0);

        when(walletRepository.findByUserIdAndRole(2L, "FREELANCER")).thenReturn(Optional.of(wallet));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                walletService.withdraw(request)
        );
        assertEquals("Insufficient balance for withdrawal.", ex.getMessage());
    }

    @Test
    void freezeAmount_success_movesAvailableToFrozen() {
        Wallet clientWallet = new Wallet();
        clientWallet.setAvailableBalance(1000.0);
        clientWallet.setFrozenBalance(0.0);

        when(walletRepository.findByUserIdAndRole(1L, "CLIENT")).thenReturn(Optional.of(clientWallet));

        walletService.freezeAmount(1L, 99L, 500.0);

        assertEquals(500.0, clientWallet.getAvailableBalance());
        assertEquals(500.0, clientWallet.getFrozenBalance());

        verify(walletRepository).save(clientWallet);
        verify(walletFreezeRepository).save(any(WalletFreeze.class));
        verify(transactionRepository).save(any(WalletTransaction.class));
    }

    @Test
    void freezeAmount_insufficientFunds_throwsException() {
        Wallet clientWallet = new Wallet();
        clientWallet.setAvailableBalance(100.0);

        when(walletRepository.findByUserIdAndRole(1L, "CLIENT")).thenReturn(Optional.of(clientWallet));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                walletService.freezeAmount(1L, 99L, 500.0)
        );
        assertEquals("Insufficient funds to accept this bid.", ex.getMessage());
    }

    @Test
    void releasePayment_success_transfersFrozenToFreelancerAvailable() {
        WalletFreeze freeze = new WalletFreeze();
        freeze.setProjectId(99L);
        freeze.setClientId(1L);
        freeze.setStatus("FROZEN");
        freeze.setAmount(500.0);

        Wallet clientWallet = new Wallet();
        clientWallet.setFrozenBalance(500.0);
        clientWallet.setUserId(1L);

        Wallet freelancerWallet = new Wallet();
        freelancerWallet.setAvailableBalance(100.0);
        freelancerWallet.setUserId(2L);

        when(walletFreezeRepository.findByProjectIdAndClientIdAndStatus(99L, 1L, "FROZEN"))
                .thenReturn(freeze);
        when(walletRepository.findByUserIdAndRole(1L, "CLIENT"))
                .thenReturn(Optional.of(clientWallet));
        when(walletRepository.findByUserIdAndRole(2L, "FREELANCER"))
                .thenReturn(Optional.of(freelancerWallet));

        walletService.releasePayment(1L, 2L, 99L, 500.0);

        assertEquals("RELEASED", freeze.getStatus());
        assertEquals(0.0, clientWallet.getFrozenBalance());
        assertEquals(600.0, freelancerWallet.getAvailableBalance());

        verify(walletFreezeRepository).save(freeze);
        verify(walletRepository).save(clientWallet);
        verify(walletRepository).save(freelancerWallet);
        verify(transactionRepository, times(2)).save(any(WalletTransaction.class));
    }
}
