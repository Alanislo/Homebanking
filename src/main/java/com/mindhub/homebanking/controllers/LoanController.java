package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    LoanService loanService;
    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;
    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    TransactionService transactionService;
    @GetMapping("/loans")
    public List<LoanDTO> getLoansDTO(){
        return loanService.getLoans();
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity <Object> newLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        Loan loan = loanService.findById(loanApplicationDTO.getId());
        double amount = loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();
        Account account = accountService.findByNumber(loanApplicationDTO.getDestinationAccount());
        if(amount == 0 || payments == null){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }
        if(amount < 0 ){
            return new ResponseEntity<>("The amount cannot be negative", HttpStatus.FORBIDDEN);
        }
        if (loan == null){
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(clientLoanService.existsByClientAndLoan(client, loan)){
            return new ResponseEntity<>("The loan already exists", HttpStatus.FORBIDDEN);
        }
        if(amount > loan.getMaxAmount()){
            return new ResponseEntity<>("The amount cannot exceed the maximum amount", HttpStatus.FORBIDDEN);
        }
        if(!loan.getPayments().contains(payments)){
            return new ResponseEntity<>("The required payment installments are not available", HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if(!client.getAccount().contains(account)){
            return new ResponseEntity<>("The destination account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }
        account.setBalance(account.getBalance() + amount);
        ClientLoan clientLoan = new ClientLoan(loan.getName(), amount *1.2 , payments);
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        clientLoanService.save(clientLoan);
        Transaction transaction = new Transaction(LocalDateTime.now(), amount, TransactionType.CREDIT, "Loan approved: "+ loan.getName(), account.getBalance(), true);
        transactionService.save(transaction);
        account.addtransactionSet(transaction);
        accountService.save(account);
        return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);
    }
    @PostMapping("/loans/create")
    public ResponseEntity<Object> newTypeLoan(@RequestBody LoanDTO loanDTO,Authentication authentication){
        Loan loan = loanService.findByName(loanDTO.getName());
        double amount = loanDTO.getMaxAmount();
        List<Integer> payments = loanDTO.getPayments();
        if(amount == 0 || payments == null){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }
        if(amount < 0 ){
            return new ResponseEntity<>("The amount cannot be negative", HttpStatus.FORBIDDEN);
        }
        Loan newLoan = new Loan(loanDTO.getName(), amount, payments);
        loanService.save(newLoan);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
