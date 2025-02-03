package com.qeema.aps.card.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeema.aps.card.adapter.out.persistance.CardRepository;
import com.qeema.aps.card.application.port.in.AddCardUseCase;
import com.qeema.aps.card.application.port.in.DeleteCardUseCase;
import com.qeema.aps.card.application.port.in.ReadCardUseCase;
import com.qeema.aps.card.application.port.out.DeleteCardPort;
import com.qeema.aps.card.application.port.out.SaveCardPort;
import com.qeema.aps.card.domain.Card;

@Service
public class CardService implements AddCardUseCase, ReadCardUseCase, DeleteCardUseCase {

    @Autowired
    SaveCardPort saveCardPort;
    @Autowired
    DeleteCardPort deleteCardPort;
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card addCard(Card card) {
        card.setIsActive(0);
        return saveCardPort.saveCard(card);
    }

    @Override
    public Card getCard(Integer id) {
        return cardRepository.findById(id).orElseThrow(() -> new RuntimeException("Card not found"));
    }

    @Override
    public List<Card> getAllCards() {
        Iterable<Card> allcards = cardRepository.findAllActiveCards();
        List<Card> cardList = new ArrayList<>();
        allcards.forEach(cardList::add);
        return cardList;
    }

    @Override
    public void deleteCard(Integer id) {
        deleteCardPort.deleteCard(id);
    }

    @Override
    public Card activateCard(Integer id) {
        Card card = cardRepository.findById(id).orElse(null);
        card.setIsActive(1);
        return saveCardPort.saveCard(card);
    }
}
