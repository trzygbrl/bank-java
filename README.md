# Simple Bank

Simple Bank is a clean, modern, and accessible desktop banking application built with Java Swing (JDK 17+). It allows users to check their account balance, make deposits, make withdrawals with overdraft protection, view a complete chronological transaction history with "balance after" audit tracking, and exit safely with a confirmation dialog.

Requirements: [docs/PRD.md](file:///c:/Users/kylac/bank-java-1/docs/PRD.md). Build plan: [docs/ROADMAP.md](file:///c:/Users/kylac/bank-java-1/docs/ROADMAP.md). Progress: [docs/STATE.md](file:///c:/Users/kylac/bank-java-1/docs/STATE.md).

---

## What the App Does

Simple Bank provides personal banking functions in an intuitive single-window GUI:

1. **Check Balance (View 1):** Displays the current balance in a prominent hero card along with contextual initialization and update captions.
2. **Deposit Money (View 2):** Accepts monetary deposits, validates user input following rigorous bounds (positive amounts, at most 2 decimal places, maximum ₱1,000,000,000.00 per transaction), updates available balance, and logs successful transactions.
3. **Withdraw Money (View 3):** Validates and processes withdrawals, preventing overdrafts when requested funds exceed the current balance, and logs successful withdrawals.
4. **Transaction History (View 4):** Displays all successful deposits and withdrawals in a styled, scrollable audit table with transaction number, colored transaction type, formatted amount, and the resulting balance after each transaction. When no transactions exist, a clean empty-state message is shown.
5. **Exit Confirmation Dialog (Menu 5):** A themed modal dialog offering Stay and Exit options, also triggered when closing the window with the window close (X) button or pressing Ctrl+5.

---

## Requirements

- **Java Development Kit (JDK):** Version 17 or higher.
- **Display:** A graphical desktop screen supporting a minimum resolution of 860 × 560 pixels.
- **Libraries:** Pure standard Java runtime only — no external third-party dependencies, Maven, or Gradle required.

---

## Build and Run Instructions

### Prerequisites Check

Verify your installed Java runtime and compiler:

```bash
java -version
javac -version
```

Both commands must report version 17 or higher.

### Compile the Application

From the repository root, compile all source files with UTF-8 encoding:

```bash
javac -encoding UTF-8 -d out src/*.java
```

To compile with all lint warnings enabled:

```bash
javac -Xlint:all -encoding UTF-8 -d out src/*.java
```

### Run the Application

From the repository root, run:

```bash
java -cp out BankApp
```

### Optional: Create and Run an Executable JAR

To package the application into a standalone runnable JAR:

```bash
jar --create --file simple-bank.jar --main-class BankApp -C out .
```

To launch the packaged JAR:

```bash
java -jar simple-bank.jar
```

---

## Running the Automated Tests

Simple Bank includes comprehensive automated logic tests and GUI smoke tests that run without external testing frameworks.

### 1. Logic Tests (Non-GUI)

Runs all unit tests for currency formatting, multi-step amount validation, bounds checking, and core bank account logic:

```bash
java -ea -cp out LogicTests
```

### 2. GUI Smoke Tests

Runs automated Swing Event Dispatch Thread (EDT) component tests verifying views, input fields, error states, and history synchronization (skips gracefully if running headless):

```bash
java -ea -cp out GuiSmokeTest
```

---

## Color Palette (PRD Section 9.2)

The application uses an intentional deep lavender and mint cream color palette designed for high contrast, calm aesthetics, and WCAG AA accessibility:

| Token | Hex | Used for |
|---|---|---|
| `primary` (Deep Lavender) | `#5B4B8A` | Primary buttons, balance card, selected sidebar item, focus rings |
| `primaryDark` | `#433670` | Sidebar background, pressed buttons |
| `primaryDeep` | `#2E2552` | Window title text on lavender, high-emphasis headings |
| `lavender` | `#E9E4F7` | Table header background, chips, hover states, selected tints |
| `lavenderMist` | `#F3F0FA` | Alternate table rows, input hover background, balance chips |
| `background` (Mint Cream) | `#F5FFFA` | Content area background |
| `surface` | `#FFFFFF` | Cards, inputs, dialogs |
| `mint` | `#BFE8D4` | Selected-item accent bar, success tint accents |
| `success` | `#2E7D5B` | Success text, deposit labels |
| `successBg` | `#E3F5EC` | Success message background |
| `error` | `#B3261E` | Error text |
| `errorBg` | `#FDECEA` | Error message background |
| `withdrawal` | `#A8324A` | Withdrawal labels in the history table |
| `textPrimary` | `#2B2540` | Main text |
| `textMuted` | `#6B6585` | Secondary text, hints |
| `border` | `#D9D3EC` | Card and input borders, dividers |
| `onPrimary` | `#FFFFFF` | Text on primary and primaryDark |
| `navText` | `#E4DEF5` | Unselected sidebar text |

---

## Program Structure (PRD Section 8)

The table below maps each architecture role defined in PRD Section 8 to its actual class implementation in the codebase:

| Name | Layer | Purpose | Real Implementation |
|---|---|---|---|
| `formatMoney(centavos)` | Logic | Builds currency string with comma thousands separators and 2 decimal places | `MoneyUtil.formatMoney(long centavos)` in [`src/MoneyUtil.java`](file:///c:/Users/kylac/bank-java-1/src/MoneyUtil.java) |
| `validateAmount(text)` | Logic | Applies validation rules 6.5 steps 1 to 5; returns AmountResult | `MoneyUtil.validateAmount(String text)` in [`src/MoneyUtil.java`](file:///c:/Users/kylac/bank-java-1/src/MoneyUtil.java) |
| `BankAccount` | Logic | Holds balance and history; provides deposit/withdraw/getTransactions | `BankAccount` in [`src/BankAccount.java`](file:///c:/Users/kylac/bank-java-1/src/BankAccount.java) |
| `recordTransaction(type, amount)` | Logic (inside BankAccount) | Appends immutable Transaction record with post-transaction balance | `BankAccount.recordTransaction(...)` in [`src/BankAccount.java`](file:///c:/Users/kylac/bank-java-1/src/BankAccount.java) |
| `handleCheckBalance()` | UI | Switches to Check Balance view and refreshes balance card | `BankFrame.handleCheckBalance()` in [`src/BankFrame.java`](file:///c:/Users/kylac/bank-java-1/src/BankFrame.java) |
| `handleDeposit()` | UI | Validates field, executes deposit, shows feedback, updates chips | `BankFrame.handleDeposit()` in [`src/BankFrame.java`](file:///c:/Users/kylac/bank-java-1/src/BankFrame.java) / `DepositView` in [`src/DepositView.java`](file:///c:/Users/kylac/bank-java-1/src/DepositView.java) |
| `handleWithdraw()` | UI | Validates field, executes withdrawal, blocks overdrafts, shows feedback | `BankFrame.handleWithdraw()` in [`src/BankFrame.java`](file:///c:/Users/kylac/bank-java-1/src/BankFrame.java) / `WithdrawView` in [`src/WithdrawView.java`](file:///c:/Users/kylac/bank-java-1/src/WithdrawView.java) |
| `handleViewHistory()` | UI | Switches to History view and rebuilds transaction table rows | `BankFrame.handleViewHistory()` in [`src/BankFrame.java`](file:///c:/Users/kylac/bank-java-1/src/BankFrame.java) / `HistoryView.refresh()` in [`src/HistoryView.java`](file:///c:/Users/kylac/bank-java-1/src/HistoryView.java) |
| `showView(view)` | UI | Switches visible content card and updates sidebar active item | `BankFrame.showView(String view)` in [`src/BankFrame.java`](file:///c:/Users/kylac/bank-java-1/src/BankFrame.java) |
| `confirmExit()` | UI | Opens modal confirmation dialog with Exit and Stay actions | `BankFrame.confirmExit()` in [`src/BankFrame.java`](file:///c:/Users/kylac/bank-java-1/src/BankFrame.java) |
| `main` | UI | Configures theme defaults and opens BankFrame on EDT | `BankApp.main(String[] args)` in [`src/BankApp.java`](file:///c:/Users/kylac/bank-java-1/src/BankApp.java) |

---

## Required Programming Concepts (PRD Section 11)

The table below maps each required programming concept to the exact class, method, and source lines where it is implemented and verified in the code:

| Concept | Required Feature | Verified Class & Method | Description / Implementation Detail |
|---|---|---|---|
| **Loops** | Comma-grouping loop | `MoneyUtil.formatMoney(long centavos)` in [`src/MoneyUtil.java`](file:///c:/Users/kylac/bank-java-1/src/MoneyUtil.java#L42-L48) | `for (int i = 0; i < len; i++)` inserts commas every 3 digits from the right. |
| **Loops** | History-row loop | `HistoryView.refresh()` in [`src/HistoryView.java`](file:///c:/Users/kylac/bank-java-1/src/HistoryView.java#L266-L274) | `for (int i = 0; i < n; i++)` iterates over `account.getTransactions()` and populates table rows. |
| **Loops** | Leading-zero loop | `MoneyUtil.validateAmount(String text)` in [`src/MoneyUtil.java`](file:///c:/Users/kylac/bank-java-1/src/MoneyUtil.java#L90-L92) | `while (firstNonZero < wholePesosStr.length() && wholePesosStr.charAt(firstNonZero) == '0')` strips leading zeros before whole-part digit count checking. |
| **Loops** | Non-zero digit inspection loop | `MoneyUtil.containsNonZeroDigit(String s)` in [`src/MoneyUtil.java`](file:///c:/Users/kylac/bank-java-1/src/MoneyUtil.java#L125-L131) | `for (int i = 0; i < s.length(); i++)` checks for any character in `'1'..'9'`. |
| **Conditional Statements** | `validateAmount` if/else chain | `MoneyUtil.validateAmount(String text)` in [`src/MoneyUtil.java`](file:///c:/Users/kylac/bank-java-1/src/MoneyUtil.java#L60-L113) | Sequential conditional rules: null check, regex syntax check, zero/negative check, decimal count check, and maximum limit checks. |
| **Conditional Statements** | Insufficient-funds check | `BankAccount.withdraw(long amountCentavos)` in [`src/BankAccount.java`](file:///c:/Users/kylac/bank-java-1/src/BankAccount.java#L30-L32) and `WithdrawView.handleWithdraw()` in [`src/WithdrawView.java`](file:///c:/Users/kylac/bank-java-1/src/WithdrawView.java#L163-L172) | Rejects withdrawal attempt when requested amount exceeds current balance. |
| **Conditional Statements** | Empty-history check | `HistoryView.refresh()` in [`src/HistoryView.java`](file:///c:/Users/kylac/bank-java-1/src/HistoryView.java#L280-L285) | `if (n == 0)` switches CardLayout to `CARD_EMPTY` ("No transactions yet."); otherwise shows `CARD_TABLE`. |
| **Conditional Statements** | Menu switch | `BankFrame.showView(String view)` in [`src/BankFrame.java`](file:///c:/Users/kylac/bank-java-1/src/BankFrame.java#L145-L161) | `switch (view)` selects view index (0: Balance, 1: Deposit, 2: Withdraw, 3: History) and updates sidebar item highlights. |
| **Arithmetic Operators** | Money calculations in whole centavos | `BankAccount.deposit`, `BankAccount.withdraw`, and `MoneyUtil.formatMoney` | `+` for balance addition, `-` for balance subtraction, `/ 100L` for whole pesos, `% 100L` for centavo remainder, and `wholePesos * 100L + fractionCentavos`. |
| **Comparison Operators** | Numeric and bound validations | `MoneyUtil.validateAmount` and `BankAccount.withdraw` | `amountCentavos > balanceCentavos`, `decimalPlaces > 2`, `strippedWhole.length() > 10`, `centavos > MAX_AMOUNT_CENTAVOS`, `cents < 0`. |
| **Data Structures** | Record collection | `BankAccount.transactions` in [`src/BankAccount.java`](file:///c:/Users/kylac/bank-java-1/src/BankAccount.java#L12) | `ArrayList<Transaction>` storing immutable `Transaction` records, exposed to UI via `Collections.unmodifiableList`. |
| **Input Validation & Exception Handling** | Non-throwing parsing and conversion | `MoneyUtil.validateAmount(String text)` in [`src/MoneyUtil.java`](file:///c:/Users/kylac/bank-java-1/src/MoneyUtil.java#L101-L115) | `try/catch (NumberFormatException e)` around `Long.parseLong` conversion, returning `AmountResult.error(ERROR_TOO_LARGE)` without throwing. |