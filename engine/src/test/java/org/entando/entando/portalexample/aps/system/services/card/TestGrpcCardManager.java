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
import io.grpc.BindableService;
import io.grpc.inprocess.InProcessChannelBuilder;
import org.entando.ent.card.grpc.CardServiceGrpc;
import org.entando.entando.ent.system.card.grpc.GrpcCardServiceClient;
import org.entando.entando.ent.system.remotebean.grpc.GrpcCardService;
import org.entando.entando.ent.system.remotebean.grpc.IGrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGrpcCardManager extends TestDefaulGrpcCardManager {
	
	@Override
    protected BindableService getBindableService() {
        return MsBaseTestCase.getApplicationContext().getBean(GrpcCardService.class);
    }
    
    @Override
    protected IGrpcClient getGrpcClient(String serverName) {
        CardServiceGrpc.CardServiceBlockingStub blockingStub = CardServiceGrpc.newBlockingStub(super.grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));
        return new MockGrpcCardServiceClient(blockingStub);
    }
    
    @Test
    public void testService() {
        BindableService service = MsBaseTestCase.getApplicationContext().getBean(GrpcCardService.class);
        Assertions.assertNotNull(service);
    }
    
    @Test
    public void testClient() {
        GrpcCardServiceClient client = MsBaseTestCase.getApplicationContext().getBean(GrpcCardServiceClient.class);
        Assertions.assertNotNull(client);
    }

}
