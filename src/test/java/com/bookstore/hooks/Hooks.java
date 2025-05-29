package com.bookstore.hooks;

import com.bookStoreAPI.utilsPages.ExtentReportUtil;
import com.bookStoreAPI.utilsPages.AllureReportUtil; // Import Allure utility
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        ExtentReportUtil.createTest(scenario.getName());
        ExtentReportUtil.step("INFO", "Starting Scenario: " + scenario.getName());
        AllureReportUtil.logStep("Starting Scenario: " + scenario.getName()); // Allure step
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            ExtentReportUtil.step("FAIL", "Scenario failed: " + scenario.getName());
            AllureReportUtil.logStatusStep("FAIL", "Scenario failed: " + scenario.getName()); // Allure failure
        } else {
            ExtentReportUtil.step("PASS", "Scenario passed: " + scenario.getName());
            AllureReportUtil.logStatusStep("PASS", "Scenario passed: " + scenario.getName()); // Allure pass
        }
        ExtentReportUtil.flushReport();
        // Allure report is automatically managed by the adapter
    }
}
