This document describes the features and capabilities of the BASIC version
implemented in this interpreter. The version is very similar to the original 
BASIC interpreters and additional functions are added to provide even more of
the original functionality. 

# GBasic language syntax
---------------------
 
## Language Elements

### Variables and Constants
Currently supported are positive integers and strings. Strings should be in 
"double quotes", and only positive integers can be parsed (though numbers 
are double internally).

Variables are identified by name which must start with a letter and can
contain letters or numbers. Case is significant for names and keywords.

Constants are not supported. All variables are globally scoped.

## Statements
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
    If the left-hand expression is a number, then adds the two expressions,
    otherwise concatenates the two strings.

* \<expression\> - \<expression\>

* \<expression\> * \<expression\>

* \<expression\> / \<expression\>

* \<expression\> < \<expression\>

* \<expression\> > \<expression\>

    You can figure it out.

* \<name\>:
    A name in an expression simply returns the value of the variable with
    that name. If the variable was never set, it defaults to 0.

All binary operators have the same precedence.
