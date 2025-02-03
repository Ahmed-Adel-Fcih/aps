package com.qeema.aps.card.application.port.in;

import java.util.List;

import com.qeema.aps.card.domain.Card;

public interface ReadCardUseCase {
    public Card getCard(Integer id);

    public List<Card> getAllCards();
}
