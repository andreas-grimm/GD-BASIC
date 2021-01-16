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
Based on the original implementation of the JASIC interpreter, this implementation
is regression tested against the JASIC programs. Nevertheless, the interpreter
will provide a superset of the programming language. Different to all other BASIC
diaplects implemented, JASIC does not require line numbers, but uses labels
to address jump destinations.

### Dartmouth Basic
This Basic interpreter is targeted to implement the defintion of the BASIC
programming language as defined by Thomas Kurtz from [Dartmouth Colleage](https://en.wikipedia.org/wiki/Dartmouth_BASIC).
The following [Link](https://www.dartmouth.edu/basicfifty/commands.html) links to the definition of the
language. At this moment, the interpreter supports a less formatted later version of the
programming language, which will remain to be supported (why throwing working functions out?).
A copy of the Dartmouth Basic programming manual is added to the documentation.

### ECMA Minimal Basic
The following link point to a [C Compiler for ECMA Minimal Basic](https://buraphakit.sourceforge.io/BASIC.shtml).
To ensure that this interpreter is compatible with the ECMA standard, the programs of the implementation are
added in the test area. A copy of the ECMA Basic standard document has been added to the documentation.

### Decimal BASIC
Decimal Basic seems to be an active Japanese BASIC implementation initative that is working also from the 
Dartmouth and ECMA standard. The project webpage can be found here: [Decimal Basic](http://hp.vector.co.jp/authors/VA008683/english/)
This link leads to an implementation of a Basic to Pascal translator, written by Shiraishi Kazuo:
[Basic to Pascal](http://hp.vector.co.jp/authors/VA008683/english/BASICAcc.htm).

### Applesoft Basic
__not supported at this time__

### Tandy TRS-80 Basic
__not supported at this time__

### GDBasic
GDBasic is a superset of the different basic dialects. This document describes - apart from communalities -
differences, where GDBasic programs exceed the different dialcts.

## Language Elements

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
*Note:* This version does not support double length floating-point or
long (double length) variables, as this variable type is already implemented using
the largest representation.

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

#### Dartmouth Basic
Variable names can be a single letter, or a single letter followed by a 
single digit. This provides for 286 possible variable names.

#### JASIC
JASIC supports the same types as the Dartmouth Basic, but it allows longer variable
names and therefore more variables:

| Type | Variable Name Structure | Values |
|------|-------------------------|--------|
| REAL | AB | est. 9.9999999 E+37 |
| STRING | AB | est. 0 to 256 Characters |


#### Applesoft Basic

| Type | Variable Name Structure | Values |
|------|-------------------------|--------|
| REAL | AB | +/- 9.9999999 E+37 |
| INTEGER | AB% | +/- 32767 |
| STRING | AB$ | 0 to 256 Characters |


#### GD Basic

| Type | Variable Name Structure | Values |
|------|-------------------------|--------|
| REAL | AB | not defined |
| INTEGER | AB% | not defined |
| STRING | AB$ | not defined |
| BOOLEAN | AB# | __true__ or __false__ |

## Reserved Words
The following key words are reserved and cannot be used for variables:

| reserved word | JASIC | Dartmouth | Applesoft | TRS-80 | GDBasic |
|---------------|-------|-----------|-----------|--------|---------|
| PRINT | x | x | x | x | x | 

##Statements
Each statement is on its own line. Optionally, a line may have a label before
the statement. A label is a name that ends with a colon:

    foo:

### Comments
Comments start with ' and proceed to the end of the line:

    print "hi there" ' this is a comment

### Assignments

Assignments are made in the form "\<name\> = \<expression\>".

They evaluate the expression on the right and assigns the 
result to the given named variable. 
    
    pi = (314159 / 10000)

### Commands

#### Input / Output

##### Output

###### Print Command    
print \<expression\>

Evaluates the expression and prints the result.

    print "hello, " + "world"

##### Input

###### Input Command
input \<name\>

Reads in a line of input from the user and stores it in the variable with 
the given name.
    
    input guess

#### Process Control

##### Unconditional Process Control (Jump)

###### Goto Command
goto \<label\>

Jumps to the statement after the label with the given name.

    goto loop

##### Conditional Process Control

if \<expression\> then \<label\>

Evaluates the expression. If it evaluates to a non-zero number, then
jumps to the statement after the given label.

    if a < b then dosomething

## Expressions
The following expressions are supported:

* \<expression\> = \<expression\>:
    Evaluates to 1 if the two expressions are equal, 0 otherwise.

* \<expression\> + \<expression\>:
  As of this version, the addition of two variables is supported for all
  data types. If the left-hand expression is a number, integer, or boolean 
  then adds the two expressions; otherwise concatenates the two strings.
  In this case the second value can be of any type, the vairable will be
  converted to a string.

* \<expression\> - \<expression\>:
  The substraction is supported for numbers and integers.

* \<expression\> * \<expression\>:
  Multiplication is supported for numbers, integers, and booleans.

* \<expression\> / \<expression\>:
  Division is supported for numbers and intwgers

* \<expression\> < \<expression\>

* \<expression\> > \<expression\>

    You can figure it out.

* \<name\>:
    A name in an expression simply returns the value of the variable with
    that name. If the variable was never set, it defaults to 0.

All binary operators have the same precedence.


## Alternative Projects and Informtion

* [Wikipedia Article on Basic](https://en.wikipedia.org/wiki/BASIC)
* [TinyBasic](https://en.wikipedia.org/wiki/Tiny_BASIC) with the related implementation for [Java](http://www.thisiscool.com/tinybasic.htm)