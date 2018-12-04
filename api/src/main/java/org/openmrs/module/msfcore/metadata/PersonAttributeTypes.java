package org.openmrs.module.msfcore.metadata;

import org.openmrs.Concept;
import org.openmrs.module.metadatadeploy.descriptor.PersonAttributeTypeDescriptor;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.util.AttributableDate;

public class PersonAttributeTypes {

    public static PersonAttributeTypeDescriptor NATIONALITY = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Concept.class;
        }

        @Override
        public String name() {
            return "Nationality";
        }

        @Override
        public String description() {
            return "Nationality of the person";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_NATIONALITY_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor PROVENANCE = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Concept.class;
        }

        @Override
        public String name() {
            return "Provenance";
        }

        @Override
        public String description() {
            return "Provenance";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_PROVENANCE_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor MARITAL_STATUS = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Concept.class;
        }

        @Override
        public String name() {
            return "Marital Status";
        }

        @Override
        public String description() {
            return "Marital status of this person";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_MARITAL_STATUS_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor EDUCATION = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Concept.class;
        }

        @Override
        public String name() {
            return "Education";
        }

        @Override
        public String description() {
            return "Education of this person";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_EDUCATION_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor CONDITION_OF_LIVING = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Concept.class;
        }

        @Override
        public String name() {
            return "Condition of living";
        }

        @Override
        public String description() {
            return "Condition of living of this person";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_CONDITION_OF_LIVING_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor EMPLOYMENT_STATUS = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Concept.class;
        }

        @Override
        public String name() {
            return "Employment Status";
        }

        @Override
        public String description() {
            return "Employment status of this person";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_EMPLOYMENT_STATUS_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor DATE_OF_ARRIVAL = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return AttributableDate.class;
        }

        @Override
        public String name() {
            return "Date of Arrival";
        }

        @Override
        public String description() {
            return "Person's date of arrival to the facility";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_DATE_OF_ARRIVAL_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor OLD_FACILITY_CODE = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Concept.class;
        }

        @Override
        public String name() {
            return "Old Facility Code";
        }

        @Override
        public String description() {
            return "Old Facility Code";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_OLD_FACILITY_CODE_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor MSF_OTHER_ID_NAME = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return String.class;
        }

        @Override
        public String name() {
            return "Other ID name";
        }

        @Override
        public String description() {
            return "Other ID name";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_OTHER_ID_NAME_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor FATHER = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return String.class;
        }

        @Override
        public String name() {
            return "Father";
        }

        @Override
        public String description() {
            return "Father's name";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_FATHER_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor MOTHER = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return String.class;
        }

        @Override
        public String name() {
            return "Mother";
        }

        @Override
        public String description() {
            return "Mother's name";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_MOTHER_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor SISTER = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return String.class;
        }

        @Override
        public String name() {
            return "Sister";
        }

        @Override
        public String description() {
            return "Sister's name";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_SISTER_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor BROTHER = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return String.class;
        }

        @Override
        public String name() {
            return "Brother";
        }

        @Override
        public String description() {
            return "Brother's name";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_BROTHER_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor UNCLE = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return String.class;
        }

        @Override
        public String name() {
            return "Uncle";
        }

        @Override
        public String description() {
            return "Uncle's name";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_UNCLE_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor AUNT = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return String.class;
        }

        @Override
        public String name() {
            return "Aunt";
        }

        @Override
        public String description() {
            return "Aunt's name";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_AUNT_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor OTHER = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return String.class;
        }

        @Override
        public String name() {
            return "Other";
        }

        @Override
        public String description() {
            return "Other relationship besides father, mother, sister, brother, uncle and aunt";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_OTHER_UUID;
        }

        @Override
        public boolean searchable() {
            return true;
        }
    };

    public static PersonAttributeTypeDescriptor MSF_EDUCATION_ABLE_TO_READ = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Boolean.class;
        }

        @Override
        public String name() {
            return "Able to read";
        }

        @Override
        public String description() {
            return "Whether the person can read";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_ABLE_TO_READ_UUID;
        }

        @Override
        public boolean searchable() {
            return false;
        }
    };

    public static PersonAttributeTypeDescriptor MSF_EDUCATION_ABLE_TO_WRITE = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 0;
        }

        @Override
        public Class<?> format() {
            return Boolean.class;
        }

        @Override
        public String name() {
            return "Able to write";
        }

        @Override
        public String description() {
            return "Whether the person can write";
        }

        public String uuid() {
            return MSFCoreConfig.PERSON_ATTRIBUTE_ABLE_TO_WRITE_UUID;
        }

        @Override
        public boolean searchable() {
            return false;
        }
    };
}
