package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDTO{
    private long id;
    private LocalDateTime dateTime;
    private double amount;
    private TransactionType type;
    private Account account;
    private String description;
    public TransactionDTO() {
    }
    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.dateTime = transaction.getDateTime();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.description= transaction.getDescription();

    }
    public long getId() {
        return id;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public double getAmount() {
        return amount;
    }
    public TransactionType getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
}
