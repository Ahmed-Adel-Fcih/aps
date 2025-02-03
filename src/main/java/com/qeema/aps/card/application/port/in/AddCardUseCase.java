package com.qeema.aps.card.application.port.in;

import com.qeema.aps.card.domain.Card;

public interface AddCardUseCase {

    public Card addCard(Card card);

    public Card activateCard(Integer id);

}
