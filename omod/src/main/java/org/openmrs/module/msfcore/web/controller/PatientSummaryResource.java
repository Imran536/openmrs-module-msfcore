package org.openmrs.module.msfcore.web.controller;

import java.util.Arrays;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.msfcore.api.PatientSummaryService;
import org.openmrs.module.msfcore.patientSummary.PatientSummary;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + MSFCoreRestController.NAMESPACE + "/patientSummary", supportedClass = PatientSummary.class, supportedOpenmrsVersions = {"2.2.*"})
public class PatientSummaryResource extends BaseDataResource {

    @Override
    protected AlreadyPaged<PatientSummary> doSearch(RequestContext context) throws ResponseException {
        Patient patient = getPatientFromId(context.getParameter("patientId"));
        return new AlreadyPaged<PatientSummary>(context, Arrays.asList(Context.getService(PatientSummaryService.class)
                        .requestPatientSummary(patient)), false);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("facility");
        description.addRequiredProperty("demographics");
        description.addProperty("vitals");
        description.addProperty("diagnoses");
        description.addProperty("allergies");
        description.addProperty("clinicalHistory");
        description.addProperty("recentLabResults");
        description.addProperty("currentMedications");
        description.addProperty("clinicalNotes");
        description.addProperty("provider");
        description.addProperty("signature");

        return description;
    }
}
