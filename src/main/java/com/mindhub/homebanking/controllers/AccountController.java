package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.Accountrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {
    @Autowired
     private Accountrepository accountrepository;
    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountrepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountrepository.findById(id).map(AccountDTO::new).orElse(null);
    }
}
