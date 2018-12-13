SELECT
    ID.IDENTIFIER AS PATIENT_ID,
    UPPER(CONCAT(NM.GIVEN_NAME, " ", NM.FAMILY_NAME)) AS PATIENT_NAME,
    PR.GENDER AS GENDER,
    YEAR(CURRENT_DATE()) - YEAR(PR.BIRTHDATE) AS AGE,
    PA.value AS CONTACT,
    LO.NAME AS FACILITY_NAME,
    (
        SELECT DISTINCT(
            group_concat(
                if(PT.patient_id = ED.patient_id,
                    (SELECT name
                     FROM `concept_name`
                     WHERE concept_id = (concat(ED.diagnosis_coded))
                        AND LOCALE_PREFERRED = 1
                        AND voided = 0
                        AND locale = "en"
                    ),
                    NULL
                )
            )
        )
    ) AS DIAGNOSE,
    AAT.name AS APPOINTMENT_TYPE,
    ATS.start_date AS APPOINTMENT_START_DATE,
    ATS.end_date AS APPOINTMENT_END_DATE

FROM `patient` AS PT
    INNER JOIN `person` AS PR ON PR.PERSON_ID = PT.PATIENT_ID  
    INNER JOIN `person_name` AS NM ON NM.PERSON_ID = PT.PATIENT_ID AND NM.voided = 0 
    INNER JOIN `patient_identifier` AS ID ON ID.PATIENT_ID = PT.PATIENT_ID AND ID.IDENTIFIER_TYPE = 4  
    LEFT JOIN `person_attribute` as PA ON PA.person_id = PT.PATIENT_ID AND person_attribute_type_id = 8 
    LEFT JOIN `encounter_diagnosis` AS ED ON  ED.PATIENT_ID = PT.PATIENT_ID AND ED.voided = 0  
    INNER JOIN `location` AS LO ON LO.LOCATION_ID = ID.LOCATION_ID  
    INNER JOIN `appointmentscheduling_appointment` AA ON AA.patient_id = PT.patient_id AND AA.status = "SCHEDULED" 
    LEFT JOIN `appointmentscheduling_time_slot` ATS ON ATS.time_slot_id = AA.time_slot_id 
    INNER JOIN `appointmentscheduling_appointment_type` AAT ON AAT.appointment_type_id = AA.appointment_type_id 

WHERE PT.voided = 0
    AND date(ATS.start_date) BETWEEN :startDate AND :endDate
    AND ID.location_id IN (:location)

GROUP BY AA.appointment_id
ORDER BY ATS.start_date