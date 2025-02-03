package com.qeema.aps.payment.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeema.aps.common.utils.AmazonConstants;
import com.qeema.aps.common.utils.AmazonUtils;
import com.qeema.aps.common.utils.ServiceCommand;
import com.qeema.aps.payment.adapter.out.persistance.PaymentRepository;
import com.qeema.aps.payment.application.port.in.PreparePaymentUseCase;
import com.qeema.aps.payment.application.port.in.ProcessPaymentUseCase;
import com.qeema.aps.payment.application.port.in.ReadPaymentUseCase;
import com.qeema.aps.payment.application.port.out.FulfillPaymentPort;
import com.qeema.aps.payment.application.port.out.SavePaymentPort;
import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.PaymentRequest;
import com.qeema.aps.payment.domain.PaymentResponse;
import com.qeema.aps.payment.domain.dto.PreparePaymentRequestDto;
import com.qeema.aps.payment.domain.dto.PreparePaymentResponseDto;
import com.qeema.aps.payment.domain.dto.RefundRequest;
import com.qeema.aps.payment.domain.dto.RefundResponse;
import com.qeema.aps.card.domain.Card;
import com.qeema.aps.customer.domain.Customer;
import com.qeema.aps.card.adapter.out.persistance.CardRepository;
import com.qeema.aps.customer.adapter.out.persistance.CustomerRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class PaymentService implements ProcessPaymentUseCase, ReadPaymentUseCase, PreparePaymentUseCase {

    private static final Integer SAR_ISO_CODE = 100;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private FulfillPaymentPort paymentPort;
    @Autowired
    private SavePaymentPort savePaymentPort;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentReq) {
        // validatePayment(payment);
        Payment payment = new Payment();

        payment.setServiceCommand(ServiceCommand.PURCHASE.getName());
        payment.setMerchant_reference(UUID.randomUUID().toString().replace("-", ""));
        payment.setPaymentStatus(PaymentStatus.CREATED.getName());
        Integer amount = (int) (paymentReq.getAmount() * SAR_ISO_CODE);
        payment.setAmount(amount);
        payment.setCurrency(paymentReq.getCurrency());
        payment.setOrder_description(removeSpecialCharacters(paymentReq.getOrder_description()));
        Card card = cardRepository.findById(paymentReq.getCard_id()).orElse(null);
        payment.setCard(card);
        Customer customer = customerRepository.findById(paymentReq.getCustomer_id()).orElse(null);
        payment.setCustomer(customer);

        paymentRepository.save(payment);
        Payment processedPayment = paymentPort.fulfillPayment(payment);
        PaymentResponse paymentResponse = new PaymentResponse(processedPayment);
        return paymentResponse;
    }

    @Override
    public List<Payment> getPaymentList() {
        Iterable<Payment> allPayments = paymentRepository.findAll();
        List<Payment> paymentList = new ArrayList<>();
        allPayments.forEach(payment -> {
            if (!Objects.equals(ServiceCommand.Tokenization.getName(), payment.getServiceCommand()))
                paymentList.add(payment);
        });
        return paymentList;
    }

    @Override
    public Payment getPayment(Integer id) {
        return paymentRepository.findById(id).get();
    }

    @Override
    public PreparePaymentResponseDto preparePayment(PreparePaymentRequestDto request, Integer customerId) {
        PreparePaymentResponseDto response = new PreparePaymentResponseDto();
        String merchant_reference = UUID.randomUUID().toString().replace("-", "");
        String signature = getSignature(merchant_reference);

        response.setAccess_code(AmazonConstants.ACCESS_CODE);
        response.setMerchant_identifier(AmazonConstants.MERCHANT_IDENTIFIER);
        response.setMerchant_reference(merchant_reference);
        response.setSignature(signature);

        savePaymentPort.saveTokenizationRequest(request, merchant_reference, customerId);

        return response;
    }

    @Override
    public RefundResponse refundPayment(String merchantReference, Integer customerId) {
        Payment payment = paymentRepository.findByMerchantReference(merchantReference);
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found for merchant reference: " + merchantReference);
        }

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setAccess_code(AmazonConstants.ACCESS_CODE);
        refundRequest.setAmount(payment.getAmount());
        refundRequest.setCurrency(payment.getCurrency());
        refundRequest.setLanguage(AmazonConstants.LANGUAGE);
        refundRequest.setMerchant_identifier(AmazonConstants.MERCHANT_IDENTIFIER);
        refundRequest.setMerchant_reference(payment.getMerchant_reference());
        refundRequest.setCommand(ServiceCommand.REFUND.getName());

        String signature = getSignature(refundRequest);
        refundRequest.setSignature(signature);

        RefundResponse refundResponse = paymentPort.callRefundAPI(refundRequest);

        Payment refundPayment = new Payment();
        refundPayment.setServiceCommand(ServiceCommand.REFUND.getName());
        refundPayment.setMerchant_reference(UUID.randomUUID().toString().replace("-", ""));
        refundPayment.setPaymentStatus(PaymentStatus.REFUNDED.getName());
        refundPayment.setAmount(payment.getAmount());
        refundPayment.setCurrency(payment.getCurrency());
        refundPayment.setOrder_description(payment.getOrder_description());
        refundPayment.setCard(payment.getCard());
        refundPayment.setCustomer(payment.getCustomer());
        refundPayment.setResponse(refundResponse.toString());
        refundPayment.setRequest(refundRequest.toString());

        paymentRepository.save(refundPayment);

        return refundResponse;
    }

    @Override
    public Payment getPaymentByMerchantReference(String merchant_reference) {
        return paymentRepository.findByMerchantReference(merchant_reference);
    }

    private String getSignature(String merchant_reference) {
        Map<String, Object> params = new HashMap<>();
        params.put("access_code", AmazonConstants.ACCESS_CODE);
        params.put("merchant_identifier", AmazonConstants.MERCHANT_IDENTIFIER);
        params.put("service_command", ServiceCommand.Tokenization.getName());
        params.put("language", AmazonConstants.LANGUAGE);
        params.put("return_url", AmazonConstants.RETURN_URL);
        params.put("merchant_reference", merchant_reference);
        // AmazonUtils.addToMapIfNotNull(params, request);
        String signature = AmazonUtils.generateSignature(params);
        return signature;
    }

    private String getSignature(RefundRequest refundRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("access_code", AmazonConstants.ACCESS_CODE);
        params.put("merchant_identifier", AmazonConstants.MERCHANT_IDENTIFIER);
        params.put("command", ServiceCommand.REFUND.getName());
        params.put("language", AmazonConstants.LANGUAGE);
        params.put("amount", refundRequest.getAmount());
        params.put("currency", refundRequest.getCurrency());
        params.put("merchant_reference", refundRequest.getMerchant_reference());
        // AmazonUtils.addToMapIfNotNull(params, request);
        String signature = AmazonUtils.generateSignature(params);
        return signature;
    }

    private String removeSpecialCharacters(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[^a-zA-Z0-9/._\\-#:$ ]", "");
    }

}