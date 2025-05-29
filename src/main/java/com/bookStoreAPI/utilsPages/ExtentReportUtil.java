package com.bookStoreAPI.utilsPages; // Package declaration

import com.aventstack.extentreports.*; // Import Extent core classes
import com.aventstack.extentreports.reporter.ExtentSparkReporter; // Import Spark reporter

/**
 * ExtentReportUtil provides static methods for initializing, logging, and flushing ExtentReports.
 * Call initReport() before any tests, use createTest() for each test, step() for step logging,
 * logValidation() for assertion results, and flushReport() at the end.
 */
public class ExtentReportUtil {
    private static ExtentReports extent; // Singleton ExtentReports instance
    private static ExtentTest test;      // Current test node

    // Initialize the Extent report (only once per run)
    public static void initReport() {
        if (extent == null) { // Singleton pattern
            ExtentSparkReporter reporter = new ExtentSparkReporter("target/extent-report.html"); // Report file location
            extent = new ExtentReports(); // Create main report object
            extent.attachReporter(reporter); // Attach the reporter
        }
    }

    // Start a new test with given name
    public static void createTest(String testName) {
        test = extent.createTest(testName); // Create and set current test node
    }

    // Log a step using status as string ("INFO", "PASS", "FAIL") and message details
    public static void step(String status, String details) {
        if (test != null) { // Only log if a test node exists
            switch (status.toUpperCase()) {
                case "INFO":
                    test.info(details);
                    break;
                case "PASS":
                    test.pass(details);
                    break;
                case "FAIL":
                    test.fail(details);
                    break;
                default:
                    test.info(details);
            }
        }
    }

    // Overloaded method to support ExtentReports' Status enum directly
    public static void step(Status status, String details) {
        step(status.toString(), details); // Delegate to string version for handling
    }

    // Flush the report to disk after all tests are done
    public static void flushReport() {
        if (extent != null) {
            extent.flush(); // Write all data to file
        }
    }

    /**
     * Log a validation (assertion) result with detailed info.
     * @param title     The validation title/description
     * @param expected  The expected value
     * @param actual    The actual value
     * @param expectedDesc Description for expected value
     * @param actualDesc   Description for actual value
     * @param pass      True if assertion passed, false otherwise
     */
    public static void logValidation(String title, Object expected, Object actual, String expectedDesc, String actualDesc, boolean pass) {
        if (test != null) {
            if (pass) {
                test.pass("✅ " + title + "\nExpected: " + expected + " - " + expectedDesc + "\nActual: " + actual + " - " + actualDesc);
            } else {
                test.fail("❌ " + title + "\nExpected: " + expected + " - " + expectedDesc + "\nActual: " + actual + " - " + actualDesc);
            }
        }
    }
}
