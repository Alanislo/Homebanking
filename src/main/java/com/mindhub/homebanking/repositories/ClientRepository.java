package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



@RepositoryRestResource
// A que clase de java esta haciendo referencia
public interface ClientRepository extends JpaRepository<Client,Long>{
    Client findByEmail(String email);
}
