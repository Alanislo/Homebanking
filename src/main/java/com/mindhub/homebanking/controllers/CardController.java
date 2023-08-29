package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;
    private String randomNumber() {
        String random = "";
        int i;
        for (i = 0; i < 4; i++){
            int min = 1000;
            int max = 8999;
            random += (int)(Math.random()*(max - min + 1) + min)+"-";
        }
    return random;
    }
    private int randomCvv(){
        int cvv = (int)(Math.random()*899+100);
        return cvv;
    }
    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> register( @RequestParam CardType type , @RequestParam CardColor color, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (type == null && color == null) {
            return new ResponseEntity<>("Missing Type", HttpStatus.FORBIDDEN);
        }
        String cardNumber = "";
        do {
            cardNumber = randomNumber();
        } while (cardRepository.findByNumber(cardNumber) != null);
        int cardCvv= 0;
        do {
             cardCvv= randomCvv();
        }while (cardRepository.findByCvv(cardCvv) != null);

        for (Card card: client.getCards()) {
            if (card.getType().equals(type) && card.getColor().equals(color)){
                return new ResponseEntity<>("No more cards", HttpStatus.FORBIDDEN);
            }
        }
        Card card = new Card(client.getFirstName()+" "+ client.getLastName(), color, type, cardNumber,  cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
        client.addCards(card);
        cardRepository.save(card);
        return new ResponseEntity<>("Created a new Card", HttpStatus.CREATED);
    }
}
