package com.qeema.aps.customer.application.port.in;

import java.util.List;

import com.qeema.aps.customer.domain.Customer;

public interface ReadCustomerUseCase {

    public Customer getCustomer(Integer id);

    public List<Customer> getAllCustomers();
}
