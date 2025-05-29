package com.bookStoreAPI.utilsPages; // Package declaration

import io.qameta.allure.Allure; // Allure core annotation and step API
import io.qameta.allure.Step;   // For optional direct use in methods

import java.nio.charset.StandardCharsets;

/**
 * AllureReportUtil provides static methods for manually logging steps and attachments
 * to Allure reports from your Java code. Use together with @Step annotation for best results.
 */
public class AllureReportUtil {

    // Log a step to Allure report with a description (used for high-level flow steps)
    public static void logStep(String description) {
        Allure.step(description); // Adds step to Allure report
    }

    // Log a step with status ('PASS', 'FAIL', etc.) and message
    public static void logStatusStep(String status, String details) {
        Allure.step("[" + status + "] " + details); // Adds status-prefixed step
    }

    // Attach plain text to the Allure report (e.g., request/response data)
    public static void addTextAttachment(String name, String content) {
        Allure.addAttachment(name, content); // Adds a plain text attachment
    }

    // Attach arbitrary content with a type (e.g., JSON, XML, etc.)
    public static void addAttachment(String name, String type, String content) {
        Allure.addAttachment(name, type, content);
    }

    // Attach byte array data (for screenshots, binary files, etc.)
    public static void addByteAttachment(String name, byte[] data) {
        Allure.addAttachment(name, new String(data, StandardCharsets.UTF_8));
    }

    // Annotated version for step grouping in test methods (optional, for use in step definitions)
    @Step("{description}")
    public static void annotatedStep(String description) {
        // This method is just for annotation grouping; Allure will record the step automatically.
    }
}
