package com.qeema.aps.payment.adapter.out.client;

import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.qeema.aps.common.utils.AmazonConstants;
import com.qeema.aps.common.utils.AmazonUtils;
import com.qeema.aps.common.utils.ServiceCommand;
import com.qeema.aps.common.utils.WSO2Utils;
import com.qeema.aps.common.utils.WSO2Utils.AuthResponse;
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
    private static final String REFUND_API_URL = "https://localhost:8243/refund/1.0.0";
    private static final String TOKEN_API_URL = "https://localhost:9443/oauth2/token";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wso2.internal.key}")
    private String internalApiKey;

    @Value("${wso2.oauth2.consumerKey}")
    private String oauthConsumerKey;
    @Value("${wso2.oauth2.consumerSecret}")
    private String oauthConsumerSecret;
    @Value("${wso2.oauth2.username}")
    private String oauthUsername;
    @Value("${wso2.oauth2.password}")
    private String oauthPassword;

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
            HttpHeaders headers = getAuthHeader("Oauth2");
            HttpEntity<PurchaseRequest> entity = new HttpEntity<>(purchaseRequest, headers);
            response = restTemplate.postForObject(PURCHASE_API_URL, entity, PurchaseResponse.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    private HttpHeaders getAuthHeader(String authType) {
        HttpHeaders headers = new HttpHeaders();
        if (Objects.equals("Basic", authType)) {
            headers.set("Internal-API-Key", internalApiKey);
        } else if (Objects.equals("Oauth2", authType)) {
            WSO2Utils.AuthResponse response = invokeWSO2Oauth2Request();
            headers.set("Authorization", "Bearer " + response.getAccess_token());
        }
        return headers;
    }

    private AuthResponse invokeWSO2Oauth2Request() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + getAccessToken());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", oauthUsername);
        body.add("password", oauthPassword);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<AuthResponse> response = restTemplate.exchange(TOKEN_API_URL, HttpMethod.POST, requestEntity,
                AuthResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to obtain access token: " + response.getStatusCode());
        }
    }

    private String getAccessToken() {
        String accessToken;
        String tokenStr = oauthConsumerKey + ":" + oauthConsumerSecret;
        accessToken = Base64.getEncoder().encodeToString(tokenStr.getBytes());
        return accessToken;
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