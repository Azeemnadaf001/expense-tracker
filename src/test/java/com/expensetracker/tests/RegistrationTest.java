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
            
            WebElement emailField = driver.findElement(By.id("email"));
            emailField.sendKeys(testEmail);
            test.log(Status.PASS, "Entered email: " + testEmail);
            
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.sendKeys("TestPass123");
            test.log(Status.PASS, "Entered password");
            
            // Step 6: Submit form
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            submitButton.click();
            test.log(Status.PASS, "Clicked Submit button");
            waitFor(3);
            
            // Step 7: Verify success message or redirection
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL after registration: " + currentUrl);
            
            // Assertion: Verify registration was processed
            Assert.assertTrue(
                currentUrl.contains("login") || currentUrl.contains("tracker") || 
                driver.getPageSource().contains("successfully"),
                "Registration should succeed"
            );
            
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
            navigateTo(BASE_URL);
            driver.findElement(By.linkText("Register")).click();
            waitFor(1);
            
            String duplicateEmail = "duplicate" + System.currentTimeMillis() + "@example.com";
            
            driver.findElement(By.id("name")).sendKeys("First User");
            driver.findElement(By.id("email")).sendKeys(duplicateEmail);
            driver.findElement(By.id("password")).sendKeys("Pass123");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            test.log(Status.INFO, "First registration completed with email: " + duplicateEmail);
            
            // Second registration with same email
            navigateTo(BASE_URL + "/register.html");
            waitFor(2);
            
            driver.findElement(By.id("name")).sendKeys("Duplicate User");
            driver.findElement(By.id("email")).sendKeys(duplicateEmail);
            driver.findElement(By.id("password")).sendKeys("Pass123");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(3);
            test.log(Status.INFO, "Attempted second registration with same email");
            
            // Verify error message appears
            String pageSource = driver.getPageSource();
            boolean hasError = pageSource.contains("already registered") || 
                              pageSource.contains("already exists") ||
                              pageSource.contains("duplicate");
            
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
            navigateTo(BASE_URL);
            driver.findElement(By.linkText("Register")).click();
            waitFor(2);
            test.log(Status.PASS, "Navigated to registration page");
            
            // Try to submit with empty fields
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            submitButton.click();
            test.log(Status.INFO, "Clicked submit with empty fields");
            waitFor(2);
            
            // Check HTML5 validation or error messages
            WebElement nameField = driver.findElement(By.id("name"));
            WebElement emailField = driver.findElement(By.id("email"));
            WebElement passwordField = driver.findElement(By.id("password"));
            
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
            driver.findElement(By.id("email")).sendKeys("invalidemail"); // No @ symbol
            driver.findElement(By.id("password")).sendKeys("Pass123");
            test.log(Status.INFO, "Entered invalid email format: invalidemail");
            
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            waitFor(2);
            
            // Check if HTML5 email validation is triggered
            WebElement emailField = driver.findElement(By.id("email"));
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
            navigateTo(BASE_URL);
            driver.findElement(By.linkText("Register")).click();
            waitFor(2);
            test.log(Status.PASS, "Navigated to registration page");
            
            // Verify Name field
            WebElement nameField = driver.findElement(By.id("name"));
            Assert.assertTrue(nameField.isDisplayed(), "Name field should be visible");
            test.log(Status.PASS, "Name field is present and visible");
            
            // Verify Email field
            WebElement emailField = driver.findElement(By.id("email"));
            Assert.assertTrue(emailField.isDisplayed(), "Email field should be visible");
            test.log(Status.PASS, "Email field is present and visible");
            
            // Verify Password field
            WebElement passwordField = driver.findElement(By.id("password"));
            Assert.assertTrue(passwordField.isDisplayed(), "Password field should be visible");
            Assert.assertEquals(passwordField.getAttribute("type"), "password", 
                              "Password field should mask input");
            test.log(Status.PASS, "Password field is present and masks input");
            
            // Verify Submit button
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            Assert.assertTrue(submitButton.isDisplayed(), "Submit button should be visible");
            Assert.assertTrue(submitButton.isEnabled(), "Submit button should be enabled");
            test.log(Status.PASS, "Submit button is present and enabled");
            
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
