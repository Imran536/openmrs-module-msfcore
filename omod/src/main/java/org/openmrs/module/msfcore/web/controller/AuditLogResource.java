package org.openmrs.module.msfcore.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.msfcore.Pagination;
import org.openmrs.module.msfcore.Pagination.PaginationBuilder;
import org.openmrs.module.msfcore.api.AuditService;
import org.openmrs.module.msfcore.audit.AuditLog;
import org.openmrs.module.msfcore.audit.AuditLog.Event;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + MSFCoreRestController.NAMESPACE + "/auditlog", supportedClass = AuditLog.class, supportedOpenmrsVersions = {"2.2.*"})
public class AuditLogResource extends BaseDataResource {

    @Override
    protected AlreadyPaged<SimpleObject> doSearch(RequestContext context) throws ResponseException {
        PaginationBuilder paginationBuilder = buildPaginationFromContext(context);
        SimpleObject response = new SimpleObject();

        String patientId = context.getParameter("patientId");
        // time in milisecs
        String startDateTime = context.getParameter("startDateTime");
        // time in milisecs
        String endDateTime = context.getParameter("endDateTime");
        // comma separated
        String selectedEventsRaw = context.getParameter("selectedEvents");
        // username
        String user = context.getParameter("user");

        Patient patient = null;
        Date startTime = null;
        Date endTime = null;
        String[] selectedEvents = null;
        if (StringUtils.isNotBlank(patientId)) {
            patient = getPatientFromId(patientId);
        }
        SimpleDateFormat dateISOFormatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (StringUtils.isNotBlank(startDateTime)) {
                startTime = dateISOFormatted.parse(startDateTime);
            }
            if (StringUtils.isNotBlank(endDateTime)) {
                endTime = dateISOFormatted.parse(endDateTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(selectedEventsRaw)) {
            selectedEvents = selectedEventsRaw.split(",");
        }
        List<Event> logEvents = new ArrayList<Event>();
        List<Patient> patients = null;
        List<User> users = null;

        if (patient != null) {
            patients = Arrays.asList(patient);
        }
        if (StringUtils.isNotBlank(user)) {
            users = new ArrayList<User>();
            User logUser = Context.getUserService().getUserByUsername(user.trim());
            if (logUser == null) {
                user = "";
            }
            users.add(logUser);
        }
        if (selectedEvents != null) {
            for (String event : selectedEvents) {
                logEvents.add(Event.valueOf(event));
            }
        } else {
            logEvents = null;
        }
        Pagination pagination = paginationBuilder.build();
        List<AuditLog> auditLogs = Context.getService(AuditService.class).getAuditLogs(startTime, endTime, logEvents, patients, users,
                        null, null, pagination);
        response.add("pagination", pagination);
        response.add("auditLogs", auditLogs);
        response.add("events", Event.values());
        response.add("userSuggestions", userSuggestions());
        response.add("startTime", startDateTime);
        response.add("endTime", endDateTime);
        response.add("selectEvents", selectedEvents);
        response.add("patientId", patient != null ? patient.getUuid() : null);
        response.add("patientDisplay", patient != null ? patient.getPersonName().getFullName() + " #"
                        + patient.getPatientIdentifier().getIdentifier() : null);
        response.add("user", user);
        return new AlreadyPaged<SimpleObject>(context, Arrays.asList(response), false);
    }

    @Override
    protected AlreadyPaged<SimpleObject> doGetAll(RequestContext context) throws ResponseException {
        Pagination pagination = Pagination.builder().build();
        SimpleObject response = new SimpleObject();
        List<AuditLog> audits = Context.getService(AuditService.class).getAuditLogs(null, null, null, null, null, null, null, pagination);
        response.add("pagination", pagination);
        response.add("auditLogs", audits);
        response.add("events", Event.values());
        response.add("userSuggestions", userSuggestions());
        return new AlreadyPaged<SimpleObject>(context, Arrays.asList(response), false);
    }

    @Override
    public AuditLog newDelegate() {
        AuditLog delegate = new AuditLog();
        delegate.setDate(new Date());
        delegate.setUuid(UUID.randomUUID().toString());
        return delegate;
    }

    @Override
    public Object create(SimpleObject propertiesToCreate, RequestContext context) throws ResponseException {
        String patientId = null;
        if (propertiesToCreate.containsKey("patientId")) {
            patientId = propertiesToCreate.get("patientId");
            propertiesToCreate.remove("patientId");
        }
        AuditLog delegate = (AuditLog) convert(propertiesToCreate);
        if (StringUtils.isNotBlank(patientId)) {
            delegate.setPatient(getPatientFromId(patientId));
        }
        setEventDetails(delegate);
        if (delegate.getUser() == null && !delegate.getEvent().equals(Event.LOGIN)) {
            delegate.setUser(Context.getAuthenticatedUser());
        }
        delegate = Context.getService(AuditService.class).getAuditLog(Context.getService(AuditService.class).saveAuditLog(delegate));
        SimpleObject ret = (SimpleObject) ConversionUtil.convertToRepresentation(delegate, context.getRepresentation());
        return ret;
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("event");
        description.addRequiredProperty("date");
        description.addRequiredProperty("detail");
        description.addProperty("patient", Representation.REF);
        description.addProperty("user", Representation.REF);
        description.addProperty("provider", Representation.REF);
        description.addProperty("location", Representation.REF);
        return description;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("event");
        description.addProperty("patient");
        description.addProperty("user");
        description.addProperty("provider");
        description.addProperty("location");
        return description;
    }

    private void setEventDetails(AuditLog auditLog) {
        if (auditLog.getEvent().equals(Event.VIEW_LAB_RESULTS)) {
            auditLog.setDetail(Context.getMessageSourceService().getMessage(
                            "msfcore.viewLabResultsEvent",
                            new Object[]{auditLog.getPatient().getPerson().getPersonName().getFullName(),
                                            auditLog.getPatient().getPatientIdentifier().getIdentifier()}, null));
        } else if (auditLog.getEvent().equals(Event.VIEW_DRUG_DISPENSING)) {
            auditLog.setDetail(Context.getMessageSourceService().getMessage(
                            "msfcore.viewDrugDispensingEvent",
                            new Object[]{auditLog.getPatient().getPerson().getPersonName().getFullName(),
                                            auditLog.getPatient().getPatientIdentifier().getIdentifier()}, null));
        } else if (auditLog.getEvent().equals(Event.VIEW_PATIENT_REFERRALS)) {
            auditLog.setDetail(Context.getMessageSourceService().getMessage(
                            "msfcore.viewPatientReferralsEvent",
                            new Object[]{auditLog.getPatient().getPerson().getPersonName().getFullName(),
                                            auditLog.getPatient().getPatientIdentifier().getIdentifier()}, null));
        }
        // TODO support others as u need in future
    }

    private List<String> userSuggestions() {
        List<String> suggestions = new ArrayList<String>();

        for (User u : Context.getUserService().getAllUsers()) {
            if (StringUtils.isNotBlank(u.getUsername())) {
                suggestions.add(u.getUsername());
            }
            if (StringUtils.isNotBlank(u.getSystemId())) {
                suggestions.add(u.getSystemId());
            }
        }
        return new ArrayList<String>(new HashSet<String>(suggestions));
    }
}
