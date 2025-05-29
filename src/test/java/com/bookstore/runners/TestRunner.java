package com.bookstore.runners; // Package declaration

import org.junit.runner.RunWith; // JUnit runner
import io.cucumber.junit.Cucumber; // Cucumber runner
import io.cucumber.junit.CucumberOptions; // Cucumber options

/**
 * TestRunner executes Cucumber feature files (default: @regression scenarios)
 * and generates HTML, JSON, and Allure reports.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",                // Feature file location
        glue = {"com.bookstore.stepdefs", "com.bookstore.hooks"},// Step definitions and hooks
        plugin = {
                "pretty",                                            // Console output
                "html:target/cucumber-reports",                      // HTML report
                "json:target/cucumber.json",                         // JSON report for Allure
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"   // Allure reporting plugin
        },
        monochrome = true,                                       // Clean console output
        tags = "@regression"                                     // Only run scenarios tagged @regression
)
public class TestRunner {
    // No code needed; configuration via annotations
}
