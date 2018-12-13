package org.openmrs.module.msfcore.result;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.module.msfcore.Pagination;
import org.openmrs.module.msfcore.api.AuditService;
import org.openmrs.module.msfcore.audit.AuditLog;
import org.openmrs.module.msfcore.audit.AuditLog.Event;
import org.openmrs.module.msfcore.result.ResultColumn.Type;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class ResultsDataTest extends BaseModuleContextSensitiveTest {

    @Test
    public void addRetriedResults_shouldAddLabTestOrders() throws ParseException {
        executeDataSet("ResultsData.xml");

        // should retrieve and add lab test orders with matching results
        ResultsData resultsData = ResultsData.builder().resultCategory(ResultCategory.LAB_RESULTS).patient(
                        Context.getPatientService().getPatient(7)).build();
        resultsData.addRetrievedResults();

        assertEquals(Arrays.asList(Context.getMessageSourceService().getMessage("msfcore.testName"), Context.getMessageSourceService()
                        .getMessage("msfcore.result"), Context.getMessageSourceService().getMessage("msfcore.uom"), Context
                        .getMessageSourceService().getMessage("msfcore.range"), Context.getMessageSourceService().getMessage(
                        "msfcore.orderDate"), Context.getMessageSourceService().getMessage("msfcore.resultDate")), resultsData.getKeys());

        // should only test orders and match their respective results
        // obs 51 for concept#500029 should have been left out since it has no
        // test order
        assertEquals(3, resultsData.getResults().size());

        // should retrieve with a completed result
        assertEquals(ResultColumn.builder().value("00ddc453-fc20-4f1b-a351-7eff54b4daf1").build(), resultsData.getResults().get(0).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.COMPLETED).build(), resultsData.getResults().get(0).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList(ResultAction.EDIT)).build(), resultsData.getResults().get(0).get("actions"));
        assertEquals(ResultColumn.builder().value("Creatinine").build(), resultsData.getResults().get(0).get(
                        Context.getMessageSourceService().getMessage("msfcore.testName")));
        assertEquals(ResultColumn.builder().value("500022AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build(), resultsData.getResults().get(0).get(
                        "concept"));
        assertEquals(ResultColumn.builder().value("0.97").editable(true).type(Type.NUMBER).build(), resultsData.getResults().get(0).get(
                        Context.getMessageSourceService().getMessage("msfcore.result")));
        assertEquals(ResultColumn.builder().value("mg/dL").build(), resultsData.getResults().get(0).get(
                        Context.getMessageSourceService().getMessage("msfcore.uom")));
        assertEquals(ResultColumn.builder().value("0.5 - 1.1").build(), resultsData.getResults().get(0).get(
                        Context.getMessageSourceService().getMessage("msfcore.range")));
        assertEquals("2018-10-31 02:14:38.0", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(resultsData.getResults().get(0).get(
                        Context.getMessageSourceService().getMessage("msfcore.orderDate")).getValue()));
        assertEquals("2018-11-01 02:00:00.0", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(resultsData.getResults().get(0).get(
                        Context.getMessageSourceService().getMessage("msfcore.resultDate")).getValue()));

        // should retrieve a pending result
        assertEquals(ResultColumn.builder().value("00ddc453-fc20-4f1b-a351-7eff54b4daf2").build(), resultsData.getResults().get(1).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.PENDING).build(), resultsData.getResults().get(1).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList(ResultAction.EDIT, ResultAction.DELETE)).build(), resultsData.getResults()
                        .get(1).get("actions"));

        // should retrieve a cancelled result
        assertEquals(ResultColumn.builder().value("00ddc453-fc20-4f1b-a351-7eff54b4daf3").build(), resultsData.getResults().get(2).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.CANCELLED).build(), resultsData.getResults().get(2).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList()).build(), resultsData.getResults().get(2).get("actions"));

        // should retreive filters
        assertEquals(ResultFilters.builder().name(Context.getMessageSourceService().getMessage("msfcore.testName")).statuses(
                        Arrays.asList(ResultStatus.CANCELLED, ResultStatus.PENDING, ResultStatus.COMPLETED)).dates(
                        Arrays.asList(Context.getMessageSourceService().getMessage("msfcore.orderDate"), Context.getMessageSourceService()
                                        .getMessage("msfcore.resultDate"))).build(), resultsData.getFilters());

        // should retrive default pagination with number of found results set
        assertEquals(Pagination.builder().totalItemsNumber(3).build(), resultsData.getPagination());

        // should retrieve right dateFormatPattern
        assertEquals(Context.getDateFormat().toPattern(), resultsData.getDateFormatPattern());
    }

    @Test
    public void parseCategory() {
        assertEquals(ResultCategory.DRUG_LIST, ResultsData.parseCategory("drugList"));
        assertEquals(ResultCategory.LAB_RESULTS, ResultsData.parseCategory("labResults"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseCategory_failWithNonMatchable() {
        ResultsData.parseCategory("anyThingElse");
    }

    @Test
    public void updateResultRow_shouldUpdateExistingLabResult() {
        executeDataSet("ResultsData.xml");

        LinkedHashMap<String, Object> mapWithPayload = new LinkedHashMap<String, Object>();
        mapWithPayload.put("category", "labResults");
        List<LinkedHashMap<String, Object>> payload = new ArrayList<LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> payloadCreatinineResultDate = new LinkedHashMap<String, Object>();
        payloadCreatinineResultDate.put("uuid_key_type_concept",
                        "00ddc453-fc20-4f1b-a351-7eff54b4daf1_5_DATE_500022AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        payloadCreatinineResultDate.put("value", "02/11/2018");
        LinkedHashMap<String, Object> payloadCreatinineResult = new LinkedHashMap<String, Object>();
        payloadCreatinineResult.put("uuid_key_type_concept",
                        "00ddc453-fc20-4f1b-a351-7eff54b4daf1_1_STRING_500022AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        payloadCreatinineResult.put("value", "0.98");

        payload.add(payloadCreatinineResultDate);
        payload.add(payloadCreatinineResult);
        mapWithPayload.put("payload", payload);
        List<Obs> updated = (List<Obs>) ResultsData.updateResultRow(mapWithPayload);
        Assert.assertEquals(1, updated.size());
        Assert.assertEquals("2018-11-02", new SimpleDateFormat("yyyy-MM-dd").format(updated.get(0).getObsDatetime()));
        Assert.assertEquals(Double.valueOf("0.98"), updated.get(0).getValueNumeric());

        // should add a respective auditLog
        AuditLog audit = Context.getService(AuditService.class).getAuditLogs(null, null, Arrays.asList(Event.EDIT_LAB_RESULT),
                        Arrays.asList(Context.getPatientService().getPatient(7)), null, null, null, null).get(0);
        Assert.assertEquals(Event.EDIT_LAB_RESULT, audit.getEvent());
        Assert.assertEquals("Edited: [Creatinine] result for Patient: Collet Test Chebaskwony - 6TS-4", audit.getDetail());
    }

    @Test
    public void updateResultRow_shouldCreateNewLabResultEntry() {
        executeDataSet("ResultsData.xml");

        LinkedHashMap<String, Object> mapWithPayload = new LinkedHashMap<String, Object>();
        mapWithPayload.put("category", "labResults");
        List<LinkedHashMap<String, Object>> payload = new ArrayList<LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> payloadCreatinineResultDate = new LinkedHashMap<String, Object>();
        payloadCreatinineResultDate.put("uuid_key_type_concept",
                        "00ddc453-fc20-4f1b-a351-7eff54b4daf3_5_DATE_500025AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        payloadCreatinineResultDate.put("value", "03/11/2018");
        LinkedHashMap<String, Object> payloadCreatinineResult = new LinkedHashMap<String, Object>();
        payloadCreatinineResult.put("uuid_key_type_concept",
                        "00ddc453-fc20-4f1b-a351-7eff54b4daf3_1_STRING_500025AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        payloadCreatinineResult.put("value", "1.8");

        payload.add(payloadCreatinineResultDate);
        payload.add(payloadCreatinineResult);
        mapWithPayload.put("payload", payload);
        List<Obs> created = (List<Obs>) ResultsData.updateResultRow(mapWithPayload);
        Assert.assertEquals(1, created.size());
        Assert.assertEquals("2018-11-03", new SimpleDateFormat("yyyy-MM-dd").format(created.get(0).getObsDatetime()));
        Assert.assertEquals(Integer.valueOf(500025), created.get(0).getConcept().getId());
        Assert.assertEquals("00ddc453-fc20-4f1b-a351-7eff54b4daf3", created.get(0).getOrder().getUuid());
        Assert.assertEquals(Double.valueOf("1.8"), created.get(0).getValueNumeric());
        Assert.assertEquals(MSFCoreConfig.ENCOUNTER_TYPE_LAB_RESULTS_UUID, created.get(0).getEncounter().getEncounterType().getUuid());

        // should add a respective auditLog
        AuditLog audit = Context.getService(AuditService.class).getAuditLogs(null, null, Arrays.asList(Event.ADD_LAB_RESULT),
                        Arrays.asList(Context.getPatientService().getPatient(7)), null, null, null, null).get(0);
        Assert.assertEquals(Event.ADD_LAB_RESULT, audit.getEvent());
        Assert.assertEquals("Added: [ECG] result for Patient: Collet Test Chebaskwony - 6TS-4", audit.getDetail());
    }

    @Test
    public void addRetriedResults_shouldAddDrugOrders() throws ParseException {
        executeDataSet("ResultsData.xml");

        // should retrieve and add lab test orders with matching results
        ResultsData resultsData = ResultsData.builder().resultCategory(ResultCategory.DRUG_LIST).patient(
                        Context.getPatientService().getPatient(7)).build();
        resultsData.addRetrievedResults();

        assertEquals(Arrays.asList(Context.getMessageSourceService().getMessage("msfcore.drugName"), Context.getMessageSourceService()
                        .getMessage("msfcore.dose"), Context.getMessageSourceService().getMessage("msfcore.frequency"), Context
                        .getMessageSourceService().getMessage("msfcore.duration"), Context.getMessageSourceService().getMessage(
                        "msfcore.instructions"), Context.getMessageSourceService().getMessage("msfcore.datePrescribed"), Context
                        .getMessageSourceService().getMessage("msfcore.stop"), Context.getMessageSourceService().getMessage(
                        "msfcore.dispensed"), Context.getMessageSourceService().getMessage("msfcore.dispenseDate"), Context
                        .getMessageSourceService().getMessage("msfcore.details")), resultsData.getKeys());

        assertEquals(6, resultsData.getResults().size());

        assertEquals(ResultColumn.builder().value("ASPIRIN").build(), resultsData.getResults().get(3).get(
                        Context.getMessageSourceService().getMessage("msfcore.drugName")));
        assertEquals(ResultColumn.builder().value("325.0 mg").build(), resultsData.getResults().get(3).get(
                        Context.getMessageSourceService().getMessage("msfcore.dose")));
        assertEquals(ResultColumn.builder().value("").build(), resultsData.getResults().get(3).get(
                        Context.getMessageSourceService().getMessage("msfcore.frequency")));
        assertEquals(ResultColumn.builder().value("").build(), resultsData.getResults().get(3).get(
                        Context.getMessageSourceService().getMessage("msfcore.duration")));

        assertEquals(ResultColumn.builder().value("2x daily").build(), resultsData.getResults().get(4).get(
                        Context.getMessageSourceService().getMessage("msfcore.instructions")));
        assertEquals("2008-08-08 00:00:00.0", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(resultsData.getResults().get(4).get(
                        Context.getMessageSourceService().getMessage("msfcore.datePrescribed")).getValue()));
        assertEquals(ResultColumn.builder().value(true).type(Type.STOP).stopDate(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2008-08-15 00:00:00.0")).build(), resultsData.getResults()
                        .get(4).get(Context.getMessageSourceService().getMessage("msfcore.stop")));
        assertEquals(ResultColumn.builder().value("").editable(true).type(Type.CODED).codedOptions(
                        Arrays.asList(CodedOption.builder().name("Partial").uuid("5000037AAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build(),
                                        CodedOption.builder().name("Full").uuid("5000038AAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build())).build(),
                        resultsData.getResults().get(4).get(Context.getMessageSourceService().getMessage("msfcore.dispensed")));
        assertEquals("", resultsData.getResults().get(4).get(Context.getMessageSourceService().getMessage("msfcore.dispenseDate"))
                        .getValue());
        assertEquals(ResultColumn.builder().editable(true).value("").build(), resultsData.getResults().get(4).get(
                        Context.getMessageSourceService().getMessage("msfcore.details")));

        assertEquals(ResultColumn.builder().value("921de0a3-05c4-444a-be03-e01b4c4b9142").build(), resultsData.getResults().get(4).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.STOPPED).build(), resultsData.getResults().get(4).get("status"));

        assertEquals(ResultColumn.builder().value(ResultStatus.PENDING).build(), resultsData.getResults().get(3).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList(ResultAction.EDIT, ResultAction.DELETE)).build(), resultsData.getResults()
                        .get(3).get("actions"));

        assertEquals(ResultColumn.builder().value("3x monthly").build(), resultsData.getResults().get(5).get(
                        Context.getMessageSourceService().getMessage("msfcore.instructions")));
        assertEquals("2006-08-08 00:00:00.0", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(resultsData.getResults().get(5).get(
                        Context.getMessageSourceService().getMessage("msfcore.datePrescribed")).getValue()));
        assertEquals(ResultColumn.builder().value(false).type(Type.STOP).build(), resultsData.getResults().get(5).get(
                        Context.getMessageSourceService().getMessage("msfcore.stop")));
        assertEquals(ResultColumn.builder().value("").editable(true).type(Type.CODED).codedOptions(
                        Arrays.asList(CodedOption.builder().name("Partial").uuid("5000037AAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build(),
                                        CodedOption.builder().name("Full").uuid("5000038AAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build())).build(),
                        resultsData.getResults().get(5).get(Context.getMessageSourceService().getMessage("msfcore.dispensed")));
        assertEquals("", resultsData.getResults().get(5).get(Context.getMessageSourceService().getMessage("msfcore.dispenseDate"))
                        .getValue());
        assertEquals(ResultColumn.builder().editable(true).value("").build(), resultsData.getResults().get(5).get(
                        Context.getMessageSourceService().getMessage("msfcore.details")));
        assertEquals(ResultColumn.builder().value("44rrc453-fc20-4f1b-a351-7eff54b4daf7").build(), resultsData.getResults().get(5).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.PENDING).build(), resultsData.getResults().get(5).get("status"));

        assertEquals(ResultColumn.builder().value("44rrc453-fc20-4f1b-a351-7eff54b4daf6").build(), resultsData.getResults().get(0).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.CANCELLED).build(), resultsData.getResults().get(0).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList()).build(), resultsData.getResults().get(0).get("actions"));
        assertEquals(ResultColumn.builder().value("ASPIRIN").build(), resultsData.getResults().get(0).get(
                        Context.getMessageSourceService().getMessage("msfcore.drugName")));

        assertEquals(ResultColumn.builder().value("44rrc453-fc20-4f1b-a351-7eff54b4daf5").build(), resultsData.getResults().get(1).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.STOPPED).build(), resultsData.getResults().get(1).get("status"));
        assertEquals(ResultColumn.builder().value(true).type(Type.STOP).stopDate(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2018-11-14 03:21:00.0")).build(), resultsData.getResults()
                        .get(1).get(Context.getMessageSourceService().getMessage("msfcore.stop")));
        assertEquals(ResultColumn.builder().value(Arrays.asList()).build(), resultsData.getResults().get(1).get("actions"));
        assertEquals(ResultColumn.builder().value("Triomune-30").build(), resultsData.getResults().get(1).get(
                        Context.getMessageSourceService().getMessage("msfcore.drugName")));
        assertEquals(ResultColumn.builder().value("Partial").editable(true).type(Type.CODED).codedOptions(
                        Arrays.asList(CodedOption.builder().name("Partial").uuid("5000037AAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build(),
                                        CodedOption.builder().name("Full").uuid("5000038AAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build())).build(),
                        resultsData.getResults().get(1).get(Context.getMessageSourceService().getMessage("msfcore.dispensed")));

        assertEquals(ResultColumn.builder().value("44rrc453-fc20-4f1b-a351-7eff54b4daf4").build(), resultsData.getResults().get(2).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.ACTIVE).build(), resultsData.getResults().get(2).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList(ResultAction.EDIT)).build(), resultsData.getResults().get(2).get("actions"));
        assertEquals(ResultColumn.builder().value("Triomune-30").build(), resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.drugName")));
        assertEquals(ResultColumn.builder().value("325.0 mg").build(), resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.dose")));
        assertEquals(ResultColumn.builder().value("4.0 p/d").build(), resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.frequency")));
        assertEquals(ResultColumn.builder().value("3 days").build(), resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.duration")));
        assertEquals(ResultColumn.builder().value("3x daily").build(), resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.instructions")));
        assertEquals(ResultColumn.builder().editable(true).value("2018-11-14 02:12:38.0").build().getValue(), new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.S").format(resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.datePrescribed")).getValue()));
        assertEquals(ResultColumn.builder().value(false).type(Type.STOP).build(), resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.stop")));
        assertEquals(ResultColumn.builder().value("Full").editable(true).type(Type.CODED).codedOptions(
                        Arrays.asList(CodedOption.builder().name("Partial").uuid("5000037AAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build(),
                                        CodedOption.builder().name("Full").uuid("5000038AAAAAAAAAAAAAAAAAAAAAAAAAAAAA").build())).build(),
                        resultsData.getResults().get(2).get(Context.getMessageSourceService().getMessage("msfcore.dispensed")));
        assertEquals(ResultColumn.builder().editable(true).value("2018-11-14 02:30:22.0").build().getValue(), new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.S").format(resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.dispenseDate")).getValue()));
        assertEquals(ResultColumn.builder().editable(true).value("taken together, on a dose fully").build(), resultsData.getResults()
                        .get(2).get(Context.getMessageSourceService().getMessage("msfcore.details")));

        // should retreive filters
        assertEquals(ResultFilters.builder().name(Context.getMessageSourceService().getMessage("msfcore.drugName")).statuses(
                        Arrays.asList(ResultStatus.CANCELLED, ResultStatus.PENDING, ResultStatus.ACTIVE, ResultStatus.STOPPED)).dates(
                        Arrays.asList(Context.getMessageSourceService().getMessage("msfcore.datePrescribed"), Context
                                        .getMessageSourceService().getMessage("msfcore.dispenseDate"))).build(), resultsData.getFilters());

        // should retrive default pagination with number of found results set
        assertEquals(Pagination.builder().totalItemsNumber(6).build(), resultsData.getPagination());

        // should retrieve right dateFormatPattern
        assertEquals(Context.getDateFormat().toPattern(), resultsData.getDateFormatPattern());
    }

    @Test
    public void updateResultRow_shouldCreateNewDrugDispensingEntry() {
        executeDataSet("ResultsData.xml");

        LinkedHashMap<String, Object> mapWithPayload = new LinkedHashMap<String, Object>();
        mapWithPayload.put("category", "drugList");
        List<LinkedHashMap<String, Object>> payload = new ArrayList<LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> payloadDispensing = new LinkedHashMap<String, Object>();
        payloadDispensing.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf7_7_CODED_");
        payloadDispensing.put("value", "5000037AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        LinkedHashMap<String, Object> payloadDispensingDate = new LinkedHashMap<String, Object>();
        payloadDispensingDate.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf7_8_DATE_");
        payloadDispensingDate.put("value", "03/11/2018");
        LinkedHashMap<String, Object> payloadDispensingDetail = new LinkedHashMap<String, Object>();
        payloadDispensingDetail.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf7_9_STRING_");
        payloadDispensingDetail.put("value", "dispensing details");

        payload.add(payloadDispensing);
        payload.add(payloadDispensingDate);
        payload.add(payloadDispensingDetail);
        mapWithPayload.put("payload", payload);
        List<Obs> created = (List<Obs>) ResultsData.updateResultRow(mapWithPayload);
        Assert.assertEquals(3, created.size());
        Assert.assertEquals("Partial", created.get(0).getValueCoded().getName().getName());
        Assert.assertEquals("03/11/2018", new SimpleDateFormat("dd/MM/yyyy").format(created.get(1).getValueDate()));
        Assert.assertEquals("dispensing details", created.get(2).getValueText());
    }

    @Test
    public void updateResultRow_shouldUpdateExistingDrugDispensing() {
        executeDataSet("ResultsData.xml");

        LinkedHashMap<String, Object> mapWithPayload = new LinkedHashMap<String, Object>();
        mapWithPayload.put("category", "drugList");
        List<LinkedHashMap<String, Object>> payload = new ArrayList<LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> payloadDispensing = new LinkedHashMap<String, Object>();
        payloadDispensing.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf4_7_CODED_");
        payloadDispensing.put("value", "5000037AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        LinkedHashMap<String, Object> payloadDispensingDate = new LinkedHashMap<String, Object>();
        payloadDispensingDate.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf4_8_DATE_");
        payloadDispensingDate.put("value", "03/11/2018");
        LinkedHashMap<String, Object> payloadDispensingDetail = new LinkedHashMap<String, Object>();
        payloadDispensingDetail.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf4_9_STRING_");
        payloadDispensingDetail.put("value", "dispensing details");

        payload.add(payloadDispensing);
        payload.add(payloadDispensingDate);
        payload.add(payloadDispensingDetail);
        mapWithPayload.put("payload", payload);
        List<Obs> updated = (List<Obs>) ResultsData.updateResultRow(mapWithPayload);
        Assert.assertEquals(3, updated.size());
        Assert.assertEquals("Partial", updated.get(0).getValueCoded().getName().getName());
        Assert.assertEquals("03/11/2018", new SimpleDateFormat("dd/MM/yyyy").format(updated.get(1).getValueDate()));
        Assert.assertEquals("dispensing details", updated.get(2).getValueText());
    }

    @Test
    public void discontinueOrder_shouldJustDiscontinueTheSameOrder() {
        executeDataSet("ResultsData.xml");

        // should retrieve and add lab test orders with matching results
        Patient patient = Context.getPatientService().getPatient(7);
        ResultsData resultsData = ResultsData.builder().resultCategory(ResultCategory.DRUG_LIST).patient(patient).build();
        resultsData.addRetrievedResults();
        assertEquals(6, resultsData.getResults().size());

        // discontinue
        Order order = Context.getOrderService().getOrder(57);
        Assert.assertNull(order.getDateStopped());
        // discontinedOrders will never be retrieved by MSFCoreService#getOrders
        Order discontinedOrder = ResultsData.discontinueOrder(order, ResultCategory.DRUG_LIST);
        Assert.assertEquals(Action.DISCONTINUE, discontinedOrder.getAction());
        Assert.assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()), new SimpleDateFormat("dd/MM/yyyy").format(Context
                        .getOrderService().getOrder(57).getDateStopped()));
        Assert.assertEquals("Discontinuing order from results widget", discontinedOrder.getOrderReasonNonCoded());

        resultsData = ResultsData.builder().resultCategory(ResultCategory.DRUG_LIST).patient(Context.getPatientService().getPatient(7))
                        .build();
        resultsData.addRetrievedResults();
        assertEquals(6, resultsData.getResults().size());
    }

    private void executeReferralsDataSets() {
        executeDataSet("ResultsData.xml");
        // TODO can you imagine OrderTypes in ResultsData as well as orders
        // fails referencing types,
        // seems its all persisted after executeDataSet
        executeDataSet("Referrals.xml");
    }

    @Test
    public void addRetriedResults_shouldAddReferralsOrders() throws ParseException {
        executeReferralsDataSets();

        assertEquals(MSFCoreConfig.REFERRAL_ORDER_TYPE_UUID, Context.getOrderService().getOrderType(18).getUuid());

        // should retrieve and add lab test orders with matching results
        ResultsData resultsData = ResultsData.builder().resultCategory(ResultCategory.REFERRAL_LIST).patient(
                        Context.getPatientService().getPatient(7)).build();
        resultsData.addRetrievedResults();

        assertEquals(Arrays.asList(Context.getMessageSourceService().getMessage("msfcore.location"), Context.getMessageSourceService()
                        .getMessage("msfcore.date"), Context.getMessageSourceService().getMessage("msfcore.reason"), Context
                        .getMessageSourceService().getMessage("msfcore.provider"), Context.getMessageSourceService().getMessage(
                        "msfcore.feedback"), Context.getMessageSourceService().getMessage("msfcore.feedbackFrom")), resultsData.getKeys());

        assertEquals(3, resultsData.getResults().size());

        assertEquals(ResultColumn.builder().value("44rrc453-fc20-4f1b-a351-7eff54b4dag0").build(), resultsData.getResults().get(0).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.CANCELLED).build(), resultsData.getResults().get(0).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList()).build(), resultsData.getResults().get(0).get("actions"));
        assertEquals(ResultColumn.builder().value("Surgery").build(), resultsData.getResults().get(0).get(
                        Context.getMessageSourceService().getMessage("msfcore.location")));

        assertEquals(ResultColumn.builder().value("44rrc453-fc20-4f1b-a351-7eff54b4daf9").build(), resultsData.getResults().get(1).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.PENDING).build(), resultsData.getResults().get(1).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList(ResultAction.EDIT, ResultAction.DELETE)).build(), resultsData.getResults()
                        .get(1).get("actions"));
        assertEquals(ResultColumn.builder().value("Cardiologist").build(), resultsData.getResults().get(1).get(
                        Context.getMessageSourceService().getMessage("msfcore.location")));

        assertEquals(ResultColumn.builder().value("44rrc453-fc20-4f1b-a351-7eff54b4daf8").build(), resultsData.getResults().get(2).get(
                        "uuid"));
        assertEquals(ResultColumn.builder().value(ResultStatus.COMPLETED).build(), resultsData.getResults().get(2).get("status"));
        assertEquals(ResultColumn.builder().value(Arrays.asList(ResultAction.EDIT)).build(), resultsData.getResults().get(2).get("actions"));
        assertEquals(ResultColumn.builder().value("Emergency").build(), resultsData.getResults().get(2).get(
                        Context.getMessageSourceService().getMessage("msfcore.location")));
        assertEquals(ResultColumn.builder().editable(true).value("needs emergency care else the heart fails soon").build(), resultsData
                        .getResults().get(2).get(Context.getMessageSourceService().getMessage("msfcore.feedback")));
        CodedOption prov = CodedOption.builder().name("Super User").uuid("c2299800-cca9-11e0-9572-0800200c9a66").build();
        assertEquals(
                        ResultColumn.builder().editable(true).type(Type.CODED).value(prov.getName()).codedOptions(Arrays.asList(prov))
                                        .build(), resultsData.getResults().get(2).get(
                                        Context.getMessageSourceService().getMessage("msfcore.feedbackFrom")));

        // should retreive filters
        assertEquals(ResultFilters.builder().name(Context.getMessageSourceService().getMessage("msfcore.location")).statuses(
                        Arrays.asList(ResultStatus.CANCELLED, ResultStatus.PENDING, ResultStatus.COMPLETED)).providers(Arrays.asList(prov))
                        .build(), resultsData.getFilters());

        // should retrive default pagination with number of found results set
        assertEquals(Pagination.builder().totalItemsNumber(3).build(), resultsData.getPagination());

        // should retrieve right dateFormatPattern
        assertEquals(Context.getDateFormat().toPattern(), resultsData.getDateFormatPattern());
    }

    @Test
    public void updateReferralOrderRow_shouldAddNewReferralValues() {
        executeReferralsDataSets();

        LinkedHashMap<String, Object> mapWithPayload = new LinkedHashMap<String, Object>();
        mapWithPayload.put("category", "referralList");
        List<LinkedHashMap<String, Object>> payload = new ArrayList<LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> payloadfeedback = new LinkedHashMap<String, Object>();
        payloadfeedback.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf9_4_STRING_");
        payloadfeedback.put("value", "meet with Cardiologist for a heart functionality test");
        payload.add(payloadfeedback);
        LinkedHashMap<String, Object> payloadfeedbackFrom = new LinkedHashMap<String, Object>();
        payloadfeedbackFrom.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf9_5_STRING_");
        payloadfeedbackFrom.put("value", "c2299800-cca9-11e0-9572-0800200c9a66");
        payload.add(payloadfeedbackFrom);
        mapWithPayload.put("payload", payload);
        List<Obs> created = (List<Obs>) ResultsData.updateResultRow(mapWithPayload);
        Assert.assertEquals(2, created.size());
        Assert.assertEquals("meet with Cardiologist for a heart functionality test", created.get(0).getValueText());
        Assert.assertEquals("c2299800-cca9-11e0-9572-0800200c9a66", created.get(1).getValueText());
    }

    @Test
    public void updateReferralOrderRow_shouldUpdateExistingReferralValues() {
        executeReferralsDataSets();

        LinkedHashMap<String, Object> mapWithPayload = new LinkedHashMap<String, Object>();
        mapWithPayload.put("category", "referralList");
        List<LinkedHashMap<String, Object>> payload = new ArrayList<LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> payloadfeedback = new LinkedHashMap<String, Object>();
        payloadfeedback.put("uuid_key_type_concept", "44rrc453-fc20-4f1b-a351-7eff54b4daf8_4_STRING_");
        payloadfeedback.put("value", "emergency referall");
        payload.add(payloadfeedback);
        mapWithPayload.put("payload", payload);
        List<Obs> created = (List<Obs>) ResultsData.updateResultRow(mapWithPayload);
        Assert.assertEquals(1, created.size());
        Assert.assertEquals("emergency referall", created.get(0).getValueText());
    }
}
