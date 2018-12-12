package org.openmrs.module.msfcore.patientSummary;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Encounter;
import org.openmrs.Visit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientSummary {
    private Representation representation;
    @Builder.Default
    private String reportDate = "";
    @Builder.Default
    private String facility = "";
    @Builder.Default
    private String programName = "";
    @Builder.Default
    private String lastAppointmentDate = "";
    @Builder.Default
    private String nextAppointmentDate = "";
    @Builder.Default
    private String patientIdentifier = "";
    private Demographics demographics;
    @Builder.Default
    private List<Vitals> vitals = new ArrayList<Vitals>();
    @Builder.Default
    private List<PatientDiagnosis> diagnoses = new ArrayList<PatientDiagnosis>();
    @Builder.Default
    private List<Allergy> allergies = new ArrayList<Allergy>();
    private ClinicalHistory clinicalHistory;
    @Builder.Default
    private List<Observation> recentLabResults = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> currentMedications = new ArrayList<Observation>();
    @Builder.Default
    private List<Observation> clinicalNotes = new ArrayList<Observation>();
    @Builder.Default
    private PatientAddress address;
    //Full Patient Record Variables
    @Builder.Default
    private List<Disease> visitDiagnosis = new ArrayList<Disease>();
    @Builder.Default
    private List<org.openmrs.module.msfcore.patientSummary.Encounter> encounters = new ArrayList<>();
    @Builder.Default
    private List<Visit> visits = new ArrayList<Visit>();
    @Builder.Default
    private List<ClinicalHistory> clinicalHistoryList = new ArrayList<ClinicalHistory>();
    @Builder.Default
    private List<ClinicalHistory> clinicalHistoryListBaseline = new ArrayList<ClinicalHistory>();
    @Builder.Default
    private List<ClinicalHistory> clinicalHistoryListFup = new ArrayList<ClinicalHistory>();
    @Builder.Default
    private List<Medication> medicationList = new ArrayList<Medication>();
    @Builder.Default
    private List<PlannedAppointment> appointmentList = new ArrayList<PlannedAppointment>();
    @Builder.Default
    private List<PatientReferral> patientReferrals = new ArrayList<PatientReferral>();
    
    /***
     * TODO: VISIT SUMMARY CHANGES
     */
    @Builder.Default
    private VisitSummary visitSummary;
    
    @Builder.Default
    private String provider = "";
    // TODO probably use complex obs or what?
    private Object signature;

    /**
     * Only set some properties on this object tree at Representation.FULL
     */
    public enum Representation {
        SUMMARY, FULL
    }
}
