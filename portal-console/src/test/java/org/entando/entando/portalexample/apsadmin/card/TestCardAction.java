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

import static com.agiletec.apsadmin.ApsAdminBaseTestCase.setUp;

import com.agiletec.MsConfigTestUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import org.entando.entando.portalexample.CardConfigTestUtils;
import org.entando.entando.portalexample.aps.system.CardSystemConstants;
import org.entando.entando.portalexample.aps.system.services.card.Card;
import org.entando.entando.portalexample.aps.system.services.card.ICardManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author E.Santoboni
 */
public class TestCardAction {
    
    @BeforeAll
    public static void setUp() throws Exception {
        ApsAdminBaseTestCase.setUp(new CardConfigTestUtils());
    }
    
    @AfterAll
    public static void tearDown() throws Exception {
        ApsAdminBaseTestCase.tearDown();
    }
	
    @Test
	public void testNewCard() throws Throwable {
		ApsAdminBaseTestCase.setUserOnSession("admin");
		ApsAdminBaseTestCase.initAction("/do/Card", "new");
		String result = ApsAdminBaseTestCase.executeAction();
        Assertions.assertEquals(Action.SUCCESS, result);
		CardAction action = (CardAction) ApsAdminBaseTestCase.getAction();
		Assertions.assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
	}
	
	@Test
	public void testEditCard() throws Throwable {
		ApsAdminBaseTestCase.setUserOnSession("admin");
		ApsAdminBaseTestCase.initAction("/do/Card", "edit");
		ApsAdminBaseTestCase.addParameter("id", "3");
		String result = ApsAdminBaseTestCase.executeAction();
		Assertions.assertEquals(Action.SUCCESS, result);
		CardAction action = (CardAction) ApsAdminBaseTestCase.getAction();
		Assertions.assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		Assertions.assertEquals("Verdi Nicola", action.getHolder());
		Assertions.assertEquals("Fair", action.getDescr());
		Assertions.assertEquals("Lorem ipsum.", action.getNote());
		Assertions.assertEquals("25/01/2011", DateConverter.getFormattedDate(action.getDate(), "dd/MM/yyyy"));
	}
	
	@Test
	public void testFailureSaveCard() throws Throwable {
		ApsAdminBaseTestCase.setUserOnSession("admin");
		ApsAdminBaseTestCase.initAction("/do/Card", "save");
		ApsAdminBaseTestCase.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
		ApsAdminBaseTestCase.addParameter("holder", "Darth Vader");
		ApsAdminBaseTestCase.addParameter("descr", "");
		ApsAdminBaseTestCase.addParameter("note", "Darth Vader is the iconic American film villain of George Lucas' award-winning science fiction saga Star Wars");
		ApsAdminBaseTestCase.addParameter("date", "wrongFormat");
		String result = ApsAdminBaseTestCase.executeAction();
		Assertions.assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = ApsAdminBaseTestCase.getAction().getFieldErrors();
		Assertions.assertEquals(2, fieldErrors.size());
		Assertions.assertEquals(1, fieldErrors.get("descr").size());
		Assertions.assertEquals(1, fieldErrors.get("date").size());
	}
	
	@Test
	public void testSuccessSaveCard() throws Throwable {
		ApsAdminBaseTestCase.setUserOnSession("admin");
		ApsAdminBaseTestCase.initAction("/do/Card", "save");
		ApsAdminBaseTestCase.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
		ApsAdminBaseTestCase.addParameter("holder", "Darth Vader");
		ApsAdminBaseTestCase.addParameter("descr", "Dark Lord of the Sith");
		ApsAdminBaseTestCase.addParameter("note", "Darth Vader is the iconic American film villain of George Lucas' award-winning science fiction saga Star Wars");
		ApsAdminBaseTestCase.addParameter("date", "25/04/1978");
		String result = ApsAdminBaseTestCase.executeAction();
		Assertions.assertEquals(Action.SUCCESS, result);
		
		ICardManager cardManager = (ICardManager) ApsAdminBaseTestCase.getService(CardSystemConstants.CARD_MANAGER);
		List<Card> cards = cardManager.searchCards("Darth Vader");
		Assertions.assertEquals(1, cards.size());
		cardManager.deleteCard(cards.get(0).getId());
		
		cards = cardManager.searchCards("Darth Vader");
		Assertions.assertEquals(0, cards.size());
	}
	
	@Test
	public void testDeleteCard() throws Throwable {
		ICardManager cardManager = (ICardManager) ApsAdminBaseTestCase.getService(CardSystemConstants.CARD_MANAGER);
		Assertions.assertNull(cardManager.getCard(70));
		Card newCard = new Card();
		newCard.setId(70);
		newCard.setHolder("Goofy Goof");
		newCard.setDescr("I'm Mickey Mouse's best friend");
		newCard.setDate(new Date());
		newCard.setNote("Annotations");
		try {
			cardManager.addCard(newCard);
			List<Card> cards = cardManager.searchCards("Goofy");
			Assertions.assertEquals(1, cards.size());
			Card card = cards.get(0);
			Assertions.assertEquals("Goofy Goof", card.getHolder());
			int id = card.getId();
			ApsAdminBaseTestCase.setUserOnSession("admin");
			ApsAdminBaseTestCase.initAction("/do/Card", "delete");
			ApsAdminBaseTestCase.addParameter("id", String.valueOf(id));
			String result = ApsAdminBaseTestCase.executeAction();
			Assertions.assertEquals(Action.SUCCESS, result);
			cards = cardManager.searchCards("Goofy");
			Assertions.assertEquals(0, cards.size());
		} catch (Throwable t) {
			List<Card> cards = cardManager.searchCards("Goofy");
			if (cards.size()==1) {
				Card card = cards.get(0);
				cardManager.deleteCard(card.getId());
				Assertions.assertNull(cardManager.getCard(card.getId()));
			}
			throw t;
		}
	}
	
}
