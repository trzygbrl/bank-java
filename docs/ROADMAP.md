# Roadmap: Simple Bank Desktop App in Java (Swing GUI)

Builds the **Simple Bank Desktop App (GUI)** described in `PRD_Simple_Bank_GUI.md` (the single source of truth) with **Java 17 and Swing**, using a professional lavender and mint cream design. Thirteen tasks, each with a prompt-generation prompt.

---

## How to use this roadmap

### What you need

- `PRD_Simple_Bank_GUI.md` (v2.0), open or copied to text.
- **Prompt-Generator AI:** any AI chat assistant. It writes prompts but never the app code.
- **Coding agent:** an AI that edits files and runs commands in your project folder (for example Claude Code, Cursor, or GitHub Copilot agent mode).
- JDK 17 or newer, a terminal, Git (recommended), and a computer with a screen (the app opens a window).

> **Before Task J-01, complete the Setup guide below:** install the tools (Part A) and set up the GitHub repository (Part B). The PRD lives in the repository at `docs/PRD.md`, so your coding agent can read it at any time.

### The pipeline for every task

1. **Read the task** in this roadmap and copy its **Prompt-generation prompt**.
2. **Paste it into the Prompt-Generator AI.** It replies with a finished coding-agent prompt in one code block, plus a 3-item checklist.
3. **Review the generated prompt** with its checklist. It must contain the exact hex colors, sizes, wording, and function names from the PRD, the file paths, the constraints, and acceptance criteria. If something is missing, tell the Prompt-Generator what to add.
4. **Paste the generated prompt into the coding agent** and let it work.
5. **Verify** against the task's Done-when list by compiling, running, and looking at the window yourself.
6. **Commit and open a pull request** from your task branch (see Setup guide, Part B8), get one teammate to review it, merge it, and make sure `docs/STATE.md` was updated.

If the result fails a check, paste the exact error, or describe or screenshot what looks wrong, back into the coding agent and ask for a fix. Do not move to the next task until the current one passes.

### One-time setup: start the Prompt-Generator session

Open a new chat with the Prompt-Generator AI and paste this once, with the full PRD at the end:

```text
You are the Prompt-Generator for my group's coding project. You do NOT write the application code yourself. Your only job is to write high-quality prompts that I will paste into a separate AI coding agent.

Project: a desktop banking app with a professional GUI, written in Java 17 using Swing only (no third-party libraries). It is built from the PRD pasted below. The PRD is the single source of truth: exact messages, hex colors, sizes, validation order, function names, and data design all come from it.

Rules for every prompt you write:
1. The prompt must be self-contained. The coding agent has not seen our chat, so put the needed PRD details inside the prompt (exact message text, hex colors, pixel sizes, function names, rules).
2. Start with a one-line role and goal. Then give context, the files to create or edit, step-by-step instructions, constraints, acceptance criteria, and a short Do-not list.
3. Constraints that always apply: JDK 17, Swing only, no FlatLaf or other libraries, files live in src/ with no package declaration, compile with javac -encoding UTF-8, money stored as long centavos, the peso sign written as the escape \u20B1 in one constant, logic classes must NOT import javax.swing or java.awt, do not change code unrelated to the task, do not add features that are not in the PRD.
4. Begin the prompt by telling the agent to read AGENTS.md, docs/PRD.md (name the sections the task needs), and docs/STATE.md in the repository, and to update docs/STATE.md at the end. Still copy the exact messages, hex codes, and sizes into the prompt. End the prompt by asking the agent to report: files changed, how to compile and run, what it tested, and to describe what the window looks like.
5. Reply with ONLY the finished prompt inside one code block, followed by a 3-item checklist of what I should verify before pasting it.
6. If something is missing, ask me up to 3 short questions before writing the prompt.

Reply with the single word Ready and wait for my first task.

PRD:
[PASTE THE FULL PRD HERE]
```

### Project State Note

The coding agent starts fresh each time, but it can read the repository (`AGENTS.md`, `docs/PRD.md`, `docs/STATE.md`). The Prompt-Generator AI cannot, so it needs the note pasted in. Keep the note in `docs/STATE.md` (the agent updates it at the end of every task and it is merged with the task's pull request). Before each task, copy the current content of `docs/STATE.md` and paste it at the TOP of the task's prompt-generation prompt. The note format is:

```text
PROJECT STATE (after Task J-__)
- Files: (list them)
- Classes and their public methods now: (name and signature of each)
- Theme constants and components that exist now: (list)
- Known problems or deviations from the PRD: (list or none)
- Compile command: javac -encoding UTF-8 -d out src/*.java
- Run command: java -cp out BankApp
```

### Rules of thumb

- Do tasks in order. Each one builds on the previous.
- One task per coding-agent prompt. Do not combine tasks.
- Always look at the real window after UI tasks (J-04 to J-11). Compare it to PRD section 9, not just "it runs".
- Temporary test code is allowed in J-02 to J-05 and is removed in J-13.
- If the Prompt-Generator drifts from the PRD, remind it: "The PRD is the single source of truth."
- If you split work among teammates: do J-01 to J-06 together, then give J-07 to J-10 to different people on separate Git branches (each view is its own file, so merges are easy; only BankFrame.java is shared).

### Troubleshooting

| Problem | Fix |
|---|---|
| Peso sign shows as a box or question mark | Make sure the constant uses the escape `\u20B1` and the font is Segoe UI or SansSerif. Compile with `-encoding UTF-8` |
| Window looks like the old blue-gray Swing | Some component is still using the default look. Ask the agent to list every default-colored component and theme it |
| Text looks jagged | Ask the agent to enable antialiasing in every custom `paintComponent` |
| Nothing appears when running over SSH or a headless machine | Swing needs a screen. Run on a machine with a display |

---

## Setup guide (do this before Task J-01)

### Part A: Install and verify the Java tools (every member)

**1. Install a JDK (version 17 or newer).** Everyone on the team should use the same major version (for example 21).

| Your system | How to install |
|---|---|
| Windows | In PowerShell: `winget install EclipseAdoptium.Temurin.21.JDK`. Or download the installer from adoptium.net and tick **Add to PATH** and **Set JAVA_HOME**. Close and reopen the terminal afterward |
| macOS | `brew install --cask temurin@21` (or the .pkg installer from adoptium.net) |
| Ubuntu or Debian | `sudo apt update && sudo apt install openjdk-21-jdk`. Use the full JDK, **not** the `-headless` package, because Swing needs the desktop libraries |
| Fedora | `sudo dnf install java-21-openjdk-devel` |

**2. Verify** in a new terminal:

```bash
java -version
javac -version
```

Both must report version 17 or higher. If `javac` is not found, you installed only a runtime (JRE) or the PATH is wrong: reinstall the JDK and reopen the terminal.

**3. A screen is required.** Swing opens a real window, so run the app on a computer with a display (not over plain SSH).

**4. Install Git** and set your identity (once per computer):

| System | Install Git |
|---|---|
| Windows | `winget install Git.Git` (then reopen the terminal) |
| macOS | `xcode-select --install` or `brew install git` |
| Linux | `sudo apt install git` |

```bash
git --version
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
git config --global init.defaultBranch main
```

Use the same email as your GitHub account. On Windows also run `git config --global core.autocrlf true`; on macOS and Linux run `git config --global core.autocrlf input`.

**5. Choose an editor (optional, you can mix).** The project is plain Java with no Maven or Gradle, so every command in this roadmap works from a terminal.

- **IntelliJ IDEA Community:** open the repository folder, then **File → Project Structure** and set the SDK to your JDK (17+). Mark the `src` folder as **Sources** and set the compiler output folder to `out`.
- **VS Code:** install the **Extension Pack for Java**, then open the repository folder. It detects the `src` folder.

**6. Set up your coding agent** (Claude Code, Cursor, GitHub Copilot agent mode, or similar) following its own installation guide, and sign in. Always open it **at the repository root**.

**7. Create a GitHub account** if you do not have one, and turn on two-factor authentication.

### Setup checklist

- [ ] `java -version` and `javac -version` show 17 or higher
- [ ] `git --version` works and your name and email are configured
- [ ] You can open a GUI window on this computer
- [ ] Your GitHub account is created and you accepted the repository invitation (Part B)
- [ ] You can open a terminal at the repository root and start your coding agent there

### Part B: Set up the GitHub repository (team collaboration)

This project uses **two separate repositories**, one per language. This roadmap uses **simple-bank-java**. The other language has its own repository (simple-bank-dart) and its own roadmap. Both repositories hold an identical copy of the PRD.

#### B1. Roles

| Role | Who | Does |
|---|---|---|
| **Repo Owner** | One member | Creates the repository, invites everyone, seeds the first files (B4), sets branch protection (B6) |
| **Members** | Everyone else (and the owner) | Each takes tasks, works on a branch, opens pull requests, reviews others' work |

#### B2. Create the repository (Repo Owner)

1. Sign in at github.com and click **New repository** (the green button, or the + menu).
2. Repository name: **simple-bank-java**. Visibility: **Private** (or Public if your instructor needs to see it without an invite).
3. Leave **Add a README**, **.gitignore**, and **license** all **unchecked** (we add our own, so the first push has no conflicts).
4. Click **Create repository** and copy its HTTPS URL, for example `https://github.com/<owner>/simple-bank-java.git`.

#### B3. Invite teammates (Repo Owner)

1. Repository **Settings** → **Collaborators** (or **Collaborators and teams**) → **Add people**.
2. Enter each member's GitHub username or email and choose the **Write** role.
3. Each member accepts the invitation from the email or from github.com/notifications. Until they accept, they cannot push.
4. Also add your instructor as a collaborator (Read) if they requested it.

#### B4. Seed the repository with the PRD, roadmap, and agent instructions (Repo Owner)

Do this once, before any coding task, so the coding agent can read the PRD at any time.

```bash
git clone https://github.com/<owner>/simple-bank-java.git
cd simple-bank-java
mkdir docs
```

Copy the files into the repository, **renaming** them:

| Copy this file | To this path |
|---|---|
| `PRD_Simple_Bank_GUI.md` | `docs/PRD.md` |
| `ROADMAP_Java_Swing_GUI.md` | `docs/ROADMAP.md` |

Then create these files at the repository root:

**`AGENTS.md`**

```markdown
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
```

**`CLAUDE.md`** (Claude Code reads this name; other agents such as Cursor, Copilot, and Codex read AGENTS.md)

```markdown
Read and follow AGENTS.md in this repository.
```

**`.gitignore`**

```text
# Build output
out/
*.class
*.jar

# IDE and OS
.idea/
*.iml
.vscode/
.DS_Store
Thumbs.db
```

**`.gitattributes`** (keeps line endings consistent between Windows, macOS, and Linux members)

```text
* text=auto
*.java text eol=lf
*.md text eol=lf
```

**`README.md`**

```markdown
# Simple Bank (Java, Swing)

Desktop banking app with a lavender and mint cream GUI.
Requirements: docs/PRD.md. Build plan: docs/ROADMAP.md. Progress: docs/STATE.md.
Group: <group name>. Members: <names>.
```

**`docs/STATE.md`**

```text
PROJECT STATE (before Task J-01)
- Files: docs/PRD.md, docs/ROADMAP.md, AGENTS.md, CLAUDE.md, README.md, .gitignore, .gitattributes
- Known problems or deviations from the PRD: none
```

Commit and push to `main`:

```bash
git add .
git commit -m "chore: add PRD, roadmap, agent instructions"
git push -u origin main
```

> **Keep the two PRD copies identical.** If the PRD ever changes, update `docs/PRD.md` in BOTH repositories through pull requests on the same day, and note the change in the README. Never edit the PRD in only one repository.

#### B5. Everyone clones the repository (each member)

```bash
git clone https://github.com/<owner>/simple-bank-java.git
cd simple-bank-java
```

The first push asks you to sign in: Git for Windows and macOS open a browser window (Git Credential Manager). Alternatively install the GitHub CLI and run `gh auth login`. Never put passwords or tokens inside repository files.

#### B6. Protect the main branch (Repo Owner)

`main` must always build and run. In **Settings** → **Rules** → **Rulesets** → **New branch ruleset** (or **Branches** → **Add branch protection rule** on older layouts):

- Target: the default branch (`main`).
- Turn on **Require a pull request before merging** and set **required approvals** to **1**.
- Turn on **Block force pushes** and **Restrict deletions**.

If GitHub says this feature is unavailable for private repositories on a free plan, either make the repository public or follow the same rules by agreement (nobody pushes directly to `main`).

#### B7. Create one issue per task (Repo Owner)

Issues let the team see who owns which task. In the web UI, create 13 issues titled like `J-05: Reusable styled components`, with the body `Follow docs/ROADMAP.md, Task J-05`. Assign each to one member. A faster way, with the GitHub CLI installed (run it in Git Bash, macOS, or Linux):

```bash
gh label create task --color 5B4B8A
while IFS='|' read -r id title; do
  gh issue create --title "$id: $title" --body "Follow docs/ROADMAP.md, Task $id." --label task
done <<'EOF'
J-01|Project setup and skeleton
J-02|Logic model: Transaction, TransactionType, BankAccount
J-03|MoneyUtil (formatMoney, validateAmount) and logic tests
J-04|Theme (colors, fonts, spacing)
J-05|Reusable styled components
J-06|Main window, sidebar, view switching, Exit dialog
J-07|Check Balance view
J-08|Deposit view
J-09|Withdraw view
J-10|Transaction History view
J-11|Keyboard, accessibility, resize, polish
J-12|Full test pass and parity screenshots
J-13|Cleanup, README, demo prep
EOF
```

Optionally add a **Project** board (Projects tab → New project → Board) with columns To do, In progress, In review, Done, and add the issues to it.

#### B8. The workflow for every task (each member)

1. **Claim** the issue (assign yourself). Only one person per task.
2. **Update and branch** from `main`:

```bash
git switch main
git pull
git switch -c task/J-05-components
```

3. **Run the pipeline** from the "How to use this roadmap" section (Prompt-Generator AI → coding agent) while your branch is checked out.
4. **Check the result yourself**: build, run, test, and look at the window (the task's Done-when list).
5. **Review the changes** before committing, then commit and push:

```bash
git status
git diff
git add -A
git commit -m "J-05: add reusable styled components"
git push -u origin task/J-05-components
```

6. **Open a pull request** on GitHub (click **Compare & pull request**). Title `J-05: ...`, description: what you built and how you checked it, plus `Closes #<issue number>`. Request a reviewer.
7. **Review (a different member):** check out the branch (`git fetch` then `git switch task/J-05-components`), build, run, check the Done-when list, compare with the PRD, then **Approve**.
8. **Merge** with **Squash and merge**, then **delete the branch**. Make sure `docs/STATE.md` was updated in the pull request.
9. **Everyone:** `git switch main` and `git pull` before starting the next task.

Commit message format: `J-<number>: <short description>` (for example `J-08: add deposit view`). Branch names: `task/J-<number>-<short-name>`.

#### B9. Working with coding agents in a team

- Start the agent with your **task branch** checked out, with the terminal opened at the repository root. The agent reads `AGENTS.md`, `docs/PRD.md`, and `docs/STATE.md` from the repository (your generated prompt tells it to).
- The agent edits files only. **You** run `git add`, `git commit`, and `git push` after reviewing the diff. Never let an agent commit to `main`, rewrite history, or force-push.
- Never paste passwords, tokens, or personal data into prompts or files.
- If the agent changes `docs/PRD.md`, undo it (`git checkout -- docs/PRD.md`). The PRD changes only by team decision.
- Update the **Project State Note** by keeping it in `docs/STATE.md`: the agent updates it at the end of each task, and you paste its latest content into the Prompt-Generator before the next task.

#### B10. Avoiding merge conflicts

- The tasks in the roadmap are ordered. Do J-01 to J-06 one after another (pairs are fine), merging each before starting the next.
- After J-06 is merged, J-07 to J-10 can run in parallel on different branches. Each view is its own file; only the shared home screen file may conflict.
- Pull `main` into your branch often (`git pull origin main`), keep pull requests small, and resolve any conflict in your IDE, keeping both sides' intent. Ask the other author if unsure.

#### B11. Final submission

When J-13 is merged: confirm `main` builds and the tests pass on a fresh clone, then tag the release:

```bash
git switch main
git pull
git tag v1.0
git push origin v1.0
```

Share the repository link (and add your instructor as a collaborator if the repository is private).

---

## Task list at a glance

| Task | What gets built | PRD sections |
|---|---|---|
| J-01 | Project setup and skeleton | 5 |
| J-02 | Logic model: Transaction, TransactionType, BankAccount | 6.1, 7, 8 |
| J-03 | MoneyUtil (formatMoney, validateAmount) and logic tests | 6.5, 7 |
| J-04 | Theme (colors, fonts, spacing) | 9.2, 9.3 |
| J-05 | Reusable styled components | 9.3, 9.6 |
| J-06 | Main window, sidebar, view switching, Exit dialog | 6.2, 9.4, 9.5 |
| J-07 | Check Balance view | 6.1, 9.5 |
| J-08 | Deposit view | 6.3, 6.5, 9.5 |
| J-09 | Withdraw view | 6.4, 6.5, 9.5 |
| J-10 | Transaction History view | 6.6, 9.5 |
| J-11 | Keyboard, accessibility, resize, polish | 9.6, 9.7 |
| J-12 | Full test pass and parity screenshots | 12, 13 |
| J-13 | Cleanup, README, demo prep | 11, 14 |

---

## Tasks

### Task J-01: Project setup and skeleton

**Goal:** A folder that compiles and opens an empty window. **Depends on:** nothing. **PRD refs:** section 5.

**Prompt-generation prompt:**

```text
TASK J-01: Project setup and skeleton.
Write a coding-agent prompt that sets up the Java Swing project for our banking app.
The repository root IS the project: the repository already contains docs/PRD.md, docs/ROADMAP.md, AGENTS.md, CLAUDE.md, README.md, .gitignore, and .gitattributes, and none of them may be deleted or overwritten. Do NOT create a subfolder for the project. The agent must create src/BankApp.java, add docs/STATE.md updates (use the Project State Note format), and make sure .gitignore contains *.class, out/, .idea/, and *.iml.
BankApp.java: public class BankApp with a main method that uses SwingUtilities.invokeLater to create a JFrame titled 'Simple Bank', size 1000x680, centered on screen, default close operation EXIT_ON_CLOSE, containing one centered JLabel 'Setup OK'.
README.md: KEEP the existing content and ADD sections for how to check the JDK (java -version and javac -version must show 17 or higher), compile with javac -encoding UTF-8 -d out src/*.java, and run with java -cp out BankApp.
Acceptance criteria: compile has no errors and the run command opens a window titled Simple Bank.
Do not add packages, Maven, Gradle, or any banking logic yet.
```

**Done when:** a window titled Simple Bank opens; the work is on branch task/J-01-setup and merged into main through a pull request.

### Task J-02: Logic model (Transaction, TransactionType, BankAccount)

**Goal:** The account and history work with no GUI at all. **Depends on:** J-01. **PRD refs:** 6.1, 7, 8.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-02: Logic model.
Write a coding-agent prompt that creates these files in src/ (none may import javax.swing or java.awt):
1. TransactionType.java: enum with DEPOSIT and WITHDRAWAL, each carrying a label ('Deposit', 'Withdrawal') and a getLabel() method.
2. Transaction.java: a record Transaction(TransactionType type, long amountCentavos, long balanceAfterCentavos).
3. BankAccount.java: public class with constant STARTING_BALANCE_CENTAVOS = 500_000L; a private long balanceCentavos initialized to it; a private final ArrayList<Transaction> transactions; methods long getBalance(); void deposit(long amountCentavos) (adds to the balance, then calls recordTransaction(DEPOSIT, amount)); boolean withdraw(long amountCentavos) (returns false and changes NOTHING if the amount is greater than the balance, otherwise subtracts, calls recordTransaction(WITHDRAWAL, amount), returns true); List<Transaction> getTransactions() (returns Collections.unmodifiableList); private void recordTransaction(TransactionType type, long amountCentavos) (creates a Transaction using the CURRENT balance as balanceAfter and adds it, so it must be called AFTER the balance changes).
Callers pass only already-validated positive amounts; the class does not validate text.
Add a TEMP check in BankApp.main (before the window code) that does a deposit of 100000 and a withdraw of 200000 and prints the balance and the list. Mark it TEMP.
Acceptance criteria: compiles with no warnings; balance ends at 400000; list has 2 records with balanceAfter 600000 and 400000; withdraw(99999999) returns false and leaves everything unchanged.
Do not write any formatting, validation, or GUI code.
```

**Done when:** the TEMP output shows the expected balance and two records.

### Task J-03: MoneyUtil and logic tests

**Goal:** Money text and amount validation, tested without a window. **Depends on:** J-02. **PRD refs:** 6.5, 7, 10.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-03: MoneyUtil and logic tests.
Write a coding-agent prompt that creates in src/ (no javax.swing or java.awt imports):
1. AmountResult.java: a small final class with fields long centavos and String errorMessage, a static factory ok(long) and error(String), and boolean isValid().
2. MoneyUtil.java with: constants CURRENCY_SYMBOL = "\u20B1", MAX_AMOUNT_CENTAVOS = 100_000_000_000L, and the message constants from PRD section 10 (invalid number, must be positive, max two decimals, too large).
   a. static String formatMoney(long centavos): CURRENCY_SYMBOL + whole pesos with comma thousands separators + '.' + exactly 2 decimals. Build the comma grouping yourself with a LOOP over the digit string (insert a comma every 3 digits from the right). Do not use String.format with the comma flag or NumberFormat.
   b. static AmountResult validateAmount(String text): apply PRD section 6.5 steps 1 to 5 IN ORDER: (1) trim; (2) must match optional minus, one or more digits, optionally a dot plus one or more digits (regex via String.matches), else the invalid-number error; (3) negative or zero (including -0 and 0.00) gives the must-be-positive error; (4) more than 2 digits after the dot gives the max-two-decimals error; (5) whole-pesos part (leading zeros stripped) longer than 10 digits, or value above MAX_AMOUNT_CENTAVOS, gives the too-large error; then convert to centavos WITHOUT floating point (split on the dot, Long.parseLong on the whole part inside try/catch NumberFormatException, fraction padded on the right to 2 digits, whole * 100 + fraction). It must never throw, even for null or very long text.
3. LogicTests.java: a main method with simple checks using a helper that prints PASS or FAIL per case and exits non-zero on any FAIL. Cases: formatMoney for 0, 5, 100, 500000, 123456789, 100000000000 (expect peso sign plus 0.00, 0.05, 1.00, 5,000.00, 1,234,567.89, 1,000,000,000.00); validateAmount for '1000' (100000), '250.75' (25075), '0.5' (50), ' 500 ' (50000), '0', '0.00', '-0', '-50' (positive error), 'abc', '', '1e3', '.5', '5.', '1,000' (invalid-number error), '10.999' (decimals error), '99999999999' (too large), null (invalid-number error); and BankAccount: starting balance 500000, deposit then withdraw sequence, overdraft returns false and changes nothing.
Run with: java -ea -cp out LogicTests
Acceptance criteria: every case prints PASS.
Do not write GUI code.
```

**Done when:** `LogicTests` prints PASS for every case (this covers PRD TC-28).

### Task J-04: Theme (colors, fonts, spacing)

**Goal:** One place that holds every design value. **Depends on:** J-03. **PRD refs:** 9.2, 9.3.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-04: Theme.
Write a coding-agent prompt that creates src/Theme.java, a final utility class (private constructor) holding every design value from PRD sections 9.2 and 9.3. Copy the hex codes EXACTLY from the PRD:
- public static final Color constants: PRIMARY #5B4B8A, PRIMARY_DARK #433670, PRIMARY_DEEP #2E2552, LAVENDER #E9E4F7, LAVENDER_MIST #F3F0FA, BACKGROUND #F5FFFA, SURFACE #FFFFFF, MINT #BFE8D4, SUCCESS #2E7D5B, SUCCESS_BG #E3F5EC, ERROR #B3261E, ERROR_BG #FDECEA, WITHDRAWAL #A8324A, TEXT_PRIMARY #2B2540, TEXT_MUTED #6B6585, BORDER #D9D3EC, ON_PRIMARY #FFFFFF, NAV_TEXT #E4DEF5.
- Spacing ints: SP_4, SP_8, SP_16, SP_24, SP_32; radii RADIUS_CARD = 12, RADIUS_CONTROL = 8; heights BUTTON_HEIGHT = 44, FIELD_HEIGHT = 44, NAV_ITEM_HEIGHT = 48, TABLE_ROW_HEIGHT = 40; SIDEBAR_WIDTH = 240.
- Font factory methods using family 'Segoe UI' with fallback to Font.SANS_SERIF when Segoe UI is not installed (check with GraphicsEnvironment font family names once): fontBrand() 22 bold, fontViewTitle() 24 bold, fontSubtitle() 14 plain, fontCardTitle() 18 bold, fontBody() 14 plain, fontBodyBold() 14 bold, fontBalance() 40 bold.
- static void applyGlobalDefaults(): sets UIManager keys so default-themed pieces match the palette: Panel.background, OptionPane.background, OptionPane.messageForeground, OptionPane.messageFont, OptionPane.buttonFont, ScrollBar colors (thumb LAVENDER-ish, track BACKGROUND), ToolTip colors.
- static void enableAntialiasing(Graphics2D g): turns on RenderingHints for geometry and text.
Call Theme.applyGlobalDefaults() at the start of BankApp.main and set the JFrame content background to BACKGROUND, so the Setup OK window now shows mint cream.
Acceptance criteria: compiles with no warnings; window background is mint cream (#F5FFFA); every hex value matches the PRD.
Do not build any components yet.
```

**Done when:** the empty window is mint cream; you compared each hex value to PRD 9.2.

### Task J-05: Reusable styled components

**Goal:** Rounded cards, buttons, field, and message box that every view will reuse. **Depends on:** J-04. **PRD refs:** 9.3, 9.6 (UI-6 to UI-9).

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-05: Reusable styled components.
Write a coding-agent prompt that creates these classes in src/, all using Theme constants and Theme.enableAntialiasing in custom paintComponent code (no third-party libraries, no default Metal look visible):
1. RoundedPanel extends JPanel: constructor (Color fill, int radius, boolean drawShadowAndBorder). Non-opaque; paints a rounded rectangle; when the flag is true it also paints a soft shadow (about 10% black, blur 12, offset y 2, drawn with a few translucent rounded rects) and a 1 px BORDER outline. Used for cards (SURFACE, radius 12).
2. AppButton extends JButton: two styles via a constructor flag, PRIMARY (PRIMARY fill, white bold text, hover blends 10% white, pressed PRIMARY_DARK) and OUTLINED (SURFACE fill, 1 px PRIMARY border, PRIMARY text, hover LAVENDER). Radius 8, preferred height 44, hand cursor, setContentAreaFilled(false), setBorderPainted(false), setFocusPainted(false), but paint a visible 2 px focus ring in PRIMARY when the button has keyboard focus.
3. AmountField extends JPanel: a single-line input of height 44 that shows a fixed prefix label with the peso sign (use the CURRENCY_SYMBOL constant from MoneyUtil) on the left, a borderless JTextField on the right (hint text '0.00' painted in TEXT_MUTED when empty and unfocused), SURFACE fill, radius 8, 1 px BORDER; on focus the border becomes 2 px PRIMARY; method setErrorState(boolean) makes the border ERROR. Expose String getText(), void setText(String), void selectAllAndFocus(), void addEnterListener(ActionListener), void addChangeListener(Runnable) (fires on any edit), and setAccessibleName(String).
4. MessageBox extends JPanel: inline message area with methods showError(String), showSuccess(String), clear(). Error uses ERROR_BG fill, ERROR text, 1 px border of the same hue; success uses SUCCESS_BG, SUCCESS text. Rounded radius 8, text wraps (use a JTextArea or HTML label inside), body font. It always reserves the same height (about 48 px) even when cleared, so layouts do not jump.
5. ViewHeader extends JPanel: title (fontViewTitle, PRIMARY_DEEP) above subtitle (fontSubtitle, TEXT_MUTED), constructor (String title, String subtitle).
Create a TEMP demo in BankApp that shows one card containing a ViewHeader, an AmountField, both button styles, and both message types so the look can be checked.
Acceptance criteria: compiles with no warnings; the demo window shows rounded lavender-and-mint styled controls; focus ring, hover, and pressed states visibly work; antialiased edges.
Do not build the sidebar or views yet.
```

**Done when:** the demo window matches PRD 9.6 (buttons, field focus, messages); you saw hover and focus states.

### Task J-06: Main window, sidebar, view switching, and Exit dialog

**Goal:** The app shell with a working menu. **Depends on:** J-05. **PRD refs:** 6.2 (FR-B1 to FR-B3), 9.4, 9.5 (Exit dialog), UI-1 to UI-4, UI-10, UI-12.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-06: Main window, sidebar, view switching, Exit dialog.
Write a coding-agent prompt that creates the app shell:
1. Interface Refreshable { void refresh(); } in src/Refreshable.java.
2. NavItem.java: a custom component (extends JComponent or JButton with custom painting) for one sidebar entry: a small rounded number badge and a label, height 48, NAV_TEXT label color. Hover paints a translucent white tint. Selected state paints a PRIMARY fill, ON_PRIMARY text and a 4 px MINT bar on the left edge. Has setSelected(boolean).
3. BankFrame.java extends JFrame: owns one BankAccount. Layout: BorderLayout with a 240 px wide sidebar on the WEST (background PRIMARY_DARK) and a content area in the CENTER (background BACKGROUND, padding 32). Sidebar top: 'Simple Bank' (fontBrand, ON_PRIMARY) and caption 'Personal Banking' (NAV_TEXT, 12 px). Then NavItems in this exact order: 1 Check Balance, 2 Deposit Money, 3 Withdraw Money, 4 View Transaction History. Then flexible space, a thin divider line, and the NavItem 5 Exit at the bottom. Content area uses CardLayout with four placeholder panels (each a ViewHeader with its title and subtitle from PRD 9.5 inside the content area) named BALANCE, DEPOSIT, WITHDRAW, HISTORY.
4. Methods: void showView(String viewName) (switches the CardLayout, updates which NavItem is selected, calls refresh() on the view if it implements Refreshable), void handleCheckBalance(), handleDeposit(), handleWithdraw(), handleViewHistory() (each just calls showView with its name for now), and void confirmExit().
5. confirmExit(): JOptionPane.showOptionDialog with title 'Exit Simple Bank', message 'Thank you for banking with us. Are you sure you want to exit?', options {'Exit', 'Stay'} with 'Stay' as the default, QUESTION_MESSAGE. Exit calls System.exit(0); Stay changes nothing. NavItem 5 never shows as selected.
6. The frame's default close operation is DO_NOTHING_ON_CLOSE with a WindowListener whose windowClosing calls confirmExit() (PRD UI-12).
7. Title 'Simple Bank', default size 1000x680, minimum size 860x560, centered. BankApp.main creates BankFrame on the Event Dispatch Thread via SwingUtilities.invokeLater; the first view shown is BALANCE. Remove the J-05 TEMP demo.
Acceptance criteria: clicking items 1 to 4 switches the placeholder and moves the highlight; clicking 5 shows the dialog; Stay keeps the app open; Exit closes it; the window X button shows the same dialog; layout matches PRD section 9.4.
Do not build the real views yet.
```

**Done when:** PRD tests TC-18, TC-19, TC-26 pass and the shell matches the wireframe in PRD 9.4.

### Task J-07: Check Balance view

**Goal:** The first screen: balance card and caption. **Depends on:** J-06. **PRD refs:** 6.1 (FR-A1, FR-A2), FR-B4, 9.5 view 1, section 10.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-07: Check Balance view.
Write a coding-agent prompt that creates src/BalanceView.java (extends JPanel, implements Refreshable) and plugs it into BankFrame in place of the BALANCE placeholder:
1. Constructor takes the BankAccount.
2. Layout from PRD 9.5 view 1: ViewHeader ('Check Balance', 'Your account at a glance'); below it a balance card (RoundedPanel filled PRIMARY, radius 12, padding 32) containing the label 'Current balance' (14 px, NAV_TEXT) and the balance text (fontBalance, ON_PRIMARY) from MoneyUtil.formatMoney(account.getBalance()); below the card a muted caption line (TEXT_MUTED, 14 px).
3. Caption behavior: the FIRST time the view is shown (right after app start) it reads exactly 'Account initialized. Current balance: ' + formatted balance. Every later time it is opened through handleCheckBalance() it reads 'Current balance: ' + formatted balance. Keep a boolean field for this.
4. refresh() re-reads the balance and updates the label and caption (using the correct caption text).
5. BankFrame.handleCheckBalance() shows the view and marks the first-time flag as used. BankFrame gets a method refreshAll() that calls refresh() on every Refreshable view (used later after transactions).
Acceptance criteria: at launch the card shows the peso sign and 5,000.00 with the startup caption; opening another view and coming back shows 'Current balance: ...'; no transaction record is created.
Do not change other views.
```

**Done when:** TC-01 and TC-02 pass.

### Task J-08: Deposit view

**Goal:** Deposit form with validation and inline messages. **Depends on:** J-07. **PRD refs:** 6.3, 6.5, 9.5 view 2, UI-9.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-08: Deposit view.
Write a coding-agent prompt that creates src/DepositView.java (extends JPanel, implements Refreshable) and plugs it into BankFrame in place of the DEPOSIT placeholder:
1. Constructor takes the BankAccount and a Runnable onBalanceChanged (BankFrame passes refreshAll).
2. Layout from PRD 9.5 view 2: ViewHeader ('Deposit Money', 'Add funds to your account'); one RoundedPanel card (SURFACE, radius 12, padding 24) containing top to bottom: card title 'Deposit details' (fontCardTitle); a chip 'Available balance ' + formatted balance (LAVENDER_MIST fill, rounded, 14 px); label 'Amount'; an AmountField with accessible name 'Amount to deposit'; a MessageBox; a button row with AppButton 'Deposit' (PRIMARY style) and AppButton 'Clear' (OUTLINED style).
3. void handleDeposit(): get the text from the field; call MoneyUtil.validateAmount; if invalid call messageBox.showError(result.errorMessage), setErrorState(true), and selectAllAndFocus() and return; if valid call account.deposit(result.centavos), clear the field, setErrorState(false), showSuccess('Deposit successful. New balance: ' + formatMoney(account.getBalance())), and run onBalanceChanged.
4. Clear button: empties the field, clears the message, resets the error state, focuses the field. Any edit in the field clears the message and the error state (use addChangeListener). Leaving the view (refresh() on show) clears the message.
5. refresh() updates the Available balance chip.
6. Pressing Enter in the field triggers handleDeposit().
Acceptance criteria: deposit 1000 gives 'Deposit successful. New balance: ' + peso sign + 6,000.00 and the field clears; 0, -50, abc, blank, 1e3, .5, 5., 1,000, 10.999, 99999999999 each show the exact PRD error and change nothing; ' 500 ' works; each success adds one history record.
Do not change the validation rules or other views.
```

**Done when:** PRD tests TC-03 to TC-10, TC-17, TC-20, TC-22 pass for deposit.

### Task J-09: Withdraw view

**Goal:** Withdraw form with the insufficient-funds rule. **Depends on:** J-08. **PRD refs:** 6.4, 6.5, 9.5 view 3.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-09: Withdraw view.
Write a coding-agent prompt that creates src/WithdrawView.java as a sibling of DepositView (same structure, same styling, same behavior for Enter, Clear, edit-clears-message) and plugs it into BankFrame in place of the WITHDRAW placeholder:
1. Header 'Withdraw Money' / 'Take funds from your account'; card title 'Withdrawal details'; chip 'Available balance ...'; label 'Amount'; AmountField with accessible name 'Amount to withdraw'; MessageBox; buttons 'Withdraw' (PRIMARY) and 'Clear' (OUTLINED).
2. void handleWithdraw(): validateAmount first (same errors as deposit). If valid, call account.withdraw(centavos). If it returns false show the error 'Insufficient funds. Your balance is ' + formatMoney(balance) + '; you cannot withdraw ' + formatMoney(amount) + '.' with error state, change nothing, and keep the field text. If it returns true clear the field, showSuccess('Withdrawal successful. New balance: ' + formatMoney(balance)), and run onBalanceChanged.
3. Withdrawing exactly the whole balance must work and leave 0.00.
Avoid duplicating large blocks of code: if DepositView and WithdrawView share most of their layout, extract a shared abstract base class AmountFormView that both extend, then re-test the Deposit view.
Acceptance criteria: from 5,000.00, withdraw 2000 gives 3,000.00; withdraw 5000 gives 0.00; withdraw 5000.01 shows the insufficient-funds message with balance and history unchanged; 0, -1, abc show the same errors as deposit.
Do not change the validation rules.
```

**Done when:** PRD tests TC-11 to TC-14 pass; Deposit still passes after any refactor.

### Task J-10: Transaction History view

**Goal:** Styled table with empty state. **Depends on:** J-09. **PRD refs:** 6.6, 9.5 view 4.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-10: Transaction History view.
Write a coding-agent prompt that creates src/HistoryView.java (extends JPanel, implements Refreshable) and plugs it into BankFrame in place of the HISTORY placeholder:
1. Layout from PRD 9.5 view 4: ViewHeader ('Transaction History', 'All successful deposits and withdrawals'); one RoundedPanel card containing a caption '{n} transaction(s)' (TEXT_MUTED) and a JTable inside a JScrollPane. The card uses a CardLayout (or similar) to switch between the TABLE panel and an EMPTY panel that shows centered text 'No transactions yet.' in TEXT_MUTED.
2. Table: non-editable DefaultTableModel with columns 'No.', 'Type', 'Amount', 'Balance After'. Row height 40, no grid lines except a 1 px BORDER line under each row, no row selection highlight in the default blue (use LAVENDER for selection if selection is enabled), fixed column order (reordering disabled). Header: custom header renderer with LAVENDER background, bold PRIMARY_DEEP text, height 40. Body rows alternate SURFACE and LAVENDER_MIST via a custom cell renderer; No. and Type left-aligned, Amount and Balance After right-aligned, 12 px horizontal padding; Type text is colored SUCCESS for Deposit and WITHDRAWAL color for Withdrawal. The scroll pane has no border, its viewport is SURFACE, and the header stays visible while rows scroll.
3. refresh() / handleViewHistory(): use a for loop over account.getTransactions() to rebuild the model (row number = index + 1, type label, formatMoney(amount), formatMoney(balanceAfter)), update the caption, and show the EMPTY panel when the list is empty. BankFrame.handleViewHistory() calls it.
Acceptance criteria: with no transactions it shows 'No transactions yet.'; after a deposit of 3000 and a withdrawal of 2500.50 it shows two rows in order with correct Balance After values; failed attempts do not appear; 60 transactions scroll smoothly with the header visible.
Do not change other views.
```

**Done when:** TC-15, TC-16, TC-23 pass.

### Task J-11: Keyboard, accessibility, resize, and polish

**Goal:** The app feels finished and is usable without a mouse. **Depends on:** J-10. **PRD refs:** 9.3 to 9.7 (UI-5, UI-9 to UI-13).

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-11: Keyboard, accessibility, resize, polish.
Write a coding-agent prompt that polishes the Java Swing app without changing behavior:
1. Keyboard shortcuts: Ctrl+1 to Ctrl+5 open Check Balance, Deposit, Withdraw, History, and the Exit dialog, using the root pane's InputMap (WHEN_IN_FOCUSED_WINDOW) and ActionMap in BankFrame.
2. Focus: when a view opens, Deposit and Withdraw put focus in the amount field; Tab order follows the visual order (field, primary button, Clear); NavItems are focusable and activate with Enter or Space, showing a visible focus ring.
3. Accessibility: set accessible names and descriptions on NavItems, buttons, fields, the table, and the message box (the message box should announce its text).
4. Resize: verify nothing clips at the 860x560 minimum or at full-screen; content cards have a max width of about 720 px (left-aligned) on wide windows; the History table card fills the available height.
5. Visual audit against PRD 9.2 to 9.6: find any component that still shows a default Swing color (scrollbars, dialog buttons, tooltips, table header, selection) and theme it. Verify all text is at least 14 px except the 12 px brand caption.
6. Check on a high-DPI display or with -Dsun.java2d.uiScale=2 that nothing looks blurry or misaligned.
Acceptance criteria: PRD tests TC-20 to TC-27 pass; the app can be used entirely from the keyboard.
Do not add features or change messages.
```

**Done when:** TC-20 to TC-25 and TC-27 pass; you tried the app without touching the mouse.

### Task J-12: Full test pass and parity screenshots

**Goal:** Prove every acceptance test and capture screenshots for the Java-versus-Dart comparison. **Depends on:** J-11. **PRD refs:** sections 12 and 13.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-12: Full test pass and parity screenshots.
Write a coding-agent prompt that makes the agent:
1. Re-run java -ea -cp out LogicTests and confirm all PASS.
2. Create tests/GUI-TEST-LOG.md: a table of PRD tests TC-01 to TC-27 with columns ID, steps, expected, result (Pass/Fail), notes. Since the agent cannot click, it should write the table with Result left as 'TO CHECK' for the tests that need a person, and mark tests it could verify by code inspection or by a small headless test.
3. Add a small automated GUI smoke test class GuiSmokeTest.java that, using SwingUtilities and the Robot-free approach (calling the view methods directly), builds BankFrame, performs a deposit and a withdrawal through DepositView and WithdrawView, and asserts the balance and history size. Run with java -ea -cp out GuiSmokeTest (it needs a display; skip gracefully if java.awt.GraphicsEnvironment.isHeadless() is true).
4. Create docs/screens/ and a SCREENSHOTS.md that lists the four views to capture (balance, deposit with an error shown, withdraw with success shown, history with 5 rows) at 1000x680 for later side-by-side comparison with the Dart app (PRD TC-29).
5. Fix any defect it finds with the smallest possible change.
Acceptance criteria: logic tests and smoke test pass; GUI-TEST-LOG.md exists.
Do not add features or rewrite working code.
```

**Then you do (by hand):** run TC-01 to TC-27 in the real window, fill the Result column, and take the four screenshots into `docs/screens/`.

**Done when:** every row of GUI-TEST-LOG.md is Pass; four screenshots saved.

### Task J-13: Cleanup, README, and demo prep

**Goal:** Submission-ready code and documentation. **Depends on:** J-12. **PRD refs:** sections 11 and 14.

**Prompt-generation prompt:**

```text
[Paste Project State Note here]
TASK J-13: Cleanup, README, demo prep.
Write a coding-agent prompt that finishes the Java project:
1. Remove all TEMP code, unused variables, and unused imports. Compile with javac -Xlint:all -encoding UTF-8 and fix every warning.
2. Add a short comment above every class and method (what it does, parameters, return value) and a header comment in BankApp.java with the project name and a group-name placeholder.
3. Rewrite README.md with: what the app does, requirements, compile and run steps, an optional step to build a runnable JAR (jar --create --file simple-bank.jar --main-class BankApp -C out .), how to run the tests, the color palette table, and a table that maps each programming concept from PRD section 11 to the exact class and method where it appears.
4. Add DEMO.md with a 2-minute demo script: exact clicks and inputs, and what each one proves (startup balance, valid deposit, zero deposit error, overdraft error, history table, Exit dialog).
Acceptance criteria: zero compiler warnings; LogicTests and GuiSmokeTest still pass; README concept table is accurate.
Do not change behavior, messages, or the design.
```

**Done when:** zero warnings; the tests still pass; your group can explain every concept row in the README table.

---

## Final checklist

- All 13 tasks done and committed.
- `LogicTests` all PASS; `tests/GUI-TEST-LOG.md` shows every test Pass.
- Four screenshots saved in `docs/screens/` for the Dart comparison (TC-29).
- You can point to a loop (comma grouping, history rows), conditionals (`validateAmount`, insufficient funds), arithmetic and comparison operators, user-defined methods, the `ArrayList` of `Transaction` records, and the validation code, as listed in PRD section 11.
