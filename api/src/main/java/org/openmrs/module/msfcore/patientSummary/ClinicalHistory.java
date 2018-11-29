package org.openmrs.module.msfcore.patientSummary;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicalHistory {
    @Builder.Default
    private String date;
    @Builder.Default
    private List<Observation> medical = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> social = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> family = new ArrayList<Observation>();
    // assesment/conclusion
    private String assessment;
    @Builder.Default
    private List<Observation> complications = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> targetOrganDamages = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> cardiovascularCholesterolScore = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> bloodGlucose = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> analysisType = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> patientEducation = new ArrayList<Observation>();

    //Added for full representation
    @Builder.Default
    private String clinicalNote;
}
