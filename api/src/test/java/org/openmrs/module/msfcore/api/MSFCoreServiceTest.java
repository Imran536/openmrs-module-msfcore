/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.msfcore.api;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.SimpleDosingInstructions;
import org.openmrs.api.context.Context;
import org.openmrs.module.msfcore.DropDownFieldOption;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.module.msfcore.Pagination;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * This is a unit test, which verifies logic in MSFCoreService.
 */
public class MSFCoreServiceTest extends BaseModuleContextSensitiveTest {

    @Override
    public Properties getRuntimeProperties() {
        Properties props = super.getRuntimeProperties();
        props.put(MSFCoreConfig.PROP_OPENHIM_USERNAME, "user");
        props.put(MSFCoreConfig.PROP_OPENHIM_PASSWORD, "pass");
        props.put(MSFCoreConfig.PROP_DHIS2_USERNAME, "admin");
        props.put(MSFCoreConfig.PROP_DHIS2_PASSWORD, "password");
        return props;
    }

    @Test
    public void openHimPropertiesShouldGetInitialised() {
        String url = "http://localhost/5001/tracker";
        Context.getAdministrationService().setGlobalProperty(MSFCoreConfig.GP_OPENHIM_TRACKER_URL, url);
        assertThat(Context.getAdministrationService().getGlobalProperty(MSFCoreConfig.GP_OPENHIM_TRACKER_URL), is(url));
        assertThat(Context.getRuntimeProperties().getProperty(MSFCoreConfig.PROP_DHIS2_USERNAME), is("admin"));
        assertThat(Context.getRuntimeProperties().getProperty(MSFCoreConfig.PROP_DHIS2_PASSWORD), is("password"));
    }

    @Test
    public void getAllConceptAnswers_shouldReturnConceptsFromAnswers() {
        Assert.assertNotNull(Context.getService(MSFCoreService.class));
        List<Concept> answers = Context.getService(MSFCoreService.class).getAllConceptAnswers(Context.getConceptService().getConcept(4));
        Assert.assertTrue(answers.contains(Context.getConceptService().getConcept(5)));
        Assert.assertTrue(answers.contains(Context.getConceptService().getConcept(6)));
    }

    @Test
    public void getAllConceptAnswerNames_shouldReturnRightDropDownOptions() {
        Assert.assertNotNull(Context.getService(MSFCoreService.class));
        Concept concept = Context.getConceptService().getConcept(4);
        List<DropDownFieldOption> options = Context.getService(MSFCoreService.class).getAllConceptAnswerNames(concept.getUuid());
        Assert.assertEquals("CIVIL STATUS", concept.getName().getName());
        Assert.assertEquals("5", options.get(0).getValue());
        Assert.assertEquals("SINGLE", options.get(0).getLabel());
        Assert.assertEquals("6", options.get(1).getValue());
        Assert.assertEquals("MARRIED", options.get(1).getLabel());
    }

    @Test
    public void getOrders_shouldDefaultPaginationToFirst25ResultsWithNoResults() {
        Pagination pagination = Pagination.builder().build();
        List<Order> orders = Context.getService(MSFCoreService.class).getOrders(Context.getPatientService().getPatient(6),
                        Context.getOrderService().getOrderType(1), null, pagination);
        Assert.assertEquals(Integer.valueOf(1), pagination.getFromItemNumber());
        Assert.assertEquals(Integer.valueOf(25), pagination.getToItemNumber());
        Assert.assertEquals(Integer.valueOf(0), pagination.getTotalItemsNumber());
        Assert.assertEquals(0, orders.size());
    }

    @Test
    public void getOrders_shouldDefaultPaginationToFirst25Results() {
        Pagination pagination = Pagination.builder().build();
        List<Order> orders = Context.getService(MSFCoreService.class).getOrders(Context.getPatientService().getPatient(7),
                        Context.getOrderService().getOrderType(1), null, pagination);
        Assert.assertEquals(Integer.valueOf(1), pagination.getFromItemNumber());
        Assert.assertEquals(Integer.valueOf(25), pagination.getToItemNumber());
        Assert.assertEquals(Integer.valueOf(2), pagination.getTotalItemsNumber());
        Assert.assertEquals(2, orders.size());
    }

    @Test
    public void getOrders_shouldUseFromAndToAppropriatelyOrderingByCreationDateDescAndSetPaginationWell() {
        Pagination pagination = Pagination.builder().toItemNumber(1).build();
        List<Order> orders = Context.getService(MSFCoreService.class).getOrders(Context.getPatientService().getPatient(7),
                        Context.getOrderService().getOrderType(1), null, pagination);
        Assert.assertEquals(Integer.valueOf(1), pagination.getFromItemNumber());
        Assert.assertEquals(Integer.valueOf(1), pagination.getToItemNumber());
        Assert.assertEquals(Integer.valueOf(2), pagination.getTotalItemsNumber());
        Assert.assertEquals(1, orders.size());
        Assert.assertEquals("111", orders.get(0).getOrderNumber());

        pagination = Pagination.builder().fromItemNumber(2).toItemNumber(2).build();
        orders = Context.getService(MSFCoreService.class).getOrders(Context.getPatientService().getPatient(7),
                        Context.getOrderService().getOrderType(1), null, pagination);
        Assert.assertEquals(Integer.valueOf(2), pagination.getFromItemNumber());
        Assert.assertEquals(Integer.valueOf(2), pagination.getToItemNumber());
        Assert.assertEquals(Integer.valueOf(2), pagination.getTotalItemsNumber());
        Assert.assertEquals(1, orders.size());
        Assert.assertEquals("1", orders.get(0).getOrderNumber());
    }

    public void generatePatientProgram_returnNullIfPatientProgramIsNull() {
        Assert.assertNull(generatePatientProgram(true, null, null));
        Assert.assertNull(generatePatientProgram(false, null, null));
    }

    @Test
    public void generatePatientProgram_returnNullIfStagesAreNull() {
        Assert.assertNull(generatePatientProgram(true, new PatientProgram(14), null));
        Assert.assertNull(generatePatientProgram(false, new PatientProgram(14), null));
    }

