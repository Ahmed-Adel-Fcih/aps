package com.qeema.aps.customer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.qeema.aps.card.domain.Card;

@Data
@Entity
@Table(name = "customerCard")
@ToString
@NoArgsConstructor
public class CustomerCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = true, updatable = true)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "card_id", insertable = true, updatable = true)
    private Card card;
    private String tokenName;
    private String cardBin;

    public CustomerCard(Customer customer, Card card, String tokenName) {
        this.customer = customer;
        this.card = card;
        this.tokenName = tokenName;
    }
}
