# SELENIUM TEST AUTOMATION - EXECUTION GUIDE
## Expense Tracker Web Application Testing

**Project:** Expense Tracker - SE Web Technology Course Project  
**Framework:** Selenium WebDriver + Java + TestNG + Maven  
**Prepared By:** Testing Team  
**Date:** January 1, 2026

---

## TABLE OF CONTENTS
1. [Project Overview](#project-overview)
2. [Prerequisites](#prerequisites)
3. [Project Structure](#project-structure)
4. [Setup Instructions](#setup-instructions)
5. [Running Tests](#running-tests)
6. [Cross-Browser Testing](#cross-browser-testing)
7. [Test Reports](#test-reports)
8. [Eclipse IDE Setup](#eclipse-ide-setup)
9. [Troubleshooting](#troubleshooting)

---

## PROJECT OVERVIEW

This project contains automated Selenium WebDriver tests for the Expense Tracker web application.

**Modules Tested:**
1. **User Registration** - 5 Test Cases (TC_REG_001 to TC_REG_005)
2. **User Login** - 7 Test Cases (TC_LOGIN_001 to TC_LOGIN_007)

**Total Test Cases:** 12  
**Expected Pass Rate:** ≥ 80%

---

## PREREQUISITES

### Software Requirements
1. **Java JDK 11 or higher**
   - Download: https://www.oracle.com/java/technologies/javase-downloads.html
   - Verify installation: `java -version`

2. **Apache Maven 3.6+**
   - Download: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **Eclipse IDE for Java Developers**
   - Download: https://www.eclipse.org/downloads/
   - Version: 2023-12 or later

4. **Web Browsers**
   - Google Chrome (Latest version)
   - Mozilla Firefox (Latest version)

5. **MongoDB**
   - Required for application backend
   - Must be running on localhost:27017

6. **Node.js and npm**
   - Required to run the Expense Tracker application
   - Verify: `node --version` and `npm --version`

---

## PROJECT STRUCTURE

```
Expense_Tracker/
├── pom.xml                          # Maven configuration
├── TEST_CASES.md                    # Manual test cases document
├── TESTING_GUIDE.md                 # This file
├── src/
│   └── test/
│       ├── java/
│       │   └── com/expensetracker/
│       │       ├── base/
│       │       │   └── BaseTest.java         # Base test class
│       │       └── tests/
│       │           ├── RegistrationTest.java # Registration tests
│       │           └── LoginTest.java        # Login tests
│       └── resources/
│           ├── testng.xml                    # Default TestNG suite
│           ├── testng-chrome.xml             # Chrome-specific suite
│           └── testng-firefox.xml            # Firefox-specific suite
└── test-output/
    ├── ExtentReport.html                     # Extent Reports
    ├── testng-results.xml                    # TestNG XML results
    └── index.html                            # TestNG HTML report
```

---

## SETUP INSTRUCTIONS

### Step 1: Start the Application

1. Open terminal/command prompt
2. Navigate to the Expense_Tracker directory:
   ```bash
   cd E:/webtech/Expense_Tracker
   ```

3. Make sure MongoDB is running:
   ```bash
   # Windows
   net start MongoDB
   
   # Or check if running
   mongod --version
   ```

4. Install dependencies (if not done):
   ```bash
   npm install
   ```

5. Start the application server:
   ```bash
   npm start
   ```

6. Verify the application is accessible:
   - Open browser and navigate to `http://localhost:3000`
   - You should see the Expense Tracker homepage

### Step 2: Install Maven Dependencies

1. Open terminal in project directory
2. Run Maven install:
   ```bash
   mvn clean install
   ```

3. This will download:
   - Selenium WebDriver
   - TestNG framework
   - Extent Reports
   - WebDriverManager
   - All other dependencies

---

## RUNNING TESTS

### Method 1: Running from Command Line (Maven)

#### Run All Tests (Default - Chrome):
```bash
mvn clean test
```

#### Run Specific Test Suite:
```bash
# Run only Registration tests
mvn test -Dtest=RegistrationTest

# Run only Login tests
mvn test -Dtest=LoginTest
```

#### Run with Specific Browser:
```bash
# Run on Chrome
mvn test -Dbrowser=chrome

# Run on Firefox
mvn test -Dbrowser=firefox
```

#### Run Specific TestNG XML File:
```bash
# Default suite
mvn test -DsuiteXmlFile=src/test/resources/testng.xml

# Chrome suite
mvn test -DsuiteXmlFile=src/test/resources/testng-chrome.xml

# Firefox suite
mvn test -DsuiteXmlFile=src/test/resources/testng-firefox.xml
```

### Method 2: Running from Eclipse IDE

#### Setup Eclipse Project:

1. Open Eclipse IDE

2. Import Maven Project:
   - File → Import → Maven → Existing Maven Projects
   - Browse to `E:/webtech/Expense_Tracker`
   - Click Finish

3. Update Project:
   - Right-click on project → Maven → Update Project
   - Check "Force Update of Snapshots/Releases"
   - Click OK

#### Run Tests from Eclipse:

**Option A: Run Individual Test Class**
1. Navigate to `src/test/java/com/expensetracker/tests/`
2. Right-click on `RegistrationTest.java` or `LoginTest.java`
3. Select `Run As → TestNG Test`
4. View results in Console and TestNG Results tab

**Option B: Run TestNG XML Suite**
1. Navigate to `src/test/resources/`
2. Right-click on `testng.xml` (or `testng-chrome.xml` / `testng-firefox.xml`)
3. Select `Run As → TestNG Suite`
4. Results will appear in Console and TestNG Results panel

**Option C: Run as Maven Test**
1. Right-click on project root
2. Select `Run As → Maven test`
3. All tests will execute
4. Results in Console

#### View Console Output in Eclipse:
- Eclipse Console tab will show:
  - ✓ Test execution progress
  - ✓ Pass/Fail status
  - ✓ Detailed logs
  - ✓ Report generation messages

---

## CROSS-BROWSER TESTING

### Chrome Browser Testing

**Command Line:**
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng-chrome.xml
```

**Eclipse:**
1. Right-click `testng-chrome.xml`
2. Run As → TestNG Suite

**What Happens:**
- WebDriverManager automatically downloads ChromeDriver
- Tests run in Chrome browser
- Browser maximizes automatically
- Screenshots captured on failures

### Firefox Browser Testing

**Command Line:**
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng-firefox.xml
```

**Eclipse:**
1. Right-click `testng-firefox.xml`
2. Run As → TestNG Suite

**What Happens:**
- WebDriverManager automatically downloads GeckoDriver
- Tests run in Firefox browser
- Browser opens in specified dimensions
- Screenshots captured on failures

### Switching Browsers

**Method 1: Change TestNG XML Parameter**
Edit `src/test/resources/testng.xml`:
```xml
<parameter name="browser" value="chrome"/>  <!-- Change to "firefox" -->
```

**Method 2: System Property**
```bash
mvn test -Dbrowser=firefox
```

**Method 3: Programmatic (in test code)**
Already configured in `BaseTest.java` - reads from system property

---

## TEST REPORTS

### Report Types Generated

#### 1. Extent Reports (HTML) - **PRIMARY REPORT**

**Location:** `test-output/ExtentReport.html`

**To View:**
1. After test execution completes
2. Navigate to `test-output` folder
3. Open `ExtentReport.html` in any browser

**Features:**
- ✅ Dashboard with test statistics
- ✅ Pass/Fail pie charts
- ✅ Test execution timeline
- ✅ Detailed test steps with logs
- ✅ System information
- ✅ Browser screenshots (on failure)
- ✅ Professional styling

**Coverage Calculation:**
- Total Tests: 12
- To achieve 80% pass rate: Minimum 10 tests must pass
- Report shows: (Passed Tests / Total Tests) × 100%

#### 2. TestNG HTML Report

**Location:** `test-output/index.html`

**Features:**
- Suite-level results
- Test execution time
- Exception stack traces
- Test groups and methods

#### 3. TestNG XML Report

**Location:** `test-output/testng-results.xml`

**Features:**
- Machine-readable XML format
- Can be parsed by CI/CD tools
- Contains detailed execution data

### Opening Reports

**Windows:**
```cmd
# Open Extent Report
start test-output\ExtentReport.html

# Open TestNG Report
start test-output\index.html
```

**Linux/Mac:**
```bash
# Open Extent Report
open test-output/ExtentReport.html

# Open TestNG Report
open test-output/index.html
```

**From Eclipse:**
1. Right-click on `test-output/ExtentReport.html`
2. Select `Open With → Web Browser`

### Report Contents

**Extent Report Dashboard:**
```
📊 Test Execution Summary
├── Total Tests: 12
├── Passed: X
├── Failed: Y
├── Skipped: Z
└── Pass Percentage: XX.XX%

📋 Module-wise Results
├── Registration Module: 5 tests
└── Login Module: 7 tests

⏱️ Execution Timeline
└── Shows execution time for each test

🔍 Detailed Test Results
├── Test Name
├── Test Description
├── Execution Steps
├── Assertions
├── Screenshots (if failed)
└── Logs
```

---

## ECLIPSE IDE SETUP

### Installing TestNG Plugin in Eclipse

1. Open Eclipse
2. Go to `Help → Eclipse Marketplace`
3. Search for "TestNG"
4. Click "Install" on "TestNG for Eclipse"
5. Restart Eclipse

### Configuring Maven in Eclipse

1. Go to `Window → Preferences`
2. Navigate to `Maven → User Settings`
3. Verify Maven installation location
4. Click "Update Settings"

### Project Configuration

1. **Set JRE/JDK:**
   - Right-click project → Properties
   - Java Build Path → Libraries
   - Ensure JDK 11+ is selected

2. **Maven Dependencies:**
   - Right-click project → Maven → Update Project
   - Force update if needed

3. **Source Folders:**
   - Verify `src/test/java` is in build path
   - Verify `src/test/resources` is in build path

---

## TROUBLESHOOTING

### Common Issues and Solutions

#### Issue 1: Application Not Running
**Error:** Connection refused to localhost:3000

**Solution:**
```bash
# Check if server is running
netstat -ano | findstr :3000

# If not running, start the server
npm start

# Verify MongoDB is running
net start MongoDB
```

#### Issue 2: MongoDB Connection Error
**Error:** MongoServerError: connect ECONNREFUSED

**Solution:**
```bash
# Start MongoDB service
net start MongoDB

# Or manually start MongoDB
mongod --dbpath C:\data\db
```

#### Issue 3: Browser Driver Not Found
**Error:** WebDriverException: chrome/geckodriver not found

**Solution:**
- WebDriverManager should auto-download drivers
- If it fails, manually download:
  - ChromeDriver: https://chromedriver.chromium.org/
  - GeckoDriver: https://github.com/mozilla/geckodriver/releases
- Place in system PATH

#### Issue 4: Tests Fail Due to Timing
**Error:** NoSuchElementException or StaleElementReferenceException

**Solution:**
- Increase wait time in `BaseTest.java`:
  ```java
  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
  ```
- Add explicit waits in test methods:
  ```java
  waitFor(3); // Wait 3 seconds
  ```

#### Issue 5: Maven Build Fails
**Error:** Cannot resolve dependencies

**Solution:**
```bash
# Clear Maven cache
mvn dependency:purge-local-repository

# Re-download dependencies
mvn clean install -U
```

#### Issue 6: TestNG Not Found in Eclipse
**Error:** TestNG libraries missing

**Solution:**
1. Install TestNG plugin (see Eclipse IDE Setup section)
2. Right-click project → Properties → Java Build Path
3. Add Library → TestNG
4. Apply and Close

---

## EXECUTION CHECKLIST

Before running tests, ensure:

- [ ] MongoDB is running
- [ ] Node.js server is running (`npm start`)
- [ ] Application accessible at http://localhost:3000
- [ ] Chrome/Firefox browsers are installed
- [ ] Maven dependencies downloaded (`mvn clean install`)
- [ ] No tests are running simultaneously
- [ ] Previous test data cleared (optional)

---

## TEST EXECUTION COMMANDS QUICK REFERENCE

```bash
# Basic Commands
mvn clean test                                    # Run all tests (Chrome)
mvn test -Dbrowser=firefox                        # Run on Firefox
mvn test -Dtest=RegistrationTest                  # Run specific test class

# With Specific Suite
mvn test -DsuiteXmlFile=testng-chrome.xml         # Chrome suite
mvn test -DsuiteXmlFile=testng-firefox.xml        # Firefox suite

# Generate Reports Only
mvn surefire-report:report                        # Generate Surefire report

# Clean and Run
mvn clean test                                    # Clean and run tests

# Verbose Output
mvn test -X                                       # Debug mode
```

---

## EXPECTED TEST RESULTS

### Success Criteria

**Registration Module (5 tests):**
- TC_REG_001: ✅ Pass - Valid registration succeeds
- TC_REG_002: ✅ Pass - Duplicate email rejected
- TC_REG_003: ✅ Pass - Empty fields validated
- TC_REG_004: ✅ Pass - Invalid email format rejected
- TC_REG_005: ✅ Pass - UI elements present

**Login Module (7 tests):**
- TC_LOGIN_001: ✅ Pass - Valid login succeeds
- TC_LOGIN_002: ✅ Pass - Invalid email rejected
- TC_LOGIN_003: ✅ Pass - Wrong password rejected
- TC_LOGIN_004: ✅ Pass - Empty fields validated
- TC_LOGIN_005: ✅ Pass - UI elements present
- TC_LOGIN_006: ✅ Pass - JWT token generated
- TC_LOGIN_007: ✅ Pass - Navigation works

**Overall: 12/12 tests = 100% pass rate**

---

## CONTACT AND SUPPORT

For issues or questions regarding test execution:
1. Check this guide first
2. Review console logs for error details
3. Check test reports in `test-output/` directory
4. Verify application is running correctly
5. Contact course instructor if issues persist

---

**Document Version:** 1.0  
**Last Updated:** January 1, 2026  
**Status:** Ready for Execution

---

## SUMMARY

✅ **Manual Test Cases:** 12 test cases documented in TEST_CASES.md  
✅ **Automated Tests:** All 12 test cases automated in Java  
✅ **Cross-Browser:** Chrome and Firefox support configured  
✅ **Reporting:** Extent Reports + TestNG Reports (80%+ coverage)  
✅ **Eclipse Compatible:** Ready to run from Eclipse IDE  
✅ **Maven Integration:** Full Maven support with pom.xml  

**You are ready to execute tests! Good luck with your SE course project!** 🚀
