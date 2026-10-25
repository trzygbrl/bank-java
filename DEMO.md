# Simple Bank — 2-Minute Demo Script

This script provides an exact, step-by-step walkthrough to demonstrate the Simple Bank application within two minutes. Each step specifies the exact user actions, expected interface responses, and the PRD requirement or programming concept it verifies.

---

## Preparation

Compile the application and launch it from the terminal:

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out BankApp
```

---

## Demo Walkthrough

### Step 1: Startup & Balance Verification (0:00 – 0:20)

- **Action:** Observe the newly opened window.
- **Expected Display:**
  - Window title is **"Simple Bank"**, sized 1000 × 680 pixels, centered on screen.
  - Left sidebar (240 px wide, deep lavender background `#433670`) shows the brand name **"Simple Bank"** and caption **"Personal Banking"**.
  - **[1] Check Balance** is selected with a deep lavender background and a 4 px mint bar (`#BFE8D4`) on its left edge.
  - The hero card shows **"Current balance"** and **₱5,000.00** in 40 px bold white text on primary lavender fill.
  - Caption below the card displays: `Account initialized. Current balance: ₱5,000.00`.
- **What this proves:**
  - **TC-01:** Startup balance is initialized to exactly 500,000 centavos (₱5,000.00).
  - First-display initialization message appears as specified in PRD Section 9.5 and Section 10.

---

### Step 2: Zero Deposit Validation Error (0:20 – 0:40)

- **Action:** Click **[2] Deposit Money** in the sidebar (or press `Ctrl+2`).
- **Expected Display:**
  - Content switches to Deposit view; amount input field automatically receives focus with hint `0.00` and peso sign `₱` prefix.
  - Chip displays: `Available balance ₱5,000.00`.
- **Action:** Type `0` into the amount field and click the **Deposit** button (or press `Enter`).
- **Expected Display:**
  - Inline error message appears in the error box: `Amount must be greater than zero.`.
  - The input field border turns red (`#B3261E`).
  - Text `0` remains selected in the field with focus retained.
  - Available balance chip remains unchanged at `Available balance ₱5,000.00`.
- **What this proves:**
  - **TC-05 & FR-F2:** Rejection of non-positive amounts using the `validateAmount` if/else validation chain without exceptions.
  - **UI-8 & UI-9:** Inline error presentation with error state styling and focus retention.

---

### Step 3: Valid Deposit (0:40 – 1:00)

- **Action:** Type `1000` into the amount field. Notice the error message and red border clear immediately upon typing.
- **Action:** Click the **Deposit** button (or press `Enter`).
- **Expected Display:**
  - Inline success message appears: `Deposit successful. New balance: ₱6,000.00`.
  - The amount field is cleared.
  - The chip immediately updates to: `Available balance ₱6,000.00`.
- **Action:** Click **[1] Check Balance** in the sidebar (or press `Ctrl+1`).
- **Expected Display:**
  - Hero balance card now displays **₱6,000.00**.
  - Caption now displays: `Current balance: ₱6,000.00` (confirming subsequent display caption behavior).
- **What this proves:**
  - **TC-02 & TC-03:** Proper balance arithmetic addition (`balanceCentavos += amountCentavos`), multi-view synchronization via `Refreshable`, and caption update logic.

---

### Step 4: Overdraft Withdrawal Error (1:00 – 1:20)

- **Action:** Click **[3] Withdraw Money** in the sidebar (or press `Ctrl+3`).
- **Expected Display:**
  - Withdraw view opens with focus in the amount field; chip displays `Available balance ₱6,000.00`.
- **Action:** Type `7000` and click the **Withdraw** button (or press `Enter`).
- **Expected Display:**
  - Inline error message displays: `Insufficient funds. Your balance is ₱6,000.00; you cannot withdraw ₱7,000.00.`.
  - The amount field border turns red and the text `7000` remains selected.
  - Account balance remains untouched at ₱6,000.00.
- **What this proves:**
  - **TC-13 & FR-F3:** Overdraft protection logic in `BankAccount.withdraw()` and `WithdrawView.handleWithdraw()`, blocking unauthorized debit attempts.

---

### Step 5: Valid Withdrawal (1:20 – 1:40)

- **Action:** Type `2000` into the amount field and click **Withdraw** (or press `Enter`).
- **Expected Display:**
  - Inline success message appears: `Withdrawal successful. New balance: ₱4,000.00`.
  - The amount field is cleared.
  - Available balance chip updates to: `Available balance ₱4,000.00`.
- **What this proves:**
  - **TC-11:** Successful withdrawal deduction in whole centavos (`balanceCentavos -= amountCentavos`).

---

### Step 6: Transaction History & Balance After Audit (1:40 – 1:55)

- **Action:** Click **[4] View Transaction History** in the sidebar (or press `Ctrl+4`).
- **Expected Display:**
  - Caption above the table reads: `2 transaction(s)`.
  - The table contains exactly two rows in chronological order:
    - **Row 1:** No. `1` | Type `Deposit` (rendered in green `#2E7D5B`) | Amount `₱1,000.00` | Balance After `₱6,000.00`
    - **Row 2:** No. `2` | Type `Withdrawal` (rendered in red `#A8324A`) | Amount `₱2,000.00` | Balance After `₱4,000.00`
  - Table headers are bold with deep lavender background; alternating rows use white (`#FFFFFF`) and lavender mist (`#F3F0FA`).
  - Failed transactions (`0` and `7000`) and the opening balance are not present in history.
- **What this proves:**
  - **TC-16, FR-E1, FR-E2, FR-E3:** Only successful transactions are recorded in `ArrayList<Transaction>`, each logging its type, amount, and exact post-transaction balance after.

---

### Step 7: Themed Exit Confirmation Dialog (1:55 – 2:00)

- **Action:** Click **[5] Exit** in the sidebar (or press `Ctrl+5`, or click the window's close `X` button).
- **Expected Display:**
  - Modal dialog titled **"Exit Simple Bank"** appears with message: `Thank you for banking with us. Are you sure you want to exit?`.
  - Buttons: **Exit** (primary filled lavender) and **Stay** (outlined), with **Stay** focused by default to prevent accidental exit.
- **Action:** Click **Stay** (or press the `Escape` key).
- **Expected Display:**
  - Dialog dismisses; the application stays open with all transaction state intact.
- **Action:** Click **[5] Exit** again, then click the **Exit** button.
- **Expected Display:**
  - The application window closes cleanly and exits with return code 0.
- **What this proves:**
  - **TC-18, TC-19, TC-26, UI-12:** Themed modal exit confirmation intercepting both menu and window close events with safe default focus.
