/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.ent.system.remotebean.grpc;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.google.protobuf.UInt32Value;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.ent.card.grpc.CardServiceGrpc.CardServiceImplBase;
import org.entando.ent.card.grpc.GrpcCard;
import org.entando.ent.card.grpc.GrpcCardIdList;
import org.entando.ent.card.grpc.GrpcCardList;
import org.entando.entando.ent.system.card.grpc.CardBuilder;
import org.entando.entando.ent.remotebean.grpc.model.GrpcFieldSearchFilterList;
import org.entando.entando.ent.system.remotebean.grpc.commons.FieldSearchFilterBuilder;
import org.entando.entando.portalexample.aps.system.services.card.Card;
import org.entando.entando.portalexample.aps.system.services.card.ICardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author eu
 */
@Component
public class GrpcCardService extends CardServiceImplBase {
    
    private static final Logger logger = LoggerFactory.getLogger(GrpcCardService.class);
    
    @Autowired
    private ICardManager cardManager;
    
    @Override
    public void searchCards(StringValue request, StreamObserver<GrpcCardList> responseObserver) {
        try {
            List<Card> cards = this.getCardManager().searchCards(request.getValue());
            GrpcCardList response = this.getCards(cards);
            this.completeResponse(response, null, responseObserver);
        } catch (Exception e) {
            logger.error("Error extracting card by holder", e);
            this.completeResponse(null, e, responseObserver);
        }
    }

    @Override
    public void getCards(Empty request, StreamObserver<GrpcCardList> responseObserver) {
        try {
            List<Card> cards = this.getCardManager().getCards();
            GrpcCardList response = this.getCards(cards);
            this.completeResponse(response, null, responseObserver);
        } catch (Exception e) {
            logger.error("Error extracting card", e);
            this.completeResponse(null, e, responseObserver);
        }
    }
    
    private GrpcCardList getCards(List<Card> cards) {
        List<GrpcCard> list = cards.stream().map(i -> CardBuilder.toGrpc(i)).collect(Collectors.toList());
        return GrpcCardList.newBuilder().addAllCards(list).build();
    }
    
    @Override
    public void deleteCard(UInt32Value request, StreamObserver<Empty> responseObserver) {
        try {
            this.getCardManager().deleteCard(request.getValue());
            this.completeResponse(Empty.getDefaultInstance(), null, responseObserver);
        } catch (Exception e) {
            logger.error("Error deleting card", e);
            this.completeResponse(null, e, responseObserver);
        }
    }

    @Override
    public void updateCard(GrpcCard request, StreamObserver<Empty> responseObserver) {
        try {
            this.getCardManager().updateCard(CardBuilder.toMain(request));
            this.completeResponse(Empty.getDefaultInstance(), null, responseObserver);
        } catch (Exception e) {
            logger.error("Error updating card", e);
            this.completeResponse(null, e, responseObserver);
        }
    }

    @Override
    public void addCard(GrpcCard request, StreamObserver<Empty> responseObserver) {
        try {
            this.getCardManager().addCard(CardBuilder.toMain(request));
            this.completeResponse(Empty.getDefaultInstance(), null, responseObserver);
        } catch (Exception e) {
            logger.error("Error adding card", e);
            this.completeResponse(null, e, responseObserver);
        }
    }
    
    @Override
    public void searchCardIds(GrpcFieldSearchFilterList request, StreamObserver<GrpcCardIdList> responseObserver) {
        try {
            List<FieldSearchFilter> filters = FieldSearchFilterBuilder.toMainList(request);
            List<Integer> ids = this.getCardManager().searchCardIds(filters.toArray(new FieldSearchFilter[0]));
            List<UInt32Value> list = ids.stream().map(i -> UInt32Value.of(i)).collect(Collectors.toList());
            GrpcCardIdList response = GrpcCardIdList.newBuilder().addAllIds(list).build();
            this.completeResponse(response, null, responseObserver);
        } catch (Exception e) {
            logger.error("Error searching card", e);
            this.completeResponse(null, e, responseObserver);
        }
    }
    
    @Override
    public void getCard(UInt32Value request, StreamObserver<GrpcCard> responseObserver) {
        try {
            Card card = this.getCardManager().getCard(request.getValue());
            GrpcCard response = null;
            if (null != card) {
                response = CardBuilder.toGrpc(card);
            } else {
                response = GrpcCard.getDefaultInstance();
            }
            this.completeResponse(response, null, responseObserver);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error extracting card", e);
            this.completeResponse(null, e, responseObserver);
        }
    }
    
    private <T> void completeResponse(T response, Throwable t, StreamObserver<T> responseObserver) {
        if (null != t) {
            responseObserver.onError(t);
        } else {
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    public ICardManager getCardManager() {
        return cardManager;
    }
    public void setCardManager(ICardManager cardManager) {
        this.cardManager = cardManager;
    }
    
}
