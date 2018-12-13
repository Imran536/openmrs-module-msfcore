package org.openmrs.module.msfcore.result;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openmrs.Concept;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNumeric;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.TestOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.module.msfcore.Pagination;
import org.openmrs.module.msfcore.api.AuditService;
import org.openmrs.module.msfcore.api.MSFCoreService;
import org.openmrs.module.msfcore.audit.AuditLog;
import org.openmrs.module.msfcore.audit.AuditLog.AuditLogBuilder;
import org.openmrs.module.msfcore.audit.AuditLog.Event;
import org.openmrs.module.msfcore.result.ResultColumn.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Used to populate and handle data to from results.gsp widget for drug orders,
 * test orders and any others results widget
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultsData {
    private Patient patient;
    @Builder.Default
    private List<String> keys = new ArrayList<String>();
    // helps on pagination
    @Builder.Default
    private Pagination pagination = Pagination.builder().build();
    private ResultCategory resultCategory;
    @Builder.Default
    private List<ResultRow> results = new ArrayList<ResultRow>();
    @Builder.Default
    private ResultFilters filters = ResultFilters.builder().build();
    private String dateFormatPattern;

    public void addRetrievedResults() {
        dateFormatPattern = Context.getDateFormat().toPattern();
        if (resultCategory.equals(ResultCategory.DRUG_LIST) || resultCategory.equals(ResultCategory.LAB_RESULTS)
                        || resultCategory.equals(ResultCategory.REFERRAL_LIST)) {
            addOrders();
        }
    }

    private static List<String> getLocalizedKeys(List<String> keys) {
        List<String> localizedKeys = new ArrayList<String>();
        for (String key : keys) {
            localizedKeys.add(Context.getMessageSourceService().getMessage(key));
        }
        return localizedKeys;
    }

    private void addOrders() {
        OrderType orderType = null;
        if (resultCategory.equals(ResultCategory.DRUG_LIST)) {
            orderType = Context.getOrderService().getOrderTypeByUuid(OrderType.DRUG_ORDER_TYPE_UUID);
            keys.addAll(getDrugOrdersKeys());
            filters.setDates(getLocalizedKeys(Arrays.asList("msfcore.datePrescribed", "msfcore.dispenseDate")));
            filters.setName(Context.getMessageSourceService().getMessage("msfcore.drugName"));
            filters.setStatuses(Arrays.asList(ResultStatus.CANCELLED, ResultStatus.PENDING, ResultStatus.ACTIVE, ResultStatus.STOPPED));
        } else if (resultCategory.equals(ResultCategory.LAB_RESULTS)) {
            orderType = Context.getOrderService().getOrderTypeByUuid(OrderType.TEST_ORDER_TYPE_UUID);
            filters.setStatuses(Arrays.asList(ResultStatus.CANCELLED, ResultStatus.PENDING, ResultStatus.COMPLETED));
            filters.setDates(getLocalizedKeys(Arrays.asList("msfcore.orderDate", "msfcore.resultDate")));
            filters.setName(Context.getMessageSourceService().getMessage("msfcore.testName"));
            keys.addAll(getLabResultKeys());
        } else if (resultCategory.equals(ResultCategory.REFERRAL_LIST)) {
            orderType = Context.getOrderService().getOrderTypeByUuid(MSFCoreConfig.REFERRAL_ORDER_TYPE_UUID);
            filters.setName(Context.getMessageSourceService().getMessage("msfcore.location"));
            filters.setStatuses(Arrays.asList(ResultStatus.CANCELLED, ResultStatus.PENDING, ResultStatus.COMPLETED));
            filters.setProviders(getAllProviders());
            keys.addAll(getReferralListKeys());
        }
        List<Order> orders = Context.getService(MSFCoreService.class).getOrders(patient, orderType, null, pagination);
        for (Order o : orders) {
            if (resultCategory.equals(ResultCategory.DRUG_LIST) && o.getOrderType().equals(orderType) && o instanceof DrugOrder) {
                addDrugOrders(o);
            } else if (resultCategory.equals(ResultCategory.LAB_RESULTS) && o.getOrderType().equals(orderType) && o instanceof TestOrder) {
                addTestOrders(o);
            } else if (resultCategory.equals(ResultCategory.REFERRAL_LIST) && o.getOrderType().equals(orderType)) {
                addReferralOrders(o);
            }
        }
    }

    private static List<String> getDrugOrdersKeys() {
        return getLocalizedKeys(Arrays.asList("msfcore.drugName", "msfcore.dose", "msfcore.frequency", "msfcore.duration",
                        "msfcore.instructions", "msfcore.datePrescribed", "msfcore.stop", "msfcore.dispensed", "msfcore.dispenseDate",
                        "msfcore.details"));
    }

    private static List<String> getLabResultKeys() {
        return getLocalizedKeys(Arrays.asList("msfcore.testName", "msfcore.result", "msfcore.uom", "msfcore.range", "msfcore.orderDate",
                        "msfcore.resultDate"));
    }

    private static List<String> getReferralListKeys() {
        return getLocalizedKeys(Arrays.asList("msfcore.location", "msfcore.date", "msfcore.reason", "msfcore.provider", "msfcore.feedback",
                        "msfcore.feedbackFrom"));
    }

    private void addTestOrders(Order o) {
        ResultRow resultRow = new ResultRow();
        TestOrder testOrder = (TestOrder) o;
        // order concept must be a labSet with expected result concepts members
        for (Concept concept : testOrder.getConcept().getSetMembers()) {
            // TODO fix for other investigation order
            Obs resultObs = getResultObs(testOrder, concept);
            resultRow.put("uuid", ResultColumn.builder().value(testOrder.getUuid()).build());
            ResultStatus status = null;
            List<ResultAction> actions = new ArrayList<ResultAction>();
            if (testOrder.getVoided()) {
                status = ResultStatus.CANCELLED;
            } else if (resultObs == null) {
                status = ResultStatus.PENDING;
                actions.add(ResultAction.EDIT);
                actions.add(ResultAction.DELETE);
            } else {
                status = ResultStatus.COMPLETED;
                actions.add(ResultAction.EDIT);
            }
            Type resultType = Type.STRING;
            if (concept.isNumeric()) {
                resultType = Type.NUMBER;
            } else if (concept.getDatatype().getUuid().equals(ConceptDatatype.BOOLEAN_UUID)) {
                resultType = Type.BOOLEAN;
            }
            resultRow.put("status", ResultColumn.builder().value(status).build());
            resultRow.put("actions", ResultColumn.builder().value(actions).build());
            resultRow.put("concept", ResultColumn.builder().value(concept.getUuid()).build());
            resultRow.put(Context.getMessageSourceService().getMessage("msfcore.testName"), ResultColumn.builder().value(
                            concept.getName().getName()).build());
            resultRow.put(Context.getMessageSourceService().getMessage("msfcore.result"), resultObs != null ? ResultColumn.builder()
                            .editable(true).type(resultType).value(resultObs.getValueAsString(Context.getLocale())).build() : ResultColumn
                            .builder().editable(true).type(resultType).value("").build());
            resultRow.put(Context.getMessageSourceService().getMessage("msfcore.uom"), ResultColumn.builder().value(getUnit(concept))
                            .build());
            resultRow.put(Context.getMessageSourceService().getMessage("msfcore.range"), ResultColumn.builder().value(getRange(concept))
                            .build());
            resultRow.put(Context.getMessageSourceService().getMessage("msfcore.orderDate"), ResultColumn.builder().type(Type.DATE).value(
                            testOrder.getDateActivated()).build());
            resultRow.put(Context.getMessageSourceService().getMessage("msfcore.resultDate"), resultObs != null ? ResultColumn.builder()
                            .editable(true).type(Type.DATE).value(resultObs.getObsDatetime()).build() : ResultColumn.builder().editable(
                            true).type(Type.DATE).value("").build());
            addResultRow(resultRow);
        }

    }

    // TODO add unit test when working on drug order
    private void addDrugOrders(Order o) {
        ResultRow resultRow = new ResultRow();
        DrugOrder drugOrder = (DrugOrder) o;
        Concept dispensedConcept = Context.getConceptService().getConceptByUuid(MSFCoreConfig.CONCEPT_UUID_DISPENSED);
        Obs dispensedObs = getResultObs(drugOrder, dispensedConcept);
        Obs dispensedDateObs = getResultObs(drugOrder, Context.getConceptService().getConceptByUuid(
                        MSFCoreConfig.CONCEPT_UUID_DESPENSED_DATE));
        Obs dispensedDetailsObs = getResultObs(drugOrder, Context.getConceptService().getConceptByUuid(
                        MSFCoreConfig.CONCEPT_UUID_DESPENSED_DETAILS));
        ResultStatus status = null;
        List<ResultAction> actions = new ArrayList<ResultAction>();
        boolean isDiscontinued = drugOrder.isDiscontinuedRightNow();
        if (drugOrder.getVoided()) {
            status = ResultStatus.CANCELLED;
        } else if (isDiscontinued) {
            status = ResultStatus.STOPPED;
        } else if (dispensedObs == null || dispensedDateObs == null) {
            status = ResultStatus.PENDING;
            actions.add(ResultAction.EDIT);
            actions.add(ResultAction.DELETE);
        } else {// only make active discontinuable at client side
            status = ResultStatus.ACTIVE;
            actions.add(ResultAction.EDIT);
        }
        resultRow.put("uuid", ResultColumn.builder().value(drugOrder.getUuid()).build());
        resultRow.put("status", ResultColumn.builder().value(status).build());
        resultRow.put("actions", ResultColumn.builder().value(actions).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.drugName"), ResultColumn.builder().value(
                        drugOrder.getDrug().getName()).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.dose"), ResultColumn.builder().value(
                        getDrugOrderDose(drugOrder)).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.frequency"), ResultColumn.builder().value(
                        drugOrder.getFrequency() != null && drugOrder.getFrequency().getFrequencyPerDay() != null ? drugOrder
                                        .getFrequency().getFrequencyPerDay()
                                        + " " + Context.getMessageSourceService().getMessage("msfcore.perDayShort") : "").build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.duration"), ResultColumn.builder().value(
                        getDrugOrderDuration(drugOrder)).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.instructions"), ResultColumn.builder().value(
                        drugOrder.getInstructions()).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.datePrescribed"), ResultColumn.builder().type(Type.DATE).value(
                        drugOrder.getDateActivated()).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.stop"), ResultColumn.builder().type(Type.STOP).value(
                        isDiscontinued).stopDate(isDiscontinued ? drugOrder.getDateStopped() : null).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.dispensed"), ResultColumn.builder().editable(true).type(
                        Type.CODED).value(dispensedObs != null ? dispensedObs.getValueCoded().getName().getName() : "").codedOptions(
                        getCodedOptionsFromConceptSet(dispensedConcept)).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.dispenseDate"), ResultColumn.builder().editable(true).type(
                        Type.DATE).value(dispensedDateObs != null ? dispensedDateObs.getValueDate() : "").build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.details"), ResultColumn.builder().editable(true).value(
                        dispensedDetailsObs != null ? dispensedDetailsObs.getValueText() : "").build());

        addResultRow(resultRow);
    }

    private void addReferralOrders(Order order) {
        ResultRow resultRow = new ResultRow();
        ResultStatus status = null;
        Obs feedback = getResultObs(order, Context.getConceptService().getConceptByUuid(MSFCoreConfig.CONCEPT_UUID_FEEDBACK));
        Obs feedbackFrom = getResultObs(order, Context.getConceptService().getConceptByUuid(MSFCoreConfig.CONCEPT_UUID_FEEDBACK_FROM));

        List<ResultAction> actions = new ArrayList<ResultAction>();
        if (order.getVoided()) {
            status = ResultStatus.CANCELLED;
        } else if (feedback == null || feedbackFrom == null) {
            status = ResultStatus.PENDING;
            actions.add(ResultAction.EDIT);
            actions.add(ResultAction.DELETE);
        } else {
            status = ResultStatus.COMPLETED;
            actions.add(ResultAction.EDIT);
        }
        resultRow.put("uuid", ResultColumn.builder().value(order.getUuid()).build());
        resultRow.put("status", ResultColumn.builder().value(status).build());
        resultRow.put("actions", ResultColumn.builder().value(actions).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.location"), ResultColumn.builder().value(
                        order.getConcept().getName().getName()).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.date"), ResultColumn.builder().value(order.getDateActivated())
                        .type(Type.DATE).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.reason"), ResultColumn.builder().value(
                        order.getOrderReasonNonCoded()).build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.provider"), ResultColumn.builder().value(
                        order.getOrderer() != null ? order.getOrderer().getName() : "").build());
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.feedback"), ResultColumn.builder().editable(true).value(
                        feedback != null ? feedback.getValueText() : "").build());
        Provider provider = feedbackFrom != null ? Context.getProviderService().getProviderByUuid(feedbackFrom.getValueText()) : null;
        resultRow.put(Context.getMessageSourceService().getMessage("msfcore.feedbackFrom"), ResultColumn.builder().editable(true).type(
                        Type.CODED).value(provider != null ? provider.getName() : "").codedOptions(getAllProviders()).build());
        addResultRow(resultRow);
    }

    private List<CodedOption> getAllProviders() {
        return Context.getProviderService().getAllProviders(false).stream()
                        .map(p -> CodedOption.builder().name(p.getName()).uuid(p.getUuid()).build()).collect(Collectors.toList());
    }
    private List<CodedOption> getCodedOptionsFromConceptSet(Concept dispensedConcept) {
        List<CodedOption> options = new ArrayList<CodedOption>();
        if (dispensedConcept.getSet()) {
            for (Concept member : dispensedConcept.getSetMembers()) {
                options.add(CodedOption.builder().name(member.getName().getName()).uuid(member.getUuid()).build());
            }
        }
        return options;
    }

    private Object getDrugOrderDuration(DrugOrder drugOrder) {
        String duration = "";
        if (drugOrder.getDuration() != null) {
            duration += drugOrder.getDuration();
        }
        if (drugOrder.getDurationUnits() != null) {
            duration += " " + drugOrder.getDurationUnits().getName().getName();
        }
        return duration;
    }

    private String getDrugOrderDose(DrugOrder drugOrder) {
        String dose = "";
        if (drugOrder.getDose() != null) {
            dose += drugOrder.getDose();
        }
        if (drugOrder.getDoseUnits() != null) {
            ConceptName doseUnitName = drugOrder.getDoseUnits().getShortNameInLocale(Context.getLocale());
            dose += " " + (doseUnitName != null ? doseUnitName.getName() : drugOrder.getDoseUnits().getName().getName());
        }
        return dose;
    }

    private void addResultRow(ResultRow resultRow) {
        if (!resultRow.isEmpty()) {
            results.add(resultRow);
        }
    }

    private String getUnit(Concept concept) {
        return concept.isNumeric() ? Context.getConceptService().getConceptNumeric(concept.getConceptId()).getUnits() : "";
    }

    // TODO use normal ranges, add another key for absoluteRange to use for
    // validation
    private String getRange(Concept concept) {
        String range = "";
        if (concept.isNumeric()) {
            ConceptNumeric cn = Context.getConceptService().getConceptNumeric(concept.getConceptId());
            if (cn.getHiAbsolute() != null && cn.getLowAbsolute() != null) {
                range = cn.getLowAbsolute() + " - " + cn.getHiAbsolute();
            }
        }
        return range;
    }

    private static Obs getResultObs(Order order, Concept concept) {
        List<Obs> obs = Context.getService(MSFCoreService.class).getObservationsByOrderAndConcept(order, concept);
        // the latest result obs
        return !obs.isEmpty() ? obs.get(0) : null;
    }

    public static ResultCategory parseCategory(String category) {
        return ResultCategory.valueOf(category.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toUpperCase());
    }

    private static Obs createBasicObsWithOrder(Order order, ResultCategory category, Concept concept) {
        Obs obs = new Obs();
        obs.setConcept(concept);
        obs.setPerson(order.getPatient());
        obs.setEncounter(buildEncounter(category, order.getPatient()));
        obs.setLocation(Context.getLocationService().getDefaultLocation());
        obs.setOrder(order);
        return obs;
    }

    private static Encounter buildEncounter(ResultCategory category, Patient patient) {
        Encounter enc = new Encounter();
        EncounterType type = null;
        if (category.equals(ResultCategory.LAB_RESULTS)) {
            type = Context.getEncounterService().getEncounterTypeByUuid(MSFCoreConfig.ENCOUNTER_TYPE_LAB_RESULTS_UUID);
        } else if (category.equals(ResultCategory.DRUG_LIST)) {
            type = Context.getEncounterService().getEncounterTypeByUuid(MSFCoreConfig.ENCOUNTER_TYPE_DISPENSE_DRUG_UUID);
        } else if (category.equals(ResultCategory.REFERRAL_LIST)) {
            type = Context.getEncounterService().getEncounterTypeByUuid(MSFCoreConfig.ENCOUNTER_TYPE_REFER_PATIENT_UUID);
        }
        enc.setEncounterType(type);// suport other enounterTypes if needed
        enc.setEncounterDatetime(new Date());
        enc.setPatient(patient);
        return enc;
    }

    public static List<Obs> updateResultRow(LinkedHashMap<String, Object> propertiesToCreate) {
        try {
            if (parseCategory((String) propertiesToCreate.get("category")).equals(ResultCategory.LAB_RESULTS)) {
                return updateLabResultRow(propertiesToCreate);
            } else if (parseCategory((String) propertiesToCreate.get("category")).equals(ResultCategory.DRUG_LIST)) {
                return updateDrugOrderRow(propertiesToCreate);
            } else if (parseCategory((String) propertiesToCreate.get("category")).equals(ResultCategory.REFERRAL_LIST)) {
                return updateReferralOrderRow(propertiesToCreate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<Obs> updateLabResultRow(LinkedHashMap<String, Object> propertiesToCreate) throws ParseException {
        Obs obs = null;
        String changeReason = "Updating lab test result obs from results widget";
        TestOrder testOrder = null;
        Concept concept = null;
        for (Map resultColumnEntry : (ArrayList<Map>) propertiesToCreate.get("payload")) {
            String[] idContent = ((String) resultColumnEntry.get("uuid_key_type_concept")).split("_");
            String value = (String) resultColumnEntry.get("value");
            String key = getLabResultKeys().get(Integer.parseInt(idContent[1]));
            if (testOrder == null) {
                testOrder = (TestOrder) Context.getOrderService().getOrderByUuid(idContent[0]);
            }
            if (concept == null) {
                concept = Context.getConceptService().getConceptByUuid(idContent[3]);
            }
            obs = retrieveOrCreateObs(obs, testOrder, concept, ResultCategory.LAB_RESULTS);
            if (idContent[2].equals("DATE")) {
                if (key.equals(Context.getMessageSourceService().getMessage("msfcore.resultDate"))) {
                    obs.setObsDatetime(Context.getDateFormat().parse(value));
                }
            } else if (key.equals(Context.getMessageSourceService().getMessage("msfcore.result"))) {
                obs.setValueAsString(value);
            }
        }
        AuditLogBuilder labResultEventBuilder = buildAuditLog(obs, Event.ADD_LAB_RESULT, Event.EDIT_LAB_RESULT);
        obs = Context.getObsService().saveObs(obs, changeReason);
        Context.getService(AuditService.class).saveAuditLog(labResultEventBuilder.build());
        return Arrays.asList(obs);
    }

    private static AuditLogBuilder buildAuditLog(Obs obs, Event addEvent, Event editEvent) {
        Patient patient = Context.getPatientService().getPatient(obs.getPerson().getPersonId());
        AuditLogBuilder resultEventBuilder = AuditLog.builder().event(editEvent).user(Context.getAuthenticatedUser()).patient(patient)
                        .detail(
                                        Context.getMessageSourceService().getMessage(
                                                        "msfcore.resultEvent",
                                                        new Object[]{"Edited", obs.getConcept().getName().getName(),
                                                                        obs.getPerson().getPersonName().getFullName(),
                                                                        patient.getPatientIdentifier().getIdentifier()}, null));
        if (obs.getEncounter().getId() == null) {// this is a new obs/result
            obs.setEncounter(Context.getEncounterService().saveEncounter(obs.getEncounter()));
            resultEventBuilder.event(addEvent).detail(
                            Context.getMessageSourceService().getMessage(
                                            "msfcore.resultEvent",
                                            new Object[]{"Added", obs.getConcept().getName().getName(),
                                                            obs.getPerson().getPersonName().getFullName(),
                                                            patient.getPatientIdentifier().getIdentifier()}, null));
        }
        return resultEventBuilder;
    }

    private static Obs retrieveOrCreateObs(Obs obs, Order order, Concept concept, ResultCategory category) {
        if (obs == null) {
            obs = getResultObs(order, concept);
        }
        if (obs == null) {
            obs = createBasicObsWithOrder(order, category, concept);
        }
        return obs;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<Obs> updateDrugOrderRow(LinkedHashMap<String, Object> propertiesToCreate) throws ParseException {
        List<Obs> obsList = new ArrayList<Obs>();
        String changeReason = "Updating drug order obs from results widget";
        for (Map resultColumnEntry : (ArrayList<Map>) propertiesToCreate.get("payload")) {
            String[] idContent = ((String) resultColumnEntry.get("uuid_key_type_concept")).split("_");
            String value = (String) resultColumnEntry.get("value");
            String key = getDrugOrdersKeys().get(Integer.parseInt(idContent[1]));
            Obs obs = null;
            DrugOrder drugOrder = (DrugOrder) Context.getOrderService().getOrderByUuid(idContent[0]);
            if (key.equals(Context.getMessageSourceService().getMessage("msfcore.dispensed"))) {
                obs = retrieveOrCreateObs(obs, drugOrder, Context.getConceptService()
                                .getConceptByUuid(MSFCoreConfig.CONCEPT_UUID_DISPENSED), ResultCategory.DRUG_LIST);
                obs.setValueCoded(Context.getConceptService().getConceptByUuid(value));
            } else if (key.equals(Context.getMessageSourceService().getMessage("msfcore.dispenseDate"))) {
                obs = retrieveOrCreateObs(obs, drugOrder, Context.getConceptService().getConceptByUuid(
                                MSFCoreConfig.CONCEPT_UUID_DESPENSED_DATE), ResultCategory.DRUG_LIST);
                obs.setValueDate(Context.getDateFormat().parse(value));
            } else if (key.equals(Context.getMessageSourceService().getMessage("msfcore.details"))) {
                obs = retrieveOrCreateObs(obs, drugOrder, Context.getConceptService().getConceptByUuid(
                                MSFCoreConfig.CONCEPT_UUID_DESPENSED_DETAILS), ResultCategory.DRUG_LIST);
                obs.setValueText(value);
            }
            obs.setObsDatetime(new Date());
            AuditLogBuilder labResultEventBuilder = buildAuditLog(obs, Event.ADD_DRUG_DISPENSING, Event.EDIT_DRUG_DISPENSING);
            obs = Context.getObsService().saveObs(obs, changeReason);
            Context.getService(AuditService.class).saveAuditLog(labResultEventBuilder.build());
            obsList.add(obs);
        }
        return obsList;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<Obs> updateReferralOrderRow(LinkedHashMap<String, Object> propertiesToCreate) throws ParseException {
        List<Obs> obsList = new ArrayList<Obs>();
        String changeReason = "Updating referral order obs from results widget";
        for (Map resultColumnEntry : (ArrayList<Map>) propertiesToCreate.get("payload")) {
            String[] idContent = ((String) resultColumnEntry.get("uuid_key_type_concept")).split("_");
            String value = (String) resultColumnEntry.get("value");
            String key = getReferralListKeys().get(Integer.parseInt(idContent[1]));
            Obs obs = null;
            Order order = Context.getOrderService().getOrderByUuid(idContent[0]);
            if (key.equals(Context.getMessageSourceService().getMessage("msfcore.feedback"))) {
                obs = retrieveOrCreateObs(obs, order, Context.getConceptService().getConceptByUuid(MSFCoreConfig.CONCEPT_UUID_FEEDBACK),
                                ResultCategory.REFERRAL_LIST);
                obs.setValueText(value);
            } else if (key.equals(Context.getMessageSourceService().getMessage("msfcore.feedbackFrom"))) {
                obs = retrieveOrCreateObs(obs, order, Context.getConceptService()
                                .getConceptByUuid(MSFCoreConfig.CONCEPT_UUID_FEEDBACK_FROM), ResultCategory.REFERRAL_LIST);
                obs.setValueText(value);// provider uuid
            }
            obs.setObsDatetime(new Date());
            AuditLogBuilder resultEventBuilder = buildAuditLog(obs, Event.ADD_REFERRAL, Event.EDIT_REFERRAL);
            obs = Context.getObsService().saveObs(obs, changeReason);
            Context.getService(AuditService.class).saveAuditLog(resultEventBuilder.build());
            obsList.add(obs);
        }
        return obsList;
    }

    /**
     * @param order
     * @param category
     * @return the order that has discontinued received order
     */
    public static Order discontinueOrder(Order order, ResultCategory category) {
        String discontinueReasonNonCoded = "Discontinuing order from results widget";
        Collection<Provider> loggedInProviders = Context.getProviderService().getProvidersByPerson(
                        Context.getAuthenticatedUser().getPerson());
        Encounter encounter = Context.getEncounterService().saveEncounter(buildEncounter(category, order.getPatient()));
        return Context.getOrderService().discontinueOrder(order, discontinueReasonNonCoded, new Date(),
                        !loggedInProviders.isEmpty() ? loggedInProviders.iterator().next() : order.getOrderer(), encounter);
    }
}
