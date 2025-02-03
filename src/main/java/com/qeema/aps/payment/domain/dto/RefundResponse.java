package com.qeema.aps.payment.domain.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RefundResponse implements Serializable {
    private String response_message;
    private String fort_id;
    private String authorization_code;
    private String payment_option;
    private String customer_ip;
}
