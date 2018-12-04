/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.msfcore;

import org.springframework.stereotype.Component;

/**
 * Contains module's config.
 */
@Component("msfcore.MSFCoreConfig")
// TODO perhaps expose some of these through settings/global properties
public class MSFCoreConfig {

    public final static String MODULE_PRIVILEGE = "MSF Core Privilege";

    public final static String REGISTRATION_APP_EXTENSION_ID = "referenceapplication.registrationapp.registerPatient";

    public final static String SEARCH_APP_EXTENSION_ID = "coreapps.findPatient";

    public final static String REPORTS_APP_EXTENSION_ID = "reportingui.reports";

    public final static String CONDITIONS_EXTENSION_ID = "coreapps.conditionlist";

    public final static String RELATIONSHIP_EXTENSION_ID = "coreapps.relationships";

    public final static String LATEST_OBS_EXTENSION_ID = "coreapps.latestObsForConceptList";

    public final static String MOST_RECENT_VITALS_EXTENSION_ID = "coreapps.mostRecentVitals";

    public final static String VISIT_BY_ENCOUNTER_TYPE_EXTENSION_ID = "coreapps.visitByEncounterType";

    public final static String DIAGNOSIS_EXTENSION_ID = "coreapps.diagnoses";

    public final static String OBS_GRAPH_EXTENSION_ID = "coreapps.obsGraph";

    public final static String GP_INSTANCE_ID = "msfcore.instanceId";

    public final static String GP_EMRAPI_EMRAPIVISITSASSIGNMENTHANDLER_ENCOUNTERTYPETONEWVISITTYPEMAP = "emrapi.EmrApiVisitAssignmentHandler.encounterTypeToNewVisitTypeMap";

    public final static String LOCATION_ATTR_TYPE_CODE_UUID = "5a504478-1701-49da-9d8b-3e1d12ab6c5a";

    public final static String PATIENT_ID_TYPE_PRIMARY_UUID = "0t504478-1701-49da-9d8b-3e1d12ab6c5b";

    public final static String PATIENT_ID_TYPE_DRIVER_LICENSE_UUID = "fc734d22-8362-47bf-a1c3-bd69cdd3413e";

    public final static String PATIENT_ID_TYPE_NATIONAL_ID_UUID = "2b3549ad-2e41-4795-b1de-171164856639";

    public final static String PATIENT_ID_TYPE_INSURANCE_CARD_UUID = "2d6b5bfd-fb7a-40c6-b0eb-1c879878c052";

    public final static String PATIENT_ID_TYPE_PASSPORT_UUID = "c4222c2e-d30c-4417-8152-d08ca8809beb";

    public final static String PATIENT_ID_TYPE_UNHCR_ID_UUID = "b324339f-1505-4e98-b699-92326cdb3102";

    public final static String PATIENT_ID_TYPE_UNRWA_ID_UUID = "c964a3cc-9431-468d-92c7-e62da43dd088";

    public final static String PATIENT_ID_TYPE_OTHER_ID_UUID = "ed4a08db-ad66-4873-aa15-9b1c4b2b9fd5";

    public final static String PATIENT_ID_TYPE_SOURCE_UUID = "9a504478-1701-49da-9d8b-3e1d12ab6c5z";

    public final static String PATIENT_ID_TYPE_OLD_PATIENT_ID_UUID = "4a504478-1701-49da-9d8b-3e1d12ab6c5s";

    public final static String MSF_ID_BASE_CHARACTER_SET = "0123456789";

    public final static String CONCEPT_NATIONALITY_UUID = "c901e362-0499-48a0-9a07-74e2d71bcd90";

    public final static String CONCEPT_PROVENANCE_UUID = "bf69e0e0-2de4-4d97-b15e-0fba26cc45b0";

    public final static String CONCEPT_MARITAL_STATUS_UUID = "3fb0d2d1-05ff-4390-8ec7-2e4665853f0e";

    public final static String CONCEPT_EDUCATION_UUID = "d04dba9f-26d5-4ea2-8967-c681ed108e84";

    public final static String CONCEPT_CONDITION_OF_LIVING_UUID = "aa981228-bafa-442f-84fa-7e592f1e0bbc";

    public final static String CONCEPT_EMPLOYMENT_STATUS_UUID = "82494b0f-0049-44ba-b166-13ab352fa408";

    public final static String PERSON_ATTRIBUTE_PHONE_NUMBER_UUID = "14d4f066-15f5-102d-96e4-000c29c2a5d7";

    public final static String PERSON_ATTRIBUTE_HEALTH_CENTER_UUID = "8d87236c-c2cc-11de-8d13-0010c6dffd0f";

    public final static String PERSON_ATTRIBUTE_NATIONALITY_UUID = "4a504478-1701-49da-9d8b-3e1d12ab6c5o";

    public final static String PERSON_ATTRIBUTE_PROVENANCE_UUID = "4a504478-1701-49da-9d8b-3e1d12ab6c5p";

    public final static String PERSON_ATTRIBUTE_TELEPHONE_UUID = "14d4f066-15f5-102d-96e4-000c29c2a5d7";

    public final static String PERSON_ATTRIBUTE_MARITAL_STATUS_UUID = "4a504478-1701-49da-9d8b-3e1d12ab6c5q";

    public final static String PERSON_ATTRIBUTE_CONDITION_OF_LIVING_UUID = "b6eddbee-1721-4dfa-a79a-8d545043da35";

    public final static String PERSON_ATTRIBUTE_EDUCATION_UUID = "15ab4723-ca8e-4b60-8366-9cc859195f89";

    public final static String PERSON_ATTRIBUTE_EMPLOYMENT_STATUS_UUID = "4a504478-1701-49da-9d8b-3e1d12ab6c5r";

    public final static String PERSON_ATTRIBUTE_DATE_OF_ARRIVAL_UUID = "d03313d1-12fc-4b0b-ba6c-c4873bee3be6";

    public final static String PERSON_ATTRIBUTE_OLD_FACILITY_CODE_UUID = "4a504478-1701-49da-9d8b-3e1d12ab6c5t";

    public final static String PERSON_ATTRIBUTE_OTHER_ID_NAME_UUID = "63a75b39-de46-4c53-b7c3-53e07fa3ec9e";

    // TODO use
    // org.openmrs.module.registrationcore.RegistrationCoreConstants#GP_OPENMRS_IDENTIFIER_SOURCE_ID
    public final static String GP_OPENMRS_IDENTIFIER_SOURCE_ID = "registrationcore.identifierSourceId";

    public final static String LOCATION_TAG_UUID_MISSION = "76433e9a-8b93-11e8-9eb6-529269fb1459";

    public final static String LOCATION_TAG_UUID_PROJECT = "76433e9a-8b93-11e8-9eb6-529269fb1460";

    public final static String LOCATION_TAG_UUID_CLINIC = "76433e9a-8b93-11e8-9eb6-529269fb1461";

    public final static String LOCATION_LEBANON_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1459";

    public final static String LOCATION_UKRAINE_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1460";

    public final static String LOCATION_CODE_ATTRIBUTE_LEBANON_UUID = "e02d72fa-8b96-11e8-9eb6-529269fb1459";

    public final static String LOCATION_CODE_ATTRIBUTE_UKRAINE_UUID = "e02d72fa-8b96-11e8-9eb6-529269fb1460";

    public final static String LOCATION_BEKAA_VALLEY_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1461";

    public final static String LOCATION_CODE_ATTRIBUTE_BEKAA_VALLEY_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1462";

    public final static String LOCATION_TRIPOLI_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1463";

    public final static String LOCATION_CODE_ATTRIBUTE_TRIPOLI_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1464";

    public final static String LOCATION_WEST_DONETSK_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1465";

    public final static String LOCATION_CODE_ATTRIBUTE_WEST_DONETSK_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1466";

    public final static String LOCATION_MARIUPOL_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1467";

    public final static String LOCATION_CODE_ATTRIBUTE_MARIUPOL_UUID = "4b1e44b6-8b94-11e8-9eb6-529269fb1468";

    public final static String LOCATION_CODE_ATTRIBUTE_AARSAL_UUID = "77081024-8b9f-11e8-9eb6-529269fb1459";

    public final static String LOCATION_CODE_ATTRIBUTE_BAALBAK_UUID = "77081024-8b9f-11e8-9eb6-529269fb1460";

    public final static String LOCATION_CODE_ATTRIBUTE_HERMEL_UUID = "77081024-8b9f-11e8-9eb6-529269fb1461";

    public final static String LOCATION_CODE_ATTRIBUTE_MAJDAL_ANJAR_UUID = "77081024-8b9f-11e8-9eb6-529269fb1462";

    public final static String LOCATION_CODE_ATTRIBUTE_ABDEH_UUID = "77081024-8b9f-11e8-9eb6-529269fb1463";

    public final static String LOCATION_CODE_ATTRIBUTE_DAZ_UUID = "77081024-8b9f-11e8-9eb6-529269fb1464";

    public final static String LOCATION_CODE_ATTRIBUTE_BEREZOVE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1465";

    public final static String LOCATION_CODE_ATTRIBUTE_KAMIANKA_2_UUID = "77081024-8b9f-11e8-9eb6-529269fb1466";

    public final static String LOCATION_CODE_ATTRIBUTE_KURAKHOVE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1467";

    public final static String LOCATION_CODE_ATTRIBUTE_MAXIMILIANOVKA_UUID = "77081024-8b9f-11e8-9eb6-529269fb1468";

    public final static String LOCATION_CODE_ATTRIBUTE_NETAILOVE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1469";

    public final static String LOCATION_CODE_ATTRIBUTE_NOVOSELIVKA_DRUHA_UUID = "77081024-8b9f-11e8-9eb6-529269fb1470";

    public final static String LOCATION_CODE_ATTRIBUTE_OPYTNE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1471";

    public final static String LOCATION_CODE_ATTRIBUTE_TARAMCHUK_UUID = "77081024-8b9f-11e8-9eb6-529269fb1472";

    public final static String LOCATION_CODE_ATTRIBUTE_VODYANE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1473";

    public final static String LOCATION_CODE_ATTRIBUTE_BERDIANSKOE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1474";

    public final static String LOCATION_CODE_ATTRIBUTE_GRANITNE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1475";

    public final static String LOCATION_CODE_ATTRIBUTE_HOSPITAL_N_1_MARIUPOL_UUID = "77081024-8b9f-11e8-9eb6-529269fb1476";

    public final static String LOCATION_CODE_ATTRIBUTE_HOSPITAL_N_3_MARIUPOL_UUID = "77081024-8b9f-11e8-9eb6-529269fb1477";

    public final static String LOCATION_CODE_ATTRIBUTE_KAMIANKA_1_UUID = "77081024-8b9f-11e8-9eb6-529269fb1478";

    public final static String LOCATION_CODE_ATTRIBUTE_LEBEDINSKOE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1479";

    public final static String LOCATION_CODE_ATTRIBUTE_MIKOLAIVKA_UUID = "77081024-8b9f-11e8-9eb6-529269fb1480";

    public final static String LOCATION_CODE_ATTRIBUTE_NOVOSELIVKA_UUID = "77081024-8b9f-11e8-9eb6-529269fb1481";

    public final static String LOCATION_CODE_ATTRIBUTE_ORLOVSKE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1482";

    public final static String LOCATION_CODE_ATTRIBUTE_PASHKOVSKOGO_UUID = "77081024-8b9f-11e8-9eb6-529269fb1483";

    public final static String LOCATION_CODE_ATTRIBUTE_PAVLOPIL_UUID = "77081024-8b9f-11e8-9eb6-529269fb1484";

    public final static String LOCATION_CODE_ATTRIBUTE_PIONERSKE_UUID = "77081024-8b9f-11e8-9eb6-529269fb1485";

    public final static String LOCATION_CODE_ATTRIBUTE_PROHOROVKA_UUID = "77081024-8b9f-11e8-9eb6-529269fb1486";

    public final static String LOCATION_CODE_ATTRIBUTE_STAROGNATIVKA_UUID = "77081024-8b9f-11e8-9eb6-529269fb1487";

    public final static String LOCATION_CODE_ATTRIBUTE_STEPANIVKA_UUID = "77081024-8b9f-11e8-9eb6-529269fb1488";

    public final static String LOCATION_AARSAL_UUID = "87081024-8b9f-11e8-9eb6-529269fb1459";

    public final static String LOCATION_BAALBAK_UUID = "87081024-8b9f-11e8-9eb6-529269fb1460";

    public final static String LOCATION_HERMEL_UUID = "87081024-8b9f-11e8-9eb6-529269fb1461";

    public final static String LOCATION_MAJDAL_ANJAR_UUID = "87081024-8b9f-11e8-9eb6-529269fb1462";

    public final static String LOCATION_ABDEH_UUID = "87081024-8b9f-11e8-9eb6-529269fb1463";

    public final static String LOCATION_DAZ_UUID = "87081024-8b9f-11e8-9eb6-529269fb1464";

    public final static String LOCATION_BEREZOVE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1465";

    public final static String LOCATION_KAMIANKA_2_UUID = "87081024-8b9f-11e8-9eb6-529269fb1466";

    public final static String LOCATION_KURAKHOVE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1467";

    public final static String LOCATION_MAXIMILIANOVKA_UUID = "87081024-8b9f-11e8-9eb6-529269fb1468";

    public final static String LOCATION_NETAILOVE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1469";

    public final static String LOCATION_NOVOSELIVKA_DRUHA_UUID = "87081024-8b9f-11e8-9eb6-529269fb1470";

    public final static String LOCATION_OPYTNE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1471";

    public final static String LOCATION_TARAMCHUK_UUID = "87081024-8b9f-11e8-9eb6-529269fb1472";

    public final static String LOCATION_VODYANE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1473";

    public final static String LOCATION_BERDIANSKOE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1474";

    public final static String LOCATION_GRANITNE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1475";

    public final static String LOCATION_HOSPITAL_N_1_MARIUPOL_UUID = "87081024-8b9f-11e8-9eb6-529269fb1476";

    public final static String LOCATION_HOSPITAL_N_3_MARIUPOL_UUID = "87081024-8b9f-11e8-9eb6-529269fb1477";

    public final static String LOCATION_KAMIANKA_1_UUID = "87081024-8b9f-11e8-9eb6-529269fb1478";

    public final static String LOCATION_LEBEDINSKOE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1479";

    public final static String LOCATION_MIKOLAIVKA_UUID = "87081024-8b9f-11e8-9eb6-529269fb1480";

    public final static String LOCATION_NOVOSELIVKA_UUID = "87081024-8b9f-11e8-9eb6-529269fb1481";

    public final static String LOCATION_ORLOVSKE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1482";

    public final static String LOCATION_PASHKOVSKOGO_UUID = "87081024-8b9f-11e8-9eb6-529269fb1483";

    public final static String LOCATION_PAVLOPIL_UUID = "87081024-8b9f-11e8-9eb6-529269fb1484";

    public final static String LOCATION_PIONERSKE_UUID = "87081024-8b9f-11e8-9eb6-529269fb1485";

    public final static String LOCATION_PROHOROVKA_UUID = "87081024-8b9f-11e8-9eb6-529269fb1486";

    public final static String LOCATION_STAROGNATIVKA_UUID = "87081024-8b9f-11e8-9eb6-529269fb1487";

    public final static String LOCATION_STEPANIVKA_UUID = "87081024-8b9f-11e8-9eb6-529269fb1488";

    public final static String GP_DAYS_TO_KEEP_LOGS = "msfcore.daysToKeepLogs";

    public final static String GP_ENABLE_MSF_UI = "msfcore.enableMSFUI";

    public final static String GP_MANADATORY = "msfcore.mandatory";

    public final static String TASK_AUTO_CLOSE_VISIT = "Auto Close Visits Task";

    // Encounter types
    public static final String ENCOUNTER_TYPE_NCD_BASELINE_UUID = "fa6f3ff5-b784-43fb-ab35-a08ab7dbf074";

    public static final String ENCOUNTER_TYPE_NCD_FOLLOWUP_UUID = "4aa6553b-be5a-413a-93fc-23962bc594e8";

    public static final String ENCOUNTER_TYPE_NCD_EXIT_UUID = "4aa6553b-be5a-413a-93fc-23962bc594e9";

    public static final String ENCOUNTER_TYPE_LAB_RESULTS_UUID = "5dd6553b-be5a-413a-93fc-23962bc594e8";

    public final static String LOCATION_ATTR_TYPE_UID_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0000";

    public final static String LOCATION_UID_ATTRIBUTE_LEBANON_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0001";

    public final static String LOCATION_UID_ATTRIBUTE_UKRAINE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0002";

    public final static String LOCATION_UID_ATTRIBUTE_BEKAA_VALLEY_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0003";

    public final static String LOCATION_UID_ATTRIBUTE_TRIPOLI_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0004";

    public final static String LOCATION_UID_ATTRIBUTE_WEST_DONETSK_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0005";

    public final static String LOCATION_UID_ATTRIBUTE_MARIUPOL_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0006";

    public final static String LOCATION_UID_ATTRIBUTE_AARSAL_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0007";

    public final static String LOCATION_UID_ATTRIBUTE_BAALBAK_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0008";

    public final static String LOCATION_UID_ATTRIBUTE_HERMEL_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0009";

    public final static String LOCATION_UID_ATTRIBUTE_MAJDAL_ANJAR_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0010";

    public final static String LOCATION_UID_ATTRIBUTE_ABDEH_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0011";

    public final static String LOCATION_UID_ATTRIBUTE_DAZ_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0012";

    public final static String LOCATION_UID_ATTRIBUTE_BEREZOVE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0013";

    public final static String LOCATION_UID_ATTRIBUTE_KAMIANKA_2_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0014";

    public final static String LOCATION_UID_ATTRIBUTE_KURAKHOVE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0015";

    public final static String LOCATION_UID_ATTRIBUTE_MAXIMILIANOVKA_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0016";

    public final static String LOCATION_UID_ATTRIBUTE_GRANITNE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0017";

    public final static String LOCATION_UID_ATTRIBUTE_HOSPITAL_N_1_MARIUPOL_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0018";

    public final static String LOCATION_UID_ATTRIBUTE_HOSPITAL_N_3_MARIUPOL_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0019";

    public final static String LOCATION_UID_ATTRIBUTE_KAMIANKA_1_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0020";

    public final static String LOCATION_UID_ATTRIBUTE_LEBEDINSKOE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0021";

    public final static String LOCATION_UID_ATTRIBUTE_MIKOLAIVKA_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0022";

    public final static String LOCATION_UID_ATTRIBUTE_NOVOSELIVKA_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0023";

    public final static String LOCATION_UID_ATTRIBUTE_ORLOVSKE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0024";

    public final static String LOCATION_UID_ATTRIBUTE_PASHKOVSKOGO_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0025";

    public final static String LOCATION_UID_ATTRIBUTE_PAVLOPIL_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0026";

    public final static String LOCATION_UID_ATTRIBUTE_PIONERSKE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0027";

    public final static String LOCATION_UID_ATTRIBUTE_PROHOROVKA_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0028";

    public final static String LOCATION_UID_ATTRIBUTE_STAROGNATIVKA_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0029";

    public final static String LOCATION_UID_ATTRIBUTE_STEPANIVKA_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0030";

    public final static String LOCATION_UID_ATTRIBUTE_BERDIANSKOE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0031";

    public final static String LOCATION_UID_ATTRIBUTE_NETAILOVE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0032";

    public final static String LOCATION_UID_ATTRIBUTE_NOVOSELIVKA_DRUHA_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0033";

    public final static String LOCATION_UID_ATTRIBUTE_OPYTNE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0034";

    public final static String LOCATION_UID_ATTRIBUTE_TARAMCHUK_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0035";

    public final static String LOCATION_UID_ATTRIBUTE_VODYANE_UUID = "ccbed524-b6d4-11e8-96f8-529269fb0036";

    public final static String GP_DHIS_HOST = "msfcore.dhisHost";

    public final static String GP_OPENHIM_TRACKER_URL = "msfcore.openhimTrackerUrl";

    /**
     * OpenHIM client id
     */
    public final static String PROP_OPENHIM_USERNAME = "msfcore.openhimUsername";

    /**
     * OpenHIM client password
     */
    public final static String PROP_OPENHIM_PASSWORD = "msfcore.openhimPassword";

    public final static String PROP_DHIS2_USERNAME = "msfcore.dhis2Username";

    public final static String PROP_DHIS2_PASSWORD = "msfcore.dhis2Password";

    public final static String GP_DHIS_NCD_PROGRAM_UID = "msfcore.dhis.ncdPRogramUid";

    public final static String GP_DHIS_NCD_PROGRAMSTAGE_UID = "msfcore.dhis.ncdPRogramStageUid";

    public final static String GP_DHIS_TRACKENTITYTYPE_UID = "msfcore.dhis.ncdTrackEntityTypeUid";

    public final static String PERSON_ATTRIBUTE_PHONENUMBER_UUID = "14d4f066-15f5-102d-96e4-000c29c2a5d7";

    public final static String GP_SYNC_WITH_DHIS2 = "msfcore.syncWithDHISOnPatientRegistration";

    public final static String URL_POSTFIX_OPTIONSETS = "/api/optionSets.json?paging=false&fields=name,options[:code,name]";

    public final static String GP_SYNC_LOCAL_FEED_URL = "msfcore.sync2.localFeedLocation";

    public final static String GP_SYNC_PARENT_FEED_URL = "msfcore.sync2.parentFeedLocation";

    public static final String SYNC2_NAME_OF_CUSTOM_CONFIGURATION = "sync2.json";

    public static final String CONFIGURATION_DIR = "configuration";

    public static final String FILENAME_DHIS_MAPPINGS_PROPERTIES = "dhis-mappings.properties";

    public static final String FILENAME_OPTION_SETS_JSON = "optionSets.json";

    public static final String HTMLFORM_REQUEST_APPOINTMENT_UUID = "f09a3a3a-810e-4cf6-b432-3d43da303911";

    public static final String CONCEPT_REQUEST_APPOINTMENT_DATE_UUID = "c72e7dce-e9eb-44e9-8114-94f9550ad5b0";

    public static final String CONCEPT_REQUEST_APPOINTMENT_COMMENT_UUID = "f3a76818-3ff4-4d94-9c51-be295cad43a1";

    public static final String SERVICE_TYPE_GENERAL_MEDICINE_UUID = "7dd9ac8e-c436-11e4-a470-82b0ea87e2d8";

    public static final String NCD_PROGRAM_UUID = "a24327d2-c601-11e8-a355-529269fb1400";

    public static final String NCD_PROGRAM_CONCEPT_UUID = "a2432c28-c601-11e8-a355-529269fb1401";

    public static final String NCD_PROGRAM_OUTCOMES_CONCEPT_UUID = "6cfb6f44-7b27-4d16-8fd1-ea189a8171a4";

    public static final String WORKFLOW_STATE_UUID_ENROLL = "a24327d2-c601-11e8-a355-529269fb1401";

    public static final String CONCEPT_UUID_PROGRAM_WORKFLOW_STATE_ENROLL = "a2432c28-c601-11e8-a355-529269fb1402";

    public static final String WORKFLOW_STATE_UUID_BASELINE_CONSULTATION = "a24327d2-c601-11e8-a355-529269fb1402";

    public static final String CONCEPT_UUID_PROGRAM_WORKFLOW_STATE_BASELINE_CONSULTATION = "a2432c28-c601-11e8-a355-529269fb1403";

    public static final String WORKFLOW_STATE_UUID_FOLLOWUP_CONSULTATION = "a24327d2-c601-11e8-a355-529269fb1404";

    public static final String CONCEPT_UUID_PROGRAM_WORKFLOW_STATE_FOLLOWUP_CONSULTATION = "a2432c28-c601-11e8-a355-529269fb1405";

    public static final String WORKFLOW_STATE_UUID_EXIT = "a24327d2-c601-11e8-a355-529269fb1405";

    public static final String CONCEPT_UUID_PROGRAM_WORKFLOW_STATE_EXIT = "a2432c28-c601-11e8-a355-529269fb1406";

    public static final String PRIVILEGE_ENROLL_IN_PROGRAM = "00fb0222-e351-4a30-86ec-79b56f4eb9aa";

    public static final String HTML_FORM_UUID_BASELINE = "06807e2b-ce97-4d65-8796-e955fcbe057d";

    public static final String HTML_FORM_UUID_FOLLOWUP = "8054636e-a5f5-4319-93a3-2843860a55f9";

    public static final String HTML_FORM_UUID_EXIT = "5f0ebfc3-a520-4ff9-bb5d-807821dd6a8d";

    public static final String CONCEPT_UUID_NONE = "09bd4cce-b1f6-4706-8958-b155a47ac203";

    public static final String CONCEPT_UUID_PROGRAM_WORKFLOW_STAGE = "a2432c28-c601-11e8-a355-529269fb1408";

    public static final String PROGRAM_WORKFLOW_UUID_STAGE = "a2432c28-c601-11e8-a355-529269fb1408";

    public static final String CONCEPT_REQUEST_APPOINTMENT_TYPE_UUID = "d0475b32-6987-43b3-a618-b975f4967ba5";

    public static final String CONCEPT_DOSE_UUID = "160856AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_DOSE_UNIT_UUID = "d09764da-2bf1-4ab6-933f-07a50add8f33";

    public static final String CONCEPT_DURATION_UUID = "159368AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_FREQUENCY_UUID = "160855AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_DURATION_UNIT_UUID = "161244AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_ROUTE_ORAL_UUID = "160240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_PRESCRIBED_MEDICATION_UUID = "17110937-6cb2-42f9-b932-3f79e9506a1d";

    public static final String CONCEPT_ADMINISTRATION_INSTRUCTIONS_UUID = "163106AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_REASON_FOR_REFERRAL_UUID = "830817c9-e1f9-48a7-a20b-c927bca52891";

    // Form UUIDS
    public static final String FORM_NCD_BASELINE_MEDICAL_HISTORY_UUID = "06807e2b-ce97-4d65-8796-e955fcbe057d";

    public static final String FORM_NCD_BASELINE_LIFESTYLE_UUID = "3209cd5f-656e-42f4-984e-ab466a5b77ef";

    public static final String FORM_NCD_BASELINE_ALLERGIES_UUID = "30d1fda4-4161-4666-ad0c-e2ba20eb73a6";

    public static final String FORM_NCD_BASELINE_DIAGNOSIS_UUID = "860d4952-7490-4a70-9e75-8cf4ebf10df8";

    public static final String FORM_NCD_BASELINE_COMPLICATIONS_UUID = "f09a3a3a-810e-4cf6-b432-3d43da303933";

    public static final String FORM_NCD_BASELINE_REQUEST_INVESTIGATION_UUID = "fc14cfa5-6cbc-47bf-9674-efdcc7628350";

    public static final String FORM_NCD_BASELINE_PRESCRIBE_MEDICATION_UUID = "aab2cab6-c280-438b-9afd-3c54e799ef2a";

    public static final String FORM_NCD_BASELINE_PATIENT_TARGET_UUID = "f88f341a-2a37-47e9-ac81-b5dae813ab26";

    public static final String FORM_NCD_BASELINE_REGULAR_PATIENT_REVIEW_UUID = "b450ec93-f4b5-4a4b-8143-4564d84028bc";

    public static final String FORM_NCD_BASELINE_CLINICAL_NOTE_UUID = "f09a3a3a-810e-4cf6-b432-3d43da303948";

    public static final String FORM_NCD_BASELINE_REQUEST_APPOINTMENT_UUID = "f09a3a3a-810e-4cf6-b432-3d43da303911";

    public static final String FORM_NCD_BASELINE_REFER_PATIENT_UUID = "a9f3411c-03d8-4652-8143-886d572cbf4d";

    public static final String FORM_NCD_FOLLOWUP_DIAGNOSIS_UUID = "c6e1c22c-d814-11e8-afdf-8b38a7ff6d67";

    public static final String FORM_NCD_FOLLOWUP_PRESCRIBE_MEDICATION_UUID = "e0c4b006-d81d-11e8-af07-63651fba21c4";

    public static final String FORM_NCD_FOLLOWUP_VISIT_DETAILS_UUID = "6b12c748-d81e-11e8-9bc4-074e329091e5";

    public static final String FORM_NCD_FOLLOWUP_COMPLICATIONS_SINCE_LAST_VISIT_UUID = "6e7c999e-d81f-11e8-95d4-8f3276b4efb8";

    public static final String FORM_NCD_FOLLOWUP_CLINICAL_NOTE_UUID = "0c570f28-d820-11e8-b904-1bb211973b4a";

    public static final String FORM_NCD_FOLLOWUP_REFER_PATIENT_UUID = "8e737974-d820-11e8-975e-371cbc2b2301";

    public static final String FORM_NCD_FOLLOWUP_REQUEST_INVESTIGATION_UUID = "034fe066-d821-11e8-8cd0-e39488e68b96";

    public static final String FORM_NCD_FOLLOWUP_REQUEST_APPOINTMENT_UUID = "5ec65100-d821-11e8-a2f8-07cd972bc32b";

