package com.qeema.aps.card.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import com.qeema.aps.customer.domain.CustomerCard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@Entity
@Table(name = "card")
@NoArgsConstructor
@ToString
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonProperty("card_number")
    private String maskedCardNumber;
    @JsonProperty("expiry_date")
    private String expiryDate;
    @JsonProperty("card_holder")
    private String cardHolderName;
    private CardType cardType;
    private Integer isActive;

    @OneToMany(mappedBy = "card")
    @JsonIgnore
    @ToString.Exclude
    private Set<CustomerCard> customerCards;

    public Card(String maskedCardNumber, String expiryDate, String cardHolderName,
            CardType cardType, Integer isActive) {
        this.maskedCardNumber = maskedCardNumber;
        this.expiryDate = expiryDate;
        this.cardHolderName = cardHolderName;
        this.cardType = cardType;
        this.isActive = isActive;
    }
}
