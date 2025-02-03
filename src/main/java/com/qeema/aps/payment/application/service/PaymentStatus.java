package com.qeema.aps.payment.application.service;

public enum PaymentStatus {
    CREATED("Created"), PURCHASED("Purchased"), REFUNDED("Received");

    String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return status;
    }

}
