package com.example.standalone.models;

import lombok.Data;

@Data
public class AccountStreamPayload {
    private String type;
    private Integer amount;
    private String timestamp;
    private Long id;
}
