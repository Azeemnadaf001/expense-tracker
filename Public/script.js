let expenses = []; // Store the list of expenses
let pieChart;
let editingIndex = -1; // Variable to store the index of the expense being edited
let currentMonth = new Date().getMonth() + 1; // Current selected month (1-12)
let currentYear = new Date().getFullYear(); // Current selected year

window.onload = function () {
  createPieChart();
  const expenseList = document.getElementById("expense-list");
  expenseList.style.display = "none"; // Initially hide the expense list
  document.getElementById("export-btn").style.display = "none"; // Hide export button initially
  setupLogoutButton(); // Setup logout button event
  displayWelcomeMessage(); // Display welcome message with user's name
  initializeMonthSelector(); // Initialize month selector to current month
  // Fetch user's expenses and budget for current month
  fetchExpensesByMonth(currentMonth, currentYear); // Fetch only current month's expenses
  fetchBudget(); // Fetch user's budget
};
function fetchExpenses() {
  const token = getCookie("authToken"); // Retrieve token from cookies
  console.log("Token:", token); // Debugging line to check the token
  if (!token) {
    console.log("No token found, user is not logged in.");
    return; // If no token, the user is not logged in, return early
  }

  fetch("/get-expenses", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.expenses) {
        expenses = data.expenses; // Set the fetched expenses
        updateExpenseList(expenses); // Update the expense list table
        updateTotalExpense(expenses); // Update the total expense
        updateChart(); // Update the pie chart with the new data
        updateBudgetStatus(); // Update budget status
        document.getElementById("expense-list").style.display = "block"; // Show the list after fetching
      } else {
        console.error("Failed to fetch expenses:", data.error);
      }
    })
    .catch((error) => console.error("Error fetching expenses:", error));
}

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(";").shift();
}

// Display a welcome message with the user's name
function displayWelcomeMessage() {
  const userName = localStorage.getItem("userName"); // Retrieve user's name from localStorage
  if (userName) {
    const header = document.querySelector(".header");
    const welcomeMessage = document.createElement("h2");
    welcomeMessage.textContent = `Welcome, ${userName}!`; // Add welcome text
    welcomeMessage.classList.add("welcome-message"); // Add a class for styling
    header.appendChild(welcomeMessage); // Append to the header section
  } else {
    // If no userName is found in localStorage, display a default welcome message
    const header = document.querySelector(".header");
    const welcomeMessage = document.createElement("h2");
    welcomeMessage.textContent = "Welcome!"; // Default message
    welcomeMessage.classList.add("welcome-message");
    header.appendChild(welcomeMessage);
  }
}

document
  .getElementById("expense-form")
  .addEventListener("submit", function (e) {
    e.preventDefault();

    const name = document.getElementById("expense-name").value;
    const amount = parseFloat(document.getElementById("expense-amount").value);
    const category = document.getElementById("expense-category").value;

    if (isNaN(amount) || amount <= 0) {
      alert("Please enter a valid amount.");
      return;
    }

    if (editingIndex === -1) {
      // Add the expense to the list (new expense)
      expenses.push({ name, amount, category });
    } else {
      // Edit the existing expense
      expenses[editingIndex] = { name, amount, category };
      editingIndex = -1; // Reset the editing index after editing
    }

    // Show the expense list if it's hidden
    document.getElementById("expense-list").style.display = "block";
    document.getElementById("export-btn").style.display = "inline-block";

    // Update the table, total, chart and budget status
    updateExpenseList(expenses);
    updateTotalExpense(expenses);
    updateChart();
    updateBudgetStatus(); // Update budget status after adding expense

    // Reset the form
    document.getElementById("expense-form").reset();
  });

// Other functions remain the same...

// Update the total expenses
function updateTotalExpense(expenseList) {
  const totalAmount = expenseList.reduce(
    (sum, expense) => sum + expense.amount,
    0
  );
  document.getElementById("total-amount").textContent = `₹${totalAmount.toFixed(
    2
  )}`;
}

// Initialize the Pie Chart
function createPieChart() {
  const ctx = document.getElementById("expense-chart").getContext("2d");
  pieChart = new Chart(ctx, {
    type: "pie",
    data: {
      labels: ["Food", "Transport", "Entertainment", "Other"],
      datasets: [
        {
          label: "Expenses by Category",
          data: [0, 0, 0, 0],
          backgroundColor: ["#FF5733", "#33FF57", "#3357FF", "#F3FF33"],
          borderColor: "#FFFFFF",
          borderWidth: 2,
        },
      ],
    },
    options: {
      responsive: true, // Ensure the chart is responsive
      maintainAspectRatio: false, // Disable maintaining aspect ratio for resizing
    },
  });
}

// Update the Pie Chart with expense data
function updateChart() {
  const categories = ["Food", "Transport", "Entertainment", "Other"];
  const categorySums = categories.map((category) => {
    return expenses
      .filter((expense) => (expense.type || expense.category) === category)
      .reduce((sum, expense) => sum + expense.amount, 0);
  });

  pieChart.data.datasets[0].data = categorySums;
  pieChart.update();
}

