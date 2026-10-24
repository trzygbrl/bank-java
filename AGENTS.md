# Agent instructions: Simple Bank (Java Swing)

## Always do this first
1. Read docs/PRD.md. It is the single source of truth. If a prompt and the PRD disagree, the PRD wins: stop and tell the user about the conflict.
2. Read docs/STATE.md to see what already exists.
3. Do ONLY the task in the prompt. Do not add features or files outside it.

## Project facts
- Java 17, Swing only. No third-party libraries. No Maven or Gradle.
- Source files are in src/ with no package declarations.
- Compile: javac -encoding UTF-8 -d out src/*.java
- Run: java -cp out BankApp
- Logic tests: java -ea -cp out LogicTests
- Money is stored as long centavos. The peso sign comes only from the constant "\u20B1".
- Logic classes (BankAccount, Transaction, TransactionType, MoneyUtil, AmountResult) must not import javax.swing or java.awt.
- Colors, sizes, and wording come from PRD sections 9 and 10. Use the constants in Theme.java; never hard-code a color.

## Rules
- Do not edit docs/PRD.md or docs/ROADMAP.md.
- Do not commit, push, switch branches, or rewrite Git history. The user does that.
- Compile with no warnings and run the available tests before reporting.
- At the end, update docs/STATE.md (files, classes and public methods, known deviations).
- Report: files changed, how you tested, and anything that deviates from the PRD.