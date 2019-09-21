package ru.ao.app.access;

import ru.ao.app.access.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountAccessService {

    List<Account> getAll();

    Account getById(long id);

    Account create(Account account);

    Account update(Account account);

    void delete(long id);

    void transfer(long fromId, long toId, BigDecimal amount);

}

