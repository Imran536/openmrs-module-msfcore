package org.openmrs.module.msfcore.metadata;

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.metadatadeploy.bundle.CoreConstructors1_10;
import org.openmrs.module.metadatadeploy.descriptor.OrderTypeDescriptor;
import org.springframework.stereotype.Component;

@Component
public class MSFMetadataBundle extends AbstractMetadataBundle {

    @Override
    public void install() throws Exception {
        log.info("Installing PersonAttributeTypes");
        install(PersonAttributeTypes.NATIONALITY);
        install(PersonAttributeTypes.PROVENANCE);
        install(PersonAttributeTypes.MARITAL_STATUS);
        install(PersonAttributeTypes.EDUCATION);
        install(PersonAttributeTypes.CONDITION_OF_LIVING);
        install(PersonAttributeTypes.EMPLOYMENT_STATUS);
        install(PersonAttributeTypes.DATE_OF_ARRIVAL);
        install(PersonAttributeTypes.OLD_FACILITY_CODE);
        install(PersonAttributeTypes.MSF_OTHER_ID_NAME);
        install(PersonAttributeTypes.MSF_EDUCATION_ABLE_TO_READ);
        install(PersonAttributeTypes.MSF_EDUCATION_ABLE_TO_WRITE);
        install(PersonAttributeTypes.FATHER);
        install(PersonAttributeTypes.MOTHER);
        install(PersonAttributeTypes.SISTER);
        install(PersonAttributeTypes.BROTHER);
        install(PersonAttributeTypes.UNCLE);
        install(PersonAttributeTypes.AUNT);
        install(PersonAttributeTypes.OTHER);

        log.info("Installing LocationAttributeTypes");
        install(LocationAttributeTypes.LOCATION_CODE);
        install(LocationAttributeTypes.LOCATION_UID);

        log.info("Installing LocationTags");
        install(LocationTags.MISSION);
        install(LocationTags.PROJECT);
        install(LocationTags.CLINIC);

        log.info("Installing MissionLocations");
        install(MissionLocations.LEBANON);
        install(MissionLocations.UKRAINE);

        log.info("Installing ProjectLocations");
        install(ProjectLocations.BEKAA_VALLEY);
        install(ProjectLocations.TRIPOLI);
        install(ProjectLocations.WEST_DONETSK);
        install(ProjectLocations.MARIUPOL);

        log.info("Installing ClinicLocations");
        install(ClinicLocations.AARSAL);
        install(ClinicLocations.BAALBAK);
        install(ClinicLocations.HERMEL);
        install(ClinicLocations.MAJDAL_ANJAR);
        install(ClinicLocations.ABDEH);
        install(ClinicLocations.DAZ);
        install(ClinicLocations.BEREZOVE);
        install(ClinicLocations.KAMIANKA_2);
        install(ClinicLocations.KURAKHOVE);
        install(ClinicLocations.MAXIMILIANOVKA);
        install(ClinicLocations.NETAILOVE);
        install(ClinicLocations.NOVOSELIVKA_DRUHA);
        install(ClinicLocations.OPYTNE);
        install(ClinicLocations.TARAMCHUK);
        install(ClinicLocations.VODYANE);
        install(ClinicLocations.BERDIANSKOE);
        install(ClinicLocations.GRANITNE);
        install(ClinicLocations.HOSPITAL_N_1_MARIUPOL);
        install(ClinicLocations.HOSPITAL_N_3_MARIUPOL);
        install(ClinicLocations.KAMIANKA_1);
        install(ClinicLocations.LEBEDINSKOE);
        install(ClinicLocations.MIKOLAIVKA);
        install(ClinicLocations.NOVOSELIVKA);
        install(ClinicLocations.ORLOVSKE);
        install(ClinicLocations.PASHKOVSKOGO);
        install(ClinicLocations.PAVLOPIL);
        install(ClinicLocations.PIONERSKE);
        install(ClinicLocations.PROHOROVKA);
        install(ClinicLocations.STAROGNATIVKA);
        install(ClinicLocations.STEPANIVKA);

        log.info("Installing PatientIdentifierTypes");
        install(PatientIdentifierTypes.MSF_PRIMARY_ID_TYPE);
        install(PatientIdentifierTypes.MSF_DRIVER_LICENSE_TYPE);
        install(PatientIdentifierTypes.MSF_NATIONAL_ID_TYPE);
        install(PatientIdentifierTypes.MSF_INSURANCE_CARD_TYPE);
        install(PatientIdentifierTypes.MSF_PASSPORT_TYPE);
        install(PatientIdentifierTypes.MSF_UNHCR_ID_TYPE);
        install(PatientIdentifierTypes.MSF_UNRWA_ID_TYPE);
        install(PatientIdentifierTypes.MSF_OTHER_ID);
        install(PatientIdentifierTypes.OLD_PATIENT_ID);

        log.info("Installing Encounter Types");
        install(EncounterTypes.MSF_NCD_BASELINE_ENCOUNTER_TYPE);
        install(EncounterTypes.MSF_NCD_FOLLOWUP_ENCOUNTER_TYPE);
        install(EncounterTypes.MSF_LAB_RESULTS_ENCOUNTER_TYPE);
        install(EncounterTypes.MSF_DISPENSE_DRUG_ENCOUNTER_TYPE);
        install(EncounterTypes.MSF_REFER_PATIENT_ENCOUNTER_TYPE);

        log.info("Installing Programs");
        install(Programs.NCD);

        log.info("Installing Privileges");
        install(Privileges.ENROLL_IN_PROGRAM);

        log.info("Installing OrderTypes");
        OrderTypeDescriptor referralDescriptor = OrderTypes.REFERRAL;
        deployService.installObject(CoreConstructors1_10.orderType(referralDescriptor.name(), referralDescriptor.description(),
                        referralDescriptor.uuid(), referralDescriptor.javaClassName(), null));
    }

}
