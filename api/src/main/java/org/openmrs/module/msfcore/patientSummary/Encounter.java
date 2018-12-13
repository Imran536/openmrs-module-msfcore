package org.openmrs.module.msfcore.patientSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Encounter {
    @Builder.Default
    private String date;
    @Builder.Default
    private String type;
    @Builder.Default
    private String provider;
}
