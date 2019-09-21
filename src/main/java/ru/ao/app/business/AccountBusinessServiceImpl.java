package ru.ao.app.business;

import ru.ao.app.access.AccountAccessService;
import ru.ao.app.access.model.Account;
import ru.ao.app.business.exception.BusinessException;
import ru.ao.app.business.exception.StatusCodeEnum;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.isNull;

@Singleton
public class AccountBusinessServiceImpl implements AccountBusinessService {

    private AccountAccessService accountAccessService;

    public AccountBusinessServiceImpl(AccountAccessService accountAccessService) {
        this.accountAccessService = accountAccessService;
    }

    @Override
    public List<Account> getAll() {
        return accountAccessService.getAll();
    }

    @Override
    public Account getById(long id) {
        return accountAccessService.getById(id);
    }

    @Override
    public Account create(Account account) {
        return accountAccessService.create(account);
    }

    @Override
    public Account delete(long id) {
        Account account = accountAccessService.getById(id);
        if (isNull(account)) {
            throw new BusinessException(StatusCodeEnum.ACCOUNT_NOT_FOUNT_ERROR);
        }
        accountAccessService.delete(id);
        return account;
    }

    @Override
    public Account withdraw(long id, BigDecimal amount) {
        Account account = accountAccessService.getById(id);
        if (isNull(account)) {
            throw new BusinessException(StatusCodeEnum.ACCOUNT_NOT_FOUNT_ERROR);
        }
        BigDecimal current = account.getBalance();
        account.setBalance(current.subtract(amount));
        return accountAccessService.update(account);
    }

    @Override
    public void transfer(long fromId, long toId, BigDecimal amount) {
        if (isNull(accountAccessService.getById(fromId))) {
            throw new BusinessException(StatusCodeEnum.SOURCE_ACCOUNT_NOT_FOUNT_ERROR);
        }
        if (isNull(accountAccessService.getById(toId))) {
            throw new BusinessException(StatusCodeEnum.TARGET_ACCOUNT_NOT_FOUNT_ERROR);
        }
        accountAccessService.transfer(fromId, toId, amount);
    }
}
