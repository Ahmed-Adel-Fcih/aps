package com.qeema.aps.domain.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    private int id;
    private String amount;
    private String currency;
    private String cardToken;
    private String cardCVV;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private String cardHolderName;
    private String cardNumber;
    private String cardType;
    private String paymentType;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentDate;
    private String paymentTime;
    private String paymentGateway;
    private String paymentGatewayResponse;
    private String paymentGatewayResponseCode;
    private String paymentGatewayResponseMessage;
    private String paymentGatewayTransactionId;
    private String paymentGatewayTransactionStatus;
}