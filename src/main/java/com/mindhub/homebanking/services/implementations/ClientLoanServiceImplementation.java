package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplementation implements ClientLoanService {
    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public ClientLoan save(ClientLoan clientLoan) {
        return clientLoanRepository.save(clientLoan);
    }

    @Override
    public boolean existsByClientAndLoan(Client client, Loan loan) {
        return clientLoanRepository.existsByClientAndLoan(client, loan);
    }

    @Override
    public ClientLoan findById(Long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }

}
