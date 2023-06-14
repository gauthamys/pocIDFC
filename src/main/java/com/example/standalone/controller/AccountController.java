package com.example.standalone.controller;

import com.example.standalone.models.Account;
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

    @GetMapping(value = "/ok", produces = "application/text")
    public ResponseEntity<String> ok(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public ResponseEntity<List<Account>> getAll() {
        return new ResponseEntity<>(accountService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/test")
    public ResponseEntity<Account> test(@Valid @RequestBody Account account) {
        Account saved = accountService.save(account);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
