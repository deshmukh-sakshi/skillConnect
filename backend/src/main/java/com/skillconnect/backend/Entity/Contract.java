package com.skillconnect.backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Contract {
    @Id
    @GeneratedValue
    private long contractId;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @OneToOne
    @JoinColumn(name = "bidId")
    private Bids bid;


    private ContractStatus contractStatus = ContractStatus.IN_PROGRESS;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private Double freelancerRating;

    public enum ContractStatus {
        IN_PROGRESS,
        COMPLETED
    }
}
