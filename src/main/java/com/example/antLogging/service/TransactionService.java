package com.example.antLogging.service;

import com.example.antLogging.events.TEvent;
import com.example.antLogging.events.TType;
import com.example.antLogging.exceptions.AccountDoesNotExistError;
import com.example.antLogging.exceptions.InsufficientBalanceException;
import com.example.antLogging.kafka.KafkaProducer;
import com.example.antLogging.models.Transaction;
import com.example.antLogging.repository.TransactionRepository;
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
