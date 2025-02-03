package com.qeema.aps.payment.domain.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PurchaseResponse implements Serializable {
    private String command;
    private String access_code;
    private String merchant_identifier;
    private String merchant_reference;
    private double amount;
    private String currency;
    private String language;
    private String customer_email;
    private String customer_ip;
    private String token_name;
    private String signature;
    private String fort_id;
    private String payment_option;
    private String order_description;
    private String statement_descriptor;
    private String authorization_code;
    private String response_message;
    private String response_code;
    private String customer_name;
    private String status;
    private String phone_number;
    private String settlement_reference;
    private String agreement_id;
}
