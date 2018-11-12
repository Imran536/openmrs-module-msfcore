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

import java.util.List;
import java.util.Map;

import org.openmrs.Allergies;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.PatientProgram;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.idgen.SequentialIdentifierGenerator;
import org.openmrs.module.msfcore.DropDownFieldOption;

/**
 * The main service of this module, which is exposed for other modules. See
 * moduleApplicationContext.xml on how it is wired up.
 */
public interface MSFCoreService extends OpenmrsService {

    List<Concept> getAllConceptAnswers(Concept question);

    List<DropDownFieldOption> getAllConceptAnswerNames(String uuid);

    boolean isConfigured();

    String getInstanceId();

    List<Location> getMSFLocations();

    String getLocationCode(Location location);

    void saveInstanceId(String instanceId);

    LocationAttribute getLocationCodeAttribute(Location location);

    void saveDefaultLocation(Location location);

    void msfIdentifierGeneratorInstallation();

    void saveSequencyPrefix(SequentialIdentifierGenerator generator);

    void overwriteSync2Configuration();

    String getCurrentLocationIdentity();

    /**
     * @param enrollment,
     *            is this an enrollment or not
     * @param patient
     * @param program
     * @param states,
     *            maps states to their respective uuid
     * @param patientProgram,
     *            must never be null
     * @param form,
     *            used for form managed states
     * @return patientProgram generated or null
     */
    PatientProgram generatePatientProgram(boolean enrollment, Map<String, ProgramWorkflowState> states, PatientProgram patientProgram,
                    Encounter ecnounter);

    Map<String, ProgramWorkflowState> getMsfStages();

    void manageNCDProgram(Encounter encounter);

    void saveTestOrders(Encounter encounter);

    void saveDrugOrders(Encounter encounter);

    Allergies saveAllergies(Encounter encounter);
}
