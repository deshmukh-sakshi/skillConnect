package com.skillconnect.backend.Wallet.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletFreeze {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freezeId;

    private Long projectId;

    private Long clientId;

    private Double amount;

    private String status;
}
