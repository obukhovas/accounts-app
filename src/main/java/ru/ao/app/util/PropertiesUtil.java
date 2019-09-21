package ru.ao.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.isNull;

public final class PropertiesUtil {

    private static final String PROPERTIES_FILE = "application.properties";

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {
        throw new AssertionError("Utility class");
    }

    private static void loadProperties() {
        try {
            InputStream resource = PropertiesUtil.class.getClassLoader()
                    .getResourceAsStream(PROPERTIES_FILE);
            PROPERTIES.load(resource);
        } catch (IOException e) {
            String message = String.format("Error while loading properties from file: %s",
                    PROPERTIES_FILE);
            throw new RuntimeException(message, e);
        }
    }

    public static String getProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        if (isNull(value)) {
            value = System.getProperty(key);
        }
        return value;
    }


}
