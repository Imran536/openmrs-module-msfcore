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

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.api.context.Context;
import org.openmrs.module.msfcore.api.dao.MSFCoreDao;
import org.openmrs.module.msfcore.api.impl.MSFCoreServiceImpl;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is a unit test, which verifies logic in MSFCoreService.
 */
public class MSFCoreServiceTest extends BaseModuleContextSensitiveTest {
	
	@InjectMocks
	MSFCoreServiceImpl msfCoreServiceImpl;
	
	@Mock
	MSFCoreDao dao;
	
	@Before
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Autowired
	MSFCoreService msfCoreService;
	
	@Test
	public void serviceNotNull() {
		assertNotNull(msfCoreServiceImpl);
		assertNotNull(msfCoreService);
		assertNotNull(Context.getService(MSFCoreService.class));
	}
}
