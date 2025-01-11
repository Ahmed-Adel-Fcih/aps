package com.qeema.aps.domain.refund;

import lombok.Data;

@Data
public class RefundResponse {
    private String response_code;
    private String response_message;
    private String merchant_reference;
    private String fort_id;
    private String amount;
    private String currency;
    private String refund_reference;
    private String signature;
    private String language;
}