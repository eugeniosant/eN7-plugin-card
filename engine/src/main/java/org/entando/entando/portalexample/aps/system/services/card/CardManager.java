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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;

import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.ent.exception.EntException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Mezzano - E.Santoboni
 */
public class CardManager extends AbstractService implements ICardManager {
	
	private static final Logger _logger =  LoggerFactory.getLogger(CardManager.class);
	
	@Override
    public void init() throws Exception {
        _logger.debug("{} ready.", this.getClass().getName());
    }

    public List<Card> getCardsForApi(Properties properties) throws EntException {
        String holder = properties.getProperty("holder");
        return this.searchCards(holder);
    }

    public Card getCardForApi(Properties properties) throws ApiException, EntException {
        String idString = properties.getProperty("id");
        int id = 0;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid number format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        Card card = this.getCard(id);
        if (null == card) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Card with id '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
        return card;
    }

	@Override
    public List<Card> getCards() throws EntException {
        List<Card> cards = null;
        try {
            cards = this.getCardDAO().loadCards();
        } catch (Throwable t) {
			_logger.error("Error loading cards",  t);
            throw new EntException("Error loading cards", t);
        }
        return cards;
    }

    @Override
    public List<Integer> searchCardIds(FieldSearchFilter[] filters) {
        return this.getCardDAO().searchCardIds(filters);
    }
    
	@Override
    public List<Card> searchCards(String holder) throws EntException {
        List<Card> cards = null;
        try {
            cards = this.getCardDAO().searchCards(holder);
        } catch (Throwable t) {
            _logger.error("Error searching cards",  t);
            throw new EntException("Error searching cards", t);
        }
        return cards;
    }

	@Override
    public Card getCard(Integer id) throws EntException {
        Card card = null;
        try {
            card = this.getCardDAO().loadCard(id);
        } catch (Throwable t) {
            _logger.error("Error searching card by id {}", id,  t);
            throw new EntException("Error searching card by id " + id, t);
        }
        return card;
    }

    public void addCardForApi(Card card) throws ApiException, EntException {
        if (null != this.getCard(card.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Card with id " + card.getId() + " already exists", Response.Status.CONFLICT);
        }
        this.addCard(card);
    }

	@Override
    public void addCard(Card card) throws EntException {
        try {
            this.getCardDAO().addCard(card);
        } catch (Throwable t) {
            _logger.error("Error adding card",  t);
            throw new EntException("Error adding card", t);
        }
    }

    public void updateCardForApi(Card card) throws ApiException, EntException {
        if (null == this.getCard(card.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Card with id " + card.getId() + " does not exist", Response.Status.CONFLICT);
        }
        this.updateCard(card);
    }

	@Override
    public void updateCard(Card card) throws EntException {
        try {
            this.getCardDAO().updateCard(card);
        } catch (Throwable t) {
            _logger.error("Error updating card",  t);
            throw new EntException("Error updating card", t);
        }
    }

    public void deleteCardForApi(Properties properties) throws Throwable {
        String id = properties.getProperty("id");
        int idInteger = 0;
        try {
            idInteger = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid number format for 'id' parameter - '" + id + "'", Response.Status.CONFLICT);
        }
        this.deleteCard(idInteger);
    }

	@Override
    public void deleteCard(Integer id) throws EntException {
        try {
            this.getCardDAO().deleteCard(id);
        } catch (Throwable t) {
            _logger.error("Error deleting card by id {}", id,  t);
            throw new EntException("Error deleting card by id " + id, t);
        }
    }
	
    protected ICardDAO getCardDAO() {
        return _cardDAO;
    }
    public void setCardDAO(ICardDAO cardDAO) {
        this._cardDAO = cardDAO;
    }

	private ICardDAO _cardDAO;

}