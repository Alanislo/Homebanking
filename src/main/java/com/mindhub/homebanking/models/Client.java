package com.mindhub.homebanking.models;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "client", fetch=FetchType.EAGER)
    private Set<Account> accountSet= new HashSet<>();


    public Client() {
    }

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Client( String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccount() {
        return accountSet;
    }

    public void setAccount(Set<Account> account) {
        this.accountSet = account;
    }
    public void addAccount(Account account) {
        account.setClient(this);
        accountSet.add(account);
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    @JsonIgnore
    public Set<Loan> getLoans() {
        return clientLoans.stream().map(ClientLoan::getLoan).collect(toSet());
    }
    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);

    }
    }

