# MANUAL TEST CASES DOCUMENT

## Expense Tracker Web Application

**Project:** Expense Tracker  
**Prepared By:** SE - Web Technology Team  
**Date:** January 1, 2026  
**Version:** 1.0

---

## MODULE 1: USER REGISTRATION

### Test Case ID: TC_REG_001

**Title:** Verify successful user registration with valid data  
**Objective:** To verify that a new user can successfully register with valid credentials  
**Preconditions:**

- Application server is running on http://localhost:3000
- MongoDB database is connected and running
- User is not already registered

**Test Data:**

- Name: Test User
- Email: testuser@example.com
- Password: TestPass123

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Register" button in the navigation bar
3. Enter "Test User" in the Name field
4. Enter "testuser@example.com" in the Email field
5. Enter "TestPass123" in the Password field
6. Click on the "Register" or "Submit" button
7. Observe the response message

**Expected Result:**

- User should be successfully registered
- Success message "User registered successfully!" should be displayed
- User details should be stored in the database
- User should be redirected or able to login

**Priority:** High  
**Test Type:** Functional Testing  
**Traceability:** US_REG_001 (User Story: As a new user, I want to register so that I can track my expenses)

---

### Test Case ID: TC_REG_002

**Title:** Verify registration fails with duplicate email  
**Objective:** To verify that system prevents registration with an already registered email  
**Preconditions:**

- Application server is running on http://localhost:3000
- MongoDB database is connected
- Email "duplicate@example.com" is already registered

**Test Data:**

- Name: Duplicate User
- Email: duplicate@example.com (already exists)
- Password: Pass123

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Register" button
3. Enter "Duplicate User" in the Name field
4. Enter "duplicate@example.com" in the Email field
5. Enter "Pass123" in the Password field
6. Click on the "Register" button
7. Observe the error message

**Expected Result:**

- Registration should fail
- Error message "Email already registered." should be displayed
- No new user record should be created in database
- User should remain on registration page

**Priority:** High  
**Test Type:** Negative Testing  
**Traceability:** US_REG_002 (User Story: As a system, I want to prevent duplicate registrations)

---

### Test Case ID: TC_REG_003

**Title:** Verify registration fails with empty mandatory fields  
**Objective:** To verify that system validates all mandatory fields during registration  
**Preconditions:**

- Application server is running
- User is on registration page

**Test Data:**

- Name: (empty)
- Email: (empty)
- Password: (empty)

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Register" button
3. Leave all fields empty
4. Click on the "Register" button
5. Observe validation messages

**Expected Result:**

- Registration should not proceed
- Validation error messages should appear for required fields
- Form should highlight empty fields
- No database entry should be created

**Priority:** Medium  
**Test Type:** Validation Testing  
**Traceability:** US_REG_003 (User Story: As a system, I want to validate input fields)

---

### Test Case ID: TC_REG_004

**Title:** Verify registration with invalid email format  
**Objective:** To verify that system validates email format during registration  
**Preconditions:**

- Application server is running
- User is on registration page

**Test Data:**

- Name: Test User
- Email: invalidemail (no @ symbol)
- Password: Pass123

**Test Steps:**

1. Navigate to http://localhost:3000/register.html
2. Enter "Test User" in the Name field
3. Enter "invalidemail" (without @) in the Email field
4. Enter "Pass123" in the Password field
5. Click on "Register" button
6. Observe validation message

**Expected Result:**

- Registration should not proceed
- Error message indicating invalid email format should appear
- Email field should be highlighted
- No database entry should be created

**Priority:** Medium  
**Test Type:** Validation Testing  
**Traceability:** US_REG_004 (User Story: As a system, I want to validate email format)

---

### Test Case ID: TC_REG_005

**Title:** Verify registration page UI elements  
**Objective:** To verify all UI elements are present and functional on registration page  
**Preconditions:**

- Application server is running

**Test Data:** N/A

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Register" button in navigation
3. Verify Name input field is present
4. Verify Email input field is present
5. Verify Password input field is present
6. Verify Register button is present
7. Verify all labels are visible
8. Check navigation links

**Expected Result:**

- All input fields should be visible and accessible
- All labels should be properly displayed
- Register button should be clickable
- Navigation bar should be visible
- Page should have proper styling

**Priority:** Low  
**Test Type:** UI Testing  
**Traceability:** US_REG_005 (User Story: As a user, I want a clear registration interface)

---

## MODULE 2: USER LOGIN

### Test Case ID: TC_LOGIN_001

**Title:** Verify successful login with valid credentials  
**Objective:** To verify that a registered user can successfully login with valid credentials  
**Preconditions:**

- Application server is running on http://localhost:3000
- MongoDB database is connected
- User is already registered with email: "validuser@example.com" and password: "ValidPass123"

**Test Data:**

- Email: validuser@example.com
- Password: ValidPass123

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Login" button in the navigation bar
3. Enter "validuser@example.com" in the Email field
4. Enter "ValidPass123" in the Password field
5. Click on the "Login" button
6. Observe the response

**Expected Result:**

- User should be successfully logged in
- Success message "Login successful!" should be displayed
- JWT token should be set in cookies
- User should be redirected to expense tracker dashboard
- User session should be created

**Priority:** High  
**Test Type:** Functional Testing  
**Traceability:** US_LOGIN_001 (User Story: As a registered user, I want to login to access my account)

---

### Test Case ID: TC_LOGIN_002

**Title:** Verify login fails with invalid email  
**Objective:** To verify that login fails when user enters an unregistered email  
**Preconditions:**

- Application server is running
- Email "unregistered@example.com" is NOT in the database

**Test Data:**

- Email: unregistered@example.com
- Password: AnyPassword123

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Login" button
3. Enter "unregistered@example.com" in the Email field
4. Enter "AnyPassword123" in the Password field
5. Click on the "Login" button
6. Observe the error message

**Expected Result:**

- Login should fail
- Error message "Invalid email or password." should be displayed
- User should not be authenticated
- No token should be generated
- User should remain on login page

**Priority:** High  
**Test Type:** Negative Testing  
**Traceability:** US_LOGIN_002 (User Story: As a system, I want to prevent unauthorized access)

---

### Test Case ID: TC_LOGIN_003

**Title:** Verify login fails with incorrect password  
**Objective:** To verify that login fails when user enters wrong password  
**Preconditions:**

- Application server is running
- User "testuser@example.com" is registered with password "CorrectPass123"

**Test Data:**

- Email: testuser@example.com
- Password: WrongPassword (incorrect)

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Login" button
3. Enter "testuser@example.com" in the Email field
4. Enter "WrongPassword" in the Password field
5. Click on the "Login" button
6. Observe the error message

**Expected Result:**

- Login should fail
- Error message "Invalid email or password." should be displayed
- User should not be authenticated
- No JWT token should be created
- User should remain on login page

**Priority:** High  
**Test Type:** Security Testing  
**Traceability:** US_LOGIN_003 (User Story: As a system, I want to validate passwords securely)

---

### Test Case ID: TC_LOGIN_004

**Title:** Verify login fails with empty credentials  
**Objective:** To verify that system validates mandatory fields on login  
**Preconditions:**

- Application server is running
- User is on login page

**Test Data:**

- Email: (empty)
- Password: (empty)

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Login" button
3. Leave both Email and Password fields empty
4. Click on the "Login" button
5. Observe validation messages

**Expected Result:**

- Login should not proceed
- Validation error messages should appear
- Required field indicators should be shown
- No API call should be made
- User should remain on login page

**Priority:** Medium  
**Test Type:** Validation Testing  
**Traceability:** US_LOGIN_004 (User Story: As a system, I want to validate login inputs)

---

### Test Case ID: TC_LOGIN_005

**Title:** Verify login page UI elements  
**Objective:** To verify all UI elements are present and functional on login page  
**Preconditions:**

- Application server is running

**Test Data:** N/A

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Login" button in navigation
3. Verify Email input field is present
4. Verify Password input field is present
5. Verify Login button is present
6. Verify "Register" link is present
7. Check password field has type="password"
8. Verify navigation bar is visible

**Expected Result:**

- All input fields should be visible and accessible
- Email field should accept text input
- Password field should mask characters
- Login button should be clickable
- Link to registration page should work
- Page should have proper styling and layout

**Priority:** Low  
**Test Type:** UI Testing  
**Traceability:** US_LOGIN_005 (User Story: As a user, I want a clear login interface)

---

### Test Case ID: TC_LOGIN_006

**Title:** Verify JWT token generation on successful login  
**Objective:** To verify that a valid JWT token is generated and stored after successful login  
**Preconditions:**

- Application server is running
- User "tokenuser@example.com" with password "TokenPass123" is registered

**Test Data:**

- Email: tokenuser@example.com
- Password: TokenPass123

**Test Steps:**

1. Navigate to http://localhost:3000
2. Open browser Developer Tools (F12)
3. Go to Application/Storage > Cookies
4. Click on "Login" button
5. Enter valid credentials
6. Click "Login" button
7. Check cookies for "authToken"
8. Verify token format and expiration

**Expected Result:**

- JWT token should be created
- Cookie named "authToken" should be present
- Token should be httpOnly
- Token should have 1 hour expiration
- Token should contain user id and email (when decoded)

**Priority:** High  
**Test Type:** Security Testing  
**Traceability:** US_LOGIN_006 (User Story: As a system, I want to maintain secure user sessions)

---

### Test Case ID: TC_LOGIN_007

**Title:** Verify navigation from login to registration page  
**Objective:** To verify that user can navigate to registration page from login page  
**Preconditions:**

- Application server is running

**Test Data:** N/A

**Test Steps:**

1. Navigate to http://localhost:3000
2. Click on "Login" button
3. Locate "Register" link on login page
4. Click on "Register" link
5. Verify redirection to registration page

**Expected Result:**

- User should be redirected to registration page
- URL should change to register.html
- Registration form should be displayed
- Navigation should be smooth without errors

**Priority:** Low  
**Test Type:** Navigation Testing  
**Traceability:** US_LOGIN_007 (User Story: As a new user, I want to access registration from login)

---

## TEST CASE SUMMARY

### Module 1: User Registration

- Total Test Cases: 5
- High Priority: 2
- Medium Priority: 2
- Low Priority: 1

### Module 2: User Login

- Total Test Cases: 7
- High Priority: 4
- Medium Priority: 1
- Low Priority: 2

**Total Test Cases: 12**

---

## TEST EXECUTION CHECKLIST

- [ ] All test cases reviewed
- [ ] Test environment setup completed
- [ ] Test data prepared
- [ ] Browser versions documented
- [ ] Test execution started
- [ ] Defects logged
- [ ] Test reports generated
- [ ] Sign-off obtained

---

## TRACEABILITY MATRIX

| Test Case ID   | User Story ID  | Module          | Priority |
| -------------- | -------------- | --------------- | -------- |
| TC_REG_001     | US_REG_001     | Registration    | High     |
| TC_REG_002     | US_REG_002     | Registration    | High     |
| TC_REG_003     | US_REG_003     | Registration    | Medium   |
| TC_REG_004     | US_REG_004     | Registration    | Medium   |
| TC_REG_005     | US_REG_005     | Registration    | Low      |
| TC_LOGIN_001   | US_LOGIN_001   | Login           | High     |
| TC_LOGIN_002   | US_LOGIN_002   | Login           | High     |
| TC_LOGIN_003   | US_LOGIN_003   | Login           | High     |
| TC_LOGIN_004   | US_LOGIN_004   | Login           | Medium   |
| TC_LOGIN_005   | US_LOGIN_005   | Login           | Low      |
| TC_LOGIN_006   | US_LOGIN_006   | Login           | High     |
| TC_LOGIN_007   | US_LOGIN_007   | Login           | Low      |
| TC_LOGIN_008   | US_LOGIN_008   | Login           | High     |
| TC_EXPENSE_001 | US_EXPENSE_001 | Expense Tracker | High     |
| TC_EXPENSE_002 | US_EXPENSE_002 | Expense Tracker | High     |
| TC_EXPENSE_003 | US_EXPENSE_003 | Expense Tracker | High     |
| TC_EXPENSE_004 | US_EXPENSE_004 | Expense Tracker | High     |
| TC_EXPENSE_005 | US_EXPENSE_005 | Expense Tracker | Medium   |
| TC_EXPENSE_006 | US_EXPENSE_006 | Expense Tracker | Medium   |

---

**Total Test Cases: 19**

- Registration: 5 test cases
- Login: 8 test cases
- Expense Tracker CRUD: 6 test cases

---

**Document End**
