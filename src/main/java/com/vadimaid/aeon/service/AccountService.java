package com.vadimaid.aeon.service;

import com.vadimaid.aeon.dto.AccountDto;
import com.vadimaid.aeon.entity.Account;
import com.vadimaid.aeon.entity.User;

public interface AccountService {

    Account create(User user);

    AccountDto makePayment(User user);

}
