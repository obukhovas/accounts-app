package ru.ao.app.endpoint;

import ru.ao.app.access.model.Account;
import ru.ao.app.business.AccountBusinessService;
import ru.ao.app.endpoint.dto.AccountsRsDTO;
import ru.ao.app.endpoint.dto.CreateAccountRqDTO;
import ru.ao.app.endpoint.dto.TransferMoneyRqDTO;
import ru.ao.app.endpoint.mapper.AccountMapper;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.nonNull;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountEndpoint {

    @Inject
    private AccountBusinessService accountBusinessService;

    @GET
    @Path("/all")
    public Response getAll() {
        List<Account> accounts = accountBusinessService.getAll();
        AccountsRsDTO entity = AccountMapper.fromEntities(accounts);
        return Response.ok()
                .entity(entity)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        Account account = accountBusinessService.getById(id);
        if (nonNull(account)) {
            return Response.ok()
                    .entity(AccountMapper.fromEntity(account))
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response create(@Valid CreateAccountRqDTO createAccountRqDTO) {
        Account account = AccountMapper.fromDTO(createAccountRqDTO);
        Account created = accountBusinessService.create(account);
        return Response.status(Response.Status.CREATED)
                .entity(AccountMapper.fromEntity(created))
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") long id) {
        Account deleted = accountBusinessService.delete(id);
        return Response.ok()
                .entity(AccountMapper.fromEntity(deleted))
                .build();
    }

    @POST
    @Path("/{id}/withdraw/{amount}")
    public Response withdraw(@PathParam("id") long id,
                             @PathParam("amount") BigDecimal amount) {
        Account account = accountBusinessService.withdraw(id, amount);
        return Response.ok()
                .entity(AccountMapper.fromEntity(account))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/transfer")
    public Response transfer(@Valid TransferMoneyRqDTO transferMoneyRqDTO) {
        accountBusinessService.transfer(
                transferMoneyRqDTO.getFromAccountId(),
                transferMoneyRqDTO.getToAccountId(),
                transferMoneyRqDTO.getAmount());
        return Response.ok()
                .build();
    }

}
