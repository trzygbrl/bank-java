# Simple Bank

Desktop banking app created using Java
Requirements: docs/PRD.md. Build plan: docs/ROADMAP.md. Progress: docs/STATE.md.

## Build and Run Instructions

### Prerequisites: JDK 17 or higher

A Java Development Kit (JDK) version of 17 or higher is required to build and run this application.

Check the installed Java runtime:

```bash
java -version
```

Check the Java compiler:

```bash
javac -version
```

Both commands must report version 17 or higher. If either command is unavailable or reports an older version, the user must install or configure an appropriate JDK.

### Compile the application

From the repository root, run:

```bash
javac -encoding UTF-8 -d out src/*.java
```

This compiles the Java source files and places the resulting class files in `out/`.

### Run the application

From the repository root, run:

```bash
java -cp out BankApp
```

This command launches the Simple Bank Swing window.