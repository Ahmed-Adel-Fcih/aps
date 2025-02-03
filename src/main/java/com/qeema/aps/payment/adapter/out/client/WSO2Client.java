package com.qeema.aps.payment.adapter.out.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.qeema.aps.common.utils.AmazonConstants;
import com.qeema.aps.common.utils.AmazonUtils;
import com.qeema.aps.common.utils.ServiceCommand;
import com.qeema.aps.customer.application.port.in.ReadCustomerCardUseCase;
import com.qeema.aps.payment.application.port.out.AmazonPaymentServiceClient;
import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.dto.PurchaseRequest;
import com.qeema.aps.payment.domain.dto.PurchaseResponse;
import com.qeema.aps.payment.domain.dto.RefundRequest;
import com.qeema.aps.payment.domain.dto.RefundResponse;

import lombok.extern.slf4j.Slf4j;

@Service("WSO2Client")
@Slf4j
public class WSO2Client implements AmazonPaymentServiceClient {

    private static final String PURCHASE_API_URL = "https://localhost:8243/purchase/1.0.0";
    private static final String REFUND_API_URL = "https://wso2.example.com/refund";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ReadCustomerCardUseCase readCustomerCardUseCase;

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Override
    public PurchaseResponse callPurchaseAPI(Payment payment) {
        // Call the purchase API
        PurchaseResponse response = new PurchaseResponse();
        PurchaseRequest purchaseRequest = new PurchaseRequest(payment);
        String tokenName = readCustomerCardUseCase.getTokenByCustomerIdAndCardId(payment.getCustomer().getId(),
                payment.getCard().getId());
        purchaseRequest.setToken_name(tokenName);
        String signature = getSignature(purchaseRequest, payment.getMerchant_reference(), ServiceCommand.PURCHASE);
        purchaseRequest.setSignature(signature);
        payment.setRequest(purchaseRequest.toString());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Internal-API-Key", internalApiKey);
            HttpEntity<PurchaseRequest> entity = new HttpEntity<>(purchaseRequest, headers);
            response = restTemplate.postForObject(PURCHASE_API_URL, entity, PurchaseResponse.class);
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