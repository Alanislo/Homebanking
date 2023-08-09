package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;

import com.mindhub.homebanking.repositories.Clientrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {
    @Autowired
    private Clientrepository clientrepository;
    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients(){

        return clientrepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientrepository.findById(id).map(ClientDTO::new).orElse(null);
    }
}
