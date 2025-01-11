package com.qeema.aps.adapter.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeema.aps.adapter.out.client.AmazonPaymentServiceClient;
import com.qeema.aps.adapter.out.persistence.PaymentRepository;
import com.qeema.aps.application.port.out.PaymentPort;
import com.qeema.aps.domain.payment.Payment;

@Service
public class PaymentAdapter implements PaymentPort {

    @Autowired
    AmazonPaymentServiceClient amazonPaymentServiceClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        amazonPaymentServiceClient.callTokenizationAPI();
        amazonPaymentServiceClient.callPurchaseAPI();
        paymentRepository.save(payment);
    }
}