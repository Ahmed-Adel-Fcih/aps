package com.qeema.aps.payment.application.port.in;

import com.qeema.aps.payment.domain.PaymentRequest;
import com.qeema.aps.payment.domain.PaymentResponse;
import com.qeema.aps.payment.domain.dto.RefundResponse;

public interface ProcessPaymentUseCase {
    PaymentResponse processPayment(PaymentRequest payment);

    RefundResponse refundPayment(String merchantReference, Integer customerId);
}
