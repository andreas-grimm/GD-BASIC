This document describes the features and capabilities of the BASIC version
implemented in this interpreter. The version is very similar to the original 
BASIC interpreters and additional functions are added to provide even more of
the original functionality. 

# GDBasic language syntax
---------------------

## Introduction

This text starts with a correction. The GDBasic interpreter is not an interpreter in the pure sense of the definition of an interpreter as it does not react on
entered words as they are entered and changes its state. It actually works more live a `JAVA` compiler, as it performs a lexical analysis of the program, then parses
the result, and finally generates executable code that is run by a runtime module. In this version all three components are in one project / `JAVA` executable, but as a plan
the runtime components might end up in a separate component.

GDBasic is also not a compiler as it does not generate machine code, but `JAVA` objects that are executed in sequence. At this time of the work that saves the translation into
platform dependent code and keeps the platform migrate-able - it shall run on Windows, Linux, and Mac OSX. The change to low level machine code might happen at a later stage.
It is nit planned, as the performance of a standard laptop or even Raspberry Pi is sufficient to outperform any classical BASIC computer.

The focus at this time of the work is to provide a function complete, extended `JAVA` based interpreter that can work standalone or can be embedded in `JAVA` programs, following
the example of `JRUBY` for Ruby or `JYTHON` for Python.

## Standards and supported Basic Implementations

### General Rules Concerning the Different Dialects
Every BASIC program consists of different elements:
* Commands,
* Variables, and
* Controls (which look like commands, but control the execution)

### JASIC
Based on the original implementation of the JASIC interpreter, this implementation is regression tested against the JASIC 
programs. Nevertheless, the interpreter will provide a superset of the programming language. Different to all other BASIC
dialects implemented, JASIC does not require line numbers, but uses labels to address jump destinations.

### Dartmouth Basic
This Basic interpreter is targeted to implement the definition of the BASIC programming language as defined by Thomas Kurtz 
from [Dartmouth College](https://en.wikipedia.org/wiki/Dartmouth_BASIC).

The following [Link](https://www.dartmouth.edu/basicfifty/commands.html) links to the definition of the language. At this 
moment, the interpreter supports both versions - JASIC, and the Dartmouth Basic format. 
The language support is mightier for the Dartmouth version, no additional language enhancement have been made for the Jasic version.
Jasic, as a less formatted later version of the programming language, will remain to be supported 
(why throwing working functions out?). A copy of the Dartmouth Basic programming manual is added to the documentation.

### ECMA Minimal Basic
The following link point to a [C Compiler for ECMA Minimal Basic](https://buraphakit.sourceforge.io/BASIC.shtml).
To ensure that this interpreter is compatible with the ECMA standard, the programs of the implementation are
added in the test area. A copy of the ECMA Basic standard document has been added to the documentation.

### Decimal BASIC
Decimal Basic seems to be an active Japanese BASIC implementation initiative that is working also from the 
Dartmouth and ECMA standard. The project webpage can be found here: [Decimal Basic](http://hp.vector.co.jp/authors/VA008683/english/)
This link leads to an implementation of a Basic to Pascal translator, written by Shiraishi Kazuo:
[Basic to Pascal](http://hp.vector.co.jp/authors/VA008683/english/BASICAcc.htm).

### GDBasic
GDBasic is a superset of the different basic dialects. This document describes - apart from commonalities -
differences, where GDBasic programs exceed the different dialects.

## Document Conventions

In this document, a number of different symbols and placeholders are used. The following list declares these symbols and
gives a high level overview on their expected use.

| Used Symbol  | Description |
|--------------|-------------|
| `<label>` | for `JASIC` only. Labels are defined as character strings terminated by a colon `:`|
| `<line_number>` | for `BASIC` only. Line numbers are integer values at the beginning of each command line. |
| `<statement>` | a statement is a command, comment, assignment (which could be understood as a special form of a command), or condition. |
| `<comment>` | a comment is a command with no function, entered to document the program. |
| `<assignment>` | an assignment is a command that assigns a value (string, numeric, or boolean) to a variable. |
| `<command>` | a command is an expression in the program that changes the state of the process. For details, refer to the command section of this document. |
| `<condition>` | a condition is a comparing statement that ends with a result, either `true` or `false`. For details, refer to the section on control statements in this document.|
| `<number>` | a number is an integer used for programming purposes. |
| `<expression>` | an expression is part of an assignment, reflecting the call to a function or a mathematical formula. |


## Language Elements

#### Structure of JASIC Programs

__Example of a JASIC Program__

          ' Example Code of a JASIC Program: 
          ' Looping over a print statement for 10 Times

          count = 5
    
          count = count * 2
    
          ' stop looping if we're done
    top:  
          if count = 0 then ende
          print "Hello, world!"
    
          ' decrement and restart the loop
          count = count - 1
          goto top
    ende:

#### Structure of BASIC Programs

__Example of a BASIC Program__

     10 REM Recorded of the JASIC Program in Dartmouth 
     20 REM Basic Format
     30 COUNT# = 5
     40 COUNT# = COUNT# * 2
     50 REM Stop looping if we are done
     60 IF COUNT# == 0 THEN
     70 GOTO 120
     80 END-IF
     90 PRINT "Hello, world!"
    100 REM Decrement and restart the loop
    110 COUNT# = COUNT# - 1
    120 GOTO 50
    130 END

### Variables and Constants
Basic is a strongly typed programming language, which means that type 
conversion can only be done using conversion functions. These functions 
will be implemented in one of the next versions of the interpreter.

Currently, implemented are Numbers (as double floats in Java), Strings, 
Integers, and Booleans. The code base supports positive integers and strings,
which will change in the next version when the other types will be supported,
too. Strings should be in "double quotes", and only positive integers can 
be parsed (though numbers are double internally).

Variables are identified by name which must start with a letter and can
contain letters or numbers. Case is significant for names and keywords.

Constants are not supported. All variables are globally scoped.

#### Reals or Numbers
Named Reals or Numbers, these variable types represent floating-point variables.
They provide the most coverage of mathematical functions, and the largest number
range. It is the default representation of variables in the interpreter.

*Note:* This version does only support double length floating-point variables, as 
this variable type is already implemented using the largest representation.

#### Booleans
Booleans Variables have two possible results: True or False.

##### Definition of the Boolean Algebra

| Additive | Multiplicative |
|----------|----------------|
| A + 0 = A | 0A = 0 |
| A + 1 = 1 | 1A = A |
| A + A = A | AA = A |
| A + non A = 1 | A (non)A = 0 |

### Naming Conventions and Support


#### GD Basic

| Type | Variable Name Structure | Values |
|------|-------------------------|--------|
| `REAL` | `AB#` or `AB!` | not defined |
| `INTEGER` | `AB%` or `AB&` | not defined |
| `STRING` | `AB$` (note: without the backslash, this has been added by Markdown) | not defined |
| `BOOLEAN` | `AB@` | `true` or `false` |
| undefined | `AB` | not defined |

#### Dartmouth Basic
Variable names can be a single letter, or a single letter followed by a 
single digit. This provides for 286 possible variable names.

#### JASIC
JASIC supports the same types as the Dartmouth Basic, but it allows longer variable
names and therefore more variables:

| Type | Variable Name Structure | Values |
|------|-------------------------|--------|
| `REAL` | `AB` | est. 9.9999999 E+37 |
| `STRING` | `AB` | est. 0 to 256 Characters |


#### Applesoft Basic

| Type | Variable Name Structure | Values |
|------|-------------------------|--------|
| `REAL` | `AB` | +/- 9.9999999 E+37 |
| `INTEGER` | `AB%` | +/- 32767 |
| `STRING` | `AB$` | 0 to 256 Characters |

## Reserved Words
The following keywords are reserved and cannot be used for variables. The following list defines the use of the keywords:

- `implemented`: the keyword is actively used in the interpreter
- `planned`: the keyword is used in the current development and/or released as a beta
- `reserved`: the keyword might be used in the future or is already on the roadmap. Do not use these keywords in order to be compatible in the future.

| Reserved Word |  GD-Basic | Jasic | 
|---------------|-----------|-------|
| `ABS` | reserved | |
| `AND` | reserved | |
| `ASC` | reserved | |
| `ATN` | reserved | |
| `CALL` | reserved | |
| `CDBL` | reserved | |
| `CHR$` | reserved | |
| `CINT` | reserved | |
| `CLOSE` | reserved | |
| `CLS` | reserved | |
| `CMD` | reserved | |
| `CONT` | reserved | |
| `CONTINUE-DO` | reserved | |
| `CONTINUE-WHILE` | reserved | |
| `COS` | reserved | |
| `CSNG` | reserved | |
| `DATA` | reserved | |
| `DEF FN` | reserved | |
| `DIM` | reserved | |
| `DO` | planned | |
| `ELSE` | planned | |
| `END` | implemented | |
| `END-IF` | implemented | |
| `END-WHILE` | planned | |
| `EOF` | reserved | |
| `EOL` | reserved | |
| `ERL` | reserved | |
| `ERR` | reserved | |
| `EXIT-DO` | planned | |
| `EXIT-WHILE` | planned | |
| `EXP` | reserved | |
| `FOR` | implemented | |
| `FRE` | reserved | |
| `GOSUB` | implemented | |
| `GOTO` | implemented | implemented |
| `IF` | implemented | implemented |
| `INSTR` | reserved | |
| `INT` | reserved | |
| `INPUT` | implemented | implemented |
| `LEFT$` | reserved | |
| `LEN` | reserved | |
| `LET` | reserved | |
| `LOG` | reserved | |
| `MEM` | reserved | |
| `MID$` | reserved | |
| `NEXT` | implemented | |
| `NOT` | reserved | |
| `ON` | reserved | |
| `OR` | reserved | |
| `PRINT` | implemented | implemented |
| `RANDOM` | reserved | |
| `READ` | reserved | |
| `REM` | implemented | |
| `REMINDER` | reserved | |
| `RETURN` | implemented | |
| `RIGHT$` | reserved | |
| `RND` | reserved | |
| `SGN` | reserved | |
| `SIN` | reserved | |
| `SQR` | reserved | |
| `STEP` | implemented | |
| `STOP` | planned | |
| `STRING$` | reserved | |
| `STR$` | reserved | |
| `SYSTEM` | reserved | |
| `TAB` | reserved | |
| `TAN` | reserved | |
| `THEN` | implemented | implemented |
| `TIME$` | reserved | |
| `TO` | implemented | |
| `UNTIL` | reserved | |
| `VAL` | reserved | |
| `WHILE` | planned | |
| `&` | reserved | |
| `+` | implemented | implemented |
| `-` | implemented | implemented |
| `*` | implemented | implemented |
| `/` | implemented | implemented |
| `:` | reserved | implemented |
| `>` | implemented | implemented |
| `=>` | implemented | |
| `<` | implemented | implemented |
| `=<` | implemented |  |
| `=` | implemented | implemented |
| `:=` | reserved | |
| `==` | implemented | |
| `!=` | implemented | |
| `^` | implemented | |
| `'` | implemented | |

## Statements

###### JASIC Syntax
Each statement is on its own line. Optionally, a line may have a label before the statement. A label is a name that ends with a colon:

    <label> <comment | assignment | command>

Example:

    foo:
        print "This is an example"

###### BASIC Syntax
Each statement is on its own line. It starts with an integer number, which is increasing for each following line. Duplicates 
of the line numbers is not allowed. The structure of a statement is:

    <line_number> <comment | assignment | command>

Example:

    100 PRINT "This is an example"

### Comments
Comments start with ' and proceed to the end of the line:

    PRINT "hi there" ' this is a comment

### Assignments

Assignments are made in the form "\<name\> `=` \<expression\>".

They evaluate the expression on the right and assigns the result to the given named variable. 

###### JASIC Syntax
    
    pi = (314159 / 10000)

###### BASIC Syntax

    PI# = (314159 / 10000)

*Note*: 
For the Basic language, the assignment equal (`=`) is different from the comparison equal (`==`). For the assignment 
operation only the assignment operator can be used. The comparison user is generating an error...

### Commands

##### DO Command

`DO <statement> CONTINUE-DO <statement> EXIT-DO <statement> UNTIL <condition>`

###### BASIC Syntax

    10 X# = 0
    20 DO
    30  PRINT X#
    40  X# = X# + 1
    50 UNTIL X# >= 10

###### CONTINUE-DO Command

###### EXIT-DO Command

##### FOR Command
The FOR loop is a command that counts a variable from a start value (in the inital expression) to an end value (after the `TO` part of the command), 
using increments defined by the `STEP` command.
Between the increment number after the step and the `NEXT` command, which triggers the next iteration, the developer can include one or multiple commands.
The syntax looks as follows:

`FOR <variable> = <number> TO <number> STEP <number> <statement> NEXT`

###### BASIC Syntax

Example for a FOR loop counting upwards from -2 to 2 in increments of 0.2 and printing the value of the variable:

    50 FOR X# = -2 TO 2 STEP .2
    60 PRINT X#
    70 NEXT
    80 REM Test2

Example for a FOR loop counting downwards from 2 to -2 in decrements of -0.2 and printing the value of the variable:

    140 PRINT "Downwards..."
    150 FOR Y# = 2 TO -2 STEP -.2
    160 PRINT Y#
    170 NEXT

##### INPUT Command
`INPUT <variable>`

Reads in a line of input from the user and stores it in the variable with
the given name.

    INPUT guess$

##### PRINT Command  

`PRINT <expression>`

Evaluates the expression and prints the result.

    PRINT "hello, " + "world"

The `PRINT` command terminates its output with CRLF (Carriage return / line feed) character, the next output is at the beginning
of a new line. By ending the `PRINT` command with a semicolon (`;`), the CRLF character sequence is not printed, the next output starts
after the last output printed.

Example:

    10 PRINT "These two commands ";
    20 PRINT "will print in the same line."

Output:

    These two commands will print in the same line.

The `PRINT` command also allows to print multiple fields in the same command. These fields are seperated by a comma (`,`).

Example:

    160 PRINT a#, " x 2 = ", b#;

*NOTE:* In the current version of BASIC, the character behind the comma has to be a white space (" "). A missing white space will
lead to an error message.

##### WHILE Command

`WHILE <condition> <statement> CONTINUE-WHILE <statement> EXIT-WHILE <statement> END-WHILE`

###### BASIC Syntax

    WHILE X# > 0 
      PRINT X#
      X# = X# -1
    END-WHILE

###### CONTINUE-WHILE Command
The `CONTINUE-WHILE` command stops the execution of the loop block and jumps back to the `WHILE` statement:

    WHILE X# > 0 
      PRINT X#
      X# = X# -1
      CONTINUE-WHILE
      PRINT "This line will not be printed."
    END-WHILE


###### EXIT-WHILE Command
The `EXIT-WHILE` command terminates the `WHILE` loop immediatedly and continues with the first command after the `END-WHILE`
statement.

### Process Control

#### Unconditional Process Control (Jump)

##### GOSUB Command

`GOSUB <line_number> <statements> RETURN`

Jumps to the statement at the given line number. Processes the program from that location on until the `RETURN` command is found.
Returns to the next statement past the command.

      100 GOSUB 200
      110 PRINT "Second line output"
      120 END
      ...
      200 PRINT "First line output"
      210 RETURN

###### RETURN Command

*NOTE:* The `RETURN` command assumes that a prior `GOSUB` command has been executed. If the `RETURN` is found without a prior `GOSUB` the
location for further processing is not predicable, if possible an error is thrown.


##### GOTO Command

The `GOTO` command represents the unconditional jump to another location in the program without return after completion 
(different to the `GOSUB` command). The command will not change any variable content or any status of the program, just 
will move the program pointer to the new location.

###### JASIC Syntax

`GOTO <label>`

Jumps to the statement after the label in the program. Processes the program from that location on.

    loop:
          PRINT "Hello"
          GOTO loop

###### BASIC Syntax

`GOTO <line_number>`

Jumps to the statement at the given line number. Processes the program from that location on.

      10 PRINT "Hello"
      20 GOTO 10

##### Conditional Process Control

###### JASIC Syntax

`IF <condition> THEN <label>`

Evaluates the expression. If it evaluates to a non-zero number, then jumps to the statement after the given label.

    IF a < b THEN jump_location

###### BASIC Syntax

`IF <condition> THEN <statement> END-IF`

Evaluates the expression. If it evaluates to a true value, then the commands in the block between the `THEN` and the `END-IF`
is executed. If it evaluates to a false value, the flow jumps to the statement past the `END-IF` statement.

    10 IF a < b THEN 
    20   PRINT "Inside the IF block"
    30 END-IF 
    40 PRINT "Outside the IF block"

## Expressions
The following expressions are supported:

* \<expression\> `=` \<expression\>: (for JASIC, but works for BASIC)
  Evaluates to 1 if the two expressions are equal, 0 otherwise.

* \<expression\> `==` \<expression\>: (BASIC only)
  Evaluates to `true` if the two expressions are equal, `false` otherwise.
* \<expression\> `!=` \<expression\>: (BASIC only)
  Evaluates to `false` if the two expressions are equal, `true` otherwise.

* \<expression\> `+` \<expression\>:
  As of this version, the addition of two variables is supported for all
  data types. If the left-hand expression is a number, integer, or boolean 
  then adds the two expressions; otherwise concatenates the two strings.
  In this case the second value can be of any type, the variable will be
  converted to a string.

* \<expression\> `-` \<expression\>:
  The subtraction is supported for numbers and integers.

* \<expression\> `*` \<expression\>:
  Multiplication is supported for numbers, integers, and booleans.

* \<expression\> `/` \<expression\>:
  Division is supported for numbers and integers

* \<expression\> `<` \<expression\>

* \<expression\> `<=` \<expression\>

* \<expression\> `>` \<expression\>

* \<expression\> `>` \<expression\>

* \<name\>:
    A name in an expression simply returns the value of the variable with
    that name. If the variable was never set, it defaults to 0.

All binary (atomic) operators have the same precedence.


## Alternative Projects and Information

* [Wikipedia Article on Basic](https://en.wikipedia.org/wiki/BASIC)
* [TinyBasic](https://en.wikipedia.org/wiki/Tiny_BASIC) with the related implementation for [Java](http://www.thisiscool.com/tinybasic.htm)
