package com.bookstore.hooks; // Package declaration

import com.bookStoreAPI.utilsPages.ExtentReportUtil; // Extent report utility
import com.bookStoreAPI.utilsPages.AllureReportUtil; // Allure report utility
import com.bookStoreAPI.utilsPages.ServerManager;    // Server manager

import io.cucumber.java.BeforeAll; // For Cucumber's BeforeAll hook

public class TestSetup {

    @BeforeAll
    public static void globalSetup() {
        ExtentReportUtil.initReport();  // Always initialize Extent report first
        ExtentReportUtil.createTest("FastAPI Server Setup"); // Create an Extent test node
//        AllureReportUtil.logStep("FastAPI Server Setup Initialization"); // Allure step

        ServerManager.startServer(); // Start the FastAPI server (this will log to both reports)
    }
}
