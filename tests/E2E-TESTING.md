# E2E Testing Guide

## Quick Start

### 1. Install Dependencies

```bash
npm install
```

### 2. Start the Application

In one terminal window:

```bash
npm start
```

Wait for the message: "Server is running on http://localhost:3000"

### 3. Run E2E Tests

In another terminal window:

```bash
npm run test:e2e
```

## Test Structure

```
tests/
└── e2e/
    └── smoke.test.js    # Basic smoke tests to verify app loads
```

## What Gets Tested

The smoke test verifies:

1. ✅ Homepage loads at http://localhost:3000
2. ✅ Page has a valid title
3. ✅ Page has content in the body
4. ✅ Navigation links are present
5. ✅ Screenshot is captured for verification

## Configuration

### Base URL

Default: `http://localhost:3000`

To test against a different URL:

```bash
BASE_URL=http://localhost:3001 npm run test:e2e
```

### Headless Mode

To run tests without opening a browser window:

1. Edit `tests/e2e/smoke.test.js`
2. Uncomment these lines (around line 26-27):
   ```javascript
   options.addArguments("--headless");
   options.addArguments("--disable-gpu");
   ```

## Troubleshooting

### Chrome Driver Issues

If you see ChromeDriver errors:

```bash
npm install chromedriver --save-dev --force
```

### Port Already in Use

If port 3000 is occupied:

```bash
# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Or use a different port
PORT=3001 npm start
BASE_URL=http://localhost:3001 npm run test:e2e
```

### MongoDB Connection Error

Ensure MongoDB is running:

```bash
# Windows (if installed as service)
net start MongoDB

# Or run manually
mongod --dbpath <your-data-path>
```

## Screenshots

Test screenshots are saved to `tests/screenshots/` and are automatically ignored by git.

## Next Steps

### Adding More Tests

Create additional test files in `tests/e2e/`:

```javascript
// tests/e2e/login.test.js
const { Builder, By, until } = require("selenium-webdriver");

async function testLogin() {
  const driver = await new Builder().forBrowser("chrome").build();
  try {
    await driver.get("http://localhost:3000/login");
    // Add your test logic here
  } finally {
    await driver.quit();
  }
}

testLogin();
```

Then update `package.json` to run all tests:

```json
"scripts": {
  "test:e2e": "node tests/e2e/smoke.test.js && node tests/e2e/login.test.js"
}
```

### Using a Test Framework

For more robust testing, consider integrating:

- **Mocha** or **Jest** for test organization
- **Chai** for assertions
- **Mochawesome** for HTML test reports

## CI/CD Integration

For GitHub Actions or other CI pipelines, ensure:

1. MongoDB is available (use Docker or MongoDB service)
2. Use headless Chrome mode
3. Start server before running tests
4. Clean up processes after tests

Example CI step:

```yaml
- name: Run E2E Tests
  run: |
    npm start &
    sleep 5
    npm run test:e2e
```
