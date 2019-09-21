package ru.ao.app.endpoint.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferMoneyRqDTO {

    @NotNull(message = "Source Account ID must not be null")
    private Long fromAccountId;
    @NotNull(message = "Target Account ID must not be null")
    private Long toAccountId;
    @NotNull(message = "Amount must not be null")
    private BigDecimal amount;

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
