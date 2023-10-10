package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.PosnetDTO;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;


@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api")

public class PosnetController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions/cards")
    public ResponseEntity<Object> createCardsTransaction(@RequestBody PosnetDTO posnetDTO) {
        Card card = cardService.findCardByNumber(posnetDTO.getNumber());
        if(card == null){
            return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
        }
        if(!card.getActive()){
            return new ResponseEntity<>("Card not active", HttpStatus.FORBIDDEN);
        }
        if(card.getFromDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))){
            return new ResponseEntity<>("Card is not active", HttpStatus.FORBIDDEN);
        }
        if(card.getCvv() != posnetDTO.getCvv()){
            return new ResponseEntity<>("CVV does not match", HttpStatus.FORBIDDEN);
        }

        Client client = card.getClient();
        Account account = client.getAccount().stream().filter(account1 -> account1.getBalance() >=posnetDTO.getAmount()).findFirst().orElse(null);

        if(account == null){
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        if(posnetDTO.getAmount() <= 0){
            return new ResponseEntity<>("Please enter an amount greater than 0", HttpStatus.FORBIDDEN);
        }

        if(posnetDTO.getDescription().isBlank()){
            return new ResponseEntity<>("Please enter a description", HttpStatus.FORBIDDEN);
        }

        if(posnetDTO.getDescription().length() > 15){
            return new ResponseEntity<>("Description can't be longer than 15 characters", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(LocalDateTime.now(), posnetDTO.getAmount() * -1, TransactionType.DEBIT, posnetDTO.getDescription(), account.getBalance() - posnetDTO.getAmount(), true);
        account.addtransactionSet(transaction);
        transactionService.save(transaction);
        account.setBalance(account.getBalance() - posnetDTO.getAmount());
        accountService.save(account);
        return new ResponseEntity<>("Transaction created", HttpStatus.CREATED);
    }
}
