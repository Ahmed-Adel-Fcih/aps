package com.qeema.aps.payment.adapter.out.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.qeema.aps.common.utils.AmazonConstants;
import com.qeema.aps.common.utils.AmazonUtils;
import com.qeema.aps.common.utils.ServiceCommand;
import com.qeema.aps.payment.application.port.out.AmazonPaymentServiceClient;
import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.dto.PurchaseRequest;
import com.qeema.aps.payment.domain.dto.PurchaseResponse;
import com.qeema.aps.payment.domain.dto.RefundRequest;
import com.qeema.aps.payment.domain.dto.RefundResponse;

import lombok.extern.slf4j.Slf4j;

@Service("AmazonClient")
@Slf4j
public class AmazonClient implements AmazonPaymentServiceClient {

    private static final String PURCHASE_API_URL = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi";
    private static final String REFUND_API_URL = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi";

    @Autowired
    private WebClient webClient;

    @Override
    public PurchaseResponse callPurchaseAPI(Payment payment, String token) {
        // Call the purchase API
        PurchaseResponse response = new PurchaseResponse();
        PurchaseRequest purchaseRequest = new PurchaseRequest(payment);
        purchaseRequest.setToken_name(token);
        String signature = getSignature(purchaseRequest, payment.getMerchant_reference(), ServiceCommand.PURCHASE);
        purchaseRequest.setSignature(signature);
        payment.setRequest(purchaseRequest.toString());
        try {
            response = webClient.post()
                    .uri(PURCHASE_API_URL)
                    .body(BodyInserters.fromValue(purchaseRequest))
                    .retrieve()
                    .bodyToMono(PurchaseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error response from purchase API: {}", e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error invoking purchase API", e);
        }
        return response;
    }

    @Override
    public RefundResponse callRefundAPI(RefundRequest refundRequest) {
        RefundResponse response = new RefundResponse();
        try {
            response = webClient.post()
                    .uri(REFUND_API_URL)
                    .body(BodyInserters.fromValue(refundRequest))
                    .retrieve()
                    .bodyToMono(RefundResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error response from refund API: {}", e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error invoking refund API", e);
        }
        return response;
    }

    private String getSignature(PurchaseRequest request, String merchant_reference, ServiceCommand command) {
        Map<String, Object> params = new HashMap<>();
        params.put("access_code", AmazonConstants.ACCESS_CODE);
        params.put("merchant_identifier", AmazonConstants.MERCHANT_IDENTIFIER);
        params.put("command", command.getName());
        params.put("language", AmazonConstants.LANGUAGE);
        params.put("merchant_reference", merchant_reference);
        Set<String> excludedFields = new HashSet<>();
        excludedFields.add("access_code");
        excludedFields.add("merchant_identifier");
        excludedFields.add("command");
        excludedFields.add("language");
        excludedFields.add("merchant_reference");
        excludedFields.add("signature");
        AmazonUtils.addToMapIfNotNull(params, request, excludedFields);
        return AmazonUtils.generateSignature(params);
    }
}