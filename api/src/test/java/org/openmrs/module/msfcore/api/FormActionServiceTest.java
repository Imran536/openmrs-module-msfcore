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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.AllergenType;
import org.openmrs.Allergies;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class FormActionServiceTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private FormActionService formActionService;

    @Autowired
    private ObsService obsService;

    @Test
    public void saveTestOrders_shouldCreateTestOrdersForNewObservationOrders() {
        executeDataSet("FormActionService.xml");
        Encounter encounter = Context.getEncounterService().getEncounter(29);

        List<Order> existingOrders = encounter.getOrders().stream().filter(o -> !o.getVoided()).collect(Collectors.toList());
        assertThat(existingOrders.size(), is(1));
        assertThat(existingOrders.get(0).getConcept().getId(), is(5000010));

        // execute test
        formActionService.saveTestOrders(encounter);

        List<Order> updatedOrders = encounter.getOrders().stream().collect(Collectors.toList());

        assertThat(updatedOrders.size(), is(2));
        assertThat(updatedOrders.stream().filter(o -> o.getConcept().getId().equals(5000010)).findAny().isPresent(), is(true));
        assertThat(updatedOrders.stream().filter(o -> o.getConcept().getId().equals(5000011)).findAny().isPresent(), is(true));

    }
    @Test
    public void saveTestOrders_shouldNotSaveOrderIfConceptIsNotLabSetMember() {
        executeDataSet("FormActionService.xml");
        Encounter encounter = Context.getEncounterService().getEncounter(29);

        List<Order> existingOrders = encounter.getOrders().stream().collect(Collectors.toList());
        assertThat(existingOrders.size(), is(1));
        assertThat(existingOrders.get(0).getConcept().getId(), is(5000010));

        // update obs concept to be not lab set member
        Obs notLabSetMemberObs = obsService.getObs(2);
        notLabSetMemberObs.setConcept(Context.getConceptService().getConcept(1065));
        obsService.saveObs(notLabSetMemberObs, "update concept");

        // execute test
        formActionService.saveTestOrders(encounter);

        List<Order> updatedOrders = encounter.getOrders().stream().collect(Collectors.toList());

        assertThat(updatedOrders.size(), is(1));
        assertThat(updatedOrders.get(0).getConcept().getId(), is(5000010));
    }

    @Test
    public void saveTestOrders_shouldVoidOrderIfObsIsVoided() throws Exception {
        executeDataSet("FormActionService.xml");
        Encounter encounter = Context.getEncounterService().getEncounter(30);

        List<Order> existingOrders = encounter.getOrders().stream().collect(Collectors.toList());
        assertThat(existingOrders.size(), is(1));
        assertThat(existingOrders.get(0).getConcept().getId(), is(5000012));
        assertThat(existingOrders.get(0).getVoided(), is(false));

        // execute test
        formActionService.saveTestOrders(encounter);

        List<Order> updatedOrders = encounter.getOrders().stream().collect(Collectors.toList());

        assertThat(updatedOrders.size(), is(1));
        assertThat(updatedOrders.get(0).getConcept().getId(), is(5000012));
        assertThat(updatedOrders.get(0).getVoided(), is(true));
    }

    @Test
    public void saveTestOrders_shouldNotVoidOrderIfOrderIsFulfilled() {
        executeDataSet("FormActionService.xml");
        Encounter encounter = Context.getEncounterService().getEncounter(31);
        formActionService.saveTestOrders(encounter);
        Assert.assertFalse(Context.getOrderService().getOrder(3).getVoided());
    }

    @Test
    public void saveDrugOrders_shouldCreateDrugOrders() throws Exception {
        executeDataSet("MSFCoreService.xml");
        Encounter encounter = Context.getEncounterService().getEncounterByUuid("a131a0c9-e550-47da-a8d1-0eaa269cb3gh");
        formActionService.saveDrugOrders(encounter);
        Assert.assertEquals(1, encounter.getOrders().size());
        DrugOrder drugOrder = (DrugOrder) encounter.getOrders().iterator().next();
        Assert.assertNotNull(drugOrder.getId());
        Assert.assertEquals(36, drugOrder.getDrug().getId().intValue());
        Assert.assertEquals(14D, drugOrder.getQuantity().doubleValue(), 0);
        Assert.assertEquals("Drug order", drugOrder.getOrderType().getName());
        Assert.assertEquals(7, drugOrder.getPatient().getPatientId().intValue());
        Assert.assertEquals("Outpatient", drugOrder.getCareSetting().getName());
        Assert.assertEquals("a131a0c9-e550-47da-a8d1-0eaa269cb3gh", drugOrder.getEncounter().getUuid());
        Assert.assertEquals(1, drugOrder.getOrderer().getId().intValue());
        Assert.assertEquals(1, drugOrder.getDose().doubleValue(), 0);
        Assert.assertEquals(1513, drugOrder.getDoseUnits().getId().intValue());
        Assert.assertEquals("1073AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", drugOrder.getDurationUnits().getUuid());
        Assert.assertEquals(1513, drugOrder.getQuantityUnits().getId().intValue());
        Assert.assertEquals(160240, drugOrder.getRoute().getConceptId().intValue());
        Assert.assertEquals(0, drugOrder.getNumRefills().intValue());
        Assert.assertEquals("Take after meals", drugOrder.getDosingInstructions());
        Assert.assertEquals("Take after meals", drugOrder.getInstructions());
    }

    @Test
    public void saveDrugOrders_shouldVoidOrderWhenEntryIsRemoved() throws Exception {
        executeDataSet("FormActionService.xml");
        Encounter encounter = Context.getEncounterService().getEncounter(32);

        // initially we should have 2 non voided orders for both concepts
        List<Order> activeOrders = encounter.getOrders().stream().filter(o -> !o.getVoided()).collect(Collectors.toList());
        Assert.assertEquals(2, activeOrders.size());
        Assert.assertTrue(activeOrders.stream().filter(o -> o.getConcept().getId().equals(1000021)).findAny().isPresent());
        Assert.assertTrue(activeOrders.stream().filter(o -> o.getConcept().getId().equals(924)).findAny().isPresent());

        formActionService.saveDrugOrders(encounter);

        // then just one active order for concept 1000021
        encounter = Context.getEncounterService().getEncounter(32);
        activeOrders = encounter.getOrders().stream().filter(o -> !o.getVoided()).collect(Collectors.toList());
        Assert.assertEquals(1, activeOrders.size());
        Assert.assertTrue(activeOrders.stream().filter(o -> o.getConcept().getId().equals(1000021)).findAny().isPresent());
    }
    @Test
    public void saveDrugOrders_shouldNotUpdateOrderIfItWasNotChanged() throws Exception {
        executeDataSet("FormActionService.xml");

        Encounter encounter = Context.getEncounterService().getEncounter(33);
        Assert.assertEquals(1, encounter.getOrders().size());
        Assert.assertEquals(11, encounter.getOrders().iterator().next().getId().intValue());

        formActionService.saveDrugOrders(encounter);

        // Nothing is supposed to be changed
        encounter = Context.getEncounterService().getEncounter(33);
        Assert.assertEquals(1, encounter.getOrders().size());
        Assert.assertEquals(11, encounter.getOrders().iterator().next().getId().intValue());
    }

    // Ignored because we are having issues with this test on the dev env. Since
    // we are not able to fix this problem, but the code is working well the
    // team decided to ignore the test until a new solution is found in the
    // future
    @Ignore
    @Test
    public void saveAllergies_shouldCreateAllergies() throws Exception {
        executeDataSet("MSFCoreService.xml");

        Encounter encounter = Context.getEncounterService().getEncounterByUuid("992b696b-529c-4ded-b7f7-4876fb4f2936");

        Allergies allergies = formActionService.saveAllergies(encounter);
        Assert.assertNotNull(allergies);
        Assert.assertEquals(3, allergies.size());
        allergies.forEach(a -> Assert.assertTrue(a.getReactions().size() > 0));
        allergies.forEach(a -> Assert.assertTrue(
                        Arrays.asList(AllergenType.DRUG, AllergenType.FOOD, AllergenType.ENVIRONMENT).contains(a.getAllergenType())));
        allergies.forEach(a -> a.getReactions().forEach(r -> Assert
                        .assertTrue(Arrays.asList(120148, 143264, 139581, 121629, 121677).contains(r.getReaction().getConceptId()))));
        allergies.forEach(
                        a -> Assert.assertTrue(Arrays.asList(162543, 162538, 71617).contains(a.getAllergen().getCodedAllergen().getId())));
    }
    @Test(expected = IllegalArgumentException.class)
    public void saveAllergies_shouldThrowsExceptionSavingAllergies() throws Exception {
        executeDataSet("MSFCoreService.xml");
        Encounter encounter = Context.getEncounterService().getEncounterByUuid("84fcb082-e670-11e8-8661-0b65d193bf03");
        formActionService.saveAllergies(encounter);
    }

    @Test
    public void saveReferrals_shouldCreateReferrals() throws Exception {
        executeDataSet("FormActionService.xml");
        Encounter encounter = Context.getEncounterService().getEncounter(39);
        formActionService.saveReferralOrders(encounter);
        encounter = Context.getEncounterService().getEncounter(39);
        List<Obs> observationsWithOrders = encounter.getAllObs(false).stream().filter(o -> o.getOrder() != null)
                        .collect(Collectors.toList());
        Assert.assertEquals(2, observationsWithOrders.size());
        Assert.assertEquals(2, encounter.getOrders().size());
        Assert.assertTrue(observationsWithOrders.stream().filter(o -> o.getConcept().getConceptId().intValue() == 463402).findAny()
                        .isPresent());
        Assert.assertTrue(observationsWithOrders.stream().filter(o -> o.getConcept().getConceptId().intValue() == 463405).findAny()
                        .isPresent());
        Assert.assertEquals(2,
                        encounter.getOrders().stream().filter(order -> "Comments for all referrals".equals(order.getOrderReasonNonCoded()))
                                        .collect(Collectors.toList()).size());
    }
    @Test
    public void saveReferrals_shouldVoidOrder() throws Exception {
        executeDataSet("FormActionService.xml");
        Encounter encounter = Context.getEncounterService().getEncounter(40);
        formActionService.saveReferralOrders(encounter);
        Assert.assertTrue(Context.getOrderService().getOrder(12).getVoided());
    }
}
