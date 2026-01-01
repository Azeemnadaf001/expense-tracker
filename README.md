# Expense Tracker

A simple application to track your daily expenses, manage budgets, and visualize spending habits.

## Features

- Add, edit, and delete expenses
- Categorize expenses (e.g., Food, Transport, Utilities)
- View expense summaries and reports
- User authentication (if applicable)
- Responsive and user-friendly interface

## Getting Started

### Prerequisites

- [Node.js](https://nodejs.org/) (if using JavaScript/Node)
- [Python](https://python.org/) (if using Python)
- Any other dependencies your project requires

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/AnuragChougule/Expense_Tracker.git
   ```
2. Navigate to the project directory:
   ```bash
   cd Expense_Tracker
   ```
3. Install dependencies:
   ```bash
   npm install
   ```

### Running the Application

1. Make sure MongoDB is running on your system
2. Start the server:
   ```bash
   npm start
   ```
3. Open your browser and navigate to `http://localhost:3000`

## Testing

### End-to-End Tests

This project includes automated E2E tests using Selenium WebDriver.

#### Prerequisites for Testing

- Chrome browser installed on your system
- Application server must be running on `http://localhost:3000`

#### Running E2E Tests

1. **Start the application server** (in a separate terminal):
   ```bash
   npm start
   ```

2. **Run the E2E tests** (in another terminal):
   ```bash
   npm run test:e2e
   ```

#### What the E2E Tests Verify

- ✅ Application loads successfully
- ✅ Homepage is accessible
- ✅ Page has proper title and content
- ✅ Navigation links are present
- ✅ Screenshots are captured for visual verification

#### Test Configuration

- **Base URL**: `http://localhost:3000` (can be changed via `BASE_URL` environment variable)
- **Timeout**: 10 seconds
- **Browser**: Chrome (headless mode available)

To run tests against a different URL:
```bash
BASE_URL=http://localhost:3001 npm run test:e2e
```

To enable headless mode, edit `tests/e2e/smoke.test.js` and uncomment the headless options.

#### Test Screenshots

Test screenshots are automatically saved to `tests/screenshots/` directory after each test run. These are ignored by git.


## Usage

1. Register or log in to your account.
2. Add your daily expenses.
3. View reports and manage your budget.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is open-sourced under the [MIT](LICENSE).


## Contact

📫 For queries, reach out at: [anuragchougule0160@gmail.com](mailto:anuragchougule0160@gmail.com)
