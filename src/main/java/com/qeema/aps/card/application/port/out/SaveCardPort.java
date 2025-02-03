package com.qeema.aps.card.application.port.out;

import com.qeema.aps.card.domain.Card;

public interface SaveCardPort {

    public Card saveCard(Card card);
}
