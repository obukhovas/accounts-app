package ru.ao.app.access.db;

import java.sql.Connection;

public interface Database {

    Connection getConnection();

    void initializeTestData();

}
