package com.qeema.aps.payment.application.port.out;

import org.springframework.stereotype.Service;

import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.dto.PurchaseResponse;
import com.qeema.aps.payment.domain.dto.RefundRequest;
import com.qeema.aps.payment.domain.dto.RefundResponse;

public interface AmazonPaymentServiceClient {

    public PurchaseResponse callPurchaseAPI(Payment payment, String token);

    public RefundResponse callRefundAPI(RefundRequest refundRequest);

}
