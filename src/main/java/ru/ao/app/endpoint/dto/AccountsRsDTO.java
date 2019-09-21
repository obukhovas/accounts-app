package ru.ao.app.endpoint.dto;

import java.util.List;

public class AccountsRsDTO {

    private List<AccountRsDTO> accounts;

    public List<AccountRsDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountRsDTO> accounts) {
        this.accounts = accounts;
    }
}
