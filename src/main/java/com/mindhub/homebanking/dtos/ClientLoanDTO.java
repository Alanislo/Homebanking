package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;


import java.util.HashSet;
import java.util.Set;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String name;
    private double amount;
    private Integer payments;
    private Set<ClientDTO> clientSet;
    public ClientLoanDTO(ClientLoan clientloan) {
        this.id = clientloan.getId();
        this.loanId = clientloan.getLoan().getId();
        this.name = clientloan.getName();
        this.amount = clientloan.getAmount();
        this.payments = clientloan.getPayments();
        this.clientSet = new HashSet<>();
    }
    public long getId() {
        return id;
    }
    public long getLoanId() {
        return loanId;
    }
    public String getName() {
        return name;
    }
    public double getAmount() {
        return amount;
    }
    public Integer getPayments() {
        return payments;
    }
    public Set<ClientDTO> getClientSet() {
        return clientSet;
    }
}
