package com.qeema.aps.customer.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeema.aps.card.adapter.out.persistance.CardRepository;
import com.qeema.aps.card.domain.Card;
import com.qeema.aps.customer.adapter.out.persistance.CustomerCardRepository;
import com.qeema.aps.customer.adapter.out.persistance.CustomerRepository;
import com.qeema.aps.customer.application.port.in.ReadCustomerCardUseCase;
import com.qeema.aps.customer.application.port.in.ReadCustomerUseCase;
import com.qeema.aps.customer.domain.Customer;
import com.qeema.aps.customer.domain.CustomerCard;

@Service
public class CustomerService implements ReadCustomerUseCase, ReadCustomerCardUseCase {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerCardRepository customerCardRepository;

    @Autowired
    CardRepository cardRepository;

    @Override
    public List<Card> getAllCustomerCards(Integer customerId) {

        Iterable<CustomerCard> allcustomerCards = customerCardRepository.findAll();
        List<Card> cardList = new ArrayList<>();
        allcustomerCards.forEach(customerCard -> {
            cardList.add(cardRepository.findById(customerCard.getCard().getId()).orElse(null));
        });
        return cardList;
    }

    @Override
    public Customer getCustomer(Integer id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public List<Customer> getAllCustomers() {
        Iterable<Customer> allcustomers = customerRepository.findAll();
        List<Customer> customerList = new ArrayList<>();
        allcustomers.forEach(customerList::add);
        return customerList;
    }

    @Override
    public String getTokenByCustomerIdAndCardId(Integer customerId, Integer cardId) {
        CustomerCard customerCard = customerCardRepository.findByCustomerIdAndCardId(customerId, cardId)
                .orElseThrow(() -> new RuntimeException("Customer card not found"));
        return customerCard.getTokenName();
    }

}
