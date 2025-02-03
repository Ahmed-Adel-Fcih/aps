package com.qeema.aps.card.application.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class CardConfigurations {

    @PostConstruct
    public void init() {
        /*
         * Card card1 = new Card();
         * Card card2 = new Card();
         * // Add more predefined cards as needed
         * 
         * cardRepository.saveAll(Arrays.asList(card1, card2));
         */
    }
}
