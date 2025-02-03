package com.qeema.aps.payment.adapter.in.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.qeema.aps.payment.application.port.in.PreparePaymentUseCase;
import com.qeema.aps.payment.application.port.in.ProcessPaymentUseCase;
import com.qeema.aps.payment.application.port.in.ReadPaymentUseCase;
import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.PaymentRequest;
import com.qeema.aps.payment.domain.PaymentResponse;
import com.qeema.aps.payment.domain.dto.PreparePaymentRequestDto;
import com.qeema.aps.payment.domain.dto.PreparePaymentResponseDto;
import com.qeema.aps.payment.domain.dto.RefundResponse;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private ProcessPaymentUseCase processPaymentUseCase;

    @Autowired
    private ReadPaymentUseCase readPaymentUseCase;

    @Autowired
    private PreparePaymentUseCase preparePaymentUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer id) {
        // Implement logic to get payment by id
        return ResponseEntity.ok(readPaymentUseCase.getPayment(id));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestHeader Integer customerId,
            @RequestBody PaymentRequest payment) {
        // Implement logic to create a new payment
        payment.setCustomer_id(customerId);
        PaymentResponse response = processPaymentUseCase.processPayment(payment);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        // Implement logic to get all payments
        return ResponseEntity.ok(readPaymentUseCase.getPaymentList());
    }

    @PostMapping("/prepare")
    public ResponseEntity<PreparePaymentResponseDto> preparePayment(@RequestHeader Integer customerId,
            @RequestBody PreparePaymentRequestDto request) {
        log.debug(request.toString());
        return ResponseEntity.ok(preparePaymentUseCase.preparePayment(request, customerId));
    }

    @PostMapping("/refund")
    public ResponseEntity<RefundResponse> refundPayment(@RequestHeader Integer customerId,
            @RequestBody String merchantReference) {
        RefundResponse response = processPaymentUseCase.refundPayment(merchantReference, customerId);
        return ResponseEntity.ok(response);
    }
}
