package org.openmrs.module.msfcore.audit;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = AuditLog.TABLE_NAME)
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1144489098510052829L;

    protected static final String TABLE_NAME = "msfcore_audit_log";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Date date = new Date();

    @Column(nullable = false, updatable = false)
    private String detail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Event event;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "patient_id", updatable = false)
    private Patient patient;

    @ManyToOne(optional = true)
    @JoinColumn(name = "provider_id", updatable = false)
    private Provider provider;

    @ManyToOne(optional = true)
    @JoinColumn(name = "location_id", updatable = false)
    private Location location;

    @Column(unique = true, nullable = false, length = 38, updatable = false)
    @Builder.Default
    private String uuid = UUID.randomUUID().toString();

    public enum Event {
        LOGIN, VIEW_PATIENT, REGISTER_PATIENT, LOGOUT, REQUEST_PATIENT_SUMMARY, ADD_LAB_RESULT, EDIT_LAB_RESULT, VIEW_LAB_RESULTS, VIEW_DRUG_DISPENSING, ADD_DRUG_DISPENSING, EDIT_DRUG_DISPENSING, RUN_REPORT, ADD_REFERRAL, EDIT_REFERRAL, VIEW_PATIENT_REFERRALS
    }

}
