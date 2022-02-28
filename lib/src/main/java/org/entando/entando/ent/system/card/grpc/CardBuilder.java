/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.ent.system.card.grpc;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.DateConverter;
import com.google.protobuf.StringValue;
import com.google.protobuf.StringValue.Builder;
import com.google.protobuf.UInt32Value;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.entando.ent.card.grpc.GrpcCard;
import org.entando.entando.portalexample.aps.system.services.card.Card;

/**
 * @author E.Santoboni
 */
public class CardBuilder {

    private CardBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static Card toMain(GrpcCard grpc) {
        if (0 == grpc.getId().getValue()) {
            return null;
        }
        Card card = new Card();
        String dateString = grpc.getDate().getValue();
        Date date = (StringUtils.isBlank(dateString)) ? null : DateConverter.parseDate(dateString, SystemConstants.API_DATE_FORMAT);
        card.setDate(date);
        card.setDescr(grpc.getDescription().getValue());
        card.setHolder(grpc.getHolder().getValue());
        card.setId(grpc.getId().getValue());
        card.setNote(grpc.getNote().getValue());
        return card;
    }

    public static GrpcCard toGrpc(Card card) {
        String dateString = (null != card.getDate()) ? DateConverter.getFormattedDate(card.getDate(), SystemConstants.API_DATE_FORMAT) : null;
        return GrpcCard.newBuilder()
                .setDate(getValue(dateString))
                .setDescription(getValue(card.getDescr()))
                .setHolder(getValue(card.getHolder()))
                .setNote(getValue(card.getNote()))
                .setId(getValue(card.getId()))
                .build();
    }

    private static Builder getValue(String value) {
        Builder builder = StringValue.newBuilder();
        if (null != value) {
            builder.setValue(value);
        }
        return builder;
    }

    private static UInt32Value.Builder getValue(int value) {
        return UInt32Value.newBuilder().setValue(value);
    }

}
