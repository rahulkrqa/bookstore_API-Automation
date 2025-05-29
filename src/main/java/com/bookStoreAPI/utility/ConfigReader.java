package com.bookStoreAPI.utility; // Package declaration

import java.io.FileInputStream; // For reading the utility file
import java.io.IOException; // For handling IO exceptions
import java.util.Properties; // For loading configuration properties
import java.io.File; // For path manipulation

/**
 * ConfigReader is a utility class to read configuration properties from a file (utility.properties).
 * It is used for environment and API configuration management.
 */
public class ConfigReader {

    // Static Properties object to hold utility values
    private static final Properties properties = new Properties();

    // Static block to load properties file only once at class load time
    static {
        try {
            // Build the path to utility.properties file
            String configPath = System.getProperty("user.dir") + File.separator + "src" +
                    File.separator + "test" + File.separator + "resources" +
                    File.separator + "config.properties";
            // Open file input stream
            FileInputStream input = new FileInputStream(configPath);
            // Load properties from the file
            properties.load(input);
            // Close the input stream (good practice)
            input.close();
        } catch (IOException e) {
            // Throw runtime exception if loading fails
            throw new RuntimeException("Failed to load utility.properties file.", e);
        }
    }

    // Get the basePagesPOJO URI for the API from utility
    public static String getBaseUri() {
        String value = properties.getProperty("base.uri");
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Missing required utility: basePagesPOJO.uri");
        }
        return value;
    }

    // Get the content type for API requests from utility
    public static String getContentType() {
        String value = properties.getProperty("content.type");
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Missing required utility: content.type");
        }
        return value;
    }

    // Generic getter for any property by key
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Missing required utility: " + key);
        }
        return value;
    }
}
