package com.bookstore.runners; // Package declaration

import org.junit.runner.RunWith; // JUnit runner
import io.cucumber.junit.Cucumber; // Cucumber runner
import io.cucumber.junit.CucumberOptions; // Cucumber options

/**
 * RegressionTestRunner executes Cucumber feature files tagged as @regression
 * and generates HTML, JSON, and Allure reports.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",           // Feature file location
        glue = "com.bookstore.stepdefs",                    // Step definitions package
        plugin = {
                "pretty",                                       // Console output
                "html:target/extent-report.html",               // Extent report HTML
                "json:target/cucumber.json",                    // JSON report for Allure
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" // Allure reporting plugin
        },
        tags = "@regression",                               // Only run scenarios tagged @regression
        monochrome = true                                   // Clean console output
)
public class RegressionTestRunner {
    // No code needed; configuration via annotations
}
