package com.example.standalone.service;

import com.example.standalone.events.TType;
import com.example.standalone.exceptions.AccountDoesNotExistError;
import com.example.standalone.exceptions.InsufficientBalanceException;
import com.example.standalone.kafka.KafkaProducer;
import com.example.standalone.models.Account;
import com.example.standalone.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    AccountRepository accountRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account save(@Valid Account account) {
        if(account.getBalance() == null) {
            account.setBalance(0);
        }
        kafkaProducer.createAccountEvent(account);
        return accountRepository.save(account);
    }

    public Account getById(Long id) throws AccountDoesNotExistError {
        Optional<Account> ret = Optional.ofNullable(
                accountRepository.findById(id).orElseThrow(() -> new AccountDoesNotExistError("account does not exist")));
        return ret.get();
    }

    public void updateBalance(TType type, int amount, Long id) throws AccountDoesNotExistError, InsufficientBalanceException {
        Account acc = accountRepository.findById(id).orElseThrow(() -> new AccountDoesNotExistError("account does not exist"));
        int curBalance = acc.getBalance();
        if (type == TType.CREDIT){
            acc.setBalance(curBalance + amount);
        } else if ( type == TType.DEBIT && curBalance < amount) {
            throw new InsufficientBalanceException("insufficient balance");
        } else {
          acc.setBalance(curBalance - amount);
        }
    }
}
