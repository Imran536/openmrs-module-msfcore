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

import org.openmrs.Allergies;
import org.openmrs.Encounter;
import org.openmrs.api.OpenmrsService;

/**
 * Service to implement Post Submission Form Actions. See moduleApplicationContext.xml on how it is
 * wired up.
 */
public interface FormActionService extends OpenmrsService {

    void saveTestOrders(Encounter encounter);

    void saveDrugOrders(Encounter encounter);

    Allergies saveAllergies(Encounter encounter);

    void saveReferralOrders(Encounter encounter);
}
