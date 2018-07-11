/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.msfcore.api.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.msfcore.api.MSFCoreService;
import org.openmrs.module.msfcore.api.dao.MSFCoreDao;

public class MSFCoreServiceImpl extends BaseOpenmrsService implements MSFCoreService {
	
	MSFCoreDao dao;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(MSFCoreDao dao) {
		this.dao = dao;
	}
	
}
