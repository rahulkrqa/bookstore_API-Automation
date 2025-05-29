package com.bookStoreAPI.utilsPages;

import java.io.BufferedReader; // Import for reading server process output
import java.io.File; // Import for directory handling
import java.io.IOException; // Import for exception handling
import java.io.InputStreamReader; // Import for reading process input

import com.bookStoreAPI.utility.ConfigReader; // Import for configuration reading

import io.restassured.RestAssured; // Import for REST API calls

public class ServerManager { // ServerManager class
    private static Process serverProcess; // Holds the server process so it can be stopped later

    public static void startServer() { // Method to start the FastAPI server

        // Check for CI environment variable, skip server start if present
        if (System.getenv("CI") != null) { // If running in CI
            System.out.println("CI environment detected — skipping FastAPI server startup."); // Print message
            ExtentReportUtil.step("INFO", "CI mode: FastAPI server is managed by GitHub Actions."); // Report step
            return; // Skip further execution
        }
        try { // Try block for exception handling

            ExtentReportUtil.step("INFO", "----------Server startup---------"); // Report server startup

            // Create a command to run the FastAPI server using uvicorn
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "uvicorn main:app --reload"); // Command to start server

            String projectRoot = System.getProperty("user.dir"); // Get current working directory
            File backendDir = new File(projectRoot + File.separator + "bookstore-main" + File.separator + "bookstore"); // Construct backend directory path
            pb.directory(backendDir); // Set process directory to backend

            serverProcess = pb.start(); // Start the server process and save it

            // Read and print the server logs in a new thread
            BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getInputStream())); // Reader for server output
            new Thread(() -> { // New thread for log output
                String line; // Variable to hold each line
                try { // Try block for reading lines
                    while ((line = reader.readLine()) != null) { // Read lines until null
                        System.out.println("SERVER LOG: " + line); // Print server log line
                    }
                } catch (IOException e) { // Catch IO exceptions
                    e.printStackTrace(); // Print stack trace
                }
            }).start(); // Start the log thread

            // Wait for FastAPI server to be up by checking the root endpoint
            boolean serverUp = false; // Flag for server status
            int maxRetries = 10; // Maximum retry attempts
            int delayMillis = 1000; // Delay between retries in milliseconds

            for (int i = 0; i < maxRetries; i++) { // Loop for retries
                if (isServerRunning()) { // If server is running
                    serverUp = true; // Set flag to true
                    break; // Exit loop
                }
                Thread.sleep(delayMillis); // Wait before retrying
            }

            if (!serverUp) { // If server isn't up after retries
                ExtentReportUtil.step("Fail", "Server started but not responding on http://1270.0.1:8000"); // Report failure
                throw new RuntimeException("Server started but not responding on http://127.0.0.1:8000"); // Throw exception
            }

            ExtentReportUtil.step("PASS", "FastAPI Server is up and ready!"); // Report success

        } catch (IOException | InterruptedException e) { // Catch IO and interruption exceptions
            throw new RuntimeException("Server startup failed.", e); // Throw runtime exception with cause
        }
    }

    public static boolean isServerRunning() {
        try {
            RestAssured.baseURI = ConfigReader.getBaseUri();
            io.restassured.response.Response response = RestAssured.get("/health");
            System.out.println("Server check HTTP status: " + response.getStatusCode());
            System.out.println("Server check response body: " + response.getBody().asString());
            return response.getStatusCode() == 200;
        } catch (Exception e) {
            System.out.println("Server check exception: " + e.getMessage());
            return false;
        }
    }



    public static void stopServer() { // Method to stop the FastAPI server
        if (serverProcess != null) { // If server process exists
            serverProcess.destroy(); // Destroy the server process
            System.out.println("🛑 FastAPI Server stopped."); // Print server stopped message
        }
    }
}
