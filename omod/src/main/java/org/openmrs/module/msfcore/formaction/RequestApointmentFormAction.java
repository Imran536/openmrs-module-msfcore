package org.openmrs.module.msfcore.formaction;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointmentscheduling.AppointmentRequest;
import org.openmrs.module.appointmentscheduling.AppointmentRequest.AppointmentRequestStatus;
import org.openmrs.module.appointmentscheduling.AppointmentType;
import org.openmrs.module.appointmentscheduling.TimeFrameUnits;
import org.openmrs.module.appointmentscheduling.api.AppointmentService;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.module.msfcore.api.util.DateUtils;
import org.openmrs.module.msfcore.formaction.handler.FormAction;
import org.springframework.stereotype.Component;

@Component
public class RequestApointmentFormAction implements FormAction {

    private static final String OBS_UPDATE_COMMENT = "Setting appointment type UUID as comment";
    private static final List<String> VALID_FORM_UUIDS = Arrays.asList(MSFCoreConfig.HTMLFORM_REQUEST_APPOINTMENT_UUID,
                    MSFCoreConfig.FORM_NCD_FOLLOWUP_REQUEST_APPOINTMENT_UUID);

    private AppointmentService appointmentService;
    private ObsService obsService;

    @Override
    public void apply(String operation, FormEntrySession session) {
        String formUuid = session.getForm().getUuid();

        if (VALID_FORM_UUIDS.contains(formUuid)) {
            Patient patient = session.getEncounter().getPatient();
            Set<Obs> observations = session.getEncounter().getObsAtTopLevel(false);
            Provider requestor = session.getEncounter().getEncounterProviders().iterator().next().getProvider();
            requestAppointment(patient, observations, requestor);
        }
    }

    public AppointmentRequest requestAppointment(Patient patient, Set<Obs> observations, Provider requestor) {
        initializeServices();
        String notes = "";
        String appointmentTypeName = "";
        Date requestedDate = null;
        Obs appointmentTypeObs = null;

        for (Obs obs : observations) {
            if (obs.getConcept().getUuid().equals(MSFCoreConfig.CONCEPT_REQUEST_APPOINTMENT_DATE_UUID)) {
                requestedDate = obs.getValueDate();
            }
            if (obs.getConcept().getUuid().equals(MSFCoreConfig.CONCEPT_REQUEST_APPOINTMENT_COMMENT_UUID)) {
                notes = obs.getValueText();
            }
            if (obs.getConcept().getUuid().equals(MSFCoreConfig.CONCEPT_REQUEST_APPOINTMENT_TYPE_UUID)) {
                appointmentTypeName = obs.getValueText();
                appointmentTypeObs = obs;
            }
        }

        Date now = new Date();
        if (requestedDate == null || requestedDate.before(now)) {
            return null;
        }

        AppointmentType appointmentType = getAppointmentTypeByName(appointmentTypeName);

        List<AppointmentRequest> appointmentRequests = appointmentService.getAllAppointmentRequests(false);
        for (AppointmentRequest request : appointmentRequests) {
            if (request.getAppointmentType() == appointmentType && request.getPatient().getId() == patient.getId()
                            && DateUtils.isSameDate(now, request.getDateCreated())
                            && request.getStatus() == AppointmentRequestStatus.PENDING) {
                return null;
            }
        }

        appointmentTypeObs.setComment(appointmentType.getUuid());
        obsService.saveObs(appointmentTypeObs, OBS_UPDATE_COMMENT);
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentType(appointmentType);
        appointmentRequest.setNotes(notes);
        appointmentRequest.setPatient(patient);
        appointmentRequest.setMinTimeFrameUnits(TimeFrameUnits.DAYS);
        appointmentRequest.setMinTimeFrameValue(DateUtils.getDaysBetweenDates(now, requestedDate));
        appointmentRequest.setStatus(AppointmentRequestStatus.PENDING);
        appointmentRequest.setRequestedOn(requestedDate);
        appointmentRequest.setRequestedBy(requestor);

        appointmentService.saveAppointmentRequest(appointmentRequest);

        return appointmentRequest;
    }

    private void initializeServices() {
        if (appointmentService == null) {
            appointmentService = Context.getService(AppointmentService.class);
            obsService = Context.getObsService();
        }
    }

    private AppointmentType getAppointmentTypeByName(String appointmentTypeName) {
        Optional<AppointmentType> type = appointmentService.getAllAppointmentTypes(false).stream()
                        .filter(a -> a.getName().equals(appointmentTypeName)).findAny();
        if (type.isPresent()) {
            return type.get();
        }
        throw new IllegalArgumentException(String.format("Appointment type not found with name: %s", appointmentTypeName));
    }
}
