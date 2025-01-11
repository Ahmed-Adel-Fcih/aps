package com.qeema.aps.adapter.out;

import com.qeema.aps.adapter.out.client.AmazonPaymentServiceClient;
import com.qeema.aps.application.port.out.RefundPort;
import com.qeema.aps.domain.refund.Refund;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundAdapter implements RefundPort {
    @Autowired
    AmazonPaymentServiceClient amazonPaymentServiceClient;

    @Override
    public void processRefund(Refund refund) {
        amazonPaymentServiceClient.callRefundAPI();
    }

}