package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

import java.util.Set;


import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long id;// identificador en la base de datos
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accountSet;
    private Set<ClientLoanDTO> clientLoans;
    private Set<CardDTO> cards;

    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accountSet = client.getAccount().stream().map(account -> new AccountDTO(account)).collect(toSet());

        this.clientLoans = client.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toSet());

        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(toSet());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccountSet() {
        return accountSet;
    }

    public Set<ClientLoanDTO> getClientLoans() {
        return clientLoans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
