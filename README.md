# 📦 Release Notes

## 🚀 Version 1.0.0 - Initial Release

### 🔥 Highlights

- 🎯 Complete API automation framework targeting a FastAPI-based Bookstore application
- 🧪 BDD-style test cases using **Cucumber + RestAssured**
- ⚙️ **Automated backend launch** with Java’s ProcessBuilder
- 📊 Dual Reporting:
  - ✅ **ExtentReports** for clean HTML summaries
  - ✅ **Allure Reports** for detailed, interactive test analytics
- 🛠️ **Reusable utilities** for service logic, configuration, and reporting
- ☁️ **CI/CD ready** with GitHub Actions integration
- 🧱 Modular, maintainable project structure designed for scalability

---

### ✅ Features

- Full authentication flow coverage (signup, login, edge cases)
- CRUD operations for Book Management with robust validations
- Centralized request specification and configuration handling
- Automatic FastAPI server startup before tests
- Dual test reports (Extent + Allure) for different stakeholder needs
- Clean separation of concerns: service classes, hooks, step definitions, and utils

---

### 📁 Folder Structure

```
bookstore-api-test/
├── bookstore-main/             # FastAPI backend
│   ├── main.py                 # FastAPI app entry point
│   └── requirements.txt        # Python dependencies
├── src/
│   └── main/java/com/bookStoreAPI
│       ├── basePagePOJO/               # Request payload models
│       ├── UserServices/             # API service classes
│       ├── utility/            # Configuration reader 
│       ├── utilsPages/              # Utilities (reporting, server, etc.)
│   └── test/java/com/bookstore
│       ├── stepdefs hooks/           # Hooks for setup/teardown
│       ├── stepdefs/              #  Step definitions
├── features/                   # Cucumber feature files
│   
├── pom.xml                     # Maven config
└── README.md                   # Project documentation
```

---

