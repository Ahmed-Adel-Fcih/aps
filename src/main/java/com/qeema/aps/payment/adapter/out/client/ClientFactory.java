package com.qeema.aps.payment.adapter.out.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.qeema.aps.payment.application.port.out.AmazonPaymentServiceClient;

@Service
public class ClientFactory {

    AmazonPaymentServiceClient amazonClient, wso2Client;

    public ClientFactory(@Qualifier("AmazonClient") AmazonPaymentServiceClient wso2Client,
            @Qualifier("AmazonClient") AmazonPaymentServiceClient amazonClient) {
        this.amazonClient = amazonClient;
        this.wso2Client = wso2Client;

    }

    public AmazonPaymentServiceClient getAmazonClient() {
        return amazonClient;
    }

    public AmazonPaymentServiceClient getWSO2Client() {
        return wso2Client;
    }

}
