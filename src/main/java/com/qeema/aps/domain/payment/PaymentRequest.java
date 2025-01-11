package com.qeema.aps.domain.payment;

import lombok.Data;

@Data
public class PaymentRequest {
    private String api_key;
    private String merchant_reference;
    private String amount;
    private String currency;
    private String language;
    private String customer_email;
    private String order_description;
    private String card_number;
    private String expiry_date;
    private String card_security_code;
    private String signature;
    private String payment_option;
    private String eci;
    private String return_url;
}