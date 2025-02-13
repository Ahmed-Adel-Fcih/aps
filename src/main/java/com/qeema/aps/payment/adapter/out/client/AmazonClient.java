package com.qeema.aps.payment.adapter.out.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
    private RestTemplate restTemplate;

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
            response = restTemplate.postForObject(PURCHASE_API_URL, purchaseRequest,
                    PurchaseResponse.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    @Override
    public RefundResponse callRefundAPI(RefundRequest refundRequest) {
        RefundResponse response = new RefundResponse();
        try {
            response = restTemplate.postForObject(REFUND_API_URL, refundRequest, RefundResponse.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    private String getSignature(PurchaseRequest request, String merchant_reference, ServiceCommand command) {
        Map<String, Object> params = new HashMap<>();
        params.put("access_code", AmazonConstants.ACCESS_CODE);
        params.put("merchant_identifier", AmazonConstants.MERCHANT_IDENTIFIER);
        params.put("command", command.getName());
        params.put("language", AmazonConstants.LANGUAGE);
        // params.put("return_url", AmazonConstants.RETURN_URL_PURCHASE);
        params.put("merchant_reference", merchant_reference);
        Set<String> excludedFields = new HashSet<>();
        excludedFields.add("access_code");
        excludedFields.add("merchant_identifier");
        excludedFields.add("command");
        excludedFields.add("language");
        // excludedFields.add("return_url");
        excludedFields.add("merchant_reference");
        excludedFields.add("signature");
        AmazonUtils.addToMapIfNotNull(params, request, excludedFields);
        String signature = AmazonUtils.generateSignature(params);
        return signature;
    }

}