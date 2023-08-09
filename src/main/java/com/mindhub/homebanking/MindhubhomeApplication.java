package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.Accountrepository;
import com.mindhub.homebanking.repositories.Clientrepository;
import com.mindhub.homebanking.repositories.Transactionrepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class MindhubhomeApplication {
	LocalDate date1 = LocalDate.now();
	LocalDate date2 = LocalDate.now().plusDays(1);

	LocalDateTime dateTime1 = LocalDateTime.now();


	public static void main(String[] args) {
		SpringApplication.run(MindhubhomeApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(Clientrepository repositoryclient , Accountrepository repositoryaccount, Transactionrepository repositorytransaction) {
		return (args) -> {
			Account account1 = new Account("VIN001", this.date1 , 5000);
			Account account2 = new Account("VIN002", this.date2 , 7500);
			Client client1 = new Client("Melba", "Morel","melbax@gmail.com");
			client1.addAccount(account2);
			client1.addAccount(account1);
			Transaction transaction1 = new Transaction(this.dateTime1, 2000, TransactionType.CREDITO, account1);

			repositoryclient.save(client1);
			repositoryaccount.save(account1);
			repositoryaccount.save(account2);
			repositorytransaction.save(transaction1);
		};
	}
}
