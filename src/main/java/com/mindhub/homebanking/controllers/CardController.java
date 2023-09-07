package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.stream.Collectors;
@RequestMapping("/api")
@RestController
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    private String randomNumber() {
        String random = "";
        int i;
        int min = 1000;
        int max = 8999;
        for (i = 0; i < 3; i++){
            random += (int)(Math.random()*(max - min + 1) + min)+"-";
        }
        random += (int)(Math.random()*(max - min + 1) + min);
    return random;
    }
    private int randomCvv(){
        int cvv = (int)(Math.random()*899+100);
        return cvv;
    }
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> register(@RequestParam String type , @RequestParam String color, Authentication authentication) {
        if( !type.equals("CREDIT") && !type.equals("DEBIT") ){
            return new ResponseEntity<>("Select the type", HttpStatus.FORBIDDEN);
        }

        if( !color.equals("GOLD") && !color.equals("SILVER") && !color.equals("TITANIUM")){
            return new ResponseEntity<>("Select the color", HttpStatus.FORBIDDEN);
        }
        CardType cardType =  CardType.valueOf(type);
        CardColor cardColor = CardColor.valueOf(color);
        Client client = clientService.findByEmail(authentication.getName());

        String cardNumber = "";
        do {
            cardNumber = randomNumber();
        } while (cardService.findCardByNumber(cardNumber) != null);
        int cardCvv = 0;
        do {
            cardCvv = randomCvv();
        } while (cardService.findByCvv(cardCvv) != null);
        if (!client.getCards().stream().filter(card1 -> card1.getType().equals(type) && card1.getColor().equals(color)).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("It already exists", HttpStatus.FORBIDDEN);
    }
        Card card = new Card(client.getFirstName()+" "+ client.getLastName(), cardColor, cardType, cardNumber,  cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
        client.addCards(card);
        cardService.save(card);
        return new ResponseEntity<>("Created a new Card", HttpStatus.CREATED);
    }
}
