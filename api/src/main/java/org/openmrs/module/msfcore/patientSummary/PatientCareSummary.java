package org.openmrs.module.msfcore.patientSummary;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Encounter;
import org.openmrs.Visit;
import org.openmrs.module.msfcore.patientSummary.PatientSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCareSummary extends PatientSummary {
    /***
     * TODO: VISIT SUMMARY CHANGES
     */
    @Builder.Default
    private VisitSummary visitSummary;

    @Builder.Default
    private String provider = "";
    // TODO probably use complex obs or what?
    private Object signature;
}
