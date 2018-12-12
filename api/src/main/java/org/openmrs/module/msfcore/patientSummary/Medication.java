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
    private String quantity;
    @Builder.Default
    private String duration;
    @Builder.Default
    private String dispensed;
    @Builder.Default
    private String prescriptionDate;
    @Builder.Default
    private String status = "";
}
