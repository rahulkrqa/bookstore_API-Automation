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

### ⚙️ Setup Notes

- Java 17 or higher recommended
- Maven is used for build and test execution
- Python dependencies are listed in `bookstore-main/requirements.txt`
- [Allure CLI](https://docs.qameta.io/allure/#_installing_a_commandline) must be installed for Allure reports

---

### 🧪 Test Execution & Reporting

#### Run Tests

```bash
mvn clean test
```

#### Generate Allure Report

```bash
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

#### View Extent Report

```bash
target/cucumber-reports/ExtentReport.html
```

---

### 🛠️ CI/CD Pipeline (GitHub Actions Example)

```yaml
name: CI Pipeline

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up Java
      uses: actions/setup-java@v3
      with:
        java-version: '17'

    - name: Install Python dependencies
      run: |
        cd bookstore-main
        pip install -r requirements.txt

    - name: Start FastAPI server
      run: |
        nohup uvicorn main:app --port 8000 &
      working-directory: bookstore-main

    - name: Run API Tests
      run: mvn clean test
```

---


## 🧪 Jenkins Integration

This project can be easily integrated into Jenkins for continuous integration and reporting.

### 🔧 Freestyle Job Setup

1. **Create a new Freestyle job** in Jenkins.
2. Under **Source Code Management**, choose Git and enter your GitHub repository URL.
3. Under **Build**, add a build step: `Invoke top-level Maven targets` and use:
   ```
   clean test
   ```
4. Under **Post-build Actions**, add **Allure Report** with results directory:
   ```
   target/allure-results
   ```

### 📊 Allure Report Access

Once the job is built, you will see an **Allure Report** link on the left sidebar. Click it to view the detailed test report.

### 🧰 Requirements on Jenkins Server

- **Java 17+**
- **Maven**
- **Python 3 & pip**
- **Allure CLI** (installed and added to system path)
- Required Jenkins plugins:
    - Git Plugin
    - Maven Integration
    - Allure Jenkins Plugin


### 📝 Jenkins Pipeline  ######

```groovy
pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'Java 17'
    }

    environment {
        ALLURE_RESULTS_DIR = "target/allure-results"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-username/bookstore-api-test.git'
            }
        }

        stage('Install Backend Dependencies') {
            steps {
                sh 'pip install -r bookstore-main/requirements.txt'
            }
        }

        stage('Start FastAPI Server') {
            steps {
                sh '''
                    cd bookstore-main
                    nohup uvicorn main:app --port 8000 &
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Allure Report') {
            steps {
                allure includeProperties: false, results: [[path: 'target/allure-results']]
            }
        }
    }
}

### 🔮 Future Enhancements

- Test parallelization using JUnit 5 or TestNG
- Dynamic environment and credential management
- Integration with Swagger/OpenAPI spec validation
- Docker support for backend and test containers
- Scheduled nightly runs and Allure history dashboard