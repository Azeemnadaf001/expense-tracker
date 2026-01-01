# Expense Tracker - SE Web Technology Course Project

A comprehensive web application to track daily expenses, manage budgets, and visualize spending habits with professional automated testing.

## 🚀 Features

- **User Registration & Authentication** with JWT tokens
- **Expense Management** - Add, edit, delete, and categorize expenses
- **Budget Tracking** - Set monthly budgets and monitor spending
- **Secure Authentication** - bcrypt password hashing
- **Responsive Design** - Works on desktop and mobile
- **Automated Testing** - Selenium WebDriver with Java & TestNG

## 📋 Technology Stack

### Frontend
- HTML5, CSS3, JavaScript
- Responsive Design

### Backend
- Node.js & Express.js
- MongoDB with Mongoose
- JWT Authentication
- bcrypt Password Hashing

### Testing Framework
- **Selenium WebDriver** (Java)
- **TestNG** Framework
- **Maven** Build Tool
- **Extent Reports** for test reporting
- **WebDriverManager** for automatic driver management
- **Cross-Browser Testing** (Chrome & Firefox)

## 📁 Project Structure

```
Expense_Tracker/
├── Public/                          # Frontend files
│   ├── index.html                  # Homepage
│   ├── login-register.html         # Login page
│   ├── register.html               # Registration page
│   └── expense-tracker.html        # Dashboard
├── server.js                        # Express backend server
├── package.json                     # Node.js dependencies
├── pom.xml                          # Maven configuration
├── TEST_CASES.md                    # 12 detailed manual test cases
├── TESTING_GUIDE.md                 # Complete testing documentation
└── src/test/java/                   # Selenium test automation
    └── com/expensetracker/
        ├── base/BaseTest.java       # Base test class
        └── tests/
            ├── RegistrationTest.java  # 5 registration tests
            └── LoginTest.java          # 7 login tests
```

## ⚙️ Getting Started

### Prerequisites

**For Running Application:**
- Node.js v14+ 
- MongoDB (running on localhost:27017)

**For Running Tests:**
- Java JDK 11+
- Apache Maven 3.6+
- Chrome & Firefox browsers
- Eclipse IDE (optional)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Azeemnadaf001/expense-tracker.git
   cd Expense_Tracker
   ```

2. **Install Node.js dependencies:**
   ```bash
   npm install
   ```

3. **Install Maven test dependencies:**
   ```bash
   mvn clean install
   ```

### Running the Application

1. **Start MongoDB:**
   ```bash
   # Windows
   net start MongoDB
   
   # Linux/Mac
   sudo systemctl start mongod
   ```

2. **Start the server:**
   ```bash
   npm start
   ```

3. **Access the application:**
   ```
   http://localhost:3000
   ```

## 🧪 Automated Testing

### Test Coverage

This project includes **12 comprehensive automated test cases**:

**Module 1: User Registration (5 tests)**
- TC_REG_001: Successful registration with valid data
- TC_REG_002: Duplicate email rejection
- TC_REG_003: Empty fields validation
- TC_REG_004: Invalid email format validation
- TC_REG_005: UI elements verification

**Module 2: User Login (7 tests)**
- TC_LOGIN_001: Successful login with valid credentials
- TC_LOGIN_002: Invalid email rejection
- TC_LOGIN_003: Incorrect password rejection
- TC_LOGIN_004: Empty credentials validation
- TC_LOGIN_005: UI elements verification
- TC_LOGIN_006: JWT token generation verification
- TC_LOGIN_007: Navigation from login to registration

### Running Tests

#### Command Line (Maven):

```bash
# Run all tests (default: Chrome)
mvn test

# Run on specific browser
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox

# Run specific test module
mvn test -Dtest=RegistrationTest
mvn test -Dtest=LoginTest

# Run with specific TestNG suite
mvn test -DsuiteXmlFile=src/test/resources/testng-chrome.xml
mvn test -DsuiteXmlFile=src/test/resources/testng-firefox.xml
```

#### Eclipse IDE:

1. **Import Project:**
   - File → Import → Maven → Existing Maven Projects
   - Browse to `Expense_Tracker` directory
   - Click Finish

2. **Run Tests:**
   - Right-click `testng.xml` in `src/test/resources/`
   - Select `Run As → TestNG Suite`
   - View results in Console and TestNG Results tab

### Test Reports

After test execution, reports are auto-generated in `test-output/`:

**📊 Extent Report** (Primary):
- Location: `test-output/ExtentReport.html`
- Features: Dashboard, pie charts, detailed logs, screenshots
- Open in browser: `start test-output\ExtentReport.html`

**📋 TestNG Report**:
- Location: `test-output/index.html`
- Features: Suite results, execution time, stack traces

### Cross-Browser Testing

**Chrome:**
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng-chrome.xml
```

**Firefox:**
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng-firefox.xml
```

### Test Results Expected

- **Total Tests:** 12
- **Target Pass Rate:** ≥ 80% (minimum 10 tests passing)
- **Execution Time:** ~2-3 minutes
- **Reports Generated:** Extent HTML + TestNG HTML + XML

## 📖 Documentation

- 📋 **[TEST_CASES.md](TEST_CASES.md)** - Detailed manual test cases with proper template
- 📖 **[TESTING_GUIDE.md](TESTING_GUIDE.md)** - Complete guide for running and managing tests

## 🔗 API Endpoints

### Authentication
- `POST /register` - Register new user
- `POST /login` - Login and get JWT token

### Expense Management (Requires Authentication)
- `POST /add-expense` - Add new expense
- `GET /get-expenses` - Get all user expenses
- `PUT /update-expense/:id` - Update expense
- `DELETE /delete-expense/:id` - Delete expense
- `PUT /update-budget` - Update monthly budget

## 🎓 Course Project Requirements Fulfilled

✅ **Manual Test Cases:** 12 test cases with proper template (Test Case ID, Title, Objective, Preconditions, Test Data, Steps, Expected Result, Priority, Type, Traceability)  
✅ **Selenium Setup:** Java + Selenium WebDriver configured with Maven  
✅ **Test Automation:** All 12 test cases automated with proper assertions  
✅ **Cross-Browser:** Chrome and Firefox support configured  
✅ **Eclipse Compatible:** Can be executed from Eclipse IDE  
✅ **Console Output:** Detailed execution logs visible in console  
✅ **Test Reports:** Extent Reports + TestNG Reports (≥80% coverage)  
✅ **No Deployment:** Focus on testing, no application deployment  
✅ **Minimal Changes:** Only testing-related files added  

## 🚦 Quick Start for Testing

```bash
# 1. Ensure application is running
npm start

# 2. In new terminal, run tests
mvn test

# 3. View report
start test-output\ExtentReport.html
```

## 🛠️ Troubleshooting

### Application Won't Start
```bash
# Check MongoDB
net start MongoDB

# Check port 3000
netstat -ano | findstr :3000
```

### Tests Fail to Run
```bash
# Update Maven dependencies
mvn clean install -U

# Verify Java version
java -version  # Should be 11+

# Verify Maven version
mvn -version   # Should be 3.6+
```

### Browser Driver Issues
- WebDriverManager automatically handles driver downloads
- Ensure Chrome/Firefox browsers are installed and updated

## 📝 Test Execution Checklist

Before running tests:
- [ ] MongoDB is running
- [ ] Application server is running (`npm start`)
- [ ] Application accessible at http://localhost:3000
- [ ] Maven dependencies installed (`mvn clean install`)
- [ ] Chrome and/or Firefox installed

## 👥 Contributing

This is an academic project. For course-related queries, contact the instructor.

## 📄 License

This project is open-source under the MIT License.

## 📧 Contact

For queries: [anuragchougule0160@gmail.com](mailto:anuragchougule0160@gmail.com)

---

**Course:** SE - Web Technology  
**Academic Year:** 2025-2026  
**Testing Framework:** Selenium WebDriver + Java + TestNG + Maven  
**Build Tool:** Apache Maven 3.6+  
**Report Generation:** Extent Reports 5.1.2 + TestNG 7.10.2  

---

## 🎯 Summary

This project demonstrates professional software testing practices with:
- 12 comprehensive automated test cases
- Cross-browser testing capability
- Detailed HTML reports with 80%+ coverage
- Industry-standard tools (Selenium, TestNG, Maven)
- Eclipse IDE integration
- Complete documentation

**Ready to execute! Follow [TESTING_GUIDE.md](TESTING_GUIDE.md) for detailed instructions.** 🚀
