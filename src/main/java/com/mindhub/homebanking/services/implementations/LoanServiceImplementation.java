package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplementation implements LoanService {
    @Autowired
    LoanRepository loanRepository;
    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan findByName(String name) {
        return loanRepository.findByName(name);
    }

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }
}
