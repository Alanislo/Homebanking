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
	private PasswordEncoder passwordEnconder;

	public static void main(String[] args) {
		SpringApplication.run(MindhubhomeApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(ClientRepository repositoryclient , AccountRepository repositoryaccount, TransactionRepository repositorytransaction, LoanRepository repositoryloan, ClientLoanRepository repositoryclientloan, CardRepository repositorycard) {
		return (args) -> {
			Account account1 = repositoryaccount.save(new Account("VIN001", this.date1 , 5000));
			Account account2 = repositoryaccount.save(new Account("VIN002", this.date2 , 7500));
			Account account3 = repositoryaccount.save(new Account("H001", this.date1, 23000));

			Client client1 = repositoryclient.save(new Client("Melba", "Morel","melba@mindhub.com", passwordEnconder.encode("melba")));
			Client client2 = repositoryclient.save(new Client("Nicolas", "Herlan", "nicolasherlan@gmail.com", passwordEnconder.encode("mandarina13")));
			Client admin = repositoryclient.save(new Client("Admin", "Admin", "admin@gmail.com", passwordEnconder.encode("admin"))) ;
			client1.addAccount(account2);
			client1.addAccount(account1);
			client2.addAccount(account3);

			Transaction transaction1 = repositorytransaction.save(new Transaction(this.dateTime1, 2000, TransactionType.CREDIT, "loan"));
			Transaction transaction2 = repositorytransaction.save(new Transaction(this.dateTime1, 200, TransactionType.CREDIT, "loan"));
			Transaction transaction3 = repositorytransaction.save(new Transaction(this.dateTime1, 2000, TransactionType.DEBIT, "rent"));
//			account 2
			Transaction transaction4 = repositorytransaction.save(new Transaction(this.dateTime1, 2000, TransactionType.CREDIT, "loan"));
			Transaction transaction5 = repositorytransaction.save(new Transaction(this.dateTime1, 200, TransactionType.DEBIT, "shopping"));
			Transaction transaction6 = repositorytransaction.save(new Transaction(this.dateTime1, 2000, TransactionType.DEBIT, "petShop"));
			account1.addtransactionSet(transaction1);
			account1.addtransactionSet(transaction2);
			account1.addtransactionSet(transaction3);
			account2.addtransactionSet(transaction4);
			account2.addtransactionSet(transaction5);
			account2.addtransactionSet(transaction6);

			// prestamos
			Loan mortgage1 = repositoryloan.save(new Loan("Mortgage", 500000, mortgage));
			Loan personal1 = repositoryloan.save(new Loan("Personal",100000,personal));
			Loan automotive1 = repositoryloan.save(new Loan("Automotive",300000,automotive));

			ClientLoan loan1 = repositoryclientloan.save(new ClientLoan("Mortgage",400000,60));
			ClientLoan loan2 = repositoryclientloan.save(new ClientLoan("Personal", 50000,12));

			ClientLoan loan3 = repositoryclientloan.save(new ClientLoan("Personal",100000, 24));
			ClientLoan loan4 = repositoryclientloan.save(new ClientLoan("Automotive",200000, 36));

			Card card1 = repositorycard.save(new Card("Melba Morel" , CardColor.GOLD,CardType.DEBIT,"3333-4457-3333-7089", (short) 999, this.thruDate1, this.fromDate1));
			Card card2 = repositorycard.save(new Card("Melba Morel", CardColor.TITANIUM,CardType.CREDIT,"3222-4555-3333-7777", (short) 123, this.thruDate1, this.fromDate1));

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



		};
	}
}
