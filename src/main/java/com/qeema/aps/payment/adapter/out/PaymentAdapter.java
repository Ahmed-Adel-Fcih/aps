package com.qeema.aps.payment.adapter.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.qeema.aps.card.adapter.out.persistance.CardRepository;
import com.qeema.aps.card.domain.Card;
import com.qeema.aps.common.utils.ServiceCommand;
import com.qeema.aps.customer.adapter.out.persistance.CustomerRepository;
import com.qeema.aps.customer.domain.Customer;
import com.qeema.aps.payment.adapter.out.persistance.PaymentRepository;
import com.qeema.aps.payment.application.port.out.AmazonPaymentServiceClient;
import com.qeema.aps.payment.application.port.out.FulfillPaymentPort;
import com.qeema.aps.payment.application.port.out.SavePaymentPort;
import com.qeema.aps.payment.application.service.PaymentStatus;
import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.dto.PreparePaymentRequestDto;
import com.qeema.aps.payment.domain.dto.PurchaseResponse;
import com.qeema.aps.payment.domain.dto.RefundRequest;
import com.qeema.aps.payment.domain.dto.RefundResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentAdapter implements FulfillPaymentPort, SavePaymentPort {

    @Autowired
    @Qualifier("AmazonClient")
    AmazonPaymentServiceClient amazonPaymentServiceClient;

    @Autowired
    @Qualifier("WSO2Client")
    AmazonPaymentServiceClient amazonPaymentServiceClient_WSO2;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestTemplate restTemplate;

    private static final String REFUND_API_URL = "https://api.example.com/refund";

    @Override
    public Payment fulfillPayment(Payment payment) {
        PurchaseResponse response = amazonPaymentServiceClient_WSO2.callPurchaseAPI(payment);
        payment.setPaymentStatus(PaymentStatus.PURCHASED.getName());
        payment.setResponse(response.toString());
        payment.setPaymentOption(response.getPayment_option());
        payment.setCustomer_ip(response.getCustomer_ip());
        payment.setFort_id(response.getFort_id());
        payment.setResponse_message(response.getResponse_message());
        payment.setAuthorization_code(response.getAuthorization_code());
        log.info("===============================================================");
        log.info(payment.toString());
        log.info("===============================================================");
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public void savePayment(Payment payment) {
        throw new UnsupportedOperationException("Unimplemented method 'savePayment'");
    }

    @Override
    public void saveTokenizationRequest(PreparePaymentRequestDto request, String merchant_reference,
            Integer customerId) {

        Payment payment = new Payment();
        Card card = cardRepository.findById(request.getCardId()).orElse(null);
        payment.setCard(card);
        Customer customer = customerRepository.findById(customerId).orElse(null);
        payment.setCustomer(customer);
        payment.setServiceCommand(ServiceCommand.Tokenization.getName());
        payment.setMerchant_reference(merchant_reference);
        log.info(payment.toString());
        paymentRepository.save(payment);
    }

    @Override
    public RefundResponse callRefundAPI(RefundRequest refundRequest) {
        return amazonPaymentServiceClient.callRefundAPI(refundRequest);
    }

}