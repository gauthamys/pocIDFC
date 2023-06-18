package com.example.standalone.service;

import com.example.standalone.events.TType;
import com.example.standalone.exceptions.AccountDoesNotExistError;
import com.example.standalone.exceptions.InsufficientBalanceException;
import com.example.standalone.kafka.KafkaProducer;
import com.example.standalone.models.Account;
import com.example.standalone.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

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
        Account acc = accountRepository.save(account);
        TopicBuilder.name("account-events."+acc.getAccNum()).build();
        return acc;
    }

    public Account getById(Long id) throws AccountDoesNotExistError {
        return accountRepository.findById(id).orElseThrow(() -> new AccountDoesNotExistError("account does not exist"));
    }

    public Account updateById(Account account, Long id) throws AccountDoesNotExistError {
        Account acc = accountRepository.findById(id).orElseThrow(() -> new AccountDoesNotExistError("account does not exist"));
        acc.setAccNum(id);
        return accountRepository.save(acc);
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
        accountRepository.save(acc);
    }
}
