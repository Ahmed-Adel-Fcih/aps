package com.qeema.aps.application.service;

import com.qeema.aps.adapter.out.persistence.PaymentRepository;
import com.qeema.aps.application.port.in.ProcessPaymentUseCase;
import com.qeema.aps.application.port.out.PaymentPort;
import com.qeema.aps.domain.payment.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService implements ProcessPaymentUseCase {

    @Autowired
    private PaymentRepository paymentRepository;
    private final PaymentPort paymentPort;

    public PaymentService(PaymentPort paymentPort) {
        this.paymentPort = paymentPort;
    }

    @Override
    public void processPayment(Payment payment) {
        validatePayment(payment);
        paymentPort.processPayment(payment);
    }

    @Override
    public List<Payment> getPaymentList() {
        Iterable<Payment> allPayments = paymentRepository.findAll();
        List<Payment> paymentList = new ArrayList<>();
        allPayments.forEach(paymentList::add);
        return paymentList;
    }

    @Override
    public Payment getPayment(Payment payment) {
        return paymentRepository.findById(payment.getId()).get();
    }

    private void validatePayment(Payment payment) {
        PaymentValidation.ValidationResult cardValidation = PaymentValidation
                .validateCard(payment.getCardNumber());
        if (!cardValidation.validity) {
            throw new IllegalArgumentException(cardValidation.message);
        }

        PaymentValidation.ValidationResult holderNameValidation = PaymentValidation
                .validateHolderName(payment.getCardHolderName());
        if (!holderNameValidation.validity) {
            throw new IllegalArgumentException(holderNameValidation.message);
        }

        PaymentValidation.ValidationResult cvvValidation = PaymentValidation.validateCVV(
                payment.getCardCVV(),
                cardValidation.cardType);
        if (!cvvValidation.validity) {
            throw new IllegalArgumentException(cvvValidation.message);
        }

        PaymentValidation.ValidationResult expiryValidation = PaymentValidation
                .validateCardExpiry(payment.getCardExpiryMonth(),
                        payment.getCardExpiryYear());
        if (!expiryValidation.validity) {
            throw new IllegalArgumentException(expiryValidation.message);
        }
    }

}