package com.qeema.aps.payment.adapter.in.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.qeema.aps.customer.application.port.out.AddCustomerCardUseCase;
import com.qeema.aps.payment.domain.dto.TokenizationRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/amazon")
@Slf4j
public class AmazonController {
        @Autowired
        private AddCustomerCardUseCase addCustomerCardUseCase;

        @Value("${app.confirmation.url}")
        private String confirmationUrl;

        @PostMapping(consumes = "application/x-www-form-urlencoded")
        public ModelAndView tokenization(@RequestParam String response_code,
                        @RequestParam String card_number,
                        @RequestParam String card_holder_name,
                        @RequestParam String signature,
                        @RequestParam String merchant_identifier,
                        @RequestParam String expiry_date,
                        @RequestParam String access_code,
                        @RequestParam String language,
                        @RequestParam String service_command,
                        @RequestParam String response_message,
                        @RequestParam String merchant_reference,
                        @RequestParam String return_url,
                        @RequestParam(required = false) String token_name,
                        @RequestParam(required = false) String card_bin,
                        @RequestParam String status) {

                TokenizationRequest tokenizationRequest = new TokenizationRequest(response_code, card_number,
                                card_holder_name,
                                signature, merchant_identifier, expiry_date, access_code, language, service_command,
                                response_message,
                                merchant_reference, return_url, token_name, card_bin, status);
                if (token_name != null) {
                        addCustomerCardUseCase.addCustomerCard(tokenizationRequest,
                                        merchant_reference);
                }
                log.info(tokenizationRequest.toString());
                ModelAndView modelAndView = new ModelAndView("redirectPage");
                modelAndView.addObject("status",
                                status.equals("18") ? "Card Added Successfully"
                                                : "Error occured while adding card(" + response_message + ")");
                modelAndView.addObject("transactionId", merchant_reference);
                modelAndView.addObject("confirmationUrl", confirmationUrl);

                return modelAndView;
        }
}
