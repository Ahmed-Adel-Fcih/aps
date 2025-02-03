package com.qeema.aps.payment.domain.dto;

import lombok.Data;

@Data
public class PreparePaymentResponseDto {
    private String access_code;
    private String merchant_identifier;
    private String merchant_reference;
    private String signature;
}
