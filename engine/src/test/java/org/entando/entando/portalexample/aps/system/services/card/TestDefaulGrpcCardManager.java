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

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.FieldSearchFilter.LikeOptionType;
import java.util.Date;
import java.util.List;
import org.entando.entando.aps.system.services.remotebean.exposed.AbstractCoreGrpcServiceClientIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDefaulGrpcCardManager extends AbstractCoreGrpcServiceClientIntegrationTest<ICardManager> {
	
	@Override
    protected Class<ICardManager> getProxyBeanClass() {
        return ICardManager.class;
    }
    /*
    @Override
    protected MsConfigTestUtils getConfigUtils() {
        return new CardConfigTestUtils();
    }
    */
    @Test
    public void testGetCards() throws Throwable {
		List<Card> cards = this.proxyManager.getCards();
		Assertions.assertEquals(4, cards.size());
	}
	
	@Test
    public void testSearchCards() throws Throwable {
		List<Card> cards = this.proxyManager.searchCards("Nicola");
        Assertions.assertEquals(1, cards.size());
		Card card = (Card) cards.get(0);
		String titolare = card.getHolder();
		Assertions.assertTrue(titolare.equals("Verdi Nicola"));
	}
	
	@Test
    public void testSearchCardIds() throws Throwable {
        FieldSearchFilter<String> filter1 = new FieldSearchFilter<>("holder", "Nicola", true, LikeOptionType.RIGHT);
        FieldSearchFilter[] filters1 = {filter1};
		List<Integer> cards = this.proxyManager.searchCardIds(filters1);
		Assertions.assertEquals(0, cards.size());
        
        FieldSearchFilter<String> filter2 = new FieldSearchFilter<>("holder", "Nicola", true, LikeOptionType.LEFT);
        FieldSearchFilter[] filters2 = {filter2};
		cards = this.proxyManager.searchCardIds(filters2);
		Assertions.assertEquals(1, cards.size());
        Assertions.assertEquals(3, cards.get(0).intValue());
        
        FieldSearchFilter<String> filter3 = new FieldSearchFilter<>("holder", "rdi", true, LikeOptionType.COMPLETE);
        FieldSearchFilter[] filters3 = {filter3};
		cards = this.proxyManager.searchCardIds(filters3);
		Assertions.assertEquals(2, cards.size());
        Assertions.assertTrue(cards.contains(3));
        Assertions.assertTrue(cards.contains(4));
	}
	
	@Test
    public void testGetCard() throws Throwable {
		Card card = this.proxyManager.getCard(1);
		Assertions.assertEquals("Bianchi Marco", card.getHolder());
		card = this.proxyManager.getCard(2);
		Assertions.assertEquals("Rossi Carla", card.getHolder());
		card = this.proxyManager.getCard(3);
		Assertions.assertEquals("Verdi Nicola", card.getHolder());
	}
	
	@Test
    public void testAddDeleteCard() throws Throwable {
		Assertions.assertNull(this.proxyManager.getCard(70));
		Card newCard = new Card();
		newCard.setId(70);
		newCard.setHolder("Goofy Goof");
		newCard.setDescr("I'm Mickey Mouse's best friend");
		newCard.setDate(new Date());
		newCard.setNote("Annotations");
		try {
			this.proxyManager.addCard(newCard);
			List<Card> cards = this.proxyManager.searchCards("Goofy");
			Assertions.assertEquals(1, cards.size());
			Card card = cards.get(0);
			Assertions.assertEquals("Goofy Goof", card.getHolder());
			Assertions.assertEquals("I'm Mickey Mouse's best friend", card.getDescr());
			Assertions.assertEquals("Annotations", card.getNote());
		} catch (Throwable t) {
			throw t;
		} finally {
			List<Card> cards = this.proxyManager.searchCards("Goofy");
			if (cards.size() == 1) {
				Card card = cards.get(0);
				this.proxyManager.deleteCard(card.getId());
				Assertions.assertNull(this.proxyManager.getCard(card.getId()));
			}
		}
	}

	@Test
    public void testUpdateCard() throws Throwable {
		Card card = this.proxyManager.getCard(3);
		String oldTitolare = card.getHolder();
		String oldDescrizione = card.getDescr();
		Date oldDataRilascio = card.getDate();
		String oldNote = card.getNote();
		card.setHolder("Verdi Nicola Luca");
		card.setDescr("Main Project Manager");
		card.setDate(new Date());
		card.setNote("with Verdi Nicola");

		this.proxyManager.updateCard(card);

		Card updatedCard = this.proxyManager.getCard(3);
		Assertions.assertEquals("Verdi Nicola Luca", updatedCard.getHolder());
		Assertions.assertEquals("Main Project Manager", updatedCard.getDescr());
		Assertions.assertEquals("with Verdi Nicola", updatedCard.getNote());

		card.setHolder(oldTitolare);
		card.setDescr(oldDescrizione);
		card.setDate(oldDataRilascio);
		card.setNote(oldNote);

		this.proxyManager.updateCard(card);

		updatedCard = this.proxyManager.getCard(3);
		Assertions.assertEquals("Verdi Nicola", updatedCard.getHolder());
		Assertions.assertEquals("Fair", updatedCard.getDescr());
		Assertions.assertEquals("Lorem ipsum.", updatedCard.getNote());
	}

}
