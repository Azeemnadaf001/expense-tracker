require("dotenv").config();
const express = require("express");
const bodyParser = require("body-parser");
const mongoose = require("mongoose");
const path = require("path");
const jwt = require("jsonwebtoken"); // For token-based authentication
const cookieParser = require("cookie-parser"); // For parsing cookies

const app = express();

// Middleware
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "Public")));

// Connect to MongoDB
const MONGO_URI =
  process.env.MONGO_URI || "mongodb://127.0.0.1:27017/expense_tracker";

mongoose
  .connect(MONGO_URI, { serverSelectionTimeoutMS: 20000 })
  .then(() => console.log("Connected to MongoDB"))
  .catch((err) => console.error("Error connecting to MongoDB:", err));

// Define Mongoose Schema
const userSchema = new mongoose.Schema({
  name: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  monthlyBudget: { type: Number, default: 0 }, // Legacy field for backward compatibility
  monthlyBudgets: [
    {
      year: { type: Number, required: true },
      month: { type: Number, required: true }, // 1-12
      amount: { type: Number, required: true },
      createdAt: { type: Date, default: Date.now },
    },
  ],
  expenses: [
    {
      description: { type: String, required: true },
      amount: { type: Number, required: true },
      type: { type: String, required: true },
      date: { type: Date, default: Date.now },
    },
  ],
});

// Create Mongoose Model
const User = mongoose.model("User", userSchema);

// Middleware to authenticate users
function authenticateUser(req, res, next) {
  const token = req.cookies.authToken; // Token stored in cookies
  if (!token) {
    return res
      .status(401)
      .json({ error: "Unauthorized access. Please log in." });
  }

  try {
    const user = jwt.verify(token, process.env.JWT_SECRET || "secretKey"); // Verify token
    req.user = user; // Attach user info to the request object
    next(); // Proceed to the next middleware/route handler
  } catch (error) {
    res
      .status(401)
      .json({ error: "Invalid or expired token. Please log in again." });
  }
}

// Register Route
app.post("/register", async (req, res) => {
  const { name, email, password } = req.body;

  try {
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ error: "Email already registered." });
    }

    const newUser = new User({ name, email, password });
    await newUser.save();

    res.status(201).json({
      message: "User registered successfully!",
      user: { id: newUser._id, name: newUser.name, email: newUser.email },
    });
  } catch (error) {
    console.error("Error registering user:", error);
    res
      .status(500)
      .json({ error: "Error registering user. Please try again." });
  }
});

// Login Route
app.post("/login", async (req, res) => {
  const { email, password } = req.body;

  try {
    const user = await User.findOne({ email });
    if (!user || user.password !== password) {
      return res.status(400).json({ error: "Invalid email or password." });
    }

    // Generate token and set it in cookies
    const token = jwt.sign(
      { id: user._id, email: user.email },
      process.env.JWT_SECRET || "secretKey",
      {
        expiresIn: "1h", // Token validity
      }
    );

    res.cookie("authToken", token, { httpOnly: true }); // Secure cookie
    res.status(200).json({
      message: "Login successful!",
      user: { id: user._id, name: user.name, email: user.email },
    });
  } catch (error) {
    console.error("Error during login:", error);
    res.status(500).json({ error: "Error logging in. Please try again." });
  }
});

// Add Expense Route
app.post("/add-expense", authenticateUser, async (req, res) => {
  const { description, amount, type, date } = req.body;

  try {
    const user = await User.findById(req.user.id);
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    // Parse the date properly - if a date string is provided, create a Date at UTC midnight
    let expenseDate;
    if (date) {
      // date comes as YYYY-MM-DD from the date input
      // Create Date object from the string, treating it as UTC
      const dateParts = date.split("-");
      expenseDate = new Date(
        Date.UTC(
          parseInt(dateParts[0]),
          parseInt(dateParts[1]) - 1,
          parseInt(dateParts[2])
        )
      );
    } else {
      expenseDate = new Date();
    }

    const newExpense = { description, amount, type, date: expenseDate };
    user.expenses.push(newExpense);
    await user.save();

    res.status(200).json({
      message: "Expense added successfully!",
      expenses: user.expenses,
    });
  } catch (error) {
    console.error("Error adding expense:", error);
    res.status(500).json({ error: "Failed to add expense." });
  }
});

// Get All Expenses Route
app.get("/get-expenses", authenticateUser, async (req, res) => {
  try {
    const user = await User.findById(req.user.id);
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    res.status(200).json({ expenses: user.expenses });
  } catch (error) {
    console.error("Error fetching expenses:", error);
    res.status(500).json({ error: "Failed to fetch expenses." });
  }
});

// Get Expense Summary by Type Route (For Pie Chart)
app.get("/expense-summary", authenticateUser, async (req, res) => {
  try {
    const user = await User.findById(req.user.id);
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    const summary = user.expenses.reduce((acc, expense) => {
      acc[expense.type] = (acc[expense.type] || 0) + expense.amount;
      return acc;
    }, {});

    res.status(200).json({ summary });
  } catch (error) {
    console.error("Error fetching expense summary:", error);
    res.status(500).json({ error: "Failed to fetch expense summary." });
  }
});

// Delete Expense Route
app.delete("/delete-expense/:expenseId", authenticateUser, async (req, res) => {
  const { expenseId } = req.params;

  try {
    const user = await User.findById(req.user.id);
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    // Find expense index
    const expenseIndex = user.expenses.findIndex(
      (expense) => expense._id.toString() === expenseId
    );

    if (expenseIndex === -1) {
      return res.status(404).json({ error: "Expense not found" });
    }

    // Hard delete - remove from array
    user.expenses.splice(expenseIndex, 1);
    await user.save();

    res.status(200).json({
      message: "Expense deleted successfully!",
      expenses: user.expenses,
    });
  } catch (error) {
    console.error("Error deleting expense:", error);
    res.status(500).json({ error: "Failed to delete expense." });
  }
});

