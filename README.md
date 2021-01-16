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
* Adding JUnit Test Cases for a majority of the functions
* Adding regression testing with provided BASIC programs

0.0.3:
* Leaving JASIC as it is and starting the BASIC interpreter functionality
* Changed variable management to new typed variables: string, integer, real, and boolean 
  * Moving math functions `+`,`*`, `-`, `/`, `=`, `<`, and `>` into the type classes
  * Added JUnit test classes  
* Changed memory management: Converting the MemoryManage class into a package withy dedicated classes  
* Introduction of new command: `END`
* Bug fixes:
  * BASIC-28: Correcting the design and implementation of the `LabeLStatement` class

0.0.4:
* Starting to build the interpreter to support Datmouth style BASIC
  * Tokenizer re-build:
    * Moving from character based tokenization to line based tokenization
  * Introducing variable naming to typed variables: string type: `variable$`, integer type: `variable%`, double type: `variable#`, long type: `variable&`, and boolean type: `variable!`
  * Implemented commands:
    * `PRINT` - general output command
    * `REM` for any remarks. This is now not only ignored but passed to the parser.

** NOTE: as of this version, all further versions pass CheckStyle and PMD tests **
