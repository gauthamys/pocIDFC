package com.example.standalone.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import scala.Int;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DepositPayload {

    @NotNull
    @NotBlank
    private Long id;

    @Size(min = 0)
    @NotNull
    private Integer amount;

}
