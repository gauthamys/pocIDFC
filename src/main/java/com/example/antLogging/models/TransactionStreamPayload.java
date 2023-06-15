package com.example.antLogging.models;

import lombok.Data;

@Data
public class TransactionStreamPayload {
    private String type;
    private String timestamp;
    private Integer amount;
    private Long toId;
    private Long fromId;
}
