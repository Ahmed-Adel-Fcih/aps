package com.qeema.aps.card.adapter.out.persistance;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.qeema.aps.card.domain.Card;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {

    @Query("SELECT c FROM Card c WHERE c.isActive = 1")
    Iterable<Card> findAllActiveCards();

}
