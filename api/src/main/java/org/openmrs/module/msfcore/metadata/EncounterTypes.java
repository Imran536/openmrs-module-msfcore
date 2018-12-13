package org.openmrs.module.msfcore.metadata;

import org.openmrs.module.metadatadeploy.descriptor.EncounterTypeDescriptor;
import org.openmrs.module.msfcore.MSFCoreConfig;

public class EncounterTypes {
    public static EncounterTypeDescriptor MSF_NCD_BASELINE_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "NCD Baseline";
        }

        @Override
        public String description() {
            return "Baseline NCD Assessment encounter type";
        }

        public String uuid() {
            return MSFCoreConfig.ENCOUNTER_TYPE_NCD_BASELINE_UUID;
        }
    };

    public static EncounterTypeDescriptor MSF_NCD_FOLLOWUP_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "NCD Follow-up";
        }

        @Override
        public String description() {
            return "NCD Followup encounter type";
        }

        public String uuid() {
            return MSFCoreConfig.ENCOUNTER_TYPE_NCD_FOLLOWUP_UUID;
        }
    };

    public static EncounterTypeDescriptor MSF_NCD_EXIT_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "NCD Exit";
        }

        @Override
        public String description() {
            return "Exit ncd encounter type";
        }

        public String uuid() {
            return MSFCoreConfig.ENCOUNTER_TYPE_NCD_EXIT_UUID;
        }
    };

    public static EncounterTypeDescriptor MSF_LAB_RESULTS_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Lab results";
        }

        @Override
        public String description() {
            return "Lab results";
        }

        public String uuid() {
            return MSFCoreConfig.ENCOUNTER_TYPE_LAB_RESULTS_UUID;
        }
    };

    public static EncounterTypeDescriptor MSF_DISPENSE_DRUG_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Dispense Drug";
        }

        @Override
        public String description() {
            return "Dispensing drugs encounter type";
        }

        public String uuid() {
            return MSFCoreConfig.ENCOUNTER_TYPE_DISPENSE_DRUG_UUID;
        }
    };

    public static EncounterTypeDescriptor MSF_REFER_PATIENT_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Refer Patient";
        }

        @Override
        public String description() {
            return "Patient referral's encounter type";
        }

        public String uuid() {
            return MSFCoreConfig.ENCOUNTER_TYPE_REFER_PATIENT_UUID;
        }
    };
}
