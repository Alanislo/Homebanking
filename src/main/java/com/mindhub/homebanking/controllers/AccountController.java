package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.AccountType;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
@RequestMapping("/api")
@RestController
public class AccountController {
    @Autowired
     private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    private String randomNumber(){
        String random;
        do {
            int number= (int)(Math.random()* 100 + 999 );
            random= "VIN-"+number;
        }while (accountService.findByNumber(random)!=null);
        return  random;
    }
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAllAccounts();
    }
    @GetMapping("clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName())).getAccountSet().stream().collect(toList());
    }
    @GetMapping("/clients/accounts/{id}")
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
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount(@RequestParam String type, Authentication authentication) {
        if( !type.equals("CHECKING") && !type.equals("SAVINGS") ){
            return new ResponseEntity<>("Select the type", HttpStatus.FORBIDDEN);
        }
        AccountType accountType =  AccountType.valueOf(type);
        if (clientService.findByEmail(authentication.getName()).getAccount().size()<=2) {
            String number = randomNumber();
            Account newAccount = new Account(number, LocalDate.now(), 0, true, accountType);
            clientService.findByEmail(authentication.getName()).addAccount(newAccount);
            accountService.save(newAccount);
        }
      else{
            return new ResponseEntity<>("Max accounts created", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/clients/current/accounts/disable")
    public ResponseEntity<Object> disableAccount(@RequestParam long id, Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        Boolean existAccount = client.getAccount().contains(account);
        Set<Transaction> transactionSet = account.getTransactionSet();
        Set<Account> accounts = client.getAccount();
        long accountActive = accounts.stream().filter(account1 -> account1.isActive()).count();
        if(account == null){
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(!existAccount){
            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }
        if(accountActive <= 1){
            return new ResponseEntity<>("You cannot delete the only account you have.", HttpStatus.FORBIDDEN);
        }
        if(account.getBalance() > 0){
            return new ResponseEntity<>("You cannot delete the account if you have money available", HttpStatus.FORBIDDEN);
        }
        transactionSet.forEach(transaction -> {
            transaction.setActive(false);
            transactionService.save(transaction);
        });
        account.setActive(false);
        accountService.save(account);
        return new ResponseEntity<>("The account has been deleted",HttpStatus.ACCEPTED);
    }
}
