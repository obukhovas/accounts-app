package ru.ao.app.endpoint.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateAccountRqDTO {

    @NotNull(message = "Name of Account must be not null")
    private String name;
    @NotNull(message = "Balance of Account must be not null")
    private BigDecimal balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
