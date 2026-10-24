# PRD: Simple Bank Desktop App (GUI) — Java and Dart

**Version 2.0 · October 24, 2026 · Applies to BOTH the Java implementation (Swing) and the Dart implementation (Flutter desktop)**

> Version 2.0 replaces the console-based v1.0. The banking rules are unchanged; the interface is now a graphical, professional lavender and mint cream design.

---

## 1. Purpose of this document

This is the central and only feature document for the project. The Java roadmap and the Dart roadmap both build exactly what is written here. If a roadmap, a coding agent, or a teammate disagrees with this PRD, **the PRD wins**. Requirements have IDs (for example FR-C3, TC-07, UI-4) so tasks and tests can point to them.

## 2. Product summary

A local bank needs a simple banking prototype with a professional graphical interface. The app opens one window holding ONE in-memory account that starts at **₱5,000.00**. A left-hand menu lets the customer check the balance, deposit, withdraw, view the transaction history, and exit. The customer can do as many transactions as they like until they choose Exit. Nothing is saved between runs.

## 3. Goals and non-goals

**Goals**

- Satisfy every banking requirement of the scenario (sections A to F).
- Provide a polished GUI that looks like a real banking product, in professional shades of **lavender** and **mint cream**.
- Demonstrate the required programming concepts (section 11).
- Look and behave the same in Java and Dart: same layout, same colors, same wording, same results.
- Keep business logic separate from UI code so it can be tested without opening a window.

**Non-goals (out of scope)**

Login or PIN, multiple accounts, database or file storage, transfers, interest, timestamps, dark mode, animations beyond simple hover and focus states, mobile layouts, and network features.

## 4. Users

- **Bank customer:** uses the menu and forms to manage the single account.
- **Evaluator or instructor:** runs the app, tries invalid inputs, and reads the code.

## 5. Platform and technical constraints

| Item | Rule |
|---|---|
| Java version | JDK 17 or newer. GUI toolkit: **Swing** (included in the JDK). No third-party libraries, no FlatLaf, no JavaFX |
| Dart version | Dart 3.x with **Flutter 3.x**, Material 3, desktop target (Windows, macOS, or Linux). No packages from pub.dev (only the Flutter SDK and its built-in test library) |
| Window | Single window titled **Simple Bank**. Default size 1000 × 680 px |
| Source encoding | UTF-8. The peso sign is written as the Unicode escape `\u20B1` in a single constant (`CURRENCY_SYMBOL` / `currencySymbol`) |
| Persistence | None. All data lives in memory |
| Layers | Logic layer (money helpers, account, transactions) must not import any GUI code. UI layer only calls the logic layer |

## 6. Functional requirements

### 6.1 Account initialization (scenario A)

- **FR-A1:** When the app starts, the account balance is ₱5,000.00.
- **FR-A2:** The window opens on the **Check Balance** view, which displays the current balance and the startup message (see section 10).

### 6.2 Banking menu (scenario B)

- **FR-B1:** The left sidebar shows exactly these options in this order: 1 Check Balance, 2 Deposit Money, 3 Withdraw Money, 4 View Transaction History, 5 Exit.
- **FR-B2:** Choosing an option opens its view in the content area. The selected option is visibly highlighted.
- **FR-B3:** The app keeps running and returns to normal use after every action. It closes only through Exit (and the window close button, UI-12).
- **FR-B4:** Option 1 shows the balance. It does NOT create a transaction record.

### 6.3 Deposit (scenario C)

- **FR-C1:** The Deposit view asks for the amount to deposit using one text field.
- **FR-C2:** Only positive amounts are accepted (rules in 6.6).
- **FR-C3:** A valid deposit is added to the balance.
- **FR-C4:** The view shows a success message with the updated balance, and every balance display in the app updates.
- **FR-C5:** The successful deposit is recorded in the transaction history.
- **FR-C6:** On an invalid entry, the matching error appears under the field, nothing changes, and the user can edit the field and try again. The field is cleared only after a successful deposit.

### 6.4 Withdrawal (scenario D)

- **FR-D1:** The Withdraw view asks for the amount to withdraw using one text field.
- **FR-D2:** Only positive amounts are accepted (rules in 6.6).
- **FR-D3:** An amount greater than the balance is blocked: show the insufficient-funds error, change nothing, record nothing.
- **FR-D4:** A valid withdrawal is deducted from the balance.
- **FR-D5:** The view shows a success message with the updated balance, and every balance display updates.
- **FR-D6:** The successful withdrawal is recorded in the transaction history.
- **FR-D7:** Withdrawing exactly the whole balance is allowed (balance becomes ₱0.00).
- **FR-D8:** Invalid entries behave like FR-C6.

### 6.5 Amount validation rules (deposit and withdrawal)

Run these checks in this exact order on the text in the field when the user presses the action button:

1. **Trim** spaces from both ends.
2. The text must be: an optional minus sign, then one or more digits, then optionally a dot followed by one or more digits. Otherwise show the **invalid-number** error. This rejects blank, letters, `1e3`, `.5`, `5.`, commas, and symbols.
3. If the value is negative or zero (for example `-50`, `0`, `0.00`, `-0`), show the **must-be-positive** error.
4. If there are more than 2 digits after the dot, show the **max-two-decimals** error.
5. If the amount is larger than ₱1,000,000,000.00 (including inputs whose whole-pesos part, with leading zeros removed, has more than 10 digits), show the **too-large** error. This is a safety limit so numbers never overflow.
6. Withdrawal only: if the amount is greater than the balance, show the **insufficient-funds** error.

Steps 1 to 5 belong to a single function, `validateAmount(text)`, that returns either the amount in centavos or an error message. It must never throw for any input.

### 6.6 Transaction history (scenario E)

- **FR-E1:** Every successful deposit and withdrawal is recorded. Failed attempts and balance checks are never recorded.
- **FR-E2:** Each record stores: transaction type, transaction amount, and account balance after the transaction.
- **FR-E3:** The History view lists all records in the order they happened, numbered from 1, in a table with the columns **No., Type, Amount, Balance After**.
- **FR-E4:** If there are no records, the view shows the empty-history message instead of an empty table.
- **FR-E5:** The starting balance is not a transaction and is not listed.

### 6.7 Input validation and error handling (scenario F)

