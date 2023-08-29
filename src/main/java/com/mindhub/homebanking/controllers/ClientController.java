package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {
    @Autowired
    private ClientRepository clientrepository;
    @Autowired
    private AccountRepository accountrepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients(){
        return clientrepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientrepository.findById(id).map(ClientDTO::new).orElse(null);
    }
    @RequestMapping(path = "/api/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("The entered fields are not valid", HttpStatus.FORBIDDEN);

        }

        if (clientrepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }
        Client newClient= new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientrepository.save(newClient);
        String newNumber = randomNumber();
        Account newAccount = new Account(newNumber, LocalDate.now(),0.0);
        newClient.addAccount(newAccount);
        accountrepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    private String randomNumber(){
        String random;
        do {
            int number= (int)(Math.random()* 100 + 999 );
            random= "VIN-"+number;
        }while (accountrepository.findByNumber(random)!=null);
        return  random;
    }

    @RequestMapping("/api/clients/current")

    public ClientDTO getClient(Authentication authentication) {

        return new ClientDTO(clientrepository.findByEmail(authentication.getName()));

    }
}
