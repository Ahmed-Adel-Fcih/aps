package com.qeema.aps.customer.adapter.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qeema.aps.card.application.port.in.AddCardUseCase;
import com.qeema.aps.card.domain.Card;
import com.qeema.aps.customer.adapter.out.persistance.CustomerCardRepository;
import com.qeema.aps.customer.adapter.out.persistance.CustomerRepository;
import com.qeema.aps.customer.application.port.out.AddCustomerCardUseCase;
import com.qeema.aps.customer.domain.Customer;
import com.qeema.aps.customer.domain.CustomerCard;
import com.qeema.aps.payment.application.port.in.ReadPaymentUseCase;
import com.qeema.aps.payment.domain.Payment;
import com.qeema.aps.payment.domain.dto.TokenizationRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerCardAdapter implements AddCustomerCardUseCase {
    @Autowired
    CustomerCardRepository customerCardRepository;

    @Autowired
    private ReadPaymentUseCase readPaymentUseCase;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private AddCardUseCase addCardUseCase;

    @Override
    public void addCustomerCard(TokenizationRequest tokenizationRequest, String merchant_reference) {
        Payment payment = readPaymentUseCase.getPaymentByMerchantReference(merchant_reference);
        Card card = payment.getCard();
        addCardUseCase.activateCard(card.getId());
        Customer customer = payment.getCustomer();
        CustomerCard customerCard = new CustomerCard();
        customerCard.setCard(card);
        customerCard.setCustomer(customer);
        customerCard.setTokenName(tokenizationRequest.getToken_name());
        customerCard.setCardBin(tokenizationRequest.getCard_bin());
        customerCardRepository.save(customerCard);
        log.info(card.toString());
        log.info(customer.toString());
        log.info(customerCard.toString());

    }

}