- **FR-F1:** Invalid menu selection is prevented by the UI (only the five valid options exist). If the app also supports keyboard shortcuts, any key that is not a shortcut does nothing and shows no error trace. *(The scenario's "invalid menu selection" requirement is satisfied by this design: the menu only offers valid choices.)*
- **FR-F2:** Zero and negative deposit or withdrawal amounts are rejected with the must-be-positive error.
- **FR-F3:** Withdrawals above the balance are blocked.
- **FR-F4:** Any other invalid input (blank, text, wrong format) shows an inline error and lets the user try again. The app must never crash, freeze, or show a stack trace for bad input.
- **FR-F5:** Error and success messages are shown inside the view (not as pop-ups). The only dialog in the app is the Exit confirmation.

## 7. Data design

| Data | Type (Java / Dart) | Notes |
|---|---|---|
| Balance | `long` / `int`, in centavos | ₱5,000.00 is stored as 500000. Whole numbers avoid decimal rounding errors in money math |
| Constants | `CURRENCY_SYMBOL`, `STARTING_BALANCE_CENTAVOS` = 500000, `MAX_AMOUNT_CENTAVOS` = 100000000000 | Dart does not allow underscores in number literals |
| Transaction type | enum: DEPOSIT, WITHDRAWAL | Display labels: Deposit, Withdrawal |
| Transaction record | record or class, 3 fields | type, amountCentavos, balanceAfterCentavos |
| Transaction history | `ArrayList<Transaction>` / `List<Transaction>` | Append only, kept in order. Exposed to the UI as a read-only view |
| Amount check result | `AmountResult` (small class) | Holds either `centavos` or `errorMessage` |

**Money format rule:** `formatMoney(centavos)` returns the symbol, the whole pesos with comma thousands separators, a dot, and exactly 2 decimals. Examples: 0 → ₱0.00, 5 → ₱0.05, 100 → ₱1.00, 500000 → ₱5,000.00, 123456789 → ₱1,234,567.89. The comma grouping is built by the program itself (a loop that inserts a comma every 3 digits from the right) in BOTH languages.

## 8. Program structure

Both languages use the same names so the programs can be compared side by side.

| Name | Layer | Purpose |
|---|---|---|
| `formatMoney(centavos)` | Logic | Builds the money text (section 7) |
| `validateAmount(text)` | Logic | Applies rules 6.5 steps 1 to 5. Returns an `AmountResult` |
| `BankAccount` | Logic | Holds balance and history. Methods: `getBalance()`, `deposit(amount)`, `withdraw(amount)` (returns false and changes nothing if amount > balance), `getTransactions()` (read-only) |
| `recordTransaction(type, amount)` | Logic (inside BankAccount) | Creates a record with the balance after and adds it to the history. Called after the balance changes |
| `handleCheckBalance()` | UI | Opens the Check Balance view and refreshes it |
| `handleDeposit()` | UI | Reads the field, calls `validateAmount`, calls `deposit`, shows the message, refreshes balances |
| `handleWithdraw()` | UI | Same for withdrawal, including the insufficient-funds check |
| `handleViewHistory()` | UI | Opens the History view and rebuilds the table rows with a loop |
| `showView(view)` | UI | Switches the content area and the sidebar highlight |
| `confirmExit()` | UI | Shows the Exit dialog and closes the app if confirmed |
| `main` | UI | Applies the theme, creates the account, opens the window |

This is far more than the required two user-defined functions.

## 9. User interface specification

### 9.1 Design principles

- **Trustworthy and calm:** generous white space, soft shadows, no loud colors, no emoji.
- **Clear hierarchy:** one primary action per view, balance always easy to find.
- **Consistent:** every view uses the same header, card, field, and button styles.
- **Readable:** minimum text size 14 px, text contrast of at least 4.5 : 1.

### 9.2 Color palette (exact values; use these hex codes in both languages)

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

No default toolkit colors (Swing's blue-gray Metal look, Flutter's default purple seed) may remain visible anywhere.

### 9.3 Typography and spacing

| Item | Spec |
|---|---|
| Font family | Java: `Segoe UI`, falling back to `SansSerif`. Flutter: platform default font (Roboto, Segoe UI, SF) |
| Brand name (sidebar) | 22 px, bold, `onPrimary` |
| View title | 24 px, bold, `primaryDeep` |
| View subtitle | 14 px, regular, `textMuted` |
| Card title | 18 px, semibold, `textPrimary` |
| Body and labels | 14 px, `textPrimary` |
| Balance figure | 40 px, bold, `onPrimary` |
| Spacing scale | 4, 8, 16, 24, 32 px. Content area padding 32 px. Space between cards 24 px |
| Corner radius | Cards 12 px, buttons 8 px, inputs 8 px |
| Heights | Buttons 44 px, input fields 44 px, sidebar items 48 px, table rows 40 px |
| Card style | `surface` fill, 1 px `border`, soft shadow (about 10% black, blur 12, offset y 2) |

### 9.4 Window layout

```
+--------------------------------------------------------------------+
| SIDEBAR 240 px (primaryDark)  |  HEADER: view title + subtitle      |
|                               |-------------------------------------|
|  Simple Bank                  |  CONTENT AREA (background #F5FFFA)  |
|  Personal Banking             |   +-------------------------------+ |
|  ---------------------------  |   |  CARD (surface, radius 12)    | |
|  [1] Check Balance            |   |                               | |
|  [2] Deposit Money            |   +-------------------------------+ |
|  [3] Withdraw Money           |                                     |
|  [4] Transaction History      |                                     |
|  ---------------------------  |                                     |
|  [5] Exit                     |                                     |
+--------------------------------------------------------------------+
```

- **UI-1:** The sidebar is a fixed 240 px wide. The content area fills the rest.
- **UI-2:** Sidebar items: number badge and label, 48 px tall, `navText` text. On hover the item gets a translucent white tint. The **selected** item has a `primary` fill, `onPrimary` text, and a 4 px `mint` bar on its left edge.
- **UI-3:** The Exit item sits at the bottom of the sidebar, separated by a thin divider line.
- **UI-4:** The brand block at the top shows the text **Simple Bank** and the small caption **Personal Banking** (no logo image required).
- **UI-5:** Java enforces a minimum window size of 860 × 560. In Flutter, the layout must not overflow at widths of 860 px or more; below that, content scrolls.

### 9.5 Views

**View 1: Check Balance**

- Header: title *Check Balance*, subtitle *Your account at a glance*.
- Balance card (`primary` fill, white text): small label *Current balance*, the balance in 40 px bold.
- Below the card, a caption line (muted): the startup message the first time, afterwards *Current balance: {bal}* each time the user opens this view.

**View 2: Deposit Money**

- Header: title *Deposit Money*, subtitle *Add funds to your account*.
- One card containing, top to bottom: card title *Deposit details*; a chip *Available balance {bal}* (`lavenderMist`); label *Amount*; the amount field (peso sign shown as a fixed prefix inside the field, hint text `0.00`); the message area; a button row with **Deposit** (primary, filled) and **Clear** (outlined).
- The message area reserves its height so the layout does not jump when a message appears.

**View 3: Withdraw Money**

- Same layout as Deposit. Title *Withdraw Money*, subtitle *Take funds from your account*, card title *Withdrawal details*, button **Withdraw** (primary) and **Clear**.

**View 4: Transaction History**

- Header: title *Transaction History*, subtitle *All successful deposits and withdrawals*.
- One card containing a caption (*{n} transaction(s)*) and a table with columns **No.**, **Type**, **Amount**, **Balance After**. Header row: `lavender` background, bold `primaryDeep` text. Body rows: alternating `surface` and `lavenderMist`, 40 px tall. No. is left-aligned, Type left-aligned, Amount and Balance After right-aligned. The type text is colored `success` for Deposit and `withdrawal` for Withdrawal. The header stays visible while the rows scroll.
- Empty state: centered text *No transactions yet.* in `textMuted`, with no table.

**Exit dialog**

- Modal dialog, `surface` background, title *Exit Simple Bank*, message *Thank you for banking with us. Are you sure you want to exit?*, buttons **Exit** (primary) and **Stay** (outlined). Stay (and Esc) returns to the app unchanged; Exit closes the app.

### 9.6 Components and states

- **UI-6 Buttons:** primary = `primary` fill, `onPrimary` text, radius 8, hover slightly lighter (blend 10% white), pressed `primaryDark`. Outlined = `surface` fill, 1 px `primary` border and `primary` text, hover `lavender`. Disabled state is not used.
- **UI-7 Inputs:** `surface` fill, 1 px `border`, radius 8. On focus the border becomes 2 px `primary`. In the error state the border becomes `error`.
- **UI-8 Messages:** an inline box under the field. Error = `errorBg` fill with `error` text; success = `successBg` fill with `success` text. Each has a 1 px border of the same hue. The wording in section 10 is used as is (no extra prefix). Messages never rely on color alone: the text itself states the problem or success.
- **UI-9 Feedback behavior:** a message disappears when the user edits the field, clicks Clear, or switches views. After an invalid entry, focus stays in the field with its text selected.
- **UI-10 Sidebar behavior:** exactly one item is selected at a time. Exit is never shown as selected.

### 9.7 Keyboard and accessibility

- **UI-11:** Pressing **Enter** in the amount field presses the Deposit or Withdraw button. **Ctrl+1** to **Ctrl+5** open the five menu options (Cmd on macOS is optional). Tab order follows the visual order.
- **UI-12:** The window close button (the X) behaves like Exit (shows the Exit dialog). In Java this is required. In Flutter it is best effort (see the Dart roadmap); the Exit menu item is always required.
- **UI-13:** The amount field has an accessible name (*Amount to deposit* / *Amount to withdraw*). Sidebar items and buttons have accessible names equal to their labels.

## 10. Text and messages (word for word, both languages)

`{bal}` and `{amt}` mean `formatMoney` output.

| Situation | Text |
|---|---|
| Window title | Simple Bank |
| Startup caption (Check Balance view, first display) | Account initialized. Current balance: {bal} |
| Check balance caption (later displays) | Current balance: {bal} |
| Invalid number | Invalid input. Please enter a numeric amount (e.g., 500 or 250.75). |
| Must be positive | Amount must be greater than zero. |
| Max two decimals | Amount can have at most 2 decimal places. |
| Too large | Amount is too large. The maximum per transaction is ₱1,000,000,000.00. |
| Insufficient funds | Insufficient funds. Your balance is {bal}; you cannot withdraw {amt}. |
| Deposit success | Deposit successful. New balance: {bal} |
| Withdrawal success | Withdrawal successful. New balance: {bal} |
| Empty history | No transactions yet. |
| Exit dialog | Title: Exit Simple Bank. Message: Thank you for banking with us. Are you sure you want to exit? Buttons: Exit, Stay |

## 11. Required programming concepts and where they appear

| Concept | Where it is demonstrated |
|---|---|
| Variables and data types | Balance (`long`/`int`), text from fields (`String`), enum, record or class, `AmountResult` |
| Arithmetic operators | Balance + amount, balance - amount, centavos to pesos with division and remainder, centavos = whole × 100 + fraction |
| Comparison operators | amount ≤ 0, amount > balance, decimal count > 2, digit count > 10 |
| Conditional statements | `if`/`else` chain in `validateAmount`, insufficient-funds check, empty-history check, `switch` or map on the selected menu item |
| Loops | Comma-grouping loop in `formatMoney`; loop that builds one table row per transaction; loop that strips leading zeros or counts digits |
| User-defined functions | Section 8 (10 or more) |
| Data structure for records | `ArrayList` / `List` of `Transaction` |
| Input validation and error handling | `validateAmount` (never throws), `try`/`catch` or `tryParse` around number conversion, inline error messages, Exit confirmation |

The GUI framework's own event loop does not count as the "loop" requirement; the loops above are written by the team.

## 12. Acceptance tests

All tests start from a fresh launch (balance ₱5,000.00) unless stated. "Pass" means the exact text from section 10 and the behavior described.

**Functional tests**

| ID | Steps | Expected result |
|---|---|---|
| TC-01 | Launch the app | Window titled Simple Bank opens on Check Balance, hero card shows ₱5,000.00, caption shows the startup message |
| TC-02 | Open Deposit, then click Check Balance | Caption now reads *Current balance: ₱5,000.00*. History still empty |
| TC-03 | Deposit 1000 | Success message with ₱6,000.00. Field cleared. Available-balance chip and Check Balance card show ₱6,000.00 |
| TC-04 | Deposit 250.75 | Balance increases by exactly ₱250.75 |
| TC-05 | Deposit 0 | Must-be-positive error. Balance unchanged. Field text kept |
| TC-06 | Deposit -50 | Must-be-positive error |
| TC-07 | Deposit abc, blank, 1e3, .5, 5., 1,000 | Invalid-number error each time |
| TC-08 | Deposit 10.999 | Max-two-decimals error |
| TC-09 | Deposit 99999999999 | Too-large error |
| TC-10 | Deposit ' 500 ' (with spaces) | Accepted as 500 |
| TC-11 | Withdraw 2000 | Success message with ₱3,000.00 |
| TC-12 | Withdraw 5000 | Allowed. Balance ₱0.00 |
| TC-13 | Withdraw 5000.01 | Insufficient-funds error. Balance and history unchanged |
| TC-14 | Withdraw 0, -1, abc | Same errors as TC-05 to TC-07 |
| TC-15 | Open History with no transactions | *No transactions yet.* and no table |
| TC-16 | Deposit 3000, withdraw 2500.50, try an invalid and an overdraft attempt, open History | Only the two successful transactions, in order, with correct Balance After values |
| TC-17 | Do 8 or more actions, switching views often | App keeps running. All balances stay consistent |
| TC-18 | Click Exit, then Stay | Dialog appears. App stays open and unchanged |
| TC-19 | Click Exit, then Exit | App closes |

**Interface tests**

| ID | Steps | Expected result |
|---|---|---|
| TC-20 | Press Enter in the amount field | Same as clicking the action button |
| TC-21 | Press Ctrl+1 to Ctrl+5 | Opens the matching view (5 opens the Exit dialog) |
| TC-22 | Type an invalid amount, then edit the field | The error message disappears |
| TC-23 | Add 60 transactions and open History | Table scrolls, header stays visible, no lag |
| TC-24 | Compare every screen to sections 9.2 to 9.6 | Colors, spacing, fonts, and alignment match. No default toolkit colors visible |
| TC-25 | Resize the window (Java: to its minimum; Flutter: to 860 px wide) | No clipped or overlapping controls |
| TC-26 | Click the window's X button | Java: Exit dialog. Flutter: Exit dialog or documented limitation |
| TC-27 | Tab through the Deposit view | Focus moves amount field → Deposit → Clear with a visible focus ring |

**Logic and parity tests**

| ID | Steps | Expected result |
|---|---|---|
| TC-28 | Run the automated logic tests (no window) | `formatMoney` and `validateAmount` pass every case in section 7, 6.5 and TC-05 to TC-10 |
| TC-29 | Place Java and Dart screenshots of all four views side by side | Same layout, colors, wording, and numbers |

## 13. Parity checklist (Java vs Dart)

- Same menu order, same view layouts, same hex colors, same message text.
- Same validation order (6.5) and same results for TC-01 to TC-29.
- Balance stored in centavos in both.
- Allowed differences: the native font rendering, scrollbar and dialog chrome, window title bar, the Flutter minimum window size not being enforced, and how the window close button is intercepted.

## 14. Definition of done

- All acceptance tests pass in both apps.
- Each project builds with no errors or warnings (`javac -Xlint:all`, `flutter analyze`).
- Logic tests run without opening a window.
- Code has short comments on each class and function, and a README with run instructions.
- The team can point to where each concept in section 11 appears, and can demo the app in two minutes.

## 15. Decisions made in this PRD (change here first if you disagree)

- **Swing for Java and Flutter for Dart.** Swing ships with the JDK, so no extra libraries are needed. Flutter is Dart's standard GUI framework.
- **Money is kept in centavos** (whole numbers) instead of decimals.
- **Invalid menu selection** (scenario F) is prevented by the design, because the GUI menu only offers the five valid options.
- **Cancel option removed.** The GUI has a Clear button instead, and the user can simply switch views.
- **Messages appear inline**, and the Exit confirmation is the only dialog.
- **A ₱1,000,000,000.00 limit per transaction** prevents number overflow.
- **History has no timestamps**, because the scenario only asks for type, amount, and balance after.
- **Palette:** deep lavender (`#5B4B8A`) with mint cream (`#F5FFFA`) as the working background, chosen for strong text contrast and a calm, professional look.
