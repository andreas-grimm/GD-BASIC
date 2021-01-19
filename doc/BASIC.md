This document describes the features and capabilities of the BASIC version
implemented in this interpreter. The version is very similar to the original 
BASIC interpreters and additional functions are added to provide even more of
the original functionality. 

# GDBasic language syntax
---------------------

## Standards and supported Basic Implementations

### General Rules Concerning the Different Dialects
Every BASIC program consists of different elements:
* Commands,
* Variables, and
* Controls (which look like commands, but control the execution)

### JASIC
Based on the original implementation of the JASIC interpreter, this implementation is regression tested against the JASIC 
programs. Nevertheless, the interpreter will provide a superset of the programming language. Different to all other BASIC
diaplects implemented, JASIC does not require line numbers, but uses labels to address jump destinations.

### Dartmouth Basic
This Basic interpreter is targeted to implement the defintion of the BASIC programming language as defined by Thomas Kurtz 
from [Dartmouth Colleage](https://en.wikipedia.org/wiki/Dartmouth_BASIC).

The following [Link](https://www.dartmouth.edu/basicfifty/commands.html) links to the definition of the language. At this 
moment, the interpreter supports both versions - Jasic, and the Dartmouth Basic format. 
The language support is mightier for the Dartmouth version, no additional language enhancement have been made for the Jasic version.
Jasic, as a less formatted later version of the programming language, will remain to be supported 
(why throwing working functions out?). A copy of the Dartmouth Basic programming manual is added to the documentation.

### ECMA Minimal Basic
The following link point to a [C Compiler for ECMA Minimal Basic](https://buraphakit.sourceforge.io/BASIC.shtml).
To ensure that this interpreter is compatible with the ECMA standard, the programs of the implementation are
added in the test area. A copy of the ECMA Basic standard document has been added to the documentation.

### Decimal BASIC
Decimal Basic seems to be an active Japanese BASIC implementation initative that is working also from the 
Dartmouth and ECMA standard. The project webpage can be found here: [Decimal Basic](http://hp.vector.co.jp/authors/VA008683/english/)
This link leads to an implementation of a Basic to Pascal translator, written by Shiraishi Kazuo:
[Basic to Pascal](http://hp.vector.co.jp/authors/VA008683/english/BASICAcc.htm).

### GDBasic
GDBasic is a superset of the different basic dialects. This document describes - apart from communalities -
differences, where GDBasic programs exceed the different dialcts.

## Language Elements

### Structure of Basic Programs


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

#### Structure of BASIC Porgrams

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
The following keywords are reserved and cannot be used for variables:

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
| `COS` | reserved | |
| `CSNG` | reserved | |
| `DATA` | reserved | |
| `DEF FN` | reserved | |
| `DIM` | reserved | |
| `ELSE` | reserved | |
| `END` | implemented | |
| `END-IF` | implemented | |
| `EOF` | reserved | |
| `EOL` | reserved | |
| `ERL` | reserved | |
| `ERR` | reserved | |
| `EXP` | reserved | |
| `FOR` | reserved | |
| `FRE` | reserved | |
| `GOSUB` | reserved | |
| `GOTO` | implemented | implemented |
| `IF` | reserved | implemented |
| `INSTR` | reserved | |
| `INT` | reserved | |
| `INPUT` | reserved | implemented |
| `LEFT$` | reserved | |
| `LEN` | reserved | |
| `LET` | reserved | |
| `LINE` | implemented | implemented |
| `LOG` | reserved | |
| `MEM` | reserved | |
| `MID$` | reserved | |
| `NEXT` | reserved | |
| `NOT` | reserved | |
| `ON` | reserved | |
| `OR` | reserved | |
| `PRINT` | implemented | implemented |
| `RANDOM` | reserved | |
| `READ` | reserved | |
| `REM` | implemented | |
| `REMINDER` | reserved | |
| `RETURN` | reserved | |
| `RIGHT$` | reserved | |
| `RND` | reserved | |
| `SGN` | reserved | |
| `SIN` | reserved | |
| `SQR` | reserved | |
| `STEP` | reserved | |
| `STOP` | reserved | |
| `STRING$` | reserved | |
| `STR$` | reserved | |
| `SYSTEM` | reserved | |
| `TAB` | reserved | |
| `TAN` | reserved | |
| `THEN` | reserved | implemented |
| `TIME$` | reserved | |
| `TO` | reserved | |
| `VAL` | reserved | |
| `&` | reserved | |
| `+` | implemented | implemented |
| `-` | implemented | implemented |
| `*` | implemented | implemented |
| `/` | implemented | implemented |
| `:` | reserved | implemented |
| `>` | reserved | implemented |
| `=>` | reserved | |
| `<` | reserved | implemented |
| `=<` | reserved |  |
| `=` | implemented | implemented |
| `:=` | reserved | |
| `==` | implemented | |
| `!=` | reserved | |
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

    line_number <comment | assignment | command>

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

##### Input

###### Input Command
input \<name\>

Reads in a line of input from the user and stores it in the variable with
the given name.

    input guess


#### Input / Output

##### Output

###### Print Command    
print \<expression\>

Evaluates the expression and prints the result.

    print "hello, " + "world"

#### Process Control

##### Unconditional Process Control (Jump)

###### Goto Command
`GOTO` \<label\>

Jumps to the statement after the label with the given name.

    GOTO loop

##### Conditional Process Control

###### JASIC Syntax

`IF <expression> THEN <label>`

Evaluates the expression. If it evaluates to a non-zero number, then jumps to the statement after the given label.

    IF a < b THEN jump_location

###### BASIC Syntax

`IF <expression> THEN <commands> END-IF`

Evaluates the expression. If it evaluates to a true value, then the commands in the block between the `THEN` and the `END-IF`
is executed. If it evaluates to a false value, the flow jumps to the statement past the `END-IF` statement.

    10 IF a < b THEN 
    20   some commands
    30 END-IF 

## Expressions
The following expressions are supported:

* \<expression\> `=` \<expression\>: (JASIC only)
  Evaluates to 1 if the two expressions are equal, 0 otherwise.

* \<expression\> `==` \<expression\>: (BASIC only)
  Evaluates to `true` if the two expressions are equal, `false` otherwise.
* \<expression\> `!=` \<expression\>: (BASIC only)
  Evaluates to `false` if the two expressions are equal, `true` otherwise.

* \<expression\> `+` \<expression\>:
  As of this version, the addition of two variables is supported for all
  data types. If the left-hand expression is a number, integer, or boolean 
  then adds the two expressions; otherwise concatenates the two strings.
  In this case the second value can be of any type, the vairable will be
  converted to a string.

* \<expression\> `-` \<expression\>:
  The substraction is supported for numbers and integers.

* \<expression\> `*` \<expression\>:
  Multiplication is supported for numbers, integers, and booleans.

* \<expression\> `/` \<expression\>:
  Division is supported for numbers and intwgers

* \<expression\> `<` \<expression\>

* \<expression\> `>` \<expression\>

    You can figure it out.

* \<name\>:
    A name in an expression simply returns the value of the variable with
    that name. If the variable was never set, it defaults to 0.

All binary operators have the same precedence.


## Alternative Projects and Informtion

* [Wikipedia Article on Basic](https://en.wikipedia.org/wiki/BASIC)
* [TinyBasic](https://en.wikipedia.org/wiki/Tiny_BASIC) with the related implementation for [Java](http://www.thisiscool.com/tinybasic.htm)
