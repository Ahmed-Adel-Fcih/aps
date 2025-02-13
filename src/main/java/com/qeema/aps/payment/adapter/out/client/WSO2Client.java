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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
    private WebClient webClient;
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
        String accessToken = getAccessToken();
        try {
            return webClient.post()
                    .uri(TOKEN_API_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + accessToken)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData("grant_type", "password")
                            .with("username", oauthUsername)
                            .with("password", oauthPassword))
                    .retrieve()
                    .bodyToMono(AuthResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error response from token API: {}", e.getResponseBodyAsString(), e);
            throw new RuntimeException("Failed to obtain access token: " + e.getStatusCode());
        } catch (Exception e) {
            log.error("Error invoking token API", e);
            throw new RuntimeException("Failed to obtain access token", e);
        }
    }

    private String getAccessToken() {
        String tokenStr = oauthConsumerKey + ":" + oauthConsumerSecret;
        return Base64.getEncoder().encodeToString(tokenStr.getBytes());
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