package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class ClientDTO{
    private long id;// identificador en la base de datos
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accountSet= new HashSet<>();

    private Set<ClientLoanDTO> clientLoans = new HashSet<>();

    public ClientDTO() {
    }

    public ClientDTO(long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }
    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accountSet= client.getAccount().stream().map(account -> new AccountDTO(account)).collect(toSet());

        this.clientLoans = client.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toSet());

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
}
