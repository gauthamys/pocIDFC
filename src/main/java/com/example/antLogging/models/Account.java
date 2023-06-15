package com.example.antLogging.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accNum;

    @NotNull
    @Column
    @Pattern(regexp = "[A-Z]{4}[0-9]{7}", message="oops")
    private String IFSC_Code;

    @Column(columnDefinition = "integer default 0")
    private Integer balance;

}
