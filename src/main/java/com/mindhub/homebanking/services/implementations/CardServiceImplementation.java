package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplementation implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public Card findCardByNumber(String number){
        return cardRepository.findByNumber(number);
    }

    @Override
    public Card save(Card card){
        return cardRepository.save(card);
    }

    @Override
    public Card findById(long id){
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card findByCvv(int cvv) {
        return cardRepository.findByCvv(cvv);
    }
}
