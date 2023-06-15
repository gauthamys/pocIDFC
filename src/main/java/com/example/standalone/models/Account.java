package com.example.standalone.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accNum;

    @Column
    @NotBlank
    @Valid
    @Pattern(regexp = "[A-Z]{4}[0-9]{7}", message="invalid IFSC_Code")
    private String ifsCode;

    @Column
    private Integer balance = 0;

}
