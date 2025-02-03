package com.qeema.aps.payment.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class PaymentResponse {

    private String merchant_reference;
    private String payment_option;
    private Integer amount;
    private String currency;
    private String customer_email;
    private String eci;
    private String card_number;
    private String response_message;

    public PaymentResponse(Payment processedPayment) {
        this.merchant_reference = processedPayment.getMerchant_reference();
        this.payment_option = processedPayment.getPaymentOption();
        this.amount = processedPayment.getAmount();
        this.currency = processedPayment.getCurrency();
        this.customer_email = processedPayment.getCustomer().getCustomerEmail();
        this.eci = processedPayment.getEci();
        this.card_number = processedPayment.getCard().getMaskedCardNumber();
        this.response_message = processedPayment.getResponse_message();
    }
}