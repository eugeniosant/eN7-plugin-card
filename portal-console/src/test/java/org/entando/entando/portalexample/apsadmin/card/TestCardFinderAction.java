/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.portalexample.apsadmin.card;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import org.entando.entando.portalexample.CardConfigTestUtils;
import org.entando.entando.portalexample.aps.system.services.card.Card;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author E.Santoboni
 */
public class TestCardFinderAction {
	
	@BeforeAll
    public static void setUp() throws Exception {
        ApsAdminBaseTestCase.setUp(new CardConfigTestUtils());
    }
    
    @AfterAll
    public static void tearDown() throws Exception {
        ApsAdminBaseTestCase.tearDown();
    }
	
	@Test
	public void testListCard() throws Throwable {
		ApsAdminBaseTestCase.setUserOnSession("admin");
		ApsAdminBaseTestCase.initAction("/do/Card", "list");
		String result = ApsAdminBaseTestCase.executeAction();
		Assertions.assertEquals(Action.SUCCESS, result);
		CardFinderAction action = (CardFinderAction) ApsAdminBaseTestCase.getAction();
		Assertions.assertEquals(4, action.getCards().size());
	}
	
	@Test
	public void testSearchCard_1() throws Throwable {
		ApsAdminBaseTestCase.setUserOnSession("admin");
		ApsAdminBaseTestCase.initAction("/do/Card", "search");
		ApsAdminBaseTestCase.addParameter("holder", "Nicola");
		String result = ApsAdminBaseTestCase.executeAction();
        Assertions.assertEquals(Action.SUCCESS, result);
		CardFinderAction action = (CardFinderAction) ApsAdminBaseTestCase.getAction();
		Assertions.assertEquals(1, action.getCards().size());
	}
	
	@Test
	public void testSearchCard_2() throws Throwable {
		ApsAdminBaseTestCase.setUserOnSession("admin");
		ApsAdminBaseTestCase.initAction("/do/Card", "search");
		ApsAdminBaseTestCase.addParameter("holder", "Anna");
		String result = ApsAdminBaseTestCase.executeAction();
		Assertions.assertEquals(Action.SUCCESS, result);
		CardFinderAction action = (CardFinderAction) ApsAdminBaseTestCase.getAction();
		Assertions.assertEquals(1, action.getCards().size());
		Card card = action.getCards().get(0);
		Assertions.assertEquals("Verdi Anna", card.getHolder());
	}
	
}
