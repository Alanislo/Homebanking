package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.enums.AccountType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
@RequestMapping("/api")
@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClients();
    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if(firstName.isBlank() && email.isBlank() && lastName.isBlank() || password.isBlank()){
            return new ResponseEntity<>("Do not leave empty fields", HttpStatus.FORBIDDEN);
        }
        if (firstName.isBlank()) {
            return new ResponseEntity<>("Enter your First Name", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()){
            return new ResponseEntity<>("Enter your email", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()){
            return new ResponseEntity<>("Enter your Last Name", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()){
            return new ResponseEntity<>("Enter a password", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient= new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.save(newClient);
        String newNumber = randomNumber();
        Account newAccount = new Account(newNumber, LocalDate.now(),0.0, true, AccountType.CHECKING);
        newClient.addAccount(newAccount);
        accountService.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    private String randomNumber(){
        String random;
        do {
            int number= (int)(Math.random()* 100 + 999 );
            random= "VIN-"+number;
        }while (accountService.findByNumber(random)!=null);
        return  random;
    }
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }
}
