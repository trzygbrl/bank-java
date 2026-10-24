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