// Set Budget Route
app.post("/set-budget", authenticateUser, async (req, res) => {
  const { budget, month, year } = req.body;

  try {
    const user = await User.findById(req.user.id);
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    const currentDate = new Date();
    const targetMonth = month || currentDate.getMonth() + 1;
    const targetYear = year || currentDate.getFullYear();

    // Check if budget already exists for this month
    const existingBudgetIndex = user.monthlyBudgets.findIndex(
      (b) => b.year === targetYear && b.month === targetMonth
    );

    if (existingBudgetIndex !== -1) {
      // Update existing budget
      user.monthlyBudgets[existingBudgetIndex].amount = budget;
    } else {
      // Add new monthly budget
      user.monthlyBudgets.push({
        year: targetYear,
        month: targetMonth,
        amount: budget,
      });
    }

    // Keep legacy field updated for current month
    if (
      targetMonth === currentDate.getMonth() + 1 &&
      targetYear === currentDate.getFullYear()
    ) {
      user.monthlyBudget = budget;
    }

    await user.save();

    res.status(200).json({
      message: "Budget set successfully!",
      budget: budget,
      month: targetMonth,
      year: targetYear,
    });
  } catch (error) {
    console.error("Error setting budget:", error);
    res.status(500).json({ error: "Failed to set budget." });
  }
});

// Get Budget Route
app.get("/get-budget", authenticateUser, async (req, res) => {
  try {
    const user = await User.findById(req.user.id);
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    const { month, year } = req.query;
    const currentDate = new Date();
    const targetMonth = month ? parseInt(month) : currentDate.getMonth() + 1;
    const targetYear = year ? parseInt(year) : currentDate.getFullYear();

    // Find budget for specified month
    const monthlyBudget = user.monthlyBudgets.find(
      (b) => b.year === targetYear && b.month === targetMonth
    );

    res.status(200).json({
      budget: monthlyBudget ? monthlyBudget.amount : 0,
      month: targetMonth,
      year: targetYear,
    });
  } catch (error) {
    console.error("Error fetching budget:", error);
    res.status(500).json({ error: "Failed to fetch budget." });
  }
});

// Get Budget History Route
app.get("/get-budget-history", authenticateUser, async (req, res) => {
  try {
    const user = await User.findById(req.user.id);
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    const { months } = req.query;
    const numberOfMonths = months ? parseInt(months) : 6;

    const currentDate = new Date();
    const history = [];

    // Generate last N months
    for (let i = 0; i < numberOfMonths; i++) {
      const date = new Date(
        currentDate.getFullYear(),
        currentDate.getMonth() - i,
        1
      );
      const month = date.getMonth() + 1;
      const year = date.getFullYear();

      // Find budget for this month
      const monthlyBudget = user.monthlyBudgets.find(
        (b) => b.year === year && b.month === month
      );

      // Calculate expenses for this month
      const monthExpenses = user.expenses.filter((expense) => {
        const expenseDate = new Date(expense.date);
        // Extract month and year using UTC methods to avoid timezone issues
        const expenseMonth = expenseDate.getUTCMonth() + 1;
        const expenseYear = expenseDate.getUTCFullYear();
        return expenseMonth === month && expenseYear === year;
      });

      const totalExpenses = monthExpenses.reduce(
        (sum, expense) => sum + expense.amount,
        0
      );

      const budgetAmount = monthlyBudget ? monthlyBudget.amount : 0;

      history.push({
        month: month,
        year: year,
        monthName: date.toLocaleString("default", { month: "long" }),
        budget: budgetAmount,
        expenses: totalExpenses,
        remaining: budgetAmount - totalExpenses,
      });
    }

    res.status(200).json({ history });
  } catch (error) {
    console.error("Error fetching budget history:", error);
    res.status(500).json({ error: "Failed to fetch budget history." });
  }
});

// Get Expenses for Specific Month
app.get("/get-expenses-by-month", authenticateUser, async (req, res) => {
  try {
    const user = await User.findById(req.user.id);
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    const { month, year } = req.query;
    const currentDate = new Date();
    const targetMonth = month ? parseInt(month) : currentDate.getMonth() + 1;
    const targetYear = year ? parseInt(year) : currentDate.getFullYear();

    // Filter expenses for the specified month
    const monthExpenses = user.expenses.filter((expense) => {
      const expenseDate = new Date(expense.date);
      // Extract month and year using UTC methods to avoid timezone issues
      const expenseMonth = expenseDate.getUTCMonth() + 1;
      const expenseYear = expenseDate.getUTCFullYear();
      return expenseMonth === targetMonth && expenseYear === targetYear;
    });

    res.status(200).json({
      expenses: monthExpenses,
      month: targetMonth,
      year: targetYear,
    });
  } catch (error) {
    console.error("Error fetching expenses:", error);
    res.status(500).json({ error: "Failed to fetch expenses." });
  }
});

// Logout Route
app.post("/logout", (req, res) => {
  res.clearCookie("authToken"); // Clear the authentication token
  res.status(200).json({ message: "Logout successful!" });
});

// Start the Server
const PORT = process.env.PORT || 3000;
const server = app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});

// Handle port already in use error
server.on("error", (err) => {
  if (err.code === "EADDRINUSE") {
    console.error(`❌ Port ${PORT} is already in use. Please either:`);
    console.error(`   1. Stop the existing server, or`);
    console.error(`   2. Use a different port: PORT=3001 node server.js`);
    process.exit(1);
  } else {
    console.error("Server error:", err);
  }
});
