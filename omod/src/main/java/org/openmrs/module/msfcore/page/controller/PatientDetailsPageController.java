package org.openmrs.module.msfcore.page.controller;

import org.openmrs.Patient;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.module.msfcore.RegistrationAppUiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class PatientDetailsPageController {
    public void controller(PageModel pageModel, @RequestParam("patientId") Patient patient) {
        RegistrationAppUiUtils uiUtils = new RegistrationAppUiUtils();
        pageModel.addAttribute("patient", patient);
        pageModel.addAttribute("nationality", uiUtils.getPersonAttributeValue(patient, MSFCoreConfig.PERSON_ATTRIBUTE_NATIONALITY_UUID));
        pageModel.addAttribute("provenance", uiUtils.getPersonAttributeValue(patient, MSFCoreConfig.PERSON_ATTRIBUTE_PROVENANCE_UUID));
        pageModel.addAttribute("telephone", uiUtils.getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_TELEPHONE_UUID));
        pageModel.addAttribute("father", uiUtils.getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_FATHER_UUID));
        pageModel.addAttribute("mother", uiUtils.getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_MOTHER_UUID));
        pageModel.addAttribute("sister", uiUtils.getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_SISTER_UUID));
        pageModel.addAttribute("brother", uiUtils.getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_BROTHER_UUID));
        pageModel.addAttribute("uncle", uiUtils.getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_UNCLE_UUID));
        pageModel.addAttribute("aunt", uiUtils.getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_AUNT_UUID));
        pageModel.addAttribute("other", uiUtils.getAttribute(patient, MSFCoreConfig.PERSON_ATTRIBUTE_OTHER_UUID));

        pageModel.addAttribute("employment_status", uiUtils.getPersonAttributeValue(patient,
                        MSFCoreConfig.PERSON_ATTRIBUTE_EMPLOYMENT_STATUS_UUID));
        pageModel.addAttribute("marital_status", uiUtils.getPersonAttributeValue(patient,
                        MSFCoreConfig.PERSON_ATTRIBUTE_MARITAL_STATUS_UUID));
        pageModel.addAttribute("education", uiUtils.getPersonAttributeValue(patient, MSFCoreConfig.PERSON_ATTRIBUTE_EDUCATION_UUID));
        pageModel.addAttribute("condition_of_living", uiUtils.getPersonAttributeValue(patient,
                        MSFCoreConfig.PERSON_ATTRIBUTE_CONDITION_OF_LIVING_UUID));

        pageModel.addAttribute("passport_number", uiUtils.getIdentifier(patient, "Passport ID type"));
        pageModel.addAttribute("drivers_license", uiUtils.getIdentifier(patient, "Driver's License Identifier type"));
        pageModel.addAttribute("national_id", uiUtils.getIdentifier(patient, "National ID type"));
        pageModel.addAttribute("insurance_card", uiUtils.getIdentifier(patient, "Insurance Card type"));
        pageModel.addAttribute("unhchr_id", uiUtils.getIdentifier(patient, "UNHCR ID type"));
        pageModel.addAttribute("unrwa_id", uiUtils.getIdentifier(patient, "UNRWA ID type"));

        pageModel.addAttribute("location", uiUtils.getLocation(patient));
    }
}
