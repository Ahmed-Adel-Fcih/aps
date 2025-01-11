package com.qeema.aps.adapter.in.web;

import com.qeema.aps.domain.payment.Payment;
import com.qeema.aps.application.port.in.ProcessPaymentUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    @Autowired
    private ProcessPaymentUseCase processPaymentUseCase;

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable String id) {
        // Implement logic to get payment by id
        return processPaymentUseCase.getPayment(new Payment());
    }

    @PostMapping
    public void createPayment(@RequestBody Payment payment) {
        // Implement logic to create a new payment
        processPaymentUseCase.processPayment(payment);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        // Implement logic to get all payments
        return processPaymentUseCase.getPaymentList();
    }
}
