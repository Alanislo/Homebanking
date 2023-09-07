package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
@RequestMapping("/api")
@RestController
public class AccountController {
    @Autowired
     private AccountService accountService;
    @Autowired
    private ClientService clientService;

    private String randomNumber(){
        String random;
        do {
            int number= (int)(Math.random()* 100 + 999 );
            random= "VIN-"+number;
        }while (accountService.findByNumber(random)!=null);
        return  random;
    }
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAllAccounts();
    }
    @RequestMapping("clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName())).getAccountSet().stream().collect(toList());
    }
    @RequestMapping("/clients/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        if (account == null){
            return new ResponseEntity<>("Account null", HttpStatus.FORBIDDEN);
        }
        if (client.getAccount().contains(account)) {
//
            return new ResponseEntity<>(new AccountDTO(account), HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>("Denegated access", HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication) {
        if (clientService.findByEmail(authentication.getName()).getAccount().size()<=2) {
            String number = randomNumber();
            Account newAccount = new Account(number, LocalDate.now(), 0);
            clientService.findByEmail(authentication.getName()).addAccount(newAccount);
            accountService.save(newAccount);
        }else{
            return new ResponseEntity<>("Max accounts created", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
