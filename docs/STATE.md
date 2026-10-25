PROJECT STATE (before Task J-01)
- Files: docs/PRD.md, docs/ROADMAP.md, AGENTS.md, CLAUDE.md, README.md, .gitignore, .gitattributes
- Known problems or deviations from the PRD: none

PROJECT STATE (after Task J-01)
- Task: J-01 — Project setup and skeleton
- Status: Completed
- Files: docs/PRD.md, docs/ROADMAP.md, AGENTS.md, README.md, .gitignore, .gitattributes, docs/STATE.md, src/BankApp.java
- Classes and their public methods now:
  - BankApp: public static void main(String[] args)
- Theme constants and components that exist now: none (skeleton only)
- Known problems or deviations from the PRD: none
- Changes:
  - Created src/BankApp.java (JFrame titled "Simple Bank", size 1000x680, centered on screen, EXIT_ON_CLOSE, centered JLabel "Setup OK").
  - Renamed .README.md to README.md and updated it with JDK prerequisites, compilation command, and run command.
  - Verified .gitignore already contains *.class, out/, .idea/, and *.iml.
  - Updated docs/STATE.md.
- Validation:
  - java -version: 23.0.2 (JDK 17+ requirement satisfied)
  - javac -version: 23.0.2 (JDK 17+ requirement satisfied)
  - Compile: javac -encoding UTF-8 -d out src/*.java (succeeded without errors or warnings)
  - Run: java -cp out BankApp (successfully launched JFrame titled "Simple Bank", dimensions 1000x680, centered on screen, displaying centered JLabel "Setup OK", default close operation EXIT_ON_CLOSE)
- Limitations: none
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp

PROJECT STATE (after Task J-02)
- Task: J-02 — Logic model
- Status: Completed
- Files: docs/PRD.md, docs/ROADMAP.md, AGENTS.md, CLAUDE.md, README.md, .gitignore, .gitattributes, docs/STATE.md, src/BankApp.java, src/TransactionType.java, src/Transaction.java, src/BankAccount.java
- Classes and their public methods now:
  - BankApp: public static void main(String[] args) (TEMP logic test remains in BankApp.main)
  - TransactionType: enum values DEPOSIT, WITHDRAWAL; public String getLabel()
  - Transaction: record components type, amountCentavos, balanceAfterCentavos
  - BankAccount: public long getBalance(), public void deposit(long amountCentavos), public boolean withdraw(long amountCentavos), public List<Transaction> getTransactions()
- Theme constants and components that exist now: none (skeleton window with "Setup OK" JLabel)
- Known problems or deviations from the PRD: none
- Changes:
  - Created src/TransactionType.java with DEPOSIT and WITHDRAWAL values and getLabel() method.
  - Created src/Transaction.java record with type, amountCentavos, and balanceAfterCentavos components.
  - Created src/BankAccount.java with STARTING_BALANCE_CENTAVOS = 500_000L, getBalance(), deposit(long), withdraw(long), and getTransactions() returning Collections.unmodifiableList.
  - Updated src/BankApp.java with TEMP test block at the start of main before window creation.
  - Updated docs/STATE.md.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java (succeeded with no errors and no warnings)
  - Run: java -cp out BankApp (TEMP test verified: starting balance 500000, after 100000 deposit balance 600000, after 200000 withdraw balance 400000 with 2 transactions recorded in chronological order; overdraft attempt with 99999999 returned false and left balance at 400000 and transaction history unaltered; Simple Bank window launched successfully)
  - Unmodifiable list verification: attempting modification on account.getTransactions() threw UnsupportedOperationException and left account history unchanged.
  - Verified logic classes do not import javax.swing or java.awt.
- Limitations: none
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp

PROJECT STATE (after Task J-03)
- Task: J-03 — MoneyUtil and logic tests
- Status: Completed
- Files: docs/PRD.md, docs/ROADMAP.md, AGENTS.md, CLAUDE.md, README.md, .gitignore, .gitattributes, docs/STATE.md, src/BankApp.java, src/TransactionType.java, src/Transaction.java, src/BankAccount.java, src/AmountResult.java, src/MoneyUtil.java, src/LogicTests.java
- Classes and their public methods now:
  - BankApp: public static void main(String[] args) (TEMP logic test remains in BankApp.main)
  - TransactionType: enum values DEPOSIT, WITHDRAWAL; public String getLabel()
  - Transaction: record components type, amountCentavos, balanceAfterCentavos
  - BankAccount: public long getBalance(), public void deposit(long amountCentavos), public boolean withdraw(long amountCentavos), public List<Transaction> getTransactions()
  - AmountResult: public static AmountResult ok(long centavos), public static AmountResult error(String message), public boolean isValid(), public long getCentavos(), public String getErrorMessage(), public long centavos(), public String errorMessage()
  - MoneyUtil: public static String formatMoney(long centavos), public static AmountResult validateAmount(String text)
  - LogicTests: public static void main(String[] args)
- Theme constants and components that exist now: none (skeleton window with "Setup OK" JLabel)
- Known problems or deviations from the PRD: none
- Changes:
  - Created src/AmountResult.java: final class holding validated centavos or error message with static ok(long) and error(String) factory methods and isValid() indicator.
  - Created src/MoneyUtil.java: constants CURRENCY_SYMBOL ("\u20B1"), MAX_AMOUNT_CENTAVOS (100_000_000_000L), exact PRD Section 10 error message constants, formatMoney(long) with comma thousands grouping loop and integer math, and validateAmount(String) implementing PRD Section 6.5 steps 1 to 5 in exact sequence.
  - Created src/LogicTests.java: standalone test runner testing formatMoney, validateAmount (all PRD cases, edge cases, and precedence), AmountResult, and BankAccount regression tests.
  - Updated docs/STATE.md.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java (succeeded with no errors and no warnings)
  - Logic tests: java -ea -cp out LogicTests (85 test cases passed, 0 failed, exit code 0)
  - Run: java -cp out BankApp (TEMP test verified balance 400000, 2 transactions, overdraft rejected, and Simple Bank window launched successfully)
  - Verified no javax.swing or java.awt imports in src/AmountResult.java, src/MoneyUtil.java, or src/LogicTests.java.
- Limitations: none
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests

PROJECT STATE NOTE (after Task J-07)
- Task: J-07 — Check Balance view
- Status: Completed
- Files changed: src/BalanceView.java, src/BankFrame.java, docs/STATE.md
- Classes and their public methods:
  - BalanceView: public BalanceView(BankAccount account), public void refresh()
  - BankFrame: public void refreshAll() (also retains J-06 public methods)
- Implementation:
  - Replaced the balance placeholder with a themed balance card and first-display/startup caption.
  - Registered BalanceView for refreshAll(); Check Balance refreshes the balance and caption whenever opened.
- Known deviations or limitations: Other three views remain placeholders as scoped for this task.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java
  - Logic tests: java -ea -cp out LogicTests
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests
PROJECT STATE (after Task J-04)
- Task: J-04 — Theme (colors, fonts, spacing)
- Status: Completed
- Files: docs/PRD.md, docs/ROADMAP.md, AGENTS.md, CLAUDE.md, README.md, .gitignore, .gitattributes, docs/STATE.md, src/BankApp.java, src/TransactionType.java, src/Transaction.java, src/BankAccount.java, src/AmountResult.java, src/MoneyUtil.java, src/LogicTests.java, src/Theme.java
- Classes and their public methods now:
  - BankApp: public static void main(String[] args) (TEMP logic test remains in BankApp.main)
  - TransactionType: enum values DEPOSIT, WITHDRAWAL; public String getLabel()
  - Transaction: record components type, amountCentavos, balanceAfterCentavos
  - BankAccount: public long getBalance(), public void deposit(long amountCentavos), public boolean withdraw(long amountCentavos), public List<Transaction> getTransactions()
  - AmountResult: public static AmountResult ok(long centavos), public static AmountResult error(String message), public boolean isValid(), public long getCentavos(), public String getErrorMessage(), public long centavos(), public String errorMessage()
  - MoneyUtil: public static String formatMoney(long centavos), public static AmountResult validateAmount(String text)
  - LogicTests: public static void main(String[] args)
  - Theme: public static Font fontBrand(), public static Font fontViewTitle(), public static Font fontSubtitle(), public static Font fontCardTitle(), public static Font fontBody(), public static Font fontBodyBold(), public static Font fontBalance(), public static void applyGlobalDefaults(), public static void enableAntialiasing(Graphics2D g)
- Theme constants and components that exist now:
  - Colors: PRIMARY (#5B4B8A), PRIMARY_DARK (#433670), PRIMARY_DEEP (#2E2552), LAVENDER (#E9E4F7), LAVENDER_MIST (#F3F0FA), BACKGROUND (#F5FFFA), SURFACE (#FFFFFF), MINT (#BFE8D4), SUCCESS (#2E7D5B), SUCCESS_BG (#E3F5EC), ERROR (#B3261E), ERROR_BG (#FDECEA), WITHDRAWAL (#A8324A), TEXT_PRIMARY (#2B2540), TEXT_MUTED (#6B6585), BORDER (#D9D3EC), ON_PRIMARY (#FFFFFF), NAV_TEXT (#E4DEF5)
  - Spacing: SP_4 (4), SP_8 (8), SP_16 (16), SP_24 (24), SP_32 (32)
  - Radius: RADIUS_CARD (12), RADIUS_CONTROL (8)
  - Heights: BUTTON_HEIGHT (44), FIELD_HEIGHT (44), NAV_ITEM_HEIGHT (48), TABLE_ROW_HEIGHT (40)
  - Sidebar width: SIDEBAR_WIDTH (240)
  - Components: skeleton window with "Setup OK" JLabel on mint cream (#F5FFFA) content background
- Known problems or deviations from the PRD: none
- Changes:
  - Created src/Theme.java: centralized design tokens (18 exact hex colors from PRD 9.2, spacing scale, corner radii, component heights, sidebar width, font factories with Segoe UI detection and SansSerif fallback, applyGlobalDefaults() for UIManager palette configuration, and enableAntialiasing(Graphics2D)).
  - Updated src/BankApp.java: added Theme.applyGlobalDefaults() at startup and set the JFrame content pane background to Theme.BACKGROUND (#F5FFFA).
  - Updated docs/STATE.md.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java (succeeded with no errors and no warnings)
  - Logic tests: java -ea -cp out LogicTests (85 test cases passed, 0 failed, exit code 0)
  - Run: java -cp out BankApp (TEMP logic test executed, Simple Bank JFrame 1000x680 centered on screen launched successfully with mint cream #F5FFFA content area background and centered "Setup OK" label)
  - Theme verification: verified all 18 hex color values, spacing, radius, height, font sizes/styles, and UIManager keys.
- Limitations: none
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests

PROJECT STATE (after Task J-05)
- Task: J-05 — Reusable styled components
- Status: Completed
- Files: docs/PRD.md, docs/ROADMAP.md, AGENTS.md, CLAUDE.md, README.md, .gitignore, .gitattributes, docs/STATE.md, src/BankApp.java, src/TransactionType.java, src/Transaction.java, src/BankAccount.java, src/AmountResult.java, src/MoneyUtil.java, src/LogicTests.java, src/Theme.java, src/RoundedPanel.java, src/AppButton.java, src/AmountField.java, src/MessageBox.java, src/ViewHeader.java
- Classes and their public methods now:
  - BankApp: public static void main(String[] args) (TEMP J-02 logic test and TEMP J-05 component demo remain)
  - TransactionType: enum values DEPOSIT, WITHDRAWAL; public String getLabel()
  - Transaction: record components type, amountCentavos, balanceAfterCentavos
  - BankAccount: public long getBalance(), public void deposit(long amountCentavos), public boolean withdraw(long amountCentavos), public List<Transaction> getTransactions()
  - AmountResult: public static AmountResult ok(long centavos), public static AmountResult error(String message), public boolean isValid(), public long getCentavos(), public String getErrorMessage(), public long centavos(), public String errorMessage()
  - MoneyUtil: public static String formatMoney(long centavos), public static AmountResult validateAmount(String text)
  - LogicTests: public static void main(String[] args)
  - Theme: public static Font fontBrand(), fontViewTitle(), fontSubtitle(), fontCardTitle(), fontBody(), fontBodyBold(), fontBalance(), public static void applyGlobalDefaults(), public static void enableAntialiasing(Graphics2D g)
  - RoundedPanel: public RoundedPanel(Color fill, int radius, boolean drawShadowAndBorder)
  - AppButton: public AppButton(String text, boolean primary), public boolean isPrimary()
  - AmountField: public String getText(), public void setText(String), public void selectAllAndFocus(), public void addEnterListener(ActionListener), public void addChangeListener(Runnable), public void setAccessibleName(String), public void setErrorState(boolean), public boolean isErrorState()
  - MessageBox: public void showError(String), public void showSuccess(String), public void clear()
  - ViewHeader: public ViewHeader(String title, String subtitle), public String getTitle(), public String getSubtitle()
- Theme constants and components that exist now:
  - All colors, spacing, radii, heights, sidebar width from J-04
  - RoundedPanel: rounded rectangle with custom fill and optional soft shadow + border outline (PRD card style)
  - AppButton: PRIMARY style (filled primary, white text, hover blend 10% white, pressed primaryDark) and OUTLINED style (surface fill, 1px primary border, primary text, hover lavender); 2px focus ring; radius 8, height 44
  - AmountField: peso prefix, borderless text field, hint "0.00", 1px border normal, 2px primary on focus, error border override; height 44, radius 8
  - MessageBox: error (errorBg/error) and success (successBg/success) states with 1px border, rounded radius 8, multiline wrapping, ~48px reserved height
  - ViewHeader: title (fontViewTitle, primaryDeep) above subtitle (fontSubtitle, textMuted)
- Known problems or deviations from the PRD: none
- Changes:
  - Created src/RoundedPanel.java: custom-painted JPanel with rounded corners, fill color, and optional soft shadow + 1px border outline.
  - Created src/AppButton.java: custom-painted JButton with PRIMARY and OUTLINED styles, hover/pressed state feedback, and 2px keyboard focus ring.
  - Created src/AmountField.java: composite input with fixed peso prefix, borderless text field, hint text, focus/error border states, and document change listener.
  - Created src/MessageBox.java: inline feedback area with error/success styles, multiline text wrapping, and stable ~48px reserved height.
  - Created src/ViewHeader.java: title and subtitle labels with Theme typography.
  - Updated src/BankApp.java: replaced "Setup OK" label with TEMP component demo showing a RoundedPanel card containing ViewHeader, AmountField, both AppButton styles, and both MessageBox states with interactive wiring.
  - Updated docs/STATE.md.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java (succeeded with no errors and no warnings)
  - Logic tests: java -ea -cp out LogicTests (85 test cases passed, 0 failed, exit code 0)
  - Run: java -cp out BankApp (TEMP logic test executed; component demo window launched on mint cream background with rounded card, header, amount field with peso prefix and hint, both button styles, and both message box styles visible)
  - Component verification: automated checks confirmed RoundedPanel shadow insets, AppButton heights and style flags, AmountField getText/setText/errorState/changeListener, MessageBox reserved height and state transitions, ViewHeader title/subtitle text.
  - Visual verification: hover, pressed, and focus states require manual inspection by the user.
- Limitations: hover, pressed, and keyboard focus ring states were verified to compile and render but require manual visual inspection
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests

PROJECT STATE NOTE (after Task J-06)
- Task: J-06 — Main window shell, sidebar, view switching, and Exit dialog
- Status: Completed
- Files changed: src/Refreshable.java, src/NavItem.java, src/BankFrame.java, src/BankApp.java, docs/STATE.md
- Classes and their public methods:
  - Refreshable: public void refresh()
  - NavItem: public NavItem(int number, String label), public void setSelected(boolean selected)
  - BankFrame: public BankFrame(), public void showView(String view), public void handleCheckBalance(), public void handleDeposit(), public void handleWithdraw(), public void handleViewHistory(), public void confirmExit()
  - BankApp: public static void main(String[] args)
- Implementation:
  - Added a 240 px themed sidebar with brand, five numbered navigation items, selected state and exit divider.
  - Added a centered 1000x680 BankFrame with an 860x560 minimum size, four CardLayout placeholder views, and window-close Exit confirmation.
  - Replaced the J-05 temporary demo and J-02 temporary logic checks with launch of BankFrame on the Swing event-dispatch thread.
- Known deviations or limitations: The four content views are headers/placeholders only; banking interaction and view refresh behavior are outside this shell task.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java (succeeded without warnings)
  - Logic tests: java -ea -cp out LogicTests (85 passed, 0 failed)
  - GUI launch and visual inspection were not performed.
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests

PROJECT STATE NOTE (after Task J-07)
- Task: J-07 — Check Balance view
- Status: Completed
- Files changed: src/BalanceView.java, src/BankFrame.java, docs/STATE.md
- Classes and their public methods:
  - BalanceView: public BalanceView(BankAccount account), public void refresh()
  - BankFrame: public void refreshAll() (also retains J-06 public methods)
- Implementation:
  - Replaced the balance placeholder with a themed balance card and first-display/startup caption.
  - Registered BalanceView for refreshAll(); Check Balance refreshes the balance and caption whenever opened.
- Known deviations or limitations: Other three views remain placeholders as scoped for that task.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java
  - Logic tests: java -ea -cp out LogicTests
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests

PROJECT STATE NOTE (after Task J-08)
- Task: J-08 — Deposit view
- Status: Completed
- Files changed: src/DepositView.java, src/BankFrame.java, docs/STATE.md
- Classes and their public methods:
  - DepositView: public DepositView(BankAccount account, Runnable onBalanceChanged), public void refresh()
  - BankFrame: public void handleDeposit(), public void refreshAll() (also retains earlier public methods)
- Implementation:
  - Replaced the Deposit placeholder with the themed amount-entry card, available-balance chip, inline feedback, validation, and clear/deposit actions.
  - Successful deposits update the account, chip, success message, and other registered balance views. Editing, clearing, and reopening the view clear feedback.
- Known deviations or limitations: Withdraw and History views remain placeholders as scoped for this task.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java (succeeded without warnings)
  - Logic tests: java -ea -cp out LogicTests (85 passed, 0 failed)
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests

PROJECT STATE NOTE (after Task J-09)
- Task: J-09 — Withdraw view
- Status: Completed
- Files changed: src/WithdrawView.java, src/BankFrame.java, docs/STATE.md
- Classes and their public methods:
  - WithdrawView: public WithdrawView(BankAccount account, Runnable onBalanceChanged), public void refresh()
  - BankFrame: public void handleWithdraw() (also retains earlier public methods)
- Implementation:
  - Replaced the Withdraw placeholder with the themed withdrawal form, validation, insufficient-funds handling, and clear/withdraw actions.
  - Supports withdrawing the exact available balance; successful withdrawals update the chip and trigger registered balance-view refreshes.
- Known deviations or limitations: Transaction History remains a placeholder as scoped for this task.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java (succeeded without warnings)
  - Logic tests: java -ea -cp out LogicTests (85 passed, 0 failed)
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests

PROJECT STATE (after Task J-10)
- Task: J-10 — Transaction History view
- Status: Completed
- Files changed: src/HistoryView.java (created), src/BankFrame.java (modified), docs/STATE.md
- Classes and their public methods now:
  - HistoryView: public HistoryView(BankAccount account), public void refresh()
  - BankFrame: retains all J-06 through J-09 public methods; handleViewHistory() now calls historyView.refresh()
- All previously existing classes and their public methods are unchanged.
- Implementation:
  - Created src/HistoryView.java: extends JPanel, implements Refreshable. Layout follows PRD 9.5 View 4.
    ViewHeader("Transaction History", "All successful deposits and withdrawals") at top.
    One RoundedPanel card (SURFACE, radius 12, shadow + border) containing:
      - Caption label "{n} transaction(s)" (TEXT_MUTED, 14 px fontBody).
      - Inner CardLayout switching between CARD_TABLE (JTable in JScrollPane) and CARD_EMPTY
        (centered "No transactions yet." JLabel, TEXT_MUTED).
    Table: non-editable DefaultTableModel, columns "No.", "Type", "Amount", "Balance After".
    Row height 40 (Theme.TABLE_ROW_HEIGHT). Horizontal grid lines only (1 px BORDER color).
    Column reordering disabled. Selection background LAVENDER.
    Custom header renderer: LAVENDER background, bold PRIMARY_DEEP, height 40, left/right alignment per column.
    Custom body renderer (BodyRenderer inner class): alternating SURFACE/LAVENDER_MIST rows; No. and Type
    left-aligned, Amount and Balance After right-aligned; 12 px horizontal padding; Type text SUCCESS for
    "Deposit", WITHDRAWAL for "Withdrawal"; body font Theme.fontBody().
    JScrollPane: no border, SURFACE viewport, setFillsViewportHeight(true), scrollbars themed via
    BasicScrollBarUI subclass (LAVENDER thumb, BACKGROUND track, zero-size arrow buttons).
    refresh() clears model, explicit for-loop over account.getTransactions(), adds rows
    [i+1, type.getLabel(), formatMoney(amount), formatMoney(balAfter)], updates caption, shows
    CARD_EMPTY when n==0, CARD_TABLE otherwise. Account and list never modified.
  - Updated src/BankFrame.java: added private final HistoryView historyView field; initialized in
    constructor; replaced HISTORY createPlaceholder() call in buildContent() with historyView;
    handleViewHistory() now calls historyView.refresh() after showView().
- Known deviations or limitations: none. createPlaceholder() helper remains for potential future use.
- Validation:
  - Compile: javac -Xlint:all -encoding UTF-8 -d out src/*.java — succeeded with zero errors, zero warnings.
  - Logic tests: java -ea -cp out LogicTests — 85 passed, 0 failed.
  - TC-16 data check (temporary TC16Check.java, removed after validation):
      deposit 300_000 centavos -> row 1: 1 / Deposit / 3,000.00 / 8,000.00 PASS
      withdraw 250_050 centavos -> row 2: 2 / Withdrawal / 2,500.50 / 5,499.50 PASS
  - TC-23 data check: 60 consecutive deposits recorded 60 transactions correctly PASS.
  - Visual acceptance (TC-15, TC-16, TC-23, TC-24): must be verified manually by running
    java -cp out BankApp and walking through the view with the real window.
- Compile command: javac -Xlint:all -encoding UTF-8 -d out src/*.java
  (javac is at: C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\javac.exe)
- Run command: java -cp out BankApp
- Logic test command: java -ea -cp out LogicTests


