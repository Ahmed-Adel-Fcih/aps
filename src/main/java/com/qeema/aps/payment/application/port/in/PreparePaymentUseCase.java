package com.qeema.aps.payment.application.port.in;

import com.qeema.aps.payment.domain.dto.PreparePaymentRequestDto;
import com.qeema.aps.payment.domain.dto.PreparePaymentResponseDto;

public interface PreparePaymentUseCase {
    public PreparePaymentResponseDto preparePayment(PreparePaymentRequestDto request, Integer customerId);
}
