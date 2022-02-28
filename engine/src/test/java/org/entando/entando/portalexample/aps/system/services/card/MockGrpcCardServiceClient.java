/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.portalexample.aps.system.services.card;

import org.entando.ent.card.grpc.CardServiceGrpc.CardServiceBlockingStub;
import org.entando.entando.ent.system.card.grpc.GrpcCardServiceClient;

/**
 * @author E.Santoboni
 */
public class MockGrpcCardServiceClient extends GrpcCardServiceClient {
    
    private CardServiceBlockingStub stub;
    
    protected MockGrpcCardServiceClient(CardServiceBlockingStub stub) {
        this.stub = stub;
    }
    
    @Override
    protected CardServiceBlockingStub getStub() {
        return this.stub;
    }
    
}
