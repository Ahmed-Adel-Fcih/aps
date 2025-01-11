package com.qeema.aps.domain.refund;

import lombok.Data;

@Data
public class RefundRequest {
    private String api_key;
    private String merchant_reference;
    private String amount;
    private String currency;
    private String language;
    private String fort_id;
    private String signature;
}