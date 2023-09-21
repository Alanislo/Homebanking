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

import static com.mindhub.homebanking.utils.LoanUtils.calcularIntereses;

@RestController
@RequestMapping("/api")
public class LoanController {
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
        Double interest = calcularIntereses(loan, loanApplicationDTO);

        double amountLoan = ((interest/100) * loanApplicationDTO.getAmount()) + loanApplicationDTO.getAmount();
        account.setBalance(account.getBalance() + amount);
        ClientLoan clientLoan = new ClientLoan(loan.getName(), amountLoan , loanApplicationDTO.getPayments(), true);
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

        Loan newLoan = new Loan(loanDTO.getName(), amount, payments, 10.0);
        loanService.save(newLoan);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Transactional
    @PatchMapping("/clients/current/loans/loanPayment")
    public ResponseEntity<Object> addLoan(@RequestParam long loanId, @RequestParam long accountId, @RequestParam double paymentAmount,  Authentication authentication) {
        ClientLoan clientLoan = clientLoanService.findById(loanId);
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(accountId);
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorised account", HttpStatus.UNAUTHORIZED);
        }

        if (!client.getClientLoans().contains(clientLoan)) {
            return new ResponseEntity<>("This loan does not belong to this client", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccount().contains(account)) {
            return new ResponseEntity<>("The selected account does not belong to this client", HttpStatus.FORBIDDEN);
        }

        if (account.getBalance() < paymentAmount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        if (clientLoan.getAmount() == 0) {
            return new ResponseEntity<>("This loan is already paid off", HttpStatus.FORBIDDEN);
        }

        if (clientLoan.getAmount() < paymentAmount) {
            return new ResponseEntity<>("Payment amount exceeds loan balance", HttpStatus.FORBIDDEN);
        }

        if (clientLoan.getPayments() == 0) {
            return new ResponseEntity<>("All installments have been paid", HttpStatus.FORBIDDEN);
        }
        else {
            account.setBalance(account.getBalance() - paymentAmount);
            Transaction transaction = new Transaction(LocalDateTime.now(), paymentAmount, TransactionType.DEBIT,  "Loan Installment - " + clientLoan.getLoan().getName(), account.getBalance(), true);
            clientLoan.setPayments(clientLoan.getPayments() - 1);
            clientLoan.setAmount((int) (clientLoan.getAmount() - paymentAmount));
            clientLoanService.save(clientLoan);
            account.addtransactionSet(transaction);
            transactionService.save(transaction);

            if(clientLoan.getAmount() == 0) {
                clientLoan.setActive(false);
                clientLoanService.save(clientLoan);
            }
            return new ResponseEntity<>("payment successful", HttpStatus.CREATED);
        }

    }
}
