package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    Account save (Account account);
    Account findByNumber(String random);
    List<AccountDTO> getAllAccounts();
    Account findById(Long id);
}
