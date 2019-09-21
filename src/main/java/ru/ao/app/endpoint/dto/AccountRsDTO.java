package ru.ao.app.endpoint.dto;

import java.math.BigDecimal;

public class AccountRsDTO {

    private Long id;
    private String name;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public AccountRsDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AccountRsDTO setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountRsDTO setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }
}