    @Test
    public void generatePatientProgram_testEnrollement() {
        ProgramWorkflowState enrollStage = new ProgramWorkflowState();
        enrollStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_ENROLL);
        ProgramWorkflowState baselineStage = new ProgramWorkflowState();
        baselineStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_BASELINE_CONSULTATION);
        ProgramWorkflowState followUpStage = new ProgramWorkflowState();
        followUpStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_FOLLOWUP_CONSULTATION);
        ProgramWorkflowState exitStage = new ProgramWorkflowState();
        exitStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_EXIT);
        PatientProgram pp = new PatientProgram();

        PatientProgram patientProgram = generatePatientProgram(true, pp, null, enrollStage, baselineStage, followUpStage, exitStage);

        assertThat(patientProgram.getStates().size(), is(1));
        assertThat(patientProgram.getStates().iterator().next().getState(), is(enrollStage));

        // enroll and exit patient from program if dateCompleted is set
        pp.setDateCompleted(new Date());
        pp.getStates().clear();
        patientProgram = generatePatientProgram(true, pp, null, enrollStage, baselineStage, followUpStage, exitStage);
        assertThat(patientProgram.getStates().size(), is(2));
        assertThat(stagesContainState(patientProgram.getStates(), enrollStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), exitStage), is(true));
    }

    @Test
    public void generatePatientProgram_testFormSubmissionWithoutBaselineOrFormOrPatientProgram() {
        ProgramWorkflowState enrollStage = new ProgramWorkflowState();
        enrollStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_ENROLL);
        ProgramWorkflowState baselineStage = new ProgramWorkflowState();
        baselineStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_BASELINE_CONSULTATION);
        ProgramWorkflowState followUpStage = new ProgramWorkflowState();
        followUpStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_FOLLOWUP_CONSULTATION);
        ProgramWorkflowState exitStage = new ProgramWorkflowState();
        exitStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_EXIT);
        Encounter encounter = new Encounter(3);
        encounter.setForm(new Form());
        PatientProgram patientProgram = generatePatientProgram(false, null, null, enrollStage, baselineStage, followUpStage, exitStage);
        PatientProgram patientProgram1 = generatePatientProgram(false, null, encounter, enrollStage, baselineStage, followUpStage,
                        exitStage);
        PatientProgram patientProgram2 = generatePatientProgram(false, new PatientProgram(4), null, enrollStage, baselineStage,
                        followUpStage, exitStage);

        Assert.assertNull(patientProgram);
        Assert.assertNull(patientProgram1);
        Assert.assertNull(patientProgram2);
    }

    private EncounterType newEncounterType(String uuid) {
        EncounterType encType = new EncounterType();
        encType.setUuid(uuid);
        return encType;
    }

    @Test
    public void generatePatientProgram_testFormsAfterEnrollement() {
        ProgramWorkflowState enrollStage = new ProgramWorkflowState();
        enrollStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_ENROLL);
        ProgramWorkflowState baselineStage = new ProgramWorkflowState();
        baselineStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_BASELINE_CONSULTATION);
        baselineStage.setInitial(true);
        ProgramWorkflowState followUpStage = new ProgramWorkflowState();
        followUpStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_FOLLOWUP_CONSULTATION);
        ProgramWorkflowState exitStage = new ProgramWorkflowState();
        exitStage.setUuid(MSFCoreConfig.WORKFLOW_STATE_UUID_EXIT);
        exitStage.setTerminal(true);
        Encounter encounter = new Encounter(3);

        // starting with any other stage besides enrollment should return null
        encounter.setEncounterType(newEncounterType(MSFCoreConfig.ENCOUNTER_TYPE_NCD_BASELINE_UUID));
        PatientProgram patientProgram = generatePatientProgram(false, new PatientProgram(), encounter, enrollStage, baselineStage,
                        followUpStage, exitStage);
        Assert.assertNull(patientProgram);

        patientProgram = generatePatientProgram(true, new PatientProgram(), null, enrollStage, baselineStage, followUpStage, exitStage);

        assertThat(patientProgram.getStates().size(), is(1));
        assertThat(patientProgram.getStates().iterator().next().getState(), is(enrollStage));
        assertThat(stagesContainState(patientProgram.getStates(), baselineStage), is(false));

        encounter.setEncounterType(newEncounterType(MSFCoreConfig.ENCOUNTER_TYPE_NCD_BASELINE_UUID));
        patientProgram = generatePatientProgram(false, patientProgram, encounter, enrollStage, baselineStage, followUpStage, exitStage);
        assertThat(patientProgram.getStates().size(), is(2));
        assertThat(stagesContainState(patientProgram.getStates(), enrollStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), baselineStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), followUpStage), is(false));

        encounter.setEncounterType(newEncounterType(MSFCoreConfig.ENCOUNTER_TYPE_NCD_FOLLOWUP_UUID));
        patientProgram = generatePatientProgram(false, patientProgram, encounter, enrollStage, baselineStage, followUpStage, exitStage);
        assertThat(patientProgram.getStates().size(), is(3));
        assertThat(stagesContainState(patientProgram.getStates(), enrollStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), baselineStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), followUpStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), exitStage), is(false));

        encounter.setEncounterType(newEncounterType(MSFCoreConfig.ENCOUNTER_TYPE_NCD_EXIT_UUID));
        patientProgram = generatePatientProgram(false, patientProgram, encounter, enrollStage, baselineStage, followUpStage, exitStage);
        assertThat(patientProgram.getStates().size(), is(4));
        assertThat(stagesContainState(patientProgram.getStates(), enrollStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), baselineStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), followUpStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), exitStage), is(true));

        // exiting should set outcome when obs exists
        encounter.setEncounterType(newEncounterType(MSFCoreConfig.ENCOUNTER_TYPE_NCD_EXIT_UUID));
        Obs outcome = new Obs();
        Concept qn = new Concept();
        qn.setUuid(MSFCoreConfig.NCD_PROGRAM_OUTCOMES_CONCEPT_UUID);
        Concept an = new Concept();
        an.setConceptId(1000);
        outcome.setConcept(qn);
        outcome.setValueCoded(an);
        encounter.addObs(outcome);
        patientProgram = generatePatientProgram(false, patientProgram, encounter, enrollStage, baselineStage, followUpStage, exitStage);
        assertThat(patientProgram.getStates().size(), is(5));// exit is repeated
        assertThat(stagesContainState(patientProgram.getStates(), baselineStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), followUpStage), is(true));
        assertThat(stagesContainState(patientProgram.getStates(), exitStage), is(true));
        assertThat(patientProgram.getOutcome().getConceptId(), is(1000));
    }

    private boolean stagesContainState(final Set<PatientState> stages, final ProgramWorkflowState state) {
        for (PatientState stage : stages) {
            if (stage.getState().equals(state)) {
                return true;
            }
        }
        return false;
    }

    private PatientProgram generatePatientProgram(boolean enrollment, PatientProgram patientProgram, Encounter encounter,
                    ProgramWorkflowState... states) {
        final Map<String, ProgramWorkflowState> stages = new HashMap<>();

        // hack to avoid invalid property setting issues
        if (states.length > 0) {
            for (final ProgramWorkflowState stage : states) {
                stage.setConcept(Context.getConceptService().getConcept(22));
                stage.setProgramWorkflow(new ProgramWorkflow());
                if (stage.getInitial() == null) {
                    stage.setInitial(false);
                }
                if (stage.getTerminal() == null) {
                    stage.setTerminal(false);
                }
            }
            stages.put(MSFCoreConfig.WORKFLOW_STATE_UUID_ENROLL, states[0]);
            stages.put(MSFCoreConfig.WORKFLOW_STATE_UUID_BASELINE_CONSULTATION, states[1]);
            stages.put(MSFCoreConfig.WORKFLOW_STATE_UUID_FOLLOWUP_CONSULTATION, states[2]);
            stages.put(MSFCoreConfig.WORKFLOW_STATE_UUID_EXIT, states[3]);
        }
        return Context.getService(MSFCoreService.class).generatePatientProgram(enrollment, stages, patientProgram, encounter);
    }
    @Test
    public void getObservationsByPatientAndOrderAndConcept_shouldRetrieveCorrectObs() {
        executeDataSet("MSFCoreService.xml");

        Assert.assertEquals(5, Context.getService(MSFCoreService.class).getObservationsByOrderAndConcept(
                        Context.getOrderService().getOrder(1), Context.getConceptService().getConcept(5000023)).get(0).getId().intValue());
    }

    @Test
    public void getObservationsByPatientAndOrderAndConcept_shouldRetrieveNoObsIfConceptDoesntMatch() {
        executeDataSet("MSFCoreService.xml");

        Assert.assertEquals(0, Context.getService(MSFCoreService.class).getObservationsByOrderAndConcept(
                        Context.getOrderService().getOrder(1), Context.getConceptService().getConcept(5000012)).size());
    }

    @Test
    public void getOrders_shouldRetrieveAllResultsIfPaginationToItemNumberIsNull() {
        Pagination pagination = Pagination.builder().toItemNumber(null).build();
        List<Order> orders = Context.getService(MSFCoreService.class).getOrders(Context.getPatientService().getPatient(7),
                        Context.getOrderService().getOrderType(1), null, pagination);
        Assert.assertEquals(2, orders.size());
    }

    @Test
    public void getOrders_shouldRetrieveResultsFromFromItemNumber() {
        Pagination pagination = Pagination.builder().build();
        List<Order> orders = Context.getService(MSFCoreService.class).getOrders(Context.getPatientService().getPatient(7),
                        Context.getOrderService().getOrderType(1), null, pagination);
        Assert.assertEquals(2, orders.size());
        DrugOrder drugOrder = new DrugOrder();
        Encounter encounter = Context.getEncounterService().getEncounter(3);
        drugOrder.setEncounter(encounter);
        drugOrder.setPatient(Context.getPatientService().getPatient(7));
        drugOrder.setCareSetting(Context.getOrderService().getCareSetting(1));
        drugOrder.setOrderer(Context.getProviderService().getProvider(1));
        drugOrder.setDateActivated(encounter.getEncounterDatetime());
        drugOrder.setDrug(Context.getConceptService().getDrug(2));
        drugOrder.setDosingType(SimpleDosingInstructions.class);
        drugOrder.setDose(300.0);
        drugOrder.setDoseUnits(Context.getConceptService().getConcept(50));
        drugOrder.setQuantity(20.0);
        drugOrder.setQuantityUnits(Context.getConceptService().getConcept(51));
        drugOrder.setFrequency(Context.getOrderService().getOrderFrequency(3));
        drugOrder.setRoute(Context.getConceptService().getConcept(22));
        drugOrder.setNumRefills(10);
        Context.getOrderService().saveOrder(drugOrder, null);

        orders = Context.getService(MSFCoreService.class).getOrders(Context.getPatientService().getPatient(7),
                        Context.getOrderService().getOrderType(1), null, pagination);
        Assert.assertEquals(3, orders.size());

        pagination = Pagination.builder().fromItemNumber(2).toItemNumber(2).build();
        orders = Context.getService(MSFCoreService.class).getOrders(Context.getPatientService().getPatient(7),
                        Context.getOrderService().getOrderType(1), null, pagination);
        Assert.assertEquals(1, orders.size());
        Assert.assertEquals(Integer.valueOf(2), pagination.getFromItemNumber());
        Assert.assertEquals(Integer.valueOf(2), pagination.getToItemNumber());
        Assert.assertEquals(Integer.valueOf(3), pagination.getTotalItemsNumber());
    }
}
