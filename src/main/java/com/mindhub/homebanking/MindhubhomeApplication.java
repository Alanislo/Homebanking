package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MindhubhomeApplication {
	LocalDate date1 = LocalDate.now();
	LocalDate date2 = LocalDate.now().plusDays(1);

	LocalDate thruDate1 = LocalDate.now();
	LocalDate fromDate1 = LocalDate.now().plusYears(5);

	LocalDateTime dateTime1 = LocalDateTime.now();

	List <Integer> mortgage = List.of(12,24,36,48,60);
	List <Integer> personal = List.of(6,12,24);
	List <Integer> automotive = List.of(6,12,24,36);
	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(MindhubhomeApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(ClientRepository repositoryclient , AccountRepository repositoryaccount, TransactionRepository repositorytransaction, LoanRepository repositoryloan, ClientLoanRepository repositoryclientloan, CardRepository repositorycard) {
		return (args) -> {
	/*		Account account1 = new Account("VIN001", this.date1 , 5000);
			Account account2 = new Account("VIN002", this.date2 , 7500);
			Account account3 = new Account("H001", this.date1, 23000);

			Client client1 = new Client("Melba", "Morel","melba@gmail.com", passwordEncoder.encode("melba"));
			Client client2 = new Client("Nicolas", "Herlan", "nicolasherlan@gmail.com", passwordEncoder.encode("nico"));
			Client admin = new Client("admin", "admin", "admin@gmail.com", passwordEncoder.encode("admin"));

			client1.addAccount(account2);
			client1.addAccount(account1);
			client2.addAccount(account3);

			Transaction transaction1 = new Transaction(this.dateTime1, 2000, TransactionType.CREDIT, "Loan");
			Transaction transaction2 = new Transaction(this.dateTime1, 200, TransactionType.CREDIT, "Loan");
			Transaction transaction3 = new Transaction(this.dateTime1, 2000, TransactionType.DEBIT, "Rent");
//			account 2
			Transaction transaction4 = new Transaction(this.dateTime1, 2000, TransactionType.CREDIT, "Loan");
			Transaction transaction5 = new Transaction(this.dateTime1, 200, TransactionType.DEBIT, "Shopping");
			Transaction transaction6 = new Transaction(this.dateTime1, 2000, TransactionType.DEBIT, "Pet Shop");
			Transaction transaction7 = new Transaction(this.dateTime1, 2000, TransactionType.DEBIT, "Pet Shop");
			account1.addtransactionSet(transaction1);
			account1.addtransactionSet(transaction2);
			account1.addtransactionSet(transaction3);
			account2.addtransactionSet(transaction4);
			account2.addtransactionSet(transaction5);
			account2.addtransactionSet(transaction6);
			account3.addtransactionSet(transaction7);

			// loans
			Loan mortgage1 = new Loan("Mortgage", 500000, mortgage);
			Loan personal1 = new Loan("Personal",100000,personal);
			Loan automotive1 = new Loan("Automotive",300000,automotive);

			ClientLoan loan1 = new ClientLoan("Mortgage",400000,60);
			ClientLoan loan2 = new ClientLoan("Personal", 50000,12);

			ClientLoan loan3 = new ClientLoan("Personal",100000, 24);
			ClientLoan loan4 = new ClientLoan("Automotive",200000, 36);

			Card card1 = new Card("Melba Morel" , CardColor.GOLD,CardType.DEBIT,"3333-4457-3333-7089", 999, this.thruDate1, this.fromDate1);
			Card card2 = new Card("Melba Morel", CardColor.TITANIUM,CardType.CREDIT,"3222-4555-3333-7777", 123, this.thruDate1, this.fromDate1);

			mortgage1.addClientLoan(loan1);
			personal1.addClientLoan(loan2);
			personal1.addClientLoan(loan3);
			automotive1.addClientLoan(loan4);

			client1.addClientLoan(loan1);
			client1.addClientLoan(loan2);

			client2.addClientLoan(loan3);
			client2.addClientLoan(loan4);

			client1.addCards(card1);
			client1.addCards(card2);

			repositoryloan.save(mortgage1);
			repositoryloan.save(personal1);
			repositoryloan.save(automotive1);

			repositoryclient.save(client1);
			repositoryclient.save(client2);
			repositoryclient.save(admin);

			repositoryaccount.save(account1);
			repositoryaccount.save(account2);
			repositoryaccount.save(account3);

			repositorytransaction.save(transaction1);
			repositorytransaction.save(transaction2);
			repositorytransaction.save(transaction3);
			repositorytransaction.save(transaction4);
			repositorytransaction.save(transaction5);
			repositorytransaction.save(transaction6);
			repositorytransaction.save(transaction7);

			repositoryclientloan.save(loan1);
			repositoryclientloan.save(loan2);
			repositoryclientloan.save(loan3);
			repositoryclientloan.save(loan4);

			repositorycard.save(card1);
			repositorycard.save(card2);*/

		};
	}
}
