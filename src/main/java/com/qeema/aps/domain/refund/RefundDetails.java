package com.qeema.aps.domain.refund;

import lombok.Data;

@Data
public class RefundDetails {
    private String amount;
    private String currency;
    private String transactionId;
}