package com.qeema.aps.customer.adapter.out.persistance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.qeema.aps.customer.domain.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
