package ru.ao.app.util;

import ru.ao.app.access.db.DataBaseFactory;

public final class DatabaseUtil {

    private DatabaseUtil() {
        throw new AssertionError("Utility class");
    }

    public static void initialize() {
        DataBaseFactory.getDataBase(DataBaseFactory.H2).initialize();
    }

    public static void initializeTestData() {
        DataBaseFactory.getDataBase(DataBaseFactory.H2).initializeTestData();
    }

}
