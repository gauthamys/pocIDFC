package com.example.standalone.controller;

import com.example.standalone.events.AType;
import com.example.standalone.events.TEvent;
import com.example.standalone.events.TType;
import com.example.standalone.exceptions.AccountDoesNotExistError;
import com.example.standalone.exceptions.InsufficientBalanceException;
import com.example.standalone.kafka.KafkaProducer;
import com.example.standalone.models.Account;
import com.example.standalone.models.Transaction;
import com.example.standalone.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    TransactionService transactionService;

    @GetMapping(value = "/ok", produces = "application/text")
    public ResponseEntity<String> ok() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping(value = "/test", produces = "application/json")
    public ResponseEntity<Transaction> send(@Valid @RequestBody Transaction transaction) throws AccountDoesNotExistError {
        try {
            Transaction t = transactionService.save(transaction);
            return new ResponseEntity<>(t, HttpStatus.CREATED);
        } catch (AccountDoesNotExistError e) {
            kafkaProducer.sendTransactionEvent(transaction, TEvent.ACCOUNT_NON_EXISTENT);
            return new ResponseEntity<>(transaction, HttpStatus.BAD_REQUEST);
        } catch (InsufficientBalanceException e) {
            kafkaProducer.sendTransactionEvent(transaction, TEvent.INSUFFICIENT_BALANCE);
            kafkaProducer.sendAccountEvent(transaction.getFromAccount(), AType.INSUFFICIENT_BALANCE);
            return new ResponseEntity<>(transaction, HttpStatus.BAD_REQUEST);
        }
    }
}
