package org.openmrs.module.msfcore.patientSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAddress {
    @Builder.Default
    private String address1;
    @Builder.Default
    private String address2;
    @Builder.Default
    private String city;
    @Builder.Default
    private String country;
}
