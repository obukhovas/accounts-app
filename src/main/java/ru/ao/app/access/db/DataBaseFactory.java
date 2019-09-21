package ru.ao.app.access.db;


public class DataBaseFactory {

    public static final int H2 = 1;

    private DataBaseFactory() {
    }

    public static Database getDataBase() {
        return getDataBase(H2);
    }

    public static Database getDataBase(int code) {
        if (code == 1) {
            return new H2Database();
        }
        return new H2Database();
    }

}
