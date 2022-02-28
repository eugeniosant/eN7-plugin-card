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
package org.entando.entando.portalexample.aps.system.services.card;

import com.agiletec.aps.MsBaseTestCase;
import java.util.Date;
import java.util.List;
import org.entando.entando.portalexample.CardConfigTestUtils;

import org.entando.entando.portalexample.aps.system.DemoSystemConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestCardManager {
	
	@BeforeAll
    public static void setUp() throws Exception {
        MsBaseTestCase.setUp(new CardConfigTestUtils());
    }
    
    @AfterAll
    protected static void tearDown() throws Exception {
        MsBaseTestCase.tearDown();
    }

    public void testGetCards() throws Throwable {
		List<Card> cards = this._cardManager.getCards();
        Assertions.assertEquals(4, cards.size());
	}
	
	public void testSearchCards() throws Throwable {
		List<Card> cards = this._cardManager.searchCards("Nicola");
		Assertions.assertEquals(1, cards.size());
		Card card = (Card) cards.get(0);
		String titolare = card.getHolder();
		Assertions.assertTrue(titolare.equals("Verdi Nicola"));
	}
	
	public void testGetCard() throws Throwable {
		Card card = this._cardManager.getCard(1);
		Assertions.assertEquals("Bianchi Marco", card.getHolder());
		card = this._cardManager.getCard(2);
		Assertions.assertEquals("Rossi Carla", card.getHolder());
		card = this._cardManager.getCard(3);
		Assertions.assertEquals("Verdi Nicola", card.getHolder());
	}
	
	public void testAddDeleteCard() throws Throwable {
		Assertions.assertNull(this._cardManager.getCard(70));
		Card newCard = new Card();
		newCard.setId(70);
		newCard.setHolder("Goofy Goof");
		newCard.setDescr("I'm Mickey Mouse's best friend");
		newCard.setDate(new Date());
		newCard.setNote("Annotations");
		try {
			this._cardManager.addCard(newCard);
			List<Card> cards = this._cardManager.searchCards("Goofy");
			Assertions.assertEquals(1, cards.size());
			Card card = cards.get(0);
			Assertions.assertEquals("Goofy Goof", card.getHolder());
			Assertions.assertEquals("I'm Mickey Mouse's best friend", card.getDescr());
			Assertions.assertEquals("Annotations", card.getNote());
		} catch (Throwable t) {
			throw t;
		} finally {
			List<Card> cards = this._cardManager.searchCards("Goofy");
			if (cards.size() == 1) {
				Card card = cards.get(0);
				this._cardManager.deleteCard(card.getId());
				Assertions.assertNull(this._cardManager.getCard(card.getId()));
			}
		}
	}

	public void testUpdateCard() throws Throwable {
		Card card = this._cardManager.getCard(3);
		String oldTitolare = card.getHolder();
		String oldDescrizione = card.getDescr();
		Date oldDataRilascio = card.getDate();
		String oldNote = card.getNote();
		card.setHolder("Verdi Nicola Luca");
		card.setDescr("Main Project Manager");
		card.setDate(new Date());
		card.setNote("with Verdi Nicola");

		this._cardManager.updateCard(card);

		Card updatedCard = this._cardManager.getCard(3);
		Assertions.assertEquals("Verdi Nicola Luca", updatedCard.getHolder());
		Assertions.assertEquals("Main Project Manager", updatedCard.getDescr());
		Assertions.assertEquals("with Verdi Nicola", updatedCard.getNote());

		card.setHolder(oldTitolare);
		card.setDescr(oldDescrizione);
		card.setDate(oldDataRilascio);
		card.setNote(oldNote);

		this._cardManager.updateCard(card);

		updatedCard = this._cardManager.getCard(3);
		Assertions.assertEquals("Verdi Nicola", updatedCard.getHolder());
		Assertions.assertEquals("Fair", updatedCard.getDescr());
		Assertions.assertEquals("Lorem ipsum.", updatedCard.getNote());
	}

    @BeforeEach
	void initClass() {
		this._cardManager = (ICardManager) MsBaseTestCase.getService(DemoSystemConstants.CARD_MANAGER);
	}

	private ICardManager _cardManager;

}
