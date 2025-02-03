package com.qeema.aps.card.adapter.in.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.qeema.aps.card.application.service.CardService;
import com.qeema.aps.card.domain.Card;

import java.util.List;

@RestController
@RequestMapping("api/v1/cards")
public class CardController {

    @Autowired
    CardService cardService;

    @GetMapping
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Integer id) {
        Card card = cardService.getCard(id);
        return ResponseEntity.ok(card);
    }

    @PostMapping
    public ResponseEntity<Card> createCard(@RequestHeader Integer customerId, @RequestBody Card card) {
        cardService.addCard(card);
        return ResponseEntity.ok(card);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Integer id) {
        cardService.deleteCard(id);
        return ResponseEntity.ok(null);
    }
}
