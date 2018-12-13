package org.openmrs.module.msfcore.patientSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {
    @Builder.Default
    private String name;
    @Builder.Default
    private String frequency;
    @Builder.Default
    private String quantity = "_";
    @Builder.Default
    private String duration = "_";
    @Builder.Default
    private String dispensed = "_";
    @Builder.Default
    private String prescriptionDate = "_";
    @Builder.Default
    private String status = "_";
}
