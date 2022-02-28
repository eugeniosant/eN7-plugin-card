/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.ent.system.card.grpc;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.google.protobuf.StringValue.Builder;
import com.google.protobuf.UInt32Value;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.entando.ent.card.grpc.CardServiceGrpc;
import org.entando.ent.card.grpc.CardServiceGrpc.CardServiceBlockingStub;
import org.entando.ent.card.grpc.GrpcCard;
import org.entando.ent.card.grpc.GrpcCardIdList;
import org.entando.ent.card.grpc.GrpcCardList;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.ent.system.remotebean.grpc.AbstractGrpcServiceClient;
import org.entando.entando.ent.system.remotebean.grpc.commons.FieldSearchFilterBuilder;
import org.entando.entando.portalexample.aps.system.services.card.Card;
import org.entando.entando.portalexample.aps.system.services.card.ICardManager;
import org.springframework.stereotype.Component;

/**
 *
 * @author eu
 */
@Component
public class GrpcCardServiceClient extends AbstractGrpcServiceClient {

    @Override
    public Class<?> getBeanClass() {
        return ICardManager.class;
    }

    public List<Card> getCards() throws EntException {
        GrpcCardList result = this.getStub().getCards(Empty.newBuilder().build());
        return result.getCardsList().stream().map(i -> CardBuilder.toMain(i)).collect(Collectors.toList());
    }
    
    public List<Integer> searchCardIds(FieldSearchFilter[] filters) {
        List<FieldSearchFilter> filterList = (null != filters) ? Arrays.asList(filters) : new ArrayList<>();
        GrpcCardIdList grpcIdList = this.getStub().searchCardIds(FieldSearchFilterBuilder.toGrpcList(filterList));
        return grpcIdList.getIdsList().stream().map(i -> i.getValue()).collect(Collectors.toList());
    }

    public List<Card> searchCards(String holder) throws EntException {
        Builder builder = StringValue.newBuilder();
        if (!StringUtils.isBlank(holder)) {
            builder.setValue(holder);
        }
        GrpcCardList grpcList = this.getStub().searchCards(builder.build());
        return grpcList.getCardsList().stream().map(i -> CardBuilder.toMain(i)).collect(Collectors.toList());
    }

    public Card getCard(Integer id) throws EntException {
        GrpcCard result = this.getStub().getCard(UInt32Value.of(id));
        return CardBuilder.toMain(result);
    }

    public void addCard(Card card) throws EntException {
        this.getStub().addCard(CardBuilder.toGrpc(card));
    }

    public void updateCard(Card card) throws EntException {
        this.getStub().updateCard(CardBuilder.toGrpc(card));
    }

    public void deleteCard(Integer id) throws EntException {
        this.getStub().deleteCard(UInt32Value.of(id));
    }
    
    protected CardServiceBlockingStub getStub() {
        return CardServiceGrpc.newBlockingStub(this.getCurrentChannel());
    }

}
