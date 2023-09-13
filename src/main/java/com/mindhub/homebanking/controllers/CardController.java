package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.CardUtils.getCVV;
import static com.mindhub.homebanking.utils.CardUtils.getCardNumber;

@RequestMapping("/api")
@RestController
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    private String cardNumber = getCardNumber();
    private int cvv = getCVV();
    private String randomNumber() {
        String random = getCardNumber();
        return random;
    }
    private int randomCvv(){
        return getCVV();
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
        Card card = new Card(client.getFirstName()+" "+ client.getLastName(), cardColor, cardType, cardNumber,  cardCvv, LocalDate.now(), LocalDate.now().plusYears(5), true);
        client.addCards(card);
        cardService.save(card);
        return new ResponseEntity<>("Created a new Card", HttpStatus.CREATED);
    }
    @PatchMapping("/clients/current/cards/deactivate")
    public ResponseEntity<Object> disableCard(@RequestParam long id, Authentication authentication){
        Card card = cardService.findById(id);
        Client client = clientService.findByEmail(authentication.getName());
        Boolean existCard = client.getCards().contains(card);
        if(card == null){
            return  new ResponseEntity<>("La tarjeta no existe", HttpStatus.FORBIDDEN);
        }
        if (!existCard){
            return  new ResponseEntity<>("Esta tarjeta no pertece a este cliente", HttpStatus.FORBIDDEN);
        }
        if(card.getActive() == false){
            return new ResponseEntity<>("Esta tarjeta ya fue eliminada", HttpStatus.FORBIDDEN);
        }

        card.setActive(false);
        cardService.save(card);
        return  new ResponseEntity<>("Se ha eliminado la tarjeta con exito", HttpStatus.OK);
    }
}
