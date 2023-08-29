package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {
    @Autowired
     private AccountRepository accountrepository;

    @Autowired
    private ClientRepository clientrepository;
    private String randomNumber(){
        String random;
        do {
            int number= (int)(Math.random()* 100 + 999 );
            random= "VIN-"+number;
        }while (accountrepository.findByNumber(random)!=null);
        return  random;
    }
    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountrepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @RequestMapping("/api/clients/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {
        Client client = clientrepository.findByEmail(authentication.getName());
        Account account = accountrepository.findById(id).orElse(null);
        if (client.getId() == account.getClient().getId()) {
            return new ResponseEntity<>(new AccountDTO(account), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Denegated access", HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> newAccount(Authentication authentication) {

        if (clientrepository.findByEmail(authentication.getName()).getAccount().size()<=2) {
            String number = randomNumber();
            Account newAccount = new Account(number, LocalDate.now(), 0);
            clientrepository.findByEmail(authentication.getName()).addAccount(newAccount);
            accountrepository.save(newAccount);
        }else{
            return new ResponseEntity<>("Max accounts created", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
