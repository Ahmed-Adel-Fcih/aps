package com.qeema.aps.payment.domain.dto;

import java.io.Serializable;

import com.qeema.aps.common.utils.AmazonConstants;
import com.qeema.aps.common.utils.ServiceCommand;
import com.qeema.aps.payment.domain.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseRequest implements Serializable {

    private String command;
    private String access_code;
    private String merchant_identifier;
    private String merchant_reference;
    private String language;
    private String amount;
    private String currency;
    private String order_description;
    private String customer_email;
    private String signature;
    private String token_name;

    public PurchaseRequest(Payment payment) {
        this.command = payment.getServiceCommand();
        this.access_code = AmazonConstants.ACCESS_CODE;
        this.merchant_identifier = AmazonConstants.MERCHANT_IDENTIFIER;
        this.merchant_reference = payment.getMerchant_reference();
        this.language = AmazonConstants.LANGUAGE;
        this.amount = payment.getAmount().toString();
        this.currency = payment.getCurrency();
        this.order_description = payment.getOrder_description();
        this.customer_email = payment.getCustomer().getCustomerEmail();
    }

}
