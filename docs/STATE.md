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