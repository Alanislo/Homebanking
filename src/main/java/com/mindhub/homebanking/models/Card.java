package com.mindhub.homebanking.models;

import com.mindhub.homebanking.models.enums.CardType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
@Entity

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String cardHolder;
    private CardColor type;
    private CardType color;
    private String number;
    private short cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;
    public Card() {
    }
    public Card(String cardHolder, CardColor type, CardType color, String number, short cvv, LocalDate thruDate, LocalDate fromDate) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
    }

    public Card(String cardHolder, CardColor type, CardType color, LocalDate thruDate, LocalDate fromDate, Client client) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.client = client;
    }

    public long getId() {
        return id;
    }
    public String getCardHolder() {
        return cardHolder;
    }
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
    public CardColor getType() {
        return type;
    }
    public void setType(CardColor type) {
        this.type = type;
    }
    public CardType getColor() {
        return color;
    }
    public void setColor(CardType color) {
        this.color = color;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public short getCvv() {
        return cvv;
    }
    public void setCvv(short cvv) {
        this.cvv = cvv;
    }
    public LocalDate getThruDate() {
        return thruDate;
    }
    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }
    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardHolder='" + cardHolder + '\'' +
                ", type=" + type +
                ", color=" + color +
                ", number='" + number + '\'' +
                ", cvv=" + cvv +
                ", thruDate=" + thruDate +
                ", fromDate=" + fromDate +
                ", client=" + client +
                '}';
    }
}
