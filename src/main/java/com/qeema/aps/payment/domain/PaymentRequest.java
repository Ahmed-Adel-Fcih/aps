package com.qeema.aps.payment.domain;

import lombok.Data;

@Data
public class PaymentRequest {
    private Double amount;
    private String currency;
    private String order_description;
    private Integer card_id;
    private Integer customer_id;
    private String payment_option;
    private String eci;
    private String return_url;
}