package com.bookstore.hooks; // Package declaration

import com.bookStoreAPI.utilsPages.ServerManager;      // Server manager for FastAPI
import com.bookStoreAPI.utilsPages.ExtentReportUtil;   // Extent report utility
import com.bookStoreAPI.utilsPages.AllureReportUtil;   // Allure report utility
import io.cucumber.java.AfterAll;              // For Cucumber's AfterAll hook

public class TestTearDown {

    @AfterAll
    public static void globalTearDown() {
        ExtentReportUtil.step("INFO", "Tearing down: Stopping FastAPI Server.");
        AllureReportUtil.logStep("Tearing down: Stopping FastAPI Server.");
        ServerManager.stopServer();
        ExtentReportUtil.step("PASS", "FastAPI Server stopped successfully.");
        AllureReportUtil.logStatusStep("PASS", "FastAPI Server stopped successfully.");
    }
}
