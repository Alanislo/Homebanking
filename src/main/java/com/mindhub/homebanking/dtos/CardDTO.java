package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;

import java.time.LocalDate;


public class CardDTO {
    private long id;
    private String cardHolder;
    private CardColor color;
    private CardType type;
    private String number;
    private short cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;

    public CardDTO() {
    }

    public CardDTO(Card card) {
       this.id = card.getId();
       this.cardHolder = card.getCardHolder();
       this.type = card.getType();
       this.color = card.getColor();
       this.number = card.getNumber();
       this.cvv = card.getCvv();
       this.thruDate = card.getThruDate();
       this.fromDate = card.getFromDate();

    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardColor getColor() {
        return color;
    }

    public CardType getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public short getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
}
