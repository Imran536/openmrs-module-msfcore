package org.openmrs.module.msfcore.patientSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitSummary {
    @Builder.Default
    private String firstVisitDate;
    @Builder.Default
    private String lastVisitDate;
    @Builder.Default
    private String totalVisits;
    @Builder.Default
    private String lastSeenBy;
}
