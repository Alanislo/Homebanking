package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;


    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount, @RequestParam String description, @RequestParam String origin, @RequestParam String destination, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        //verifico si la cuenta de origen le pertence al ciente autenticado
        Account originAccount = client.getAccount().stream().filter(accountOrigen -> accountOrigen.getNumber().equals(origin)).collect(Collectors.toList()).get(0);
        Account destinationAccount = accountRepository.findByNumber(destination);
        if( description.isBlank() && origin.isBlank() || destination.isBlank()){
            return new ResponseEntity<>("Do not leave empty fields", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()) {
            return new ResponseEntity<>("Enter description", HttpStatus.FORBIDDEN);
        }
        if (origin.isBlank()){
            return new ResponseEntity<>("Choose source account", HttpStatus.FORBIDDEN);
        }
        if (destination == null){
            return new ResponseEntity<>("Destination account doesn't exists", HttpStatus.FORBIDDEN);
        }

        if (amount <= 0){
            return new ResponseEntity<>("Enter the amount", HttpStatus.FORBIDDEN );
        }
        if(origin.equals(destination)){
            return new ResponseEntity<>( "The accounts cannot be the same", HttpStatus.FORBIDDEN);
        }
        if(originAccount.getBalance() < amount){
            return new ResponseEntity<> ("Not enough funds", HttpStatus.FORBIDDEN);
        }else {
            originAccount.setBalance(originAccount.getBalance() - amount);
            destinationAccount.setBalance(destinationAccount.getBalance() + amount);
            Transaction debit1 =  new Transaction(LocalDateTime.now(),amount, TransactionType.DEBIT,description);
            Transaction credit1 = new Transaction(LocalDateTime.now(),amount, TransactionType.CREDIT,description);
            originAccount.addtransactionSet(debit1);
            destinationAccount.addtransactionSet(credit1);
            accountRepository.save(originAccount);
            accountRepository.save(destinationAccount);
            transactionRepository.save(debit1);
            transactionRepository.save(credit1);

        }
        return new ResponseEntity<> ("Successful transaction", HttpStatus.ACCEPTED);
    }
}
