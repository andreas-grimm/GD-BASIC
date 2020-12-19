This is the ReadMe File of the Project:

# GriCom Diminutive BASIC Interpreter (GDBI)

&copy; 2020 - Andreas Grimm, Use according to the included licence file (LICENSE.md)

---

Based on the [JASIC project of Bob Nystrom](https://github.com/munificent/jasic)

---

Project Planning and Control

This project is using Jira to manage progress, but also to log bugs and plan new features. The Jira repository for the Project is here:
[Jira Link](https://gricom.atlassian.net/jira/software/projects/BASIC)

---
Build Process:

This package has been tested to build with Maven 3.6.3, using Oracle Java 1.8.0_131 under Mac OS X 10.15.7

Use the following command line:

    mvn clean compile dependency:tree dependency:copy-dependencies package

--- 

The current version contains the following changes, bug fixes, and enhancements:

0.0.1:
* Setup and project re-factor the original project by Bob Nystrom, containing:
* "Mavenizing" the project: Using Apache Maven as the build processor, also allowing a CI/CD development process
* Adding Checkstyle and PMD static code testing

0.0.2:
* Adding tooling to the existing code:
* Command Line - Using Apache Commons CLI to process the input parameter
* Logger - lightweight logger to avoid heavy-weight log4j
* ResourceHandler - Simple Resource Handler to use .ini files as resources for programs
* Adding JUnit Test Cases for a majority of the functions
* Adding regression testing with provided BASIC programs

0.0.3:
* Changed variable management to typed variables: string type: `variable$`, integer type: `variable%`, double type: `variable#`, long type: `variable&`, and boolean type: `variable!`
* Introduction of new commands: `REM`, `TRON`, and `TROFF`

** NOTE: as of this version, all further versions pass CheckStyle and PMD tests **
