package com.qeema.aps.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TokenizationRequest {
    private String response_code;
    private String card_number;
    private String card_holder_name;
    private String signature;
    private String merchant_identifier;
    private String expiry_date;
    private String access_code;
    private String language;
    private String service_command;
    private String response_message;
    private String merchant_reference;
    private String return_url;
    private String token_name;
    private String card_bin;
    private String status;
}
