package com.vadimaid.aeon.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.vadimaid.aeon.dto.AccountDto;
import com.vadimaid.aeon.entity.*;
import com.vadimaid.aeon.repository.AccountHistoryRepository;
import com.vadimaid.aeon.repository.AccountRepository;
import com.vadimaid.aeon.repository.CurrencyRepository;
import com.vadimaid.aeon.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class AccountServiceImpl implements AccountService {

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("8.0");
    private static final BigDecimal SUBTRACTION = new BigDecimal("1.1");
    private static final String USD_CURRENCY = "USD";

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public Account create(User user) {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("Account cannot be bound to null user");
        }

        Optional<Currency> currency = currencyRepository.findByCode(USD_CURRENCY);
        if (!currency.isPresent()) {
            throw new IllegalArgumentException("No USD currency found");
        }

        Account account = new Account();
        account.setBalance(INITIAL_BALANCE);
        account.setUser(user);
        account.setCurrency(currency.get());

        return accountRepository.save(account);
    }

    @Transactional
    @Override
    public AccountDto makePayment(User user) {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("User cannot be null");
        }

        BooleanBuilder builder = new BooleanBuilder();
        QAccount root = QAccount.account;

        builder.and(root.user.eq(user));
        Optional<Account> account = accountRepository.findOne(builder.getValue());
        if (!account.isPresent()) {
            throw new IllegalArgumentException("No account found on user " + user.getUsername());
        }

        if (account.get().getBalance().compareTo(SUBTRACTION) < 0) {
            throw new IllegalArgumentException("Cannot make payment from current account. Account balance is " + account.get().getBalance().toString());
        }

        BigDecimal resultBalance = account.get().getBalance().subtract(SUBTRACTION);

        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setAccount(account.get());
        accountHistory.setSpentAmount(SUBTRACTION);
        accountHistory.setLeftBalance(resultBalance);
        accountHistory.setTransactionTime(LocalDateTime.now());
        accountHistoryRepository.save(accountHistory);

        account.get().setBalance(resultBalance);
        accountRepository.save(account.get());

        return AccountDto
                .builder()
                .balance(resultBalance)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .currency(account.get().getCurrency().getName())
                .build();
    }
}
