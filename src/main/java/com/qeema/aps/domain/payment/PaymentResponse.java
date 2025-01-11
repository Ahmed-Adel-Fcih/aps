package com.qeema.aps.domain.payment;

import lombok.Data;

@Data
public class PaymentResponse {
    private String response_code;
    private String response_message;
    private String merchant_reference;
    private String fort_id;
    private String payment_option;
    private String amount;
    private String currency;
    private String customer_email;
    private String eci;
    private String card_number;
    private String authorization_code;
    private String response_summary;
    private String customer_ip;
    private String signature;
    private String order_description;
    private String expiry_date;
    private String language;
    private String return_url;
}