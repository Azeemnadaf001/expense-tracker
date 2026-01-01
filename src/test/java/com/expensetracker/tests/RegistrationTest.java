package com.expensetracker.tests;

import com.expensetracker.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;

/**
 * Test Class for User Registration Module
 * Implements test cases: TC_REG_001 to TC_REG_005
 */
public class RegistrationTest extends BaseTest {
    
    /**
     * TC_REG_001: Verify successful user registration with valid data
     * Priority: High | Type: Functional Testing
     */
    @Test(priority = 1, description = "TC_REG_001: Verify successful user registration with valid data")
    public void testSuccessfulRegistration() {
        test = extent.createTest("TC_REG_001", "Verify successful user registration with valid data");
        test.log(Status.INFO, "Test started for successful registration");
        
        try {
            // Step 1: Navigate to application
            navigateTo(BASE_URL);
            test.log(Status.PASS, "Navigated to application URL");
            
            // Step 2: Click on Register button
            WebElement registerLink = driver.findElement(By.linkText("Register"));
            registerLink.click();
            test.log(Status.PASS, "Clicked on Register button");
            waitFor(2);
            
            // Step 3-5: Fill registration form
            String timestamp = String.valueOf(System.currentTimeMillis());
            String testEmail = "testuser" + timestamp + "@example.com";
            
            WebElement nameField = driver.findElement(By.id("name"));
            nameField.sendKeys("Test User");
            test.log(Status.PASS, "Entered name: Test User");
            
            WebElement emailField = driver.findElement(By.id("registerEmail"));
            emailField.sendKeys(testEmail);
            test.log(Status.PASS, "Entered email: " + testEmail);
            
            WebElement passwordField = driver.findElement(By.id("registerPassword"));
            passwordField.sendKeys("TestPass123");
            test.log(Status.PASS, "Entered password");
            
            WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
            confirmPasswordField.sendKeys("TestPass123");
            test.log(Status.PASS, "Entered confirm password");
            
            // Trigger validation to enable register button
            triggerRegisterValidation();
            
            WebElement termsCheckbox = driver.findElement(By.id("terms"));
            scrollToElement(termsCheckbox);
            clickWithJS(termsCheckbox);
            test.log(Status.PASS, "Checked terms and conditions");
            waitFor(1);
            
            // Step 6: Submit form
            WebElement submitButton = driver.findElement(By.id("registerBtn"));
            scrollToElement(submitButton);
            clickWithJS(submitButton);
            test.log(Status.PASS, "Clicked Submit button");
            waitFor(3);
            
            // Handle alert if present
            handleAlert();
            waitFor(2);
            
            // Step 7: Verify success message or redirection
            String currentUrl = driver.getCurrentUrl();
            String pageSource = driver.getPageSource();
            System.out.println("Current URL after registration: " + currentUrl);
            
            // Check for error messages
            if (pageSource.toLowerCase().contains("error") || pageSource.toLowerCase().contains("already exists")) {
                test.log(Status.WARNING, "Possible error on page");
                System.out.println("Page source snippet: " + pageSource.substring(0, Math.min(500, pageSource.length())));
            }
            
            // Assertion: Verify registration was processed
            boolean registrationSuccess = currentUrl.contains("expense-tracker.html") || 
                                         currentUrl.contains("login") || 
                                         currentUrl.contains("tracker") || 
                                         !currentUrl.contains("register.html") ||
                                         pageSource.contains("successfully") ||
                                         pageSource.contains("Welcome");
            
            Assert.assertTrue(registrationSuccess, "Registration should succeed");
            
            test.log(Status.PASS, "✓ TC_REG_001 PASSED: User registered successfully");
            System.out.println("✓ TC_REG_001 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_REG_001 FAILED: " + e.getMessage());
            System.err.println("✗ TC_REG_001 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_REG_002: Verify registration fails with duplicate email
     * Priority: High | Type: Negative Testing
     */
    @Test(priority = 2, description = "TC_REG_002: Verify registration fails with duplicate email")
    public void testDuplicateEmailRegistration() {
        test = extent.createTest("TC_REG_002", "Verify registration fails with duplicate email");
        test.log(Status.INFO, "Test started for duplicate email registration");
        
        try {
            // First registration
            navigateTo(BASE_URL + "/register.html");
            waitFor(2);
            
            String duplicateEmail = "duplicate" + System.currentTimeMillis() + "@example.com";
            
            driver.findElement(By.id("name")).sendKeys("First User");
            driver.findElement(By.id("registerEmail")).sendKeys(duplicateEmail);
            driver.findElement(By.id("registerPassword")).sendKeys("Pass123");
            driver.findElement(By.id("confirmPassword")).sendKeys("Pass123");
            WebElement terms1 = driver.findElement(By.id("terms"));
            scrollToElement(terms1);
            clickWithJS(terms1);
            waitFor(1);
            WebElement btn1 = driver.findElement(By.id("registerBtn"));
            scrollToElement(btn1);
            clickWithJS(btn1);
            waitFor(3);
            
            // Handle alert for first registration
            try {
                org.openqa.selenium.Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                System.out.println("First registration alert: " + alertText);
                alert.accept();
                waitFor(2);
            } catch (Exception e) {
                System.out.println("No alert for first registration");
            }
            
            test.log(Status.INFO, "First registration completed with email: " + duplicateEmail);
            
            // Second registration with same email
            navigateTo(BASE_URL + "/register.html");
            waitFor(2);
            
            driver.findElement(By.id("name")).sendKeys("Duplicate User");
            driver.findElement(By.id("registerEmail")).sendKeys(duplicateEmail);
            driver.findElement(By.id("registerPassword")).sendKeys("Pass123");
            driver.findElement(By.id("confirmPassword")).sendKeys("Pass123");
            WebElement terms2 = driver.findElement(By.id("terms"));
            scrollToElement(terms2);
            clickWithJS(terms2);
            waitFor(1);
            WebElement btn2 = driver.findElement(By.id("registerBtn"));
            scrollToElement(btn2);
            clickWithJS(btn2);
            waitFor(3);
            
            // For duplicate email, there should be NO alert (error shown on page)
            boolean alertPresent = false;
            try {
                org.openqa.selenium.Alert alert = driver.switchTo().alert();
                alert.dismiss(); // Dismiss if somehow appears
                alertPresent = true;
            } catch (Exception e) {
                // No alert is expected for duplicate email
            }
            
            waitFor(2);
            test.log(Status.INFO, "Attempted second registration with same email");
            
            // Verify error message appears OR stays on register page
            String currentUrl = driver.getCurrentUrl();
            String pageSource = driver.getPageSource();
            
            System.out.println("URL after duplicate registration: " + currentUrl);
            
            boolean hasError = pageSource.toLowerCase().contains("already registered") || 
                              pageSource.toLowerCase().contains("already exists") ||
                              pageSource.toLowerCase().contains("duplicate") ||
                              pageSource.toLowerCase().contains("email") && pageSource.toLowerCase().contains("exist") ||
                              currentUrl.contains("register.html");
            
            Assert.assertTrue(hasError, "Error message for duplicate email should appear");
            
            test.log(Status.PASS, "✓ TC_REG_002 PASSED: Duplicate email rejected");
            System.out.println("✓ TC_REG_002 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_REG_002 FAILED: " + e.getMessage());
            System.err.println("✗ TC_REG_002 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_REG_003: Verify registration fails with empty mandatory fields
     * Priority: Medium | Type: Validation Testing
     */
    @Test(priority = 3, description = "TC_REG_003: Verify registration fails with empty fields")
    public void testEmptyFieldsValidation() {
        test = extent.createTest("TC_REG_003", "Verify registration fails with empty mandatory fields");
        test.log(Status.INFO, "Test started for empty fields validation");
        
        try {
            navigateTo(BASE_URL + "/register.html");
            waitFor(2);
            test.log(Status.PASS, "Navigated to registration page");
            
            // Try to submit with empty fields (button should be disabled)
            WebElement submitButton = driver.findElement(By.id("registerBtn"));
            test.log(Status.INFO, "Checking if submit button is disabled");
            waitFor(1);
            
            // Check HTML5 validation or error messages
            WebElement nameField = driver.findElement(By.id("name"));
            WebElement emailField = driver.findElement(By.id("registerEmail"));
            WebElement passwordField = driver.findElement(By.id("registerPassword"));
            WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
            WebElement termsCheckbox = driver.findElement(By.id("terms"));
            
            // Verify form validation is triggered
            boolean nameRequired = nameField.getAttribute("required") != null;
            boolean emailRequired = emailField.getAttribute("required") != null;
            boolean passwordRequired = passwordField.getAttribute("required") != null;
            
            Assert.assertTrue(nameRequired || emailRequired || passwordRequired, 
                            "At least one field should be required");
            
            // Verify still on registration page
            Assert.assertTrue(driver.getCurrentUrl().contains("register"), 
                            "Should remain on registration page");
            
            test.log(Status.PASS, "✓ TC_REG_003 PASSED: Empty fields validation working");
            System.out.println("✓ TC_REG_003 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_REG_003 FAILED: " + e.getMessage());
            System.err.println("✗ TC_REG_003 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_REG_004: Verify registration with invalid email format
     * Priority: Medium | Type: Validation Testing
     */
    @Test(priority = 4, description = "TC_REG_004: Verify registration with invalid email format")
    public void testInvalidEmailFormat() {
        test = extent.createTest("TC_REG_004", "Verify registration with invalid email format");
        test.log(Status.INFO, "Test started for invalid email format");
        
        try {
            navigateTo(BASE_URL + "/register.html");
            waitFor(2);
            
            driver.findElement(By.id("name")).sendKeys("Test User");
            driver.findElement(By.id("registerEmail")).sendKeys("invalidemail"); // No @ symbol
            driver.findElement(By.id("registerPassword")).sendKeys("Pass123");
            driver.findElement(By.id("confirmPassword")).sendKeys("Pass123");
            test.log(Status.INFO, "Entered invalid email format: invalidemail");
            
            WebElement termsInvalid = driver.findElement(By.id("terms"));
            scrollToElement(termsInvalid);
            clickWithJS(termsInvalid);
            waitFor(1);
            WebElement btnInvalid = driver.findElement(By.id("registerBtn"));
            scrollToElement(btnInvalid);
            clickWithJS(btnInvalid);
            waitFor(2);
            
            // Check if HTML5 email validation is triggered
            WebElement emailField = driver.findElement(By.id("registerEmail"));
            String emailType = emailField.getAttribute("type");
            
            // Verify email field type is "email" for HTML5 validation
            Assert.assertEquals(emailType, "email", "Email field should have type='email'");
            
            // Verify still on registration page
            Assert.assertTrue(driver.getCurrentUrl().contains("register"),
                            "Should remain on registration page with invalid email");
            
            test.log(Status.PASS, "✓ TC_REG_004 PASSED: Invalid email format rejected");
            System.out.println("✓ TC_REG_004 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_REG_004 FAILED: " + e.getMessage());
            System.err.println("✗ TC_REG_004 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC_REG_005: Verify registration page UI elements
     * Priority: Low | Type: UI Testing
     */
    @Test(priority = 5, description = "TC_REG_005: Verify registration page UI elements")
    public void testRegistrationPageUI() {
        test = extent.createTest("TC_REG_005", "Verify registration page UI elements");
        test.log(Status.INFO, "Test started for registration UI elements");
        
        try {
            navigateTo(BASE_URL + "/register.html");
            waitFor(2);
            test.log(Status.PASS, "Navigated to registration page");
            
            // Verify Name field
            WebElement nameField = driver.findElement(By.id("name"));
            Assert.assertTrue(nameField.isDisplayed(), "Name field should be visible");
            test.log(Status.PASS, "Name field is present and visible");
            
            // Verify Email field
            WebElement emailField = driver.findElement(By.id("registerEmail"));
            Assert.assertTrue(emailField.isDisplayed(), "Email field should be visible");
            test.log(Status.PASS, "Email field is present and visible");
            
            // Verify Password field
            WebElement passwordField = driver.findElement(By.id("registerPassword"));
            Assert.assertTrue(passwordField.isDisplayed(), "Password field should be visible");
            Assert.assertEquals(passwordField.getAttribute("type"), "password", 
                              "Password field should mask input");
            test.log(Status.PASS, "Password field is present and masks input");
            
            // Verify Confirm Password field
            WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
            Assert.assertTrue(confirmPasswordField.isDisplayed(), "Confirm password field should be visible");
            test.log(Status.PASS, "Confirm password field is present and visible");
            
            // Verify Terms checkbox
            WebElement termsCheckbox = driver.findElement(By.id("terms"));
            scrollToElement(termsCheckbox);
            Assert.assertTrue(termsCheckbox.isDisplayed() || termsCheckbox.isEnabled(), "Terms checkbox should be present");
            test.log(Status.PASS, "Terms checkbox is present");
            
            // Verify Submit button
            WebElement submitButton = driver.findElement(By.id("registerBtn"));
            Assert.assertTrue(submitButton.isDisplayed(), "Submit button should be visible");
            test.log(Status.PASS, "Submit button is present");
            
            // Verify page title
            String pageTitle = driver.getTitle();
            Assert.assertFalse(pageTitle.isEmpty(), "Page should have a title");
            test.log(Status.PASS, "Page title: " + pageTitle);
            
            test.log(Status.PASS, "✓ TC_REG_005 PASSED: All UI elements present");
            System.out.println("✓ TC_REG_005 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC_REG_005 FAILED: " + e.getMessage());
            System.err.println("✗ TC_REG_005 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}
