const { Builder, By, until } = require("selenium-webdriver");
const chrome = require("selenium-webdriver/chrome");

/**
 * E2E Smoke Test for Expense Tracker
 *
 * This test verifies that:
 * 1. The application server is running
 * 2. The homepage loads successfully
 * 3. Basic page elements are present
 */

const BASE_URL = process.env.BASE_URL || "http://localhost:3000";
const TIMEOUT = 10000; // 10 seconds

async function runSmokeTest() {
  let driver;
  let testsPassed = 0;
  let testsFailed = 0;

  console.log("🚀 Starting E2E Smoke Tests...\n");

  try {
    // Configure Chrome options for headless mode (optional)
    const options = new chrome.Options();

    // Uncomment the following lines to run in headless mode
    // options.addArguments('--headless');
    // options.addArguments('--disable-gpu');
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--window-size=1920,1080");

    // Initialize WebDriver
    console.log("📦 Initializing Chrome WebDriver...");
    driver = await new Builder()
      .forBrowser("chrome")
      .setChromeOptions(options)
      .build();

    console.log("✅ WebDriver initialized successfully\n");

    // Test 1: Navigate to homepage
    console.log(`Test 1: Navigate to ${BASE_URL}`);
    try {
      await driver.get(BASE_URL);
      await driver.wait(until.elementLocated(By.tagName("body")), TIMEOUT);
      console.log("✅ Test 1 PASSED: Homepage loaded successfully\n");
      testsPassed++;
    } catch (error) {
      console.error("❌ Test 1 FAILED:", error.message, "\n");
      testsFailed++;
    }

    // Test 2: Verify page title
    console.log("Test 2: Verify page title");
    try {
      const title = await driver.getTitle();
      if (title && title.length > 0) {
        console.log(`✅ Test 2 PASSED: Page title is "${title}"\n`);
        testsPassed++;
      } else {
        throw new Error("Page title is empty");
      }
    } catch (error) {
      console.error("❌ Test 2 FAILED:", error.message, "\n");
      testsFailed++;
    }

    // Test 3: Verify page has HTML content
    console.log("Test 3: Verify page has HTML content");
    try {
      const body = await driver.findElement(By.tagName("body"));
      const bodyText = await body.getText();
      if (bodyText && bodyText.length > 0) {
        console.log("✅ Test 3 PASSED: Page has content\n");
        testsPassed++;
      } else {
        throw new Error("Page body is empty");
      }
    } catch (error) {
      console.error("❌ Test 3 FAILED:", error.message, "\n");
      testsFailed++;
    }

    // Test 4: Check for navigation/links (basic structure test)
    console.log("Test 4: Verify page has links");
    try {
      const links = await driver.findElements(By.tagName("a"));
      if (links.length > 0) {
        console.log(
          `✅ Test 4 PASSED: Found ${links.length} links on the page\n`
        );
        testsPassed++;
      } else {
        throw new Error("No links found on the page");
      }
    } catch (error) {
      console.error("❌ Test 4 FAILED:", error.message, "\n");
      testsFailed++;
    }

    // Test 5: Take a screenshot for verification
    console.log("Test 5: Take screenshot");
    try {
      const screenshot = await driver.takeScreenshot();
      const fs = require("fs");
      const path = require("path");
      const screenshotDir = path.join(__dirname, "..", "screenshots");

      // Create screenshots directory if it doesn't exist
      if (!fs.existsSync(screenshotDir)) {
        fs.mkdirSync(screenshotDir, { recursive: true });
      }

      const timestamp = new Date()
        .toISOString()
        .replace(/:/g, "-")
        .split(".")[0];
      const screenshotPath = path.join(
        screenshotDir,
        `smoke-test-${timestamp}.png`
      );
      fs.writeFileSync(screenshotPath, screenshot, "base64");
      console.log(`✅ Test 5 PASSED: Screenshot saved to ${screenshotPath}\n`);
      testsPassed++;
    } catch (error) {
      console.error("❌ Test 5 FAILED:", error.message, "\n");
      testsFailed++;
    }
  } catch (error) {
    console.error("❌ Fatal error during test execution:", error.message);
    testsFailed++;
  } finally {
    // Close the browser
    if (driver) {
      await driver.quit();
      console.log("🔒 Browser closed\n");
    }

    // Print test summary
    console.log("=".repeat(50));
    console.log("📊 Test Summary");
    console.log("=".repeat(50));
    console.log(`Total Tests: ${testsPassed + testsFailed}`);
    console.log(`✅ Passed: ${testsPassed}`);
    console.log(`❌ Failed: ${testsFailed}`);
    console.log("=".repeat(50));

    // Exit with appropriate code
    if (testsFailed > 0) {
      console.log("\n❌ Some tests failed. Please check the output above.");
      process.exit(1);
    } else {
      console.log("\n🎉 All tests passed!");
      process.exit(0);
    }
  }
}

// Run the test
runSmokeTest();