// Update the expense list in the table
function updateExpenseList(expenseList) {
  const tableBody = document.getElementById("expense-list");
  tableBody.innerHTML = "";

  expenseList.forEach((expense, index) => {
    const row = tableBody.insertRow();
    const expenseDate = expense.date
      ? new Date(expense.date).toLocaleDateString()
      : new Date().toLocaleDateString();
    const expenseName = expense.description || expense.name || "";
    const expenseCategory = expense.type || expense.category || "";

    row.innerHTML = `
            <td>${expenseName}</td>
            <td>${expense.amount.toFixed(2)}</td>
            <td>${expenseCategory}</td>
            <td>${expenseDate}</td>
            <td>
                <button onclick="editExpense(${index})">Edit</button>
                <button onclick="deleteExpense(${index})" class="delete-btn">Delete</button>
            </td>
        `;
  });
}

// Filter expenses by category
document
  .getElementById("filter-category")
  .addEventListener("change", function () {
    const selectedCategory = this.value;
    if (selectedCategory === "All") {
      updateExpenseList(expenses); // Show all expenses
      updateTotalExpense(expenses); // Show total for all expenses
    } else {
      const filteredExpenses = expenses.filter(
        (expense) => expense.category === selectedCategory
      );
      updateExpenseList(filteredExpenses); // Show filtered expenses
      updateTotalExpense(filteredExpenses); // Show total for filtered expenses
    }
    updateBudgetStatus(); // Update budget status when filtering
  });

// Export expenses to CSV
function exportToCSV() {
  const csvContent =
    "Description,Amount,Category\n" +
    expenses
      .map(
        (exp) => `${exp.name},${exp.amount},${exp.category}` // Add expense data to CSV
      )
      .join("\n");

  const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.setAttribute("href", url);
  link.setAttribute("download", "expenses.csv");
  link.click();
}

// Edit an expense
function editExpense(index) {
  const expense = expenses[index];
  editingIndex = index; // Set the index of the expense being edited

  // Pre-fill the form with the expense data
  document.getElementById("expense-name").value = expense.name;
  document.getElementById("expense-amount").value = expense.amount;
  document.getElementById("expense-category").value = expense.category;

  // Change the button label to "Update" to indicate editing
  document.getElementById("submit-btn").textContent = "Update Expense";
}

