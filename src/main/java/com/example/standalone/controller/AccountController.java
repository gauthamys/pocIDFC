package com.example.standalone.controller;

import com.example.standalone.events.AType;
import com.example.standalone.events.TType;
import com.example.standalone.exceptions.AccountDoesNotExistError;
import com.example.standalone.exceptions.InsufficientBalanceException;
import com.example.standalone.kafka.KafkaProducer;
import com.example.standalone.models.Account;
import com.example.standalone.models.DepositPayload;
import com.example.standalone.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    KafkaProducer kafkaProducer;

    @GetMapping(value = "/ok", produces = "application/text")
    public ResponseEntity<String> ok(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public ResponseEntity<List<Account>> getAll() {
        return new ResponseEntity<>(accountService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/post", produces = "application/json")
    public ResponseEntity<Account> test(@Valid @RequestBody Account account) {
        Account saved = accountService.save(account);
        kafkaProducer.sendAccountEvent(saved.getAccNum(), AType.CREATE, saved.getBalance());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/json")
    public ResponseEntity<Account> update(@Valid @RequestBody Account account, @PathVariable Long id) throws AccountDoesNotExistError {
        Account acc = accountService.updateById(account, id);
        kafkaProducer.sendAccountEvent(acc.getAccNum(), AType.UPDATE, account.getBalance());
        return new ResponseEntity<>(acc, HttpStatus.OK);
    }

    @PostMapping(value = "/deposit", produces = "application/json")
    public ResponseEntity<String> deposit(@Valid @RequestBody DepositPayload depositPayload) throws InsufficientBalanceException, AccountDoesNotExistError {
        accountService.updateBalance(TType.CREDIT, depositPayload.getAmount(), depositPayload.getId());
        kafkaProducer.sendAccountEvent(depositPayload.getId(), AType.DEPOSIT, depositPayload.getAmount());
        return new ResponseEntity<>("Succesfully added " + depositPayload.getAmount() + "to account " + depositPayload.getId(), HttpStatus.OK);
    }
}
