package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {
     Card findCardByNumber(String number);
     Card save(Card card);
     Card findById(long id);
     Card findByCvv(int cvv);
}
