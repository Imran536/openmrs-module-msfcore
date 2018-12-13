SELECT ID.IDENTIFIER AS PATIENT_ID,UPPER(CONCAT(NM.GIVEN_NAME, ' ', NM.FAMILY_NAME)) AS PATIENT_NAME, PR.GENDER as Gender, YEAR(CURRENT_DATE()) - YEAR(PR.BIRTHDATE) AS AGE,PA.value as Contact,
LO.NAME as Facility_Name,EN.encounter_id,
(select distinct(group_concat(if(PT.patient_id=ED.patient_id , (Select name from `openmrs`.`concept_name` where concept_id=(concat( ED.diagnosis_coded)) and LOCALE_PREFERRED= 1 and voided=0 and locale='en'), NULL)))) AS Diagnose,
ats.start_date as appointment_start_date,date(EN.encounter_datetime) date_last_encounter, TIMESTAMPDIFF(MONTH,date(EN.encounter_datetime),current_date()) as month_last_seen
-- group_concat(if(o_sc.concept_id= 165718,(Select name from `openmrs`.`concept_name` where concept_id=(concat(o_sc.value_coded)) and LOCALE_PREFERRED= 1 and voided=0 and locale='en'), NULL)) AS Patient_Condition,
FROM
 `openmrs`.`patient` AS PT 
INNER JOIN `openmrs`.`person` AS PR ON PR.PERSON_ID = PT.PATIENT_ID 
INNER JOIN `openmrs`.`person_name` AS NM ON NM.PERSON_ID = PT.PATIENT_ID and NM.voided=0
INNER JOIN `openmrs`.`patient_identifier` AS ID ON ID.PATIENT_ID = PT.PATIENT_ID AND ID.IDENTIFIER_TYPE = 4 
left JOIN `openmrs`.`person_attribute` as PA on PA.person_id=PT.PATIENT_ID and person_attribute_type_id=8
left JOIN `openmrs`.`encounter` AS EN ON EN.PATIENT_ID = PT.PATIENT_ID 
and EN.encounter_id=(select max(encounter_id) from encounter where patient_id=PT.patient_id)
left JOIN `openmrs`.`encounter_diagnosis` AS ED ON  ED.PATIENT_ID=PT.PATIENT_ID and ED.voided= 0 

INNER JOIN `openmrs`.`location` AS LO ON LO.LOCATION_ID = ID.LOCATION_ID 
inner join `openmrs`.`appointmentscheduling_appointment` AA on AA.patient_id=PT.patient_id and AA.status='SCHEDULED' 
and AA.appointment_id=(select max(appointment_id) from `appointmentscheduling_appointment` where patient_id=PT.patient_id)
left join `openmrs`.`appointmentscheduling_time_slot` ats on ats.time_slot_id=AA.time_slot_id

where PT.voided= 0  and TIMESTAMPDIFF(MONTH,date(ats.start_date),current_date())>3
and date(ats.start_date) between :startDate and :endDate
and ID.location_id in (:location) 
group by AA.appointment_id order by ats.start_date