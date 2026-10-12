package com.smartplacement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityCheckResponse {
    private boolean eligible;
    @Builder.Default
    private List<String> passedCriteria = new ArrayList<>();
    @Builder.Default
    private List<String> failedCriteria = new ArrayList<>();
    private String summary;
}
