package com.dh.accountservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransactionDTO {
    private Long id;

    private String alias;

    private String cvu;

    private Double balance;

    @JsonIgnoreProperties(value = {"accountId"})
    private Transaction transaction;
}

