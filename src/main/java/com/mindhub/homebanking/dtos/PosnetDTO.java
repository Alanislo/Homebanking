package com.mindhub.homebanking.dtos;

public class PosnetDTO {
    private Long id;
    private String number;
    private int cvv;
    private double amount;
    private String description;
    private String account;
    public PosnetDTO(){}
    public PosnetDTO(Long id, String number, int cvv, double amount, String description, String account) {
        this.id = id;
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getAccount() {
        return account;
    }
}