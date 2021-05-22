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

    mvn -Drevision=0.0.5-SNAPSHOT -Dmaven.javadoc.skip=true clean site package

or execute the build shell script at `bin/build`

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

Implemented Backlog Items: 

    [BASIC-16], [BASIC-7], [BASIC-11], [BASIC-12], [BASIC-14], [BASIC-17]

0.0.3:
* Leaving JASIC as it is and starting the BASIC interpreter functionality
* Changed variable management to new typed variables: string, integer, real, and boolean 
  * Moving math functions `+`,`*`, `-`, `/`, `=`, `<`, and `>` into the type classes
  * Added JUnit test classes  
* Changed memory management: Converting the MemoryManage class into a package with dedicated classes  
* Introduction of new command: `END`
* Bug fixes:
  * `[BASIC-28]`: Correcting the design and implementation of the `LabeLStatement` class

Implemented Backlog Items: 

    [BASIC-4], [BASIC-23], [BASIC-8], [BASIC-38], [BASIC-25], [BASIC-6], [BASIC-39], [BASIC-9], [BASIC-28]

0.0.4:
* Starting to build the interpreter to support Dartmouth style BASIC
  * Tokenizer and Parser re-build:
    * Moving from character based tokenization to line based tokenization
    * General code cleansing
    * Added JUnit test cases for new classes
  * Implemented commands:
    * All JASIC commands are now implemented in the BASIC branch
    * Added functionality to the `IF` command: using block structure to allow more commands after an `IF` command (BASIC branch only)  
    * `PRINT` - general output command
    * `REM` for any remarks. This is now not only ignored but passed to the parser.
* Introducing variable naming to typed variables: string type: `variable$`, integer type: `variable%`, real type: `variable#`, and boolean type: `variable!`
* Added additional math operators: `^` (power of), `!=`, `<=`, and `>=`; replaced `=` in comparisons by `==`
* Added JUNIT tests for all new functions

Implemented Backlog Items: 

    [BASIC-15], [BASIC-10], [BASIC-47], [BASIC-40], [BASIC-56], [BASIC-21], [BASIC-20], [BASIC-22], [BASIC-32], [BASIC-33],
    [BASIC-34], [BASIC-35], [BASIC-18], [BASIC-29], [BASIC-30], [BASIC-31], [BASIC-44], [BASIC-45], [BASIC-46], [BASIC-59], 
    [BASIC-60], [BASIC-61], [BASIC-63]

0.0.5: Tagged as Loop Release
* Added the `FOR-NEXT`-loop
* Added the `DO-UNTIL`-loop  
* Added `GOSUB`-statement
* `PRINT` with a trailing semicolon `;` surpresses LF at the end of the output. Also using a comma `,` in the argument list to
  print multiple outputs in a single command.
* Added variable naming to typed variables: long type: `variable&`

Implemented Backlog Items: 

    [BASIC-24], [BASIC-26], [BASIC-43], [BASIC-48], [BASIC-58], [BASIC-62], [BASIC-66], [BASIC-65], [BASIC-74],
    [BASIC-80], [BASIC-67], [BASIC-68], [BASIC-69], [BASIC-70], [BASIC-71], [BASIC-72], [BASIC-81], [BASIC-84]

0.0.6: Tagged as Array and Function Release
* Removed the `-i` mandatory parameter in the command line
* Removed the JASIC functionality  
* Added the `@PRAGMA` feature. At this moment it allows to change the setting of the log level, but it can also be 
  used to change any other execute parameter of the interpreter.
* Added the colon `:` programming feature to the interpreter, which allows multiple commands in a single line.  
* Added array function to all data types. The array function allows the use of n-dimensional arrays. The index of the 
  array can be one or more variables itself. The documentation is adjusted to it.
* Adding functionality to retrieve single characters out of any string. A string's characters can be retrieved by 
  using squared brackets:
  `[` and `]`.
* Added mathematical operators: `>>`, `<<`, `%`, `AND`, and `OR`  
* Added mathematical functions: `ABS`, `ATN`, `CDBL`, `CINT`, `COS`, `EXP`, `LOG`, `LOG10`, `NOT`, `RND`, `SIN`, `SQR`, 
  `TAN`
* Added string functions: `ASC`, `CHR`, `INSTR`, `LEFT`, `LEN`, `MID`, `RIGHT`, `STR`, `VAL`
* Added system functions: `MEM`, `SYSTEM`, `TIME`
* Added `gradle` build script
* Added `READ` and `DATA` statements
* Fixed issues:
1. `FOR` statement only accepted fixed numbers for start-, end-, and step- value. This has changed that now 
   variables, arrays, and functions can be used for those parameters.
2. `IF` statement is now able to use a direct jump target after the `THEN` keyword. This is a reaction to problems 
   using a `GOTO` out of the block, leaving an unresolved stack entry back. To allow a developer to jump out of any 
   block (`DO`,`FOR`, `IF`, and `WHILE`) a new statement `CLEAN` is planned that removes the un-used stack entry. 
   The BASIC programming guide will be added to discuss best practices concerning the `GOTO` command: with the 
   existing commands, the use of the `GOTO` command should be avoided.
3. `IF` statement is now supporting the `ELSE` extension, generating a command block for the alternate flow processing.

Implemented Backlog Items:

    [BASIC-19][BASIC-37][BASIC-53][BASIC-64][BASIC-73][BASIC-89][BASIC-102][BASIC-104][BASIC-105][BASIC-108][BASIC-109]
    [BASIC-110][BASIC-117][BASIC-120][BASIC-127]

0.0.7: Tagged as Macro Release
* Added the framework to handle macro constructs

Implemented Backlog Items:

    [BASIC-121]

Under Development:

    [BASIC-48][BASIC-76][BASIC-79][BASIC-82][BASIC-83][BASIC-88][BASIC-115][BASIC-124]

---
Implemented test and demonstration programs, located at `src/test/resources/GD_Basic_Examples`:
- `Fibonacci.bas`: translation of the ECMA demonstration `FIBONACCI.BAS` program
- `Eratosthenese.bas`: translation of the ECMA demonstration `ERATOSTHENES.BAS` program

** NOTE: as of this version, all further versions pass the CheckStyle test **
