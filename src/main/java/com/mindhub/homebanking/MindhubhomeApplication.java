package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.accountrepository;
import com.mindhub.homebanking.repositories.clientrepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class MindhubhomeApplication {
	LocalDate date1 = LocalDate.now();
	LocalDate date2 = LocalDate.now().plusDays(1);
	public static void main(String[] args) {
		SpringApplication.run(MindhubhomeApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(clientrepository repositoryclient , accountrepository repositoryaccount) {
		return (args) -> {
			Account account1 = new Account("VIN001", this.date1 , 5000);
			Account account2 = new Account("VIN002", this.date2 , 7500);
			Client client = new Client("Melba", "Morel","melbax@gmail.com");
			client.addAccount(account2);
			client.addAccount(account1);

			repositoryclient.save(client);
			repositoryaccount.save(account1);
			repositoryaccount.save(account2);
		};
	}
}
