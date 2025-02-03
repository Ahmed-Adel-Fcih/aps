package com.qeema.aps.customer.application.port.in;

import java.util.List;

import com.qeema.aps.card.domain.Card;

public interface ReadCustomerCardUseCase {

    public List<Card> getAllCustomerCards(Integer customerId);

    public String getTokenByCustomerIdAndCardId(Integer customerId, Integer cardId);

}
