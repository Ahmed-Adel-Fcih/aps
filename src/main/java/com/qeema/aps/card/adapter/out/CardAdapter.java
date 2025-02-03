package com.qeema.aps.card.adapter.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeema.aps.card.adapter.out.persistance.CardRepository;
import com.qeema.aps.card.application.port.out.DeleteCardPort;
import com.qeema.aps.card.application.port.out.SaveCardPort;
import com.qeema.aps.card.domain.Card;

@Service
public class CardAdapter implements SaveCardPort, DeleteCardPort {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);

    }

    @Override
    public void deleteCard(Integer id) {
        cardRepository.deleteById(id);
    }

}
