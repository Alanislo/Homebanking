package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class ClientServiceImplementation implements ClientService {
@Autowired
private ClientRepository clientRepository;
    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @Override
    public ClientDTO getClientDTO(long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }
    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }


}
