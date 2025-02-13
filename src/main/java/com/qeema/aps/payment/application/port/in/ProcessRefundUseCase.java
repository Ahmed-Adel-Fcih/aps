package com.qeema.aps.payment.application.port.in;

import com.qeema.aps.payment.domain.dto.RefundResponse;

public interface ProcessRefundUseCase {
    RefundResponse refundPayment(String merchantReference, Integer customerId);
}
