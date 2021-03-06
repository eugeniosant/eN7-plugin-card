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
import java.util.List;

import org.entando.entando.ent.exception.EntException;

/**
 * @author E.Mezzano
 */
public interface ICardManager {
    
    public List<Card> getCards() throws EntException;
    
    public List<Integer> searchCardIds(FieldSearchFilter[] filters);
    
    public List<Card> searchCards(String holder) throws EntException;
    
    public Card getCard(Integer id) throws EntException;
    
    public void addCard(Card card) throws EntException;
    
    public void updateCard(Card card) throws EntException;
    
    public void deleteCard(Integer id) throws EntException;
    
}