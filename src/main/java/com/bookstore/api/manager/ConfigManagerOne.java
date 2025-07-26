package com.bookstore.api.manager;

import java.io.*;
import java.util.Properties;

public class ConfigManagerOne {

    private static final String CONFIG_FILE_PATH = System.getProperty("user.home") +
    		"/Documents/GitWorkArea/BookStoreAPIJuly2025/src/test/resources/config/config.properties";
    private static Properties properties = new Properties();

    static {
        try {
            File file = new File(CONFIG_FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            try (InputStream input = new FileInputStream(file)) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.out.println("Failed to load properties.");
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        properties.setProperty(key, value);
        save();
    }

    private static void save() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.store(output, "Saved by ConfigManagerOne");
        } catch (IOException e) {
            System.out.println(" Failed to save properties.");
            e.printStackTrace();
        }
    }
}
