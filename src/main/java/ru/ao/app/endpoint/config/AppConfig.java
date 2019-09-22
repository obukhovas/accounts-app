package ru.ao.app.endpoint.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import ru.ao.app.access.AccountAccessService;
import ru.ao.app.access.AccountAccessServiceImpl;
import ru.ao.app.business.AccountBusinessService;
import ru.ao.app.business.AccountBusinessServiceImpl;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages(true, "ru.ao.app.endpoint");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                AccountAccessServiceImpl accountAccessService = new AccountAccessServiceImpl();
                bind(accountAccessService).to(AccountAccessService.class);
                bind(new AccountBusinessServiceImpl(accountAccessService)).to(AccountBusinessService.class);
            }
        });
        register(AppJacksonJaxbJsonProvider.class);
        register(AppExceptionMapper.class);
        register(ConstraintViolationExceptionMapper.class);
    }
}
