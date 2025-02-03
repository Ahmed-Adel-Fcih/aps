package com.qeema.aps.customer.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.qeema.aps.customer.adapter.out.persistance.CustomerRepository;
import com.qeema.aps.customer.domain.Customer;

import jakarta.annotation.PostConstruct;

@Configuration
public class CustomerConfiguration {

    @Autowired
    private CustomerRepository customerRepository;

    @PostConstruct
    public void init() {
        Customer customer1 = new Customer("Admin", "admin@aps.com", "0123456789");

        customerRepository.save(customer1);
    }
}
