package com.smartplacement.dto;

import com.smartplacement.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    private String officerNotes;
    private String currentRound;

    // Optional offer details when status is SELECTED
    private Double offeredCtc;
    private String offerLetterUrl;
    private Boolean offerAccepted; // student accepts/declines
}
