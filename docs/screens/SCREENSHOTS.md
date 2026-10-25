# Screenshot Instructions (TC-29 Parity Verification)

These instructions define the four screenshots to capture for side-by-side visual parity comparison with the Dart/Flutter application (PRD section 12 test **TC-29**).

All screenshots must be captured at the default window resolution of **1000 × 680 pixels**.

---

### Screenshot 1: Check Balance at Launch
- **File Name:** `01-balance.png`
- **View:** Check Balance (View 1)
- **State:** Fresh application launch.
- **Steps:**
  1. Compile and launch the app:
     ```powershell
     java -cp out BankApp
     ```
  2. The window opens automatically to the **Check Balance** view.
  3. Ensure the window is at default size (1000 × 680).
  4. Capture the entire application window.
- **Expected Visual Elements:**
  - Sidebar: "[1] Check Balance" selected (primary fill with 4 px mint left bar).
  - Header: Title "Check Balance", subtitle "Your account at a glance".
  - Hero Card: Current balance "₱5,000.00" on deep lavender background.
  - Startup Caption: "Account initialized. Current balance: ₱5,000.00" in muted text.

---

### Screenshot 2: Deposit with Invalid-Input Error
- **File Name:** `02-deposit-error.png`
- **View:** Deposit Money (View 2)
- **State:** Inline validation error displayed with highlighted field border.
- **Steps:**
  1. Click **Deposit Money** in the sidebar (or press `Ctrl+2`).
  2. In the Amount field, enter the invalid text:
     ```
     abc
     ```
  3. Click the **Deposit** button (or press `Enter`).
  4. Capture the entire application window.
- **Expected Visual Elements:**
  - Sidebar: "[2] Deposit Money" selected.
  - Header: Title "Deposit Money", subtitle "Add funds to your account".
  - Card: Card title "Deposit details", Available balance chip "Available balance ₱5,000.00".
  - Amount Field: Displays "abc" with a 2 px red/error border (`#B3261E`).
  - Error Message Box: Red background (`#FDECEA`), red border, and exact text:
    `Invalid input. Please enter a numeric amount (e.g., 500 or 250.75).`
  - Action Buttons: "Deposit" and "Clear" aligned with the amount field.

---

### Screenshot 3: Withdraw with Success Message
- **File Name:** `03-withdraw-success.png`
- **View:** Withdraw Money (View 3)
- **State:** Successful transaction with updated balance chip and inline success banner.
- **Steps:**
  1. From a fresh launch (or balance of ₱5,000.00), click **Withdraw Money** in the sidebar (or press `Ctrl+3`).
  2. In the Amount field, enter:
     ```
     2000
     ```
  3. Click the **Withdraw** button (or press `Enter`).
  4. Capture the entire application window.
- **Expected Visual Elements:**
  - Sidebar: "[3] Withdraw Money" selected.
  - Header: Title "Withdraw Money", subtitle "Take funds from your account".
  - Card: Card title "Withdrawal details".
  - Available Balance Chip: Updated to "Available balance ₱3,000.00".
  - Amount Field: Cleared back to hint text `0.00`.
  - Success Message Box: Green background (`#E3F5EC`), green border (`#2E7D5B`), and exact text:
    `Withdrawal successful. New balance: ₱3,000.00`
  - Action Buttons: "Withdraw" and "Clear".

---

### Screenshot 4: Transaction History with 5 Rows
- **File Name:** `04-history.png`
- **View:** Transaction History (View 4)
- **State:** Table populated with 5 completed transactions in chronological order.
- **Steps:**
  1. From a fresh launch (balance ₱5,000.00), perform the following 5 transactions in order:
     - **Deposit** `1000` (new balance: ₱6,000.00)
     - **Withdraw** `500` (new balance: ₱5,500.00)
     - **Deposit** `2500` (new balance: ₱8,000.00)
     - **Withdraw** `1250.50` (new balance: ₱6,749.50)
     - **Deposit** `3000` (new balance: ₱9,749.50)
  2. Click **View Transaction History** in the sidebar (or press `Ctrl+4`).
  3. Capture the entire application window.
- **Expected Visual Elements:**
  - Sidebar: "[4] View Transaction History" selected.
  - Header: Title "Transaction History", subtitle "All successful deposits and withdrawals".
  - Card: Caption "5 transaction(s)".
  - Table: Lavender header row ("No.", "Type", "Amount", "Balance After").
  - Table Rows: Alternating white and lavender mist rows:
    1. `1` | `Deposit` (green) | `₱1,000.00` | `₱6,000.00`
    2. `2` | `Withdrawal` (red) | `₱500.00` | `₱5,500.00`
    3. `3` | `Deposit` (green) | `₱2,500.00` | `₱8,000.00`
    4. `4` | `Withdrawal` (red) | `₱1,250.50` | `₱6,749.50`
    5. `5` | `Deposit` (green) | `₱3,000.00` | `₱9,749.50`
