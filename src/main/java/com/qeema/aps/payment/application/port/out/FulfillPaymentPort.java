package com.qeema.aps.payment.application.port.out;

import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.dto.RefundRequest;
import com.qeema.aps.payment.domain.dto.RefundResponse;

public interface FulfillPaymentPort {
    Payment fulfillPayment(Payment payment);

    public RefundResponse callRefundAPI(RefundRequest refundRequest);
}
