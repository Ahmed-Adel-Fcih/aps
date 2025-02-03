package com.qeema.aps.payment.domain;

import java.time.LocalDateTime;

import com.qeema.aps.common.utils.ServiceCommand;
import com.qeema.aps.card.domain.Card;
import com.qeema.aps.customer.domain.Customer;
import com.qeema.aps.payment.domain.dto.PurchaseRequest;
import com.qeema.aps.payment.domain.dto.PurchaseResponse;
import com.qeema.aps.payment.domain.dto.RefundRequest;
import com.qeema.aps.payment.domain.dto.RefundResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "payment")
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer amount;
    private String currency;
    private String cardHolderName;
    private String cardType;
    private String serviceCommand;
    private String paymentOption;
    private String paymentStatus;
    private LocalDateTime timestamp;
    private String order_description;
    private String customer_ip;
    private String eci;
    private String fort_id;
    private String authorization_code;
    private String response_message;
    private String remember_me;
    @Column(length = 4000)
    private String request;
    @Column(length = 4000)
    private String response;
    private String merchant_reference;
    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.timestamp = LocalDateTime.now();
    }
}