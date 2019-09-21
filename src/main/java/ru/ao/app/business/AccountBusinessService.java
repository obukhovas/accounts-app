package ru.ao.app.business;

import ru.ao.app.access.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountBusinessService {

    List<Account> getAll();

    Account getById(long id);

    Account create(Account account);

    Account delete(long id);

    Account withdraw(long id, BigDecimal amount);

    void transfer(long fromId, long toId, BigDecimal amount);

}
