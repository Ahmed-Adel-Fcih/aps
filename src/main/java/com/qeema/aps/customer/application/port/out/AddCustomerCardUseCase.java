package com.qeema.aps.customer.application.port.out;

import com.qeema.aps.payment.domain.dto.TokenizationRequest;

public interface AddCustomerCardUseCase {
    public void addCustomerCard(TokenizationRequest tokenizationRequest, String merchant_reference);
}
