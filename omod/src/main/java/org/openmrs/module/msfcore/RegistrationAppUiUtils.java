package org.openmrs.module.msfcore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.openmrs.ConceptName;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.api.context.Context;

public class RegistrationAppUiUtils {
    /**
     * Gets the person attribute value for the specified patient for the
     * getPersonAttributeTypeByUuid that matches the specified uuid
     * 
     * @return the attribute value
     */
    public String getAttribute(Patient patient, String attributeTypeUuid) {
        if (patient != null) {
            PersonAttribute attr = patient.getAttribute(Context.getPersonService().getPersonAttributeTypeByUuid(attributeTypeUuid));
            if (attr != null) {
                return attr.getValue();
            }
        }

        return null;
    }

    public String getIdentifier(Patient patient, String identifierName) {
        if (patient != null) {
            PatientIdentifier identifier = patient.getPatientIdentifier(identifierName);
            if (identifier != null) {
                return identifier.getIdentifier();
            } else {
                return null;
            }
        }

        return null;
    }

    public String getPersonAttributeValue(Patient patient, String attributeTypeUuid) {
        if (patient != null) {
            PersonAttribute attr = patient.getAttribute(Context.getPersonService().getPersonAttributeTypeByUuid(attributeTypeUuid));
            if (attr != null) {
                String conceptId = attr.getValue();
                ConceptName conceptName = Context.getConceptService().getConcept(Integer.parseInt(conceptId)).getName();
                if (conceptName != null) {
                    return conceptName.getName();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public PersonAddress getAddress(Patient patient) {
        if (patient != null) {
            Set<PersonAddress> addresses = patient.getAddresses();
            if (addresses != null) {
                for (PersonAddress address : addresses) {
                    if (!address.getVoided()) {
                        return address;
                    }
                }
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Date getDateOfArrival(Patient patient) {
        String date = getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_DATE_OF_ARRIVAL_UUID);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
        try {
            return date != null ? format.parse(date) : new Date();
        } catch (ParseException e) {
            return new Date();
        }
    }

    public Location getLocation(Patient patient) {
        String locationId = getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_HEALTH_CENTER_UUID);
        return locationId != null ? Context.getLocationService().getLocation(Integer.valueOf(locationId)) : Context.getLocationService()
                        .getDefaultLocation();
    }
}