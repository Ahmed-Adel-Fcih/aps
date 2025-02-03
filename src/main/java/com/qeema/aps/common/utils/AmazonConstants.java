package com.qeema.aps.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmazonConstants {
    public static final String LANGUAGE = "en";
    public static String RETURN_URL_PURCHASE = null;
    public static String ACCESS_CODE;
    public static String MERCHANT_IDENTIFIER;
    public static String SHA_REQUEST_PHRASE;
    public static String SHA_RESPONSE_PHRASE;
    public static String MERCHANT_NAME;
    public static String RETURN_URL;

    @Value("${payment.service.access_code}")
    public void setAccessCode(String accessCode) {
        AmazonConstants.ACCESS_CODE = accessCode;
    }

    @Value("${payment.service.merchant_identifier}")
    public void setMerchantIdentifier(String merchantIdentifier) {
        AmazonConstants.MERCHANT_IDENTIFIER = merchantIdentifier;
    }

    @Value("${payment.service.sha_request_phrase}")
    public void setShaRequestPhrase(String shaRequestPhrase) {
        AmazonConstants.SHA_REQUEST_PHRASE = shaRequestPhrase;
    }

    @Value("${payment.service.sha_response_phrase}")
    public void setShaResponsePhrase(String shaResponsePhrase) {
        AmazonConstants.SHA_RESPONSE_PHRASE = shaResponsePhrase;
    }

    @Value("${payment.service.merchant_name}")
    public void setMerchantName(String merchantName) {
        AmazonConstants.MERCHANT_NAME = merchantName;
    }

    @Value("${payment.service.tokenization.return_url}")
    public void setReturnUrl(String returnUrl) {
        AmazonConstants.RETURN_URL = returnUrl;
    }

    @Value("${payment.service.purchase.return_url}")
    public void setReturnUrlPurchase(String returnUrl) {
        AmazonConstants.RETURN_URL_PURCHASE = returnUrl;
    }
}
