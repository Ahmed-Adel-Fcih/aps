package com.qeema.aps.adapter.out.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WSO2Client implements AmazonPaymentServiceClient {
    private static final String TOKENIZATION_API_URL = "https://wso2.example.com/tokenization";
    private static final String PURCHASE_API_URL = "https://wso2.example.com/purchase";
    private static final String REFUND_API_URL = "https://wso2.example.com/refund";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String callTokenizationAPI() {
        // Call the tokenization API
        String response = restTemplate.postForObject(TOKENIZATION_API_URL, null, String.class);
        return response;
    }

    @Override
    public String callPurchaseAPI() {
        // Call the purchase API
        String response = restTemplate.postForObject(PURCHASE_API_URL, null, String.class);
        return response;
    }

    @Override
    public String callRefundAPI() {
        // Call the refund API
        String response = restTemplate.postForObject(REFUND_API_URL, null, String.class);
        return response;
    }

}