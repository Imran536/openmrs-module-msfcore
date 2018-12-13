package org.openmrs.module.msfcore.patientSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannedAppointment {
    @Builder.Default
    private String type;
    @Builder.Default
    private String date;
}
