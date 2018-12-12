package org.openmrs.module.msfcore.patientSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientReferral {
    @Builder.Default
    private String referredTo;
    @Builder.Default
    private String referralDate;
    @Builder.Default
    private String provider;
    @Builder.Default
    private String feedback;
}
