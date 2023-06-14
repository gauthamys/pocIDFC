package com.example.standalone.service;

import com.example.standalone.events.TEvent;
import com.example.standalone.events.TType;
import com.example.standalone.exceptions.AccountDoesNotExistError;
import com.example.standalone.exceptions.InsufficientBalanceException;
import com.example.standalone.kafka.KafkaProducer;
import com.example.standalone.models.Account;
import com.example.standalone.models.Transaction;
import com.example.standalone.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountService accountService;

    public Transaction save(Transaction transaction) throws AccountDoesNotExistError, InsufficientBalanceException {
        Long toId = transaction.getToAccount();
        Long fromId = transaction.getFromAccount();
        int amount = transaction.getAmount();

        // update balances
        // void accountService.updateBalance(TType type, int amount, Long id);
        accountService.updateBalance(TType.CREDIT, amount, toId);
        accountService.updateBalance(TType.DEBIT, amount, fromId);

        kafkaProducer.sendTransactionEvent(transaction, TEvent.SUCCESS);
        kafkaProducer.transAccountEvent(amount, toId, fromId);

        return transactionRepository.save(transaction);
    }
}