    public static final String ALLERGY_TYPE_UUID = "cab5578c-0c62-4582-b668-28e3f218b635";

    public static final String ALLERGIC_REACTIONS_UUID = "456ce14a-a5f0-4c3e-bf13-2feb3dfb26dc";

    public static final String COMMENT_UUID = "161011AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String ALLERGY_PROBLEM_SEVERITY_UUID = "162820AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String REFERENCE_APPLICATION_COMMON_FOOD_ALLERGENS_UUID = "162553AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String REFERENCE_APPLICATION_COMMON_DRUG_ALLERGENS_UUID = "162552AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String REFERENCE_APPLICATION_COMMON_ENVIROMENTAL_ALLERGENS_UUID = "162554AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String ALLERGY_UUID = "73c9a85e-7de9-4b35-97ec-03d693f56fab";

    public static final String FOOD_ALLERGY_LIST_UUID = "1325f5d0-bbb8-402a-a6dc-21d7c6f82330";

    public static final String DRUG_ALLERGY_LIST_UUID = "1141bd11-1f91-4af7-a8d7-a26394140e08";

    public static final String OTHER_ALLERGY_LIST_UUID = "a0740eb7-92cd-4f79-a9b2-08e0f363532e";

    public static final String CONCEPT_SET_LAB_ORDERS_UUID = "23281464-74d8-47d9-9a39-7a1f1d7caa49";

    public static final String CONCEPT_SET_REFERRAL_ORDERS_UUID = "51d1044c-b97a-406f-93c8-324b6bee1e61";

    public static final String CONCEPT_UUID_DISPENSED = "5000036AAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_UUID_DESPENSED_DATE = "5000039AAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_UUID_DESPENSED_DETAILS = "5000040AAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String ENCOUNTER_TYPE_DISPENSE_DRUG_UUID = "0aa6553b-be5z-413a-93fr-23962bc594e0";

    public static final String CHART_SEARCH_EXTENSION_ID = "chartsearch.chartSearchLink";

    public static final String ADMIT_TO_INPATIENT_EXTENSION_ID = "referenceapplication.realTime.simpleAdmission";

    public static final String VISIT_NOTE_EXTENSION_ID = "referenceapplication.realTime.simpleVisitNote";

    public static final String PERSON_ATTRIBUTE_OTHER_UUID = "0cd7e060-ec40-11e8-8eb2-f2801f1b9fd1";

    public static final String PERSON_ATTRIBUTE_AUNT_UUID = "0cd7e060-ec40-11e8-8eb2-f2801f1b9fd2";

    public static final String PERSON_ATTRIBUTE_UNCLE_UUID = "0cd7e060-ec40-11e8-8eb2-f2801f1b9fd3";

    public static final String PERSON_ATTRIBUTE_BROTHER_UUID = "0cd7e060-ec40-11e8-8eb2-f2801f1b9fd4";

    public static final String PERSON_ATTRIBUTE_SISTER_UUID = "0cd7e060-ec40-11e8-8eb2-f2801f1b9fd5";

    public static final String PERSON_ATTRIBUTE_MOTHER_UUID = "0cd7e060-ec40-11e8-8eb2-f2801f1b9fd6";

    public static final String PERSON_ATTRIBUTE_FATHER_UUID = "0cd7e060-ec40-11e8-8eb2-f2801f1b9fd7";

    public static final String REFERRAL_ORDER_TYPE_UUID = "1bb0842a-ed99-11e8-8eb2-f2801f1b9fd1";

    public static final String CONCEPT_UUID_FEEDBACK = "5000042AAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String CONCEPT_UUID_FEEDBACK_FROM = "5000043AAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String ENCOUNTER_TYPE_REFER_PATIENT_UUID = "0aa6553b-be5z-444a-93fr-23962bc594e1";

    public static final String CONCEPT_ABLE_TO_WRITE_UUID = "34cebc3e-882f-47b1-aac4-32304f571591";

    public static final String CONCEPT_ABLE_TO_READ_UUID = "159400AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static final String PERSON_ATTRIBUTE_ABLE_TO_READ_UUID = "ebb49035-b40b-4659-a1c6-0cb3ceade639";

    public static final String PERSON_ATTRIBUTE_ABLE_TO_WRITE_UUID = "ccf9a5cf-c6ff-456c-b8f3-3cf3f02b4150";

}
