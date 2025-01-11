package com.qeema.aps.adapter.out.client;

public interface AmazonPaymentServiceClient {

    public String callTokenizationAPI();

    public String callPurchaseAPI();

    public String callRefundAPI();

}
