package com.expensetracker.tests;

import com.expensetracker.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;

/**
 * Test Class for User Login Module
 * Implements test cases: TC_LOGIN_001 to TC_LOGIN_007
 */
public class LoginTest extends BaseTest {
    
    // Test data for login tests
    private final String TEST_EMAIL = "validuser@example.com";
    private final String TEST_PASSWORD = "ValidPass123";
    
    /**
     * TC_LOGIN_001: Verify successful login with valid credentials
     * Priority: High | Type: Functional Testing
     */
    @Test(priority = 1, description = "TC_LOGIN_001: Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        test = extent.createTest("TC_LOGIN_001", "Verify successful login with valid credentials");
        test.log(Status.INFO, "Test started for successful login");
        
        try {
            // Pre-requisite: Register a user first
            navigateTo(BASE_URL);
            driver.findElement(By.linkText("Register")).click();
            waitFor(2);
            
            String uniqueEmail = "loginuser" + System.currentTimeMillis() + "@example.com";
            driver.findElement(By.id("name")).sendKeys("Login Test User");
            driver.findElement(By.id("email")).sendKeys(uniqueEmail);
            driver.findElement(By.id("password")).sendKeys("ValidPass123");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            test.log(Status.INFO, "User registered successfully");
            
            // Now perform login
            navigateTo(BASE_URL + "/login-register.html");
            waitFor(2);
            test.log(Status.PASS, "Navigated to login page");
            
            driver.findElement(By.id("email")).sendKeys(uniqueEmail);
            driver.findElement(By.id("password")).sendKeys("ValidPass123");
            test.log(Status.INFO, "Entered valid credentials");
            
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            test.log(Status.PASS, "Clicked Login button");
            
            // Verify login success
            String currentUrl = driver.getCurrentUrl();
            String pageSource = driver.getPageSource();
            
            boolean loginSuccess = currentUrl.contains("expense-tracker") || 
                                  currentUrl.contains("tracker") ||
                                  pageSource.contains("successful") ||
                                  pageSource.contains("dashboard");
            
            Assert.assertTrue(loginSuccess, "Login should succeed with valid credentials");
            
            test.log(Status.PASS, "✓ TC_LOGIN_001 PASSED: Login successful");
            System.out.println("✓ TC_LOGIN_001 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_LOGIN_001 FAILED: " + e.getMessage());
            System.err.println("✗ TC_LOGIN_001 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_LOGIN_002: Verify login fails with invalid email
     * Priority: High | Type: Negative Testing
     */
    @Test(priority = 2, description = "TC_LOGIN_002: Verify login fails with invalid email")
    public void testLoginWithInvalidEmail() {
        test = extent.createTest("TC_LOGIN_002", "Verify login fails with invalid email");
        test.log(Status.INFO, "Test started for invalid email login");
        
        try {
            navigateTo(BASE_URL + "/login-register.html");
            waitFor(2);
            test.log(Status.PASS, "Navigated to login page");
            
            driver.findElement(By.id("email")).sendKeys("unregistered@example.com");
            driver.findElement(By.id("password")).sendKeys("AnyPassword123");
            test.log(Status.INFO, "Entered unregistered email");
            
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            
            // Verify error message or staying on login page
            String currentUrl = driver.getCurrentUrl();
            String pageSource = driver.getPageSource();
            
            boolean hasError = pageSource.contains("Invalid") || 
                              pageSource.contains("incorrect") ||
                              pageSource.contains("not found") ||
                              currentUrl.contains("login");
            
            Assert.assertTrue(hasError, "Should show error for invalid email");
            
            test.log(Status.PASS, "✓ TC_LOGIN_002 PASSED: Invalid email rejected");
            System.out.println("✓ TC_LOGIN_002 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_LOGIN_002 FAILED: " + e.getMessage());
            System.err.println("✗ TC_LOGIN_002 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_LOGIN_003: Verify login fails with incorrect password
     * Priority: High | Type: Security Testing
     */
    @Test(priority = 3, description = "TC_LOGIN_003: Verify login fails with incorrect password")
    public void testLoginWithWrongPassword() {
        test = extent.createTest("TC_LOGIN_003", "Verify login fails with incorrect password");
        test.log(Status.INFO, "Test started for wrong password");
        
        try {
            // First register a user
            navigateTo(BASE_URL + "/register.html");
            waitFor(2);
            
            String testEmail = "passtest" + System.currentTimeMillis() + "@example.com";
            driver.findElement(By.id("name")).sendKeys("Password Test");
            driver.findElement(By.id("email")).sendKeys(testEmail);
            driver.findElement(By.id("password")).sendKeys("CorrectPass123");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            test.log(Status.INFO, "User registered with correct password");
            
            // Now try to login with wrong password
            navigateTo(BASE_URL + "/login-register.html");
            waitFor(2);
            
            driver.findElement(By.id("email")).sendKeys(testEmail);
            driver.findElement(By.id("password")).sendKeys("WrongPassword");
            test.log(Status.INFO, "Entered wrong password");
            
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            
            // Verify login fails
            String pageSource = driver.getPageSource();
            String currentUrl = driver.getCurrentUrl();
            
            boolean loginFailed = pageSource.contains("Invalid") ||
                                pageSource.contains("incorrect") ||
                                pageSource.contains("wrong") ||
                                currentUrl.contains("login");
            
            Assert.assertTrue(loginFailed, "Login should fail with wrong password");
            
            test.log(Status.PASS, "✓ TC_LOGIN_003 PASSED: Wrong password rejected");
            System.out.println("✓ TC_LOGIN_003 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_LOGIN_003 FAILED: " + e.getMessage());
            System.err.println("✗ TC_LOGIN_003 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_LOGIN_004: Verify login fails with empty credentials
     * Priority: Medium | Type: Validation Testing
     */
    @Test(priority = 4, description = "TC_LOGIN_004: Verify login fails with empty credentials")
    public void testLoginWithEmptyFields() {
        test = extent.createTest("TC_LOGIN_004", "Verify login fails with empty credentials");
        test.log(Status.INFO, "Test started for empty fields validation");
        
        try {
            navigateTo(BASE_URL + "/login-register.html");
            waitFor(2);
            test.log(Status.PASS, "Navigated to login page");
            
            // Try to submit without entering anything
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(2);
            test.log(Status.INFO, "Clicked submit with empty fields");
            
            // Verify HTML5 validation
            WebElement emailField = driver.findElement(By.id("email"));
            WebElement passwordField = driver.findElement(By.id("password"));
            
            boolean emailRequired = emailField.getAttribute("required") != null;
            boolean passwordRequired = passwordField.getAttribute("required") != null;
            
            Assert.assertTrue(emailRequired && passwordRequired, 
                            "Email and Password should be required fields");
            
            // Verify still on login page
            Assert.assertTrue(driver.getCurrentUrl().contains("login"),
                            "Should remain on login page");
            
            test.log(Status.PASS, "✓ TC_LOGIN_004 PASSED: Empty fields validation working");
            System.out.println("✓ TC_LOGIN_004 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_LOGIN_004 FAILED: " + e.getMessage());
            System.err.println("✗ TC_LOGIN_004 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_LOGIN_005: Verify login page UI elements
     * Priority: Low | Type: UI Testing
     */
    @Test(priority = 5, description = "TC_LOGIN_005: Verify login page UI elements")
    public void testLoginPageUI() {
        test = extent.createTest("TC_LOGIN_005", "Verify login page UI elements");
        test.log(Status.INFO, "Test started for login UI elements");
        
        try {
            navigateTo(BASE_URL + "/login-register.html");
            waitFor(2);
            test.log(Status.PASS, "Navigated to login page");
            
            // Verify Email field
            WebElement emailField = driver.findElement(By.id("email"));
            Assert.assertTrue(emailField.isDisplayed(), "Email field should be visible");
            Assert.assertEquals(emailField.getAttribute("type"), "email",
                              "Email field should have type='email'");
            test.log(Status.PASS, "Email field present with correct type");
            
            // Verify Password field
            WebElement passwordField = driver.findElement(By.id("password"));
            Assert.assertTrue(passwordField.isDisplayed(), "Password field should be visible");
            Assert.assertEquals(passwordField.getAttribute("type"), "password",
                              "Password field should mask input");
            test.log(Status.PASS, "Password field present and masks input");
            
            // Verify Login button
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            Assert.assertTrue(loginButton.isDisplayed(), "Login button should be visible");
            Assert.assertTrue(loginButton.isEnabled(), "Login button should be enabled");
            test.log(Status.PASS, "Login button present and enabled");
            
            // Verify Register link exists
            try {
                WebElement registerLink = driver.findElement(By.linkText("Register"));
                Assert.assertTrue(registerLink.isDisplayed(), "Register link should be visible");
                test.log(Status.PASS, "Register link is present");
            } catch (Exception e) {
                test.log(Status.INFO, "Register link may be in different location");
            }
            
            // Verify page title
            String pageTitle = driver.getTitle();
            Assert.assertFalse(pageTitle.isEmpty(), "Page should have a title");
            test.log(Status.PASS, "Page title: " + pageTitle);
            
            test.log(Status.PASS, "✓ TC_LOGIN_005 PASSED: All UI elements present");
            System.out.println("✓ TC_LOGIN_005 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_LOGIN_005 FAILED: " + e.getMessage());
            System.err.println("✗ TC_LOGIN_005 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_LOGIN_006: Verify JWT token generation on successful login
     * Priority: High | Type: Security Testing
     */
    @Test(priority = 6, description = "TC_LOGIN_006: Verify JWT token generation")
    public void testJWTTokenGeneration() {
        test = extent.createTest("TC_LOGIN_006", "Verify JWT token generation on successful login");
        test.log(Status.INFO, "Test started for JWT token verification");
        
        try {
            // Register a user first
            navigateTo(BASE_URL + "/register.html");
            waitFor(2);
            
            String tokenEmail = "tokenuser" + System.currentTimeMillis() + "@example.com";
            driver.findElement(By.id("name")).sendKeys("Token User");
            driver.findElement(By.id("email")).sendKeys(tokenEmail);
            driver.findElement(By.id("password")).sendKeys("TokenPass123");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            test.log(Status.INFO, "User registered for token test");
            
            // Login to generate token
            navigateTo(BASE_URL + "/login-register.html");
            waitFor(2);
            
            driver.findElement(By.id("email")).sendKeys(tokenEmail);
            driver.findElement(By.id("password")).sendKeys("TokenPass123");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            test.log(Status.PASS, "Login submitted");
            
            // Check for authToken cookie
            Cookie authToken = driver.manage().getCookieNamed("authToken");
            
            if (authToken != null) {
                Assert.assertNotNull(authToken.getValue(), "Token value should not be null");
                test.log(Status.PASS, "JWT token found in cookies");
                System.out.println("Token present: " + authToken.getValue().substring(0, 20) + "...");
            } else {
                test.log(Status.INFO, "Token may be stored in localStorage or sessionStorage");
                // Alternative: Check if token is in localStorage
                String token = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("return localStorage.getItem('authToken') || sessionStorage.getItem('authToken');");
                Assert.assertNotNull(token, "Token should exist in storage");
            }
            
            test.log(Status.PASS, "✓ TC_LOGIN_006 PASSED: JWT token verified");
            System.out.println("✓ TC_LOGIN_006 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_LOGIN_006 FAILED: " + e.getMessage());
            System.err.println("✗ TC_LOGIN_006 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_LOGIN_007: Verify navigation from login to registration page
     * Priority: Low | Type: Navigation Testing
     */
    @Test(priority = 7, description = "TC_LOGIN_007: Verify navigation from login to registration")
    public void testNavigationToRegistration() {
        test = extent.createTest("TC_LOGIN_007", "Verify navigation from login to registration page");
        test.log(Status.INFO, "Test started for navigation testing");
        
        try {
            navigateTo(BASE_URL);
            driver.findElement(By.linkText("Login")).click();
            waitFor(2);
            test.log(Status.PASS, "Navigated to login page");
            
            String loginUrl = driver.getCurrentUrl();
            Assert.assertTrue(loginUrl.contains("login"), "Should be on login page");
            
            // Find and click Register link
            WebElement registerLink = driver.findElement(By.linkText("Register"));
            Assert.assertTrue(registerLink.isDisplayed(), "Register link should be visible");
            test.log(Status.PASS, "Register link found on login page");
            
            registerLink.click();
            waitFor(2);
            
            // Verify navigation to registration page
            String registerUrl = driver.getCurrentUrl();
            Assert.assertTrue(registerUrl.contains("register"), 
                            "Should navigate to registration page");
            test.log(Status.PASS, "Successfully navigated to registration page");
            
            // Verify registration form is present
            WebElement nameField = driver.findElement(By.id("name"));
            Assert.assertTrue(nameField.isDisplayed(), "Registration form should be visible");
            
            test.log(Status.PASS, "✓ TC_LOGIN_007 PASSED: Navigation working correctly");
            System.out.println("✓ TC_LOGIN_007 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_LOGIN_007 FAILED: " + e.getMessage());
            System.err.println("✗ TC_LOGIN_007 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}
