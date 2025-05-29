package com.bookStoreAPI.utilsPages; // Package declaration

import java.io.InputStream; // Import for file input stream
import java.util.Properties; // Import for properties handling

/**
 * ConfigLoader is a thread-safe singleton class to load configuration
 * properties from resources/utility.properties for global use.
 */
public class ConfigLoader {

    // Singleton instance variable (volatile for thread safety)
    private static volatile ConfigLoader configLoader;
    // Properties object to hold key-value pairs
    private final Properties properties;

    // Private constructor to prevent external instantiation
    private ConfigLoader() {
        properties = new Properties(); // Initialize properties object
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) { // Check if file exists in resources
                throw new RuntimeException("utility.properties not found in resources directory");
            }
            properties.load(input); // Load the properties from file
        } catch (Exception e) {
            throw new RuntimeException("Failed to load utility.properties file", e); // Fail fast if not loaded
        }
    }

    // Public method to get singleton instance (double-checked locking for thread safety)
    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            synchronized (ConfigLoader.class) {
                if (configLoader == null) {
                    configLoader = new ConfigLoader();
                }
            }
        }
        return configLoader;
    }

    // Get basePagesPOJO URL from loaded properties, with validation
    public String getBaseUrl() {
        String value = properties.getProperty("baseUrl");
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Missing required property: baseUrl");
        }
        return value;
    }

    // Generic getter for any property by key, with validation
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Missing required property: " + key);
        }
        return value;
    }
}
