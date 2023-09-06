package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoansDTO(){
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity <Object> newLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplicationDTO.getId());
        double amount = loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();
        Account account = accountRepository.findByNumber(loanApplicationDTO.getDestinationAccount());
        if(amount == 0 || payments == null){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }
        if(amount < 0 ){
            return new ResponseEntity<>("El monto no puede ser negativo", HttpStatus.FORBIDDEN);
        }
        if (loan == null){
            return new ResponseEntity<>("Loans no existe", HttpStatus.FORBIDDEN);
        }
        if(clientLoanRepository.existsByClientAndLoan(client, loan)){
            return new ResponseEntity<>("El loan ya existe", HttpStatus.FORBIDDEN);
        }
        if(amount > loan.getMaxAmount()){
            return new ResponseEntity<>("El monto no puede superar el monto maximo", HttpStatus.FORBIDDEN);
        }
        if(!loan.getPayments().contains(payments)){
            return new ResponseEntity<>("Las cuotas no estan disponibles", HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("La cuenta destino no existe", HttpStatus.FORBIDDEN);
        }
        if(!client.getAccount().contains(account)){
            return new ResponseEntity<>("La cuenta destino no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
        }
        account.setBalance(account.getBalance() + amount);
        ClientLoan clientLoan = new ClientLoan(loan.getName(), amount *1.2, payments);
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        clientLoanRepository.save(clientLoan);
        Transaction transaction = new Transaction(LocalDateTime.now(), amount, TransactionType.CREDIT, "Loan approved"+ loan.getName());
        transactionRepository.save(transaction);
        account.addtransactionSet(transaction);
        accountRepository.save(account);
        return new ResponseEntity<>("Loan aprobado", HttpStatus.CREATED);
    }
}
