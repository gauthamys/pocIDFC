package com.example.standalone.kafka;

import com.example.standalone.StandaloneApplication;
import com.example.standalone.events.AType;
import com.example.standalone.events.TEvent;
import com.example.standalone.events.TType;
import com.example.standalone.models.Account;
import com.example.standalone.models.AccountStreamPayload;
import com.example.standalone.models.Transaction;
import com.example.standalone.models.TransactionStreamPayload;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import javax.validation.Valid;
import java.time.LocalDateTime;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@EnableKafka
@Service
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAccountEvent(Long accNum, AType status){
        AccountStreamPayload event = new AccountStreamPayload();
        event.setType(status.toString());
        event.setTimestamp(LocalDateTime.now().toString());
        kafkaTemplate.send("account-events." + accNum, event);
        LOGGER.info("account event sent to account-events." + accNum);
    }

    public void transAccountEvent(int amount, Long toId, Long fromId) {
        AccountStreamPayload event = new AccountStreamPayload();

        event.setTimestamp(LocalDateTime.now().toString());
        event.setId(toId);
        event.setAmount(amount);
        event.setType(TType.CREDIT.toString());

        kafkaTemplate.send("account-events." + event.getId(), event);
        LOGGER.info(event.getId() + " : " + event.getType() + " : " + event.getAmount());

        event.setId(fromId);
        event.setType(TType.DEBIT.toString());

        kafkaTemplate.send("account-events." + event.getId(), event);
        LOGGER.info(event.getId() + " : " + event.getType() + " : " + event.getAmount());

    }

    public void sendTransactionEvent(@Valid Transaction transaction, TEvent status){
        TransactionStreamPayload event = new TransactionStreamPayload();
        event.setTimestamp(LocalDateTime.now().toString());
        event.setToId(transaction.getToAccount());
        event.setFromId(transaction.getFromAccount());
        event.setType(status.toString());
        event.setAmount(transaction.getAmount());
        kafkaTemplate.send("transaction-events", event);
        LOGGER.info("transaction event sent to transaction-events");
    }
}

