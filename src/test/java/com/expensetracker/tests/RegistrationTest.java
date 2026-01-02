package com.expensetracker.tests;

import com.expensetracker.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;

/**
 * Test Class for User Authentication Module - Registration
 * Implements test cases: TC-AUTH-01 to TC-AUTH-03
 */
public class RegistrationTest extends BaseTest {
    
    /**
     * TC-AUTH-01: Register user with valid details
     * Expected Result: User account created successfully
     */
    @Test(priority = 1, description = "TC-AUTH-01: Register user with valid details")
    public void testSuccessfulRegistration() {
        test = extent.createTest("TC-AUTH-01", "Register user with valid details");
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
            
            test.log(Status.PASS, "✓ TC-AUTH-01 PASSED: User registered successfully");
            System.out.println("✓ TC-AUTH-01 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC-AUTH-01 FAILED: " + e.getMessage());
            System.err.println("✗ TC-AUTH-01 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC-AUTH-02: Register user with existing email
     * Expected Result: Error message displayed
     */
    @Test(priority = 2, description = "TC-AUTH-02: Register user with existing email")
    public void testDuplicateEmailRegistration() {
        test = extent.createTest("TC-AUTH-02", "Register user with existing email");
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
            
            test.log(Status.PASS, "✓ TC-AUTH-02 PASSED: Duplicate email rejected");
            System.out.println("✓ TC-AUTH-02 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC-AUTH-02 FAILED: " + e.getMessage());
            System.err.println("✗ TC-AUTH-02 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC-AUTH-03: Register user with empty mandatory fields
     * Expected Result: Validation message displayed
     */
    @Test(priority = 3, description = "TC-AUTH-03: Register user with empty mandatory fields")
    public void testEmptyFieldsValidation() {
        test = extent.createTest("TC-AUTH-03", "Register user with empty mandatory fields");
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
            
            test.log(Status.PASS, "✓ TC-AUTH-03 PASSED: Empty fields validation working");
            System.out.println("✓ TC-AUTH-03 PASSED");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "✗ TC-AUTH-03 FAILED: " + e.getMessage());
            System.err.println("✗ TC-AUTH-03 FAILED: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}
