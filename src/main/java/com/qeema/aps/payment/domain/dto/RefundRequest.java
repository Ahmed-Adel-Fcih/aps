package com.qeema.aps.payment.domain.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RefundRequest implements Serializable {
    private String access_code;
    private Integer amount;
    private String currency;
    private String language;
    private String merchant_identifier;
    private String merchant_reference;
    private String command;
    private String signature;
}
