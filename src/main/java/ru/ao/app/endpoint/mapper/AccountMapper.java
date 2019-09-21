package ru.ao.app.endpoint.mapper;

import ru.ao.app.access.model.Account;
import ru.ao.app.endpoint.dto.AccountRsDTO;
import ru.ao.app.endpoint.dto.AccountsRsDTO;
import ru.ao.app.endpoint.dto.CreateAccountRqDTO;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public final class AccountMapper {

    private AccountMapper() {
    }

    public static AccountsRsDTO fromEntities(List<Account> accounts) {
        List<AccountRsDTO> accountRsDTOList = new ArrayList<>();
        if (nonNull(accounts)) {
            accounts.forEach(
                    account -> accountRsDTOList.add(fromEntity(account))
            );
        }
        AccountsRsDTO accountRsDTO = new AccountsRsDTO();
        accountRsDTO.setAccounts(accountRsDTOList);
        return accountRsDTO;
    }

    public static AccountRsDTO fromEntity(Account account) {
        AccountRsDTO accountRsDTO = new AccountRsDTO();
        accountRsDTO.setId(account.getId());
        accountRsDTO.setName(account.getName());
        accountRsDTO.setBalance(account.getBalance());
        return accountRsDTO;
    }

    public static Account fromDTO(CreateAccountRqDTO dto) {
        Account account = new Account();
        account.setName(dto.getName());
        account.setBalance(dto.getBalance());
        return account;
    }

}
