package ru.ao.app.access.db;

import org.h2.tools.RunScript;
import ru.ao.app.business.exception.BusinessException;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.ao.app.business.exception.StatusCodeEnum.GET_CONNECTION_ERROR;
import static ru.ao.app.business.exception.StatusCodeEnum.INITIALIZE_TEST_DATA_ERROR;
import static ru.ao.app.util.PropertiesUtil.getProperty;

public class H2Database implements Database {

    private static final String DRIVER = getProperty("h2.driver");
    private static final String CONNECTION_URL = getProperty("h2.connection.url");
    private static final String USER = getProperty("h2.user");
    private static final String PASSWORD = getProperty("h2.password");

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new BusinessException(GET_CONNECTION_ERROR, e);
        }
    }

    @Override
    public void initialize() {
        try (Connection conn = getConnection()) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("account-table.sql");
            RunScript.execute(conn, new InputStreamReader(is));
        } catch (Exception e) {
            throw new BusinessException(INITIALIZE_TEST_DATA_ERROR, e);
        }
    }

    @Override
    public void initializeTestData() {
        initialize();
        try (Connection conn = getConnection()) {
            RunScript.execute(conn, new FileReader("src/test/resources/db/h2-init.sql"));
        } catch (Exception e) {
            throw new BusinessException(INITIALIZE_TEST_DATA_ERROR, e);
        }
    }
}
