package com.qeema.aps.customer.adapter.out.persistance;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.qeema.aps.customer.domain.CustomerCard;

@Repository
public interface CustomerCardRepository extends CrudRepository<CustomerCard, Integer> {

    @Query("SELECT cc FROM CustomerCard cc WHERE cc.customer.id = ?1 AND cc.card.id = ?2")
    Optional<CustomerCard> findByCustomerIdAndCardId(Integer customerId, Integer cardId);

}