// Delete an expense
async function deleteExpense(index) {
  const expense = expenses[index];

  if (!expense || !expense._id) {
    console.error("Expense ID not found");
    return;
  }

  if (!confirm("Are you sure you want to permanently delete this expense?")) {
    return;
  }

  try {
    const response = await fetch(`/delete-expense/${expense._id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();

    if (response.ok) {
      // Update local expenses array
      expenses = data.expenses;
      updateExpenseList(expenses);
      updateTotalExpense(expenses);
      updateChart();
      updateBudgetStatus();
      alert("Expense deleted successfully!");
    } else {
      alert(data.error || "Failed to delete expense");
    }
  } catch (error) {
    console.error("Error deleting expense:", error);
    alert("Error deleting expense. Please try again.");
  }
}

// Setup the logout button functionality
function setupLogoutButton() {
  document.getElementById("logout-btn").addEventListener("click", function () {
    window.location.href = "login.html";
  });
}

// Fetch Budget Function
async function fetchBudget(month = currentMonth, year = currentYear) {
  try {
    const response = await fetch(`/get-budget?month=${month}&year=${year}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();

    if (response.ok) {
      const budgetDisplay = document.getElementById("budget-display");
      const budgetStatus = document.getElementById("budget-status");
      const monthDisplay = document.getElementById("current-month-display");

      if (budgetDisplay && budgetStatus) {
        budgetDisplay.textContent = data.budget.toFixed(2);

        if (monthDisplay) {
          const date = new Date(data.year, data.month - 1);
          monthDisplay.textContent = date.toLocaleString("default", {
            month: "long",
            year: "numeric",
          });
        }

        if (data.budget > 0) {
          budgetStatus.style.display = "block";
          updateBudgetStatus();
        }
      }
    }
  } catch (error) {
    console.error("Error fetching budget:", error);
  }
}

// Initialize Month Selector
function initializeMonthSelector() {
  const monthInput = document.getElementById("view-month");
  if (monthInput) {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0");
    monthInput.value = `${year}-${month}`;
  }
}

// Load Data for Selected Month
async function loadMonthData() {
  const monthInput = document.getElementById("view-month");
  if (!monthInput) return;

  const [year, month] = monthInput.value.split("-");
  currentYear = parseInt(year);
  currentMonth = parseInt(month);

  // Hide history table when viewing specific month
  const historyDiv = document.getElementById("budget-history");
  if (historyDiv) {
    historyDiv.style.display = "none";
  }

  // Fetch budget and expenses for selected month
  await fetchBudget(currentMonth, currentYear);
  await fetchExpensesByMonth(currentMonth, currentYear);
}

// Fetch Expenses for Specific Month
async function fetchExpensesByMonth(month, year) {
  try {
    const response = await fetch(
      `/get-expenses-by-month?month=${month}&year=${year}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    const data = await response.json();

    if (response.ok && data.expenses) {
      expenses = data.expenses;
      updateExpenseList(expenses);
      updateTotalExpense(expenses);
      updateChart();
      updateBudgetStatus();
      document.getElementById("expense-list").style.display = "block";
    }
  } catch (error) {
    console.error("Error fetching expenses:", error);
  }
}

// View Budget History for Last 6 Months
async function viewBudgetHistory() {
  try {
    const response = await fetch("/get-budget-history?months=6", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();

    if (response.ok && data.history) {
      displayBudgetHistory(data.history);
      // Fetch all expenses for the last 6 months and update chart
      await fetch6MonthsExpenses();
    }
  } catch (error) {
    console.error("Error fetching budget history:", error);
  }
}

// Fetch and display expenses for last 6 months
async function fetch6MonthsExpenses() {
  try {
    const response = await fetch("/get-expenses", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();

    if (response.ok && data.expenses) {
      // Get last 6 months range
      const now = new Date();
      const sixMonthsAgo = new Date(now.getFullYear(), now.getMonth() - 5, 1);

      // Filter expenses for last 6 months
      const last6MonthsExpenses = data.expenses.filter((expense) => {
        const expenseDate = new Date(expense.date);
        return expenseDate >= sixMonthsAgo;
      });

      // Update the global expenses array and chart for 6-month view
      expenses = last6MonthsExpenses;
      updateChart();
    }
  } catch (error) {
    console.error("Error fetching 6 months expenses:", error);
  }
}

// Display Budget History Table
function displayBudgetHistory(history) {
  const historyDiv = document.getElementById("budget-history");
  const historyList = document.getElementById("budget-history-list");

  if (!historyDiv || !historyList) return;

  // Hide current budget status
  const budgetStatus = document.getElementById("budget-status");
  if (budgetStatus) {
    budgetStatus.style.display = "none";
  }

  // Clear previous history
  historyList.innerHTML = "";

  // Populate history table
  history.forEach((entry) => {
    const row = historyList.insertRow();
    const statusClass = entry.remaining >= 0 ? "status-good" : "status-bad";
    const statusText =
      entry.remaining >= 0 ? "✓ Within Budget" : "✗ Over Budget";

    row.innerHTML = `
      <td>${entry.monthName} ${entry.year}</td>
      <td>₹${entry.budget.toFixed(2)}</td>
      <td>₹${entry.expenses.toFixed(2)}</td>
      <td>₹${entry.remaining.toFixed(2)}</td>
      <td class="${statusClass}">${statusText}</td>
    `;
  });

  historyDiv.style.display = "block";
}

// Update Budget Status and Intensity
function updateBudgetStatus() {
  const budgetDisplay = document.getElementById("budget-display");
  if (!budgetDisplay) return;

  const budget = parseFloat(budgetDisplay.textContent || 0);

  if (budget === 0) return; // Don't update if budget is not set

  // Calculate total expenses
  const totalExpense = expenses.reduce(
    (sum, expense) => sum + expense.amount,
    0
  );
  const remaining = budget - totalExpense;
  const usagePercentage = (totalExpense / budget) * 100;

  // Update display elements if they exist
  const expenseDisplay = document.getElementById("expense-display");
  const remainingDisplay = document.getElementById("remaining-display");
  const usagePercentageDisplay = document.getElementById("usage-percentage");
  const intensityIndicator = document.getElementById("intensity-indicator");
  const budgetIntensity = document.getElementById("budget-intensity");

  if (expenseDisplay) expenseDisplay.textContent = totalExpense.toFixed(2);
  if (remainingDisplay) remainingDisplay.textContent = remaining.toFixed(2);
  if (usagePercentageDisplay)
    usagePercentageDisplay.textContent = usagePercentage.toFixed(1);

  // Determine intensity level
  if (intensityIndicator && budgetIntensity) {
    // Remove existing classes
    budgetIntensity.className = "budget-intensity";

    if (usagePercentage <= 70) {
      budgetIntensity.classList.add("safe");
      intensityIndicator.innerHTML = "🟢 Safe - Spending under control";
    } else if (usagePercentage <= 90) {
      budgetIntensity.classList.add("warning");
      intensityIndicator.innerHTML = "🟡 Warning - Near budget limit";
    } else {
      budgetIntensity.classList.add("exceeded");
      intensityIndicator.innerHTML = "🔴 Exceeded - Budget crossed / danger";
    }
  }

  // Show budget status
  const budgetStatus = document.getElementById("budget-status");
  if (budgetStatus) budgetStatus.style.display = "block";
}
