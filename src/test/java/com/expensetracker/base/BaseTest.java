package com.expensetracker.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.Duration;

/**
 * Base Test Class for Selenium WebDriver Tests
 * Handles browser initialization, configuration, and reporting
 */
public class BaseTest {
    
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ExtentTest test;
    
    // Application URL
    protected static final String BASE_URL = "http://localhost:3000";
    
    // Browser configuration - Change this to switch browsers
    // Options: "chrome", "firefox"
    protected String browser = System.getProperty("browser", "chrome");
    
    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Expense Tracker Test Report");
        sparkReporter.config().setReportName("Selenium Automation Test Results");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "Expense Tracker");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Browser", browser);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Tester", "SE - Web Technology Team");
    }
    
    @BeforeMethod
    @Parameters({"browser"})
    public void setup(@Optional("chrome") String browserName) {
        this.browser = browserName;
        
        // Initialize WebDriver based on browser parameter
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
            System.out.println("✓ Chrome Driver initialized successfully");
        } 
        else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
            driver = new FirefoxDriver(options);
            System.out.println("✓ Firefox Driver initialized successfully");
        } 
        else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        System.out.println("✓ Navigating to: " + BASE_URL);
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✓ Browser closed successfully");
        }
    }
    
    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
            System.out.println("✓ Test report generated successfully");
            System.out.println("Report location: test-output/ExtentReport.html");
        }
    }
    
    /**
     * Navigate to a specific page
     * @param url Page URL
     */
    protected void navigateTo(String url) {
        driver.get(url);
        System.out.println("Navigated to: " + url);
    }
    
    /**
     * Wait for a specified time in seconds
     * @param seconds Time to wait
     */
    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Click element using JavaScript (for elements that are not directly interactable)
     * @param element WebElement to click
     */
    protected void clickWithJS(org.openqa.selenium.WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
    
    /**
     * Scroll element into view
     * @param element WebElement to scroll to
     */
    protected void scrollToElement(org.openqa.selenium.WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        waitFor(1);
    }
    
    /**
     * Trigger validation on registration form fields
     * This dispatches input events to enable the register button
     */
    protected void triggerRegisterValidation() {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "var fields = ['name', 'registerEmail', 'registerPassword', 'confirmPassword'];" +
            "fields.forEach(function(id) {" +
            "  var element = document.getElementById(id);" +
            "  if (element) element.dispatchEvent(new Event('input', { bubbles: true }));" +
            "});"
        );
        waitFor(1);
    }
    
    /**
     * Trigger validation on login form fields
     * This dispatches input events to enable the login button
     */
    protected void triggerLoginValidation() {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "var fields = ['email', 'password'];" +
            "fields.forEach(function(id) {" +
            "  var element = document.getElementById(id);" +
            "  if (element) element.dispatchEvent(new Event('input', { bubbles: true }));" +
            "});"
        );
        waitFor(1);
    }
    
    /**
     * Handle alert if present
     * @return true if alert was handled, false if no alert present
     */
    protected boolean handleAlert() {
        try {
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("Alert detected: " + alertText);
            alert.accept();
            return true;
        } catch (org.openqa.selenium.NoAlertPresentException e) {
            return false;
        }
    }
}
