package com.skillconnect.backend.Chat.DTO;

import com.skillconnect.backend.Chat.Enum.MilestoneStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MilestoneStatusRequest {

    @NotNull(message = "Milestone status is required")
    private MilestoneStatus status;
}