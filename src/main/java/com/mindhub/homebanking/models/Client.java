package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity // hace la de la clase una entidad para jpa

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // genera el id de manera automatica
    @GenericGenerator(name = "native", strategy = "native")
    private long id;// identificador en la base de datos
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "client", fetch=FetchType.EAGER)
    private Set<Account> accountSet= new HashSet<>();
    public Client() {
    }

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

}
