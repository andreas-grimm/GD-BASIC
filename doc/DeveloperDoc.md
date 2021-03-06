This document contains the documentation of the different concepts used for the implementation of the 
interprester. It duplicates in some areas the language documentation, 
which cannot be avoided.

# Developer Documentation
This documentation has been written as a notebook or scrapbook of the
developer(s) of the interpreter - mainly to document the ideas and concepts
used in the implementation. It is a living document, under version control,
and should be extended as the code growths and needs some additional documentation.

### Markdown
All documentation is written using the MarkDown format. The documentation of the
format can be found here: [www.markdownguide.org](https://www.markdownguide.org/basic-syntax/),
 with the extended syntax documented here [Extended Syntax](https://www.markdownguide.org/extended-syntax/),
or here: [github.com](https://guides.github.com/features/mastering-markdown/)

### Some Important Concepts
The GD-Basic Interpreter is not an industrial grade interpeter. The target of this project is to provide a BASIC interpreter
that allows to run as much as possible the different programs build in the 1970's and 1980's - and at the same time provide
a bit more functionality. The interpreter is designed to be embedded into JAVA programs. This brings some very basic requirements
for the development:
- In order to be portable the interpreter has a very limited number of third party packages. Actually the only one used at this time
is Apache CLI package.
- Some functions are build in such a way that heavy weight external packages are avoided (e.g. the `logging`-framework). Rather than
using the `log4j` package or a similar package, this interpreter uses a very simple own logging function.
- This interpreter is currently built on Java 8. Development and test are done using version `JDK 1.8.0_131-b11`. The Q2 release
will upgrade to Java 14 - and use some of the new features of the higher release (see logging framework).
- The objective of the project is to provide code of high quality and standard. All code has an automated JUnit test attached,
additionally the code is verified using Checkstyle and PMD static code analysis. Release code will have as limited as possible
reported issues.  

## Installing and Using the Interpreter
The interpreter is delivered source code only. To use it, the package is using Apache Maven to compile.

Use the `git clone` command to retrieve the latest version of the interpreter. The package will download into any location, there is 
no need for a special filesystem set-up. The package has been tested using Mac OS X and Linux, but the installation should support
Windows as well.

For the compilation, the project is using *Apache Maven* as the build manager tool. The implementation is tested with Maven 3.6.3, but
any newer version of Maven should work as well. The command line to run Maven is:

    mvn clean compile dependency:tree dependency:copy-dependencies package

During the build process, the interpreter is compiled, but the process also runs a number of JUNIT test cases, and performs
a static code analysis using Checkstyle. The definition ofgi the syntax for Checkstyle is in the `./etc` directory, Checkstyle
is working with the code style guide of the project. The code style guide is located in the projects root directory, named `STYLEGUIDE.md`.
If you want to participate, please adopt your contributions to the look and feel of the project. Code submissions not reflecting
the styleguide will be rejected.

A wrapper around this command can be found in the `./bin`-directory: `./bin/build`. A number of test
scripts are added: `bin/test` is running the _JASIC_ test programs, `bin/test2` runs the same program logic as a _BASIC_
program. `bin/dev` is used to run the development _BASIC_ program that is used to implement and unit test the _BASIC_
version of the interpreter.

At this stage, the interpreter is using a command line interface (`CLI`). The syntax of the `CLI` is (setting the link for the `JAVA` libraries up before):

    java -jar target/BASIC-<version-SNAPSHOT>.jar <parameter> <program name>.<.bas|.jas>

or (if the `JAVA` libraries are nto in the path):

    java -jar target/BASIC-<version-SNAPSHOT>-jar-with-dependencies.jar  <parameter> <program name>.<.bas|.jas>

Possible optional parameter supported are:
* "`-h`" - help (This screen), no further arguments
* "`-q`" - quiet mode, disables all interpeter messages and only displays the output of the program
* "`-v`" - verbose level: parameter defines the debug level, e.g. `info`, `debug`. See the following example.
* "`-b`" - language type: `J` = Jasic, `B` = Basic, default is set to `B`

The only mandatory parameter is:
* "`-i`" - mandatory name of the input file, with the name of the input file as an argument

_Example_:

    java -jar target/BASIC-0.0.5-SNAPSHOT-jar-with-dependencies.jar -v"debug|info" -i src/test/resources/testfile_basic.bas

## Participation in the Development of the Interpreter
As of version `0.1.0` (planned) the interpreter is avaiable in a public _GITHUB_ (`github.com`) repository. It is covered by
a modified version of the __CDDL 1.0 License__, see `LICENSE.md` in the project root directory.

The project is following open source project standard processes. Anybody with an interest to participate can and may clone or
fork a copy of the project. Deliveries back to the author can be done using pull requests. Refer to the `git` documentation
to gain information on the process.

A `GIST` space might be opened at a later stage, right now the project is using a _Jira_ space to plan additional functionality
and release dates. The _Jira_ project can be found here:

[Jira Link](https://gricom.atlassian.net/jira/software/projects/BASIC)

## Document Standard...

When we show the structure of the BASIC code, we follow there document standards:
- [mandatory information]: This field is required for the execution of the command
- <[optional information]>: This field is optional and not needed for the execution
- ... : Repetition of the content

## General Structure of the Interpreter

![Interpreter Structure](https://github.com/andreas-grimm/Interpreters/blob/development/doc/jpg/ProcessFlow.jpg)

### Tokenizer
The Tokenizer or Lexer of the interpreter translates the read source code into a list of recognized tokens.
All files related to the tokenization process are in the `eu.gricom.interpreter.basic.tokenizer` package.
This implementation consists one Java tokenizer interface class (`Tokenizer.java`), so that the interface 
structures are similar, even as the outcoming list of token is slightly different.

The tokenizers are named after the Basic versions they are implementing:
* __Basic__ - contains the tokenization of programs following the dialects based on Dartmouth BASIC formats (such like ECMA, 
  Tandy TRS Basic Level II, Applesoft Basic, or Commodore Basic).
* __Jasic__ - contains the tokenization following the original Jasic programming format, e.g. without line numbers and using 
  labels as jumping destinations.

The result of the tokenization process is a list of objects of type Token (see `eu.gricom.interpreter.basic.tokenizier.Token`). 
Each token object holds three attributes:
* __Type__: the token type, as defined in `eu.gricom.interpreter.basic.tokenizer.TokenType.java`.
* __Text__: the program code identified by the token
* __Line__: the line number of the program code in the original program. This is used i.e. for interpreter error messages.

### Processing a Program in the Tokenizer

#### BASIC Programs
The tokenization of a `BASIC` program is performed in the class `BasicLexer.java` in the `tokenizer` package. The tokenizer
retrieves the program as an array of program lines in string format. The program is then processing the program line by line.

The main function of the tokenizer class is named `tokenize`. It follows this process:

1. Separation of the program in single lines.
2. Splitting the lines into line numbers and line contents.
3. For each line:
- remove empty line and handle lines that only contain the line number
- retrieve the line number. If the line number is smaller or equal to the previous line number, the lexer will throw an exception.
- normalize the program line:
a) Remove whitespaces and tabs from any location outside quotation marks (`"`). 
b) Separate parenthesis (`(` and `)`) from the keywords. This is only done for keywords, not for arrays.
- find keywords and generate a list of tokens.    

#### JASIC Programs

### Adding new token to the Tokenizer

- Step 1: Add the new reserved word into the list of reserved words (`eu.gricom.interpreter.basic.tokenizer.ReservedWords.java`)
  and the token file (`eu.gricom.interpeter.basic.tokenizer.TokenType.java`).
- Step 2: Verify that the BASIC tokenizer (`eu.gricom.interpreter.basic.BasicLexer.java`) can process the added new tokens and reserved word.

### Parser
The parser translates the program, representated as a list of token, to a list of statement objects. These statement objects
contain all logic of the different commands, so the runtime part of the interpreter does barely contain any logic.

#### Adding New Basic Commands
New commands, unlike new structures, do not require a change in the Tokenizer. The main part to add new commands
is in the [BasicParser](https://github.com/andreas-grimm/Interpreters/blob/feature_tokenizer/doc/jpg/DeveloperDoc.md#BasicParser-Class)
class. In this class, the method __parse__ contains the logic to convert the identified token into the sequence of commands
that will be executed in the interpreter.

Before Adding new Basic Commands to the Parser, it is important to prepare and verify that
1. Both, Keyword and Token Type are generated. The TokenType is in the `TokenType`-file in the `tokenizer` package. The mapping between the keyword and the token type is defined
   in the `ReservedWords` file in the same package. Note: The sequence of token and keyword defines the relationship. Ensure that the sequence of the two lists in the `ReservedWords`
   file is always maintained.
2. Generate the statement (alternative expression where appropriate) in the `statements` package. Every statement is an implementation of the `Statement` interface class and inherits
   a number of methods that are required for the execution:
   * `int getLineNumber()`
   * `void execute() throws Exception`, and
   * `String content() throws Exception` - this function is mainly need if the program is run in `debug` mode.
3. Generate the JUnit test cases for the statement classes.

When adding the command into the parser, add the token in the `case` statement of the `parse()` method in the `BasicParser` class in the `parser` package.
Every `case` block starts with:

    case [Token]:
      _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [Token] ");
      iOrgPosition = _iPosition++;
      oLineNumber.putLineNumber(getToken(0).getLine(), iOrgPosition);

This code is required to map the Basic source code line number with the number of the token and ultimately with the number of the statement in the program execution flow.

4. Generate the parser code to retrieve the parameter for the statement class constructor
5. Add the instantiation of the statement object into the statement list (aka executable program):

   `aoStatements.add(new GotoStatement(iOrgPosition, strLineNumber));`

Some rules about the implementations of the Statement classes:
* The statement class does not implement any persistent information, e.g. uses any static variables inside the class. If the statement needs to keep information, the information
should be managed by the `VariableManagement` class or the `Stack` class in the `memoryManager` package.

### Runtime

Internally the parsed program is stored in a number of data structures:
- The `parse` method of the Jasic and the Basic parser returns a list of objects (instantiated statement classes) in a processing sequence. This list is defined as `List<Statement> aoStatements`.
- For Jasic programs: The execute function of the program utilizes the `LabelStatement` class to have a reference for jumps and conditions.
- For Basic programs: The execute function of the program utilizes the `LineNumberXRef` class to have a reference between the basic line numbers, token number, and statement number.

## Basic Concepts

### Memory Management

### Program Control

#### Relationship between Program Lines, Token, and Object Statements


![Line Number Relationship](https://github.com/andreas-grimm/Interpreters/blob/development/doc/jpg/LineNumberRelationship.jpg)

##### Definitions
For the navigation between program lines in the BASIC program and the executables, the BASIC interpreter provides a central reference mechanism.
This part of the documentation describes the function of this part of the implementation.

###### Program Lines
The project lines in this context are the program line numbers of the Dartmouth style BASIC program.

###### Token Numbers
Every token identified by the lexer is in a strict sequential list. The position of the list is part of the attributes of the token object.

###### Object Statement Numbers
The instantiated objects for the program execution are in a strict sequential list. As the position is not known at the time of the creation,
the number is at this version not an attribute of the object. This will change in the next main release, the object will also provide a method
to retrieve the information.

###### Implementation of the Relationship
The relationship is implemented by using two in-memory key-value stores. These key-value stores are hidden in the mentioned class below. It is possible to navigate between
the key-value stores (or program lists). None of the information is accessible from outside the class and all information is stored in the class. In a later version, as the programs can
be stored in a tokenized and parsed format (called object format) all information in the key-value stores and the program coded processable data will be part of the object file.

##### The Class `LineNumberXRef`
All functions to retrieve navigational information and manipulate the structure of the program in the different incarnations are located in the class
`LineNumberXRef` in the package `eu.gricom.interpreter.basic.memoryManager`.

The class has implemented a number of methods that allow the navigation between the different representations of the BASIC program. The following list explains the methods:

Input methods:
- `public final void putLineNumber(final int iLineNumber, final int iTokenNumber)` - add a BASIC line number with a related token number into the object.
- `public final void putStatementNumber(final int iTokenNumber, final int iStatementNumber)` - add a token number with the related statement number into the object.

Output methods:
- `public final int getLineNumberFromToken(final int iTokenNumber) throws RuntimeException` - get the line number related to a token number.
- `public final int getTokenFromStatement(final int iStatement) throws RuntimeException` - get the token number related to a statement number.
- `public final int getStatementFromLineNumber(final int iLineNumber) throws RuntimeException` - get the statement number related to a line number.
- `public final int getStatementFromToken(final int iTokenNumber) throws RuntimeException` - get the statement number related to a token number.

Utility methods:
- `public final int getNextLineNumber(final int iLineNumber)` - get the next line number following the line number found.

##### Example
1. Set the program pointer to the next BASIC program line following the BASIC line number defined in `iTargetNumber`:


    import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
    ...
    private final LineNumberXRef oLineNumberObject = new LineNumberXRef();
    ...
    _oProgramPointer.setCurrentStatement(oLineNumberObject.getStatementFromLineNumber(
            oLineNumberObject.getNextLineNumber(
                    oLineNumberObject.getLineNumberFromToken(
                            oLineNumberObject.getTokenFromStatement(iTargetLineNumber)))));


## Main Classes

![Main Class Structure](https://github.com/andreas-grimm/Interpreters/blob/development/doc/png/basic.png)

### Parser Package
![Parser Class Structure](https://github.com/andreas-grimm/Interpreters/blob/development/doc/png/parser.png)

#### JasicParser

#### BasicParser

Available methods in the BasicParser class to navigate the tokenized list and to find the information needed for the population of the statement class constructors are:

###### findToken
This method is used to scan the list of tokens provided by the lexer to identify the existence and location of a particular token. This function will report the first token 
fitting a certain token type. If the token is found, the method returns the found token - but does not change the position of the parse process in the list of tokens.
If the token is not found, the method will throw a `SyntaxErrorException` and terminate the parsing step.
This is the definition of the method:

    public final Token findToken(final TokenType oType) throws SyntaxErrorException


###### getToken
The `getToken`-Method does return the token found at a location defined by an offset. The offset is calculated by a number of tokens from the current token.

    public final Token getToken(final int iOffset)

###### consumeToken

    public final Token consumeToken(final TokenType oType) throws SyntaxErrorException

##### Expression processing

    private Expression expression() throws SyntaxErrorException
    public final Expression operator() throws SyntaxErrorException
    public final Expression atomic() throws SyntaxErrorException

##### Depreciated methods

###### matchNextToken
The method is used in the Jasic parser, but is not needed in the Basic parser at this time. As a general the Basic parser will not use the
name in the token, but the token type. In this release, the method is not used and marked as depreciated.

    public final boolean matchNextToken(final String strName)


### Tokenizer Package
![Tokenizer Class Structure](https://github.com/andreas-grimm/Interpreters/blob/development/doc/png/tokenizer.png)

![Referencing Structure](https://github.com/andreas-grimm/Interpreters/blob/development/doc/png/tokenizer_reference.png)

#### Token.java

#### Tokenizer.java

#### JasicLexer.java

#### TokenizeState.java

#### BasicLexer.java

#### TokenType.java
The content of the TokenType file is limited to the definition of the available token types.

### The MemoryManager Package
![Memory Manager Class Structure](https://github.com/andreas-grimm/Interpreters/blob/development/doc/png/memoryManager.png)

The memory manager functionality, located in the package `eu.gricom.interpreter.basic.memoryManager`, consists of three main
classes:
- the program pointer class (`ProgramPointer`), which contains the functionality related with the current place of execution.
- the stack class (`Stack`), which implements the needed stack functionality for the interpreter
- the variable management class (`VariableManagement`), which host all variables used in the program
- the line number cross-reference class (`LineNumberXRef`), replaced the `LineNumberStatement` class of previous versions - build the
reference between BASIC program lines, token numbers, and statement numbers.

A further planned class is:
- the array management class (`ArrayManagement`) that handles arrays of variables

#### The `ProgramPointer` Class

#### The `Stack` Class

The `Stack` class (`eu.gricom.interpreter.basic.memoryManager.Stack`) is a wrapper around the standard Java Stack class (`java.util.Stack`).
In order to make a common stack available in the interpreter, the `Stack` class implements internally a static private stack object
which can be accessed using the class `push` and `pop` method.

The implementation does only allow 

#### The `VariableManagement` Class

### The VariableType Package

#### Value Interface

#### Variable Type Boolean

#### Variable Type Integer

#### Variable Type Integer
supported as of the Q2 release...

#### Variable Type Real

#### Mathematical Functions

#### Variable Type String
The String variable type is implemented in the `StringValue` class in the `eu.gricom.interpreter.basic.variableTypes` package. Compared
to the other BASIC implementation, the String is not already implemented as an array of characters. To address single characters in
a String, the datatype supports squared brackets (`[` and `]`) rather than round ones, which are reserved for arrays.

The storage of the String value inside the `StringValue` class is done with a `Hashmap` implementation. All values for a
variable are in a key value storage. The loss of performance is compensated with the flexibility of implementation, allow
the easy implementation of n-dimensional arrays. Those strings that are not used as arrays are still using the Hashmap structure,
with a single key value, implemented as a constant: `strNoIndex = "noIndex"`.

To ensure that the array element can be retrieved, all incoming keys are normalized (i.e. all space characters are removed)
and the key is stored with the value in the Hashmap structure. This implementation allows later versions to use any form of
index in the use of strings. Refer to the description of the implementation or Arrays.

### The Statements Package
![Statements Class Structure](https://github.com/andreas-grimm/Interpreters/blob/development/doc/png/statements.png)

#### Expressions

##### OperatorExpression
The __OperatorExpression__ class implements the different functions implemented as operators. This class
originally contained not only the definition of the operators, but also the implementation. As of this
version, the operators are now in the [VariableTypes](https://github.com/andreas-grimm/Interpreters/blob/feature_tokenizer/doc/DeveloperDoc.md#The-VariableType-Package). By using the 
[common interface](https://github.com/andreas-grimm/Interpreters/blob/feature_tokenizer/doc/DeveloperDoc.md#value-interface), the __OperatorExpression__ is now independent of the actual values 
and does not need to be modified in case additional types (like 
[integers](https://github.com/andreas-grimm/Interpreters/blob/feature_tokenizer/doc/DeveloperDoc.md#variable-type-integer) or
[booleans](https://github.com/andreas-grimm/Interpreters/blob/feature_tokenizer/doc/DeveloperDoc.md#variable-type-boolean)) are added in a later version.

#### Statements

##### Assignment to variables and basic mathematical operators

##### `DO - UNTIL` Loop Statement

The `DO - UNTIL` loop provides the function of a receiving loop (contrary to the `WHILE` loop, which is a )

    DO <statements> EXIT <statements> UNTIL <condition>


###### `EXIT` Statement

###### `UNTIL` Statement

##### `FOR` Statement
A `For` statement counts an integer or real value from a start value to an end value - and with every increase it
loops through the block from the `For` statement to the next `Next` statement. When the target value is reached, the
program flow will jump to the statement past the `NEXT` statement.

When the loop command is reached the first time (initial execution), the variable named in the loop command is set with the initial
value. The current implementation of the `FOR` loop allows this variable to be either of type `real` or `integer`. After 
this, when the loop is iterating, the variable is incremented, and the result of the increment is compared with the target
value. As long as the target value is not reached the iteration continues. As the loop exceeds the target value, the loop 
terminates and processes with the next line after the `NEXT` statement. At this moment, the variable has the value of the last
increment, e.g. the value exceeds or equals the target value. The target of the new program pointer is defined as a line number
in the `FOR` loop - and the loop with use the `LineNumberXRef` class to retrieve the actual statement number for the
jump.

This logic will work the same way for negative loops, e.g. the flow decrements the variable, the loop counts downwards.

The `FOR` statement uses the `STACK` object to store the location of the `FOR` statement, so that the `NEXT` statement
finds it. Any `STACK` manipulation needs to be aware of this.

###### `NEXT` Statement
The `NEXT` statement terminates the execution of the `FOR` loop. In the program flow, the command has two functions:
- when the program flow reaches the `NEXT` command, the command will retrieve the location of the `FOR` command from the
`STACK` object. This target information is at this moment destroyed (see the description of the `Stack`-class). The program
flow will then continue at this location.
- when the `FOR`-loop reaches the end of the processing, the program flow will be adjusted to the first command after the `NEXT`
statement. *NOTE:* Intentionally the interpreter will always pair the `FOR` statement with the next `NEXT` statement. Overlapping
  or named `NEXT` statements (e.g. `NEXT X#`) like in other BASIC dialects are not supported. Overlapping loops are seen as an anti-pattern
  and violating the idea of structured programming.

##### `GOTO` Statement
The `GOTO` Statement - or non-conditional jump - moves the execution of the running Basic program
to a different location in the program. The executable functionality is located in class 
`eu.gricom.interpreter.basic.statements.GotoStatement`. The class is an implementation of the 
`eu.gricom.interpreter.basic.statements.Statement` interface. 

The implementation of the functionality is realized by using two different classes: 
`eu.gricom.interpreter.basic.statements.LabelStatement` for the JASIC programs, and 
`eu.gricom.interpreter.basic.memoryManagement.LineNumberXRef` for the BASIC program. These classes are used to identify
the target for the jump, which is either a Label (see `LabelStatement`), or a line number.

When using labels, the actual label is identified by the parsing process and is added to the list of locations in the
`LabelStatement` class. The class then has a second method to retrieve the location of the label in the program, and the
program pointer can be set to the location in the program. While the label functionality is relatively simple, the 
`LineNumberXRef` needs some extra effort to implement the use of line numbers. The class uses two internal hashmaps 
to build the logical connection between the BASIC source code lines and the executable statements, using the identified 
token as a link. As the target of the jump is a location, not a command or token, the class has to perform some look-ups
to determine the target of the jump. The link looks as follows:

Basic Source Code Line -> Token -> Executable Statement

When the `GOTO` command is executed, it will first look whether the argument with the command is located in the list of 
the labels. If there is no reference in the `Label` object (which will only be populated for JASIC programs), the flow
will use the `LineNumberXRef` object to determine the target of the jump. For this, the `GOTO` statement
will use the token sequence number (an attribute of the `Statement` classes) to retrieve the line number in the BASIC 
program.


The argument in the `GOTO` command is either a string (and then in the JASIC handling of the flow), or (alternatively) a 
number - which makes it part of the BASIC implementation.

Limitations of the current implementation:
* As the comments commands `REM` and `'` are not reflected in the executables, those commands can not be justed as targets 
  for a jump. The current functionality needs to be extended in such a way that if the target line is not found in the
  reference list in `LineNumberXRef`, then the flow needs to find the command with the next higher statement / token / 
  source line number. [link](https://gricom.atlassian.net/browse/BASIC-58)
* The performance of the retrieval of the BASIC source line is non-optimal and should be improved.

##### `GOSUB` Statement
The `GOSUB` Statement - or non-conditional jump into a subroutine - moves the execution of the running Basic program
to a different location in the program. The executable functionality is located in class `eu.gricom.interpreter.basic.statements.GosubStatement`. 
The class is an implementation of the `eu.gricom.interpreter.basic.statements.Statement` interface.

The implementation of the functionality is realized by using two different classes:
`eu.gricom.interpreter.basic.memoryManagement.LineNumberXRef` to determine the target for the jump and `eu.gricom.interpreter.basic.memoryManager.Stack`
to store the location of the `GOSUB` command. The later is needed for the calculation of the return to the main program
using the `RETURN` command.

Limitations of the current implementation:
* As the comments commands `REM` and `'` are not reflected in the executables, those commands can not be justed as targets
  for a jump. The current functionality needs to be extended in such a way that if the target line is not found in the
  reference list in `LineNumberXRef`, then the flow needs to find the command with the next higher statement / token /
  source line number. [link](https://gricom.atlassian.net/browse/BASIC-58)

###### `RETURN` Statement
The `RETURN` statement retrieves the location of the `GOSUB` statement from the stack, and calculates the location of the
next following command. It the performs an un-conditional jump to that location. At this time, the information of the calling 
`GOSUB` is removed from the stack.

*NOTE:* If a program execution runs into a `RETURN` statement without a prior `GOSUB` call being made, the stack will be empty and the program will terminate with an exception.
If the `RETURN` statement finds information on the stack - like from a previous `FOR` or `GOSUB` statement on the stack, it will execute on
this information - which will cause deterministic but incorrect behaviour and will ultimately end in an error situation.

##### `IF-THEN` Statement
The `IF-THEN` statement implements the main control statement in the programming language. The main structure of the control
statement consists of 4 token: `IF`, `THEN`, `ELSE`, `END-IF`. In the JASIC implementation the structure only consist of
two token: `IF` and `THEN`.

The structure of the command in BASIC is:
`IF` [condition] `THEN` [commands] <`ELSE` [commands]> `END-IF`

The same structure in JASIC is:
`IF` [condition] `THEN` [jump target]

The implementation of the structure for BASIC is similar to the JASIC implementation. 

Limitations of the current implementation:
* With this version of the interpreter, the negative case of the control statement (`ELSE`) is not yet implemented 
  [link](https://gricom.atlassian.net/browse/BASIC-64)
* Potential change of the logic (to be determined): If the BASIC `IF-THEN` command has no token, but a number following 
  the `THEN` statement, the command could execute a jump to the line number in the statement.

###### `END-IF` Statement
The `END-IF` statement closes the `IF-THEN` block, It is mainly used from the `IF-THEN` statement as a jump target in case the IF clause
fails. No other function is implemented for that statement.

##### `INPUT` Statement

##### `PRINT` Statement
The `PRINT` command is the general output command for the BASIC language. This version follows the Dartmouth standard for
the output, which allows concatenating multiple variables and text fields in a single line by listing them separated by a comma `,`.

The `PRINT` command terminates its output with CRLF (Carriage return / line feed) character, the next output is at the beginning
of a new line. By ending the `PRINT` command with a semicolon (`;`), the CRLF character sequence is not printed, the next output starts
after the last output printed.

This behaviour is different from the ECMA version of BASIC, which uses the semicolon to separate different fields and the comma to surpress
the CRLF characters at the end of the line.

`PRINT` [expression] <`,` [expression]`,` ... [expression]`;`>

The implementation of the `PRINT` command can be found in the statement package (`eu.gricom.interpreter.basic.statements`) in the
`PrintStatement` class. Adding the comma and semicolon to the `PRINT` command needs some special code in the `BasicLexer` class:
In order to make sure that the lexer identifies the characters, the lexer adds in the method `tokenize` an extra white space in front of the respective
character. As this will interfere with the formatting of any string using the characters, the white space will be removed 
in every string. This function should therefore not change the output.

##### `WHILE` Loop Statement

    WHILE <condition> <statements> CONTINUE <statements> EXIT-WHILE <statements> END-WHILE

###### `EXIT` Statement

###### `END-WHILE` Statement

### The Functions Package

#### Mathematical Functions

#### String Functions

#### OS-Related Functions

##### `System` Function
The `SYSTEM` function executes commands on OS level. The function requires two parameters: a command, and the
parameter. For the detail of usage out of BASIC, refer to the programming guide.

    RES$ = SYSTEM("RUN", <command> )

or

    RES$ = SYSTEM("START", <command> )

At this stage the `SYSTEM` function is implemented for `Unix` and `Linux` operating systems. The requirement for the 
execution is the installation of the `BASH` command interpreter. In a later version, the function will support 
Microsoft and Unix systems without installed `BASH` shell.

## Appendix

### Reserved Words

BASIC uses a number of reserved words for the implementation of the program. The following list provides
a full list of all reserved words for both dialects, BASIC and Jasic. To help programmers using different
Basic dialcts, the list also contains the reserved words of three major other Basic dialects. The semantics
of the keywords can vary - refer to the language manual for the use of the reserved words.

| Reserved Word |  GD-Basic | Jasic | TRS-80 Level II Basic | Applesoft Basic | Commodore Basic | Notes |
|---------------|-----------|-------|-----------------------|-----------------|-----------------|-------|
| `ABS` | implemented |  | implemented | implemented | implemented |
| `AND` | reserved |  | implemented | implemented | implemented |
| `ASC` | implemented |  | implemented | implemented | implemented |
| `AT` |  |  |  | implemented |  | 
| `ATN` | implemented |  | implemented | implemented | implemented |
| `AUTO` |  |  | implemented |  |  |
| `CALL` | reserved |  |  | implemented |  |
| `CDBL` | implemented |  | implemented |  |  |
| `CHR` | implemented |  | implemented | implemented | implemented |
| `CINT` | implemented |  | implemented |  |  |
| `CLEAR` |  |  | implemented | implemented |  |
| `CLOSE` | reserved |  | implemented | implemented | implemented |
| `CLOAD` |  |  | implemented |  |  |
| `CLS` | reserved |  | implemented |  |  |
| `CLR` |  |  |  |  | implemented |
| `CONT` | reserved |  |  |  |  |  |
| `CSAVE` |  |  | implemented | | |
| `CMD` | reserved |  | implemented | | implemented |
| `COLOR` |  |  |  | implemented |  |
| `CONT` | reserved |  | implemented | implemented | implemented |
| `COS` | implemented |  | implemented | implemented | implemented |
| `CSNG` |  |  | implemented |  |  |
| `CVD` |  | not planned | implemented |  |  |
| `CVI` |  |  | implemented |  |  |
| `CVS` |  |  | implemented |  |  |
| `DATA` | reserved |  | implemented | implemented | implemented |
| `DEF` |  |  | implemented | implemented |
| `DEF FN` | reserved |  |  | implemented |  |
| `DEFDBL` |  |  | implemented |  |  |
| `DEFINT` |  |  | implemented |  |  |
| `DEFSNG` |  |  | implemented |  |  |
| `DEFSTR` |  |  | implemented |  |  |
| `DEL` |  |  |  | implemented |  |
| `DELETE` |  |  | implemented |  |  |
| `DIM` | depreciated |  | implemented | implemented | implemented |
| `DO` | reserved |  |  |  |  |
| `DRAW` |  |  |  | implemented |  |
| `EDIT` |  |  | implemented |  |  |
| `ELSE` | reserved |  | implemented | implemented | implemented |
| `END` | implemented |  | implemented | implemented | implemented |
| `END-IF` | implemented |  |  |  |  |
| `END-WHILE` | implemented |  |  |  |  |
| `EOF` | reserved |  | implemented |  |  |
| `EOL` | reserved |  |  |  |  |
| `EOP` | implemented | implemented |  |  |  | `EOP` is a token used to mark the end of the program. It is not the implementation of a reserved word.
| `ERL` | reserved |  | implemented |  |  |
| `ERR` | reserved |  | implemented |  |
| `ERROR` |  |  | implemented |  |  |
| `EXP` | reserved |  | implemented | implemented | implemented |
| `EXIT` | reserved |  |  |  |  |
| `FIELD` |  |  | implemented |  |  |
| `FIX` | | not planned | implemented | | |
| `FLASH` |  |  | implemented |  |  |
| `FN` |  |  | implemented | implemented | implemented |
| `FOR` | implemented |  | implemented | implemented | implemented |
| `FRE` | reserved |  | implemented | implemented | implemented | 
| `GET` |  |  | implemented | implemented | implemented |
| `GO` |  |  |  |  | implemented |
| `GOSUB` | implemented |  | implemented | implemented | implemented |
| `GOTO` | implemented | implemented | implemented | implemented | implemented |
| `GR` |  |  |  | implemented |  |
| `HCOLOR` |  |  |  | implemented |  |
| `HGR` |  |  |  | implemented |  |
| `HGR2` |  |  |  | implemented |  |
| `HIMEM` |  |  |  | implemented |  |
| `HLIN` |  |  |  | implemented |  |
| `HOME` |  |  |  | implemented |  |
| `HTAB` |  |  |  | implemented |  |
| `HPLOT` |  |  |  | implemented |  |
| `IF` | implemented | implemented | implemented | implemented | implemented |
| `IN #` |  |  |  | implemented |  |
| `INKEY` |  |  | implemented |  |  |
| `INP` |  |  | implemented |  |  |
| `INSTR` | reserved |  | implemented |  |  |
| `INT` |  |  | implemented | implemented | implemented |
| `INVERSE` |  |  |  | implemented |  |
| `INPUT` | implemented | implemented | implemented | implemented | implemented |
| `INPUT#` |  |  |  |  | implemented |
| `KILL` |  |  | implemented |  |  |
| `LEFT` | reserved |  | implemented | implemented | implemented |
| `LEN` | reserved |  | implemented | implemented | implemented |
| `LENGTH` | reserved |  | implemented | implemented | implemented |
| `LET` | depreciated |  | implemented | implemented | implemented |
| `LINE` | implemented | implemented | implemented (different function) | | | The `LINE` token is used to mark empty program lines, the token has no responding reserved word.
| `LIST` |  |  | implemented | implemented | implemented |
| `LLIST` |  |  | implemented |  |  |
| `LOAD` |  |  | implemented |  | implemented |
| `LOC` |  |  | implemented |  |  |
| `LOF` |  |  | implemented |  |  |
| `LOG` | reserved |  | implemented | implemented | implemented |
| `LOMEM` |  |  |  | implemented |  |
| `LPRINT` |  |  | implemented |  |  |
| `LSET` |  |  | implemented |  |  |
| `MEM` | implemented |  | implemented |  |
| `MERGE` |  |  | implemented |  |  |
| `MID` | reserved, used token: `MID` |  | implemented | implemented | implemented |
| `MKD` |  |  | implemented | | |
| `MKI` |  |  | implemented | | |
| `MKS` |  |  | implemented | | |
| `NAME` |  |  | implemented | | |
| `NEW` |  |  | implemented | implemented | implemented |
| `NEXT` | reserved |  | implemented | implemented | implemented |
| `NORMAL` |  |  |  | implemented |  |
| `NOT` | reserved |  | implemented | implemented | implemented |
| `NOTRACE` |  | not planned |  | implemented |  |
| `ON` | reserved |  | implemented |  | implemented |
| `ONERR` |  |  |  | implemented |  |
| `OPEN` |  |  | implemented | implemented | implemented |
| `OR` | reserved |  | implemented | implemented | implemented |
| `OUT` |  |  | implemented |  |  |
| `PDL` |  |  |  | implemented |  |
| `PEEK` |  |  | implemented | implemented | implemented |
| `PLOT` |  |  |  | implemented |  |
| `POINT` |  | not planned | implemented |  |  |
| `POKE` |  |  | implemented | implemented | implemented |
| `POP` |  |  |  | implemented |  |
| `POS` |  |  | implemented | implemented | implemented |
| `PR #` |  |  |  | implemented | |
| `PRINT` | implemented | implemented | implemented | implemented | implemented |
| `PRINT#` |  |  |  |  | implemented |
| `PUT` |  |  | implemented |  |  |
| `RANDOM` | reserved |  | implemented |  |  |
| `READ` | reserved |  | implemented | implemented | implemented |
| `RECALL` |  |  |  | implemented |  |
| `REM` | implemented |  | implemented | implemented | implemented |
| `REMINDER` | reserved, used token instead of `%` |  |  |  |  |
| `RESET` |  |  | implemented |  |  |
| `RESUME` |  |  | implemented | implemented |  |
| `RETURN` | implemented |  | implemented | implemented | implemented |
| `RESTO` |  |  | implemented |  |  |
| `RESTORE` |  |  |  | implemented | implemented |
| `RIGHT` | reserved, used token: `RIGHT` |  | implemented | implemented | implemented |
| `RND` | implemented |  | implemented | implemented | implemented |
| `ROT` |  |  |  | implemented |  |
| `RSET` |  |  | implemented |  |  |
| `RUN` |  |  | implemented | implemented | implemented |
| `SAVE` |  |  | implemented |  | implemented |
| `SCALE` |  |  |  | implemented |  |
| `SET` |  |  | implemented |  |  |
| `SCRN` |  |  |  | implemented |  |
| `SGN` |  |  | implemented | implemented | implemented |
| `SHLOAD` |  |  |  | implemented |  |
| `SIN` | implemented |  | implemented | implemented | implemented |
| `SPC` |  |  |  | implemented | implemented |
| `SPEED` |  |  |  | implemented |  |
| `SQR` | implemented |  | implemented | implemented | implemented |
| `STEP` | reserved |  | implemented | implemented | implemented |
| `STOP` | reserved |  | implemented | implemented | implemented |
| `STORE` |  |  |  | implemented |  |
| `STRING` | reserved, used token: `TOSTRING` |  | implemented |  |  |
| `STR` | reserved |  | implemented | implemented | implemented |
| `SYSTEM` | reserved |  | implemented |  |  |
| `SYS` |  |  |  |  | implemented |
| `TAB` | reserved |  | implemented | implemented | implemented |
| `TAN` | implemented |  | implemented | implemented | implemented |
| `TEXT` |  |  |  | implemented |  |
| `THEN` | implemented | implemented | implemented | implemented | implemented |
| `TIME` | implemented |  | implemented |  |  |
| `TO` | implemented |  | implemented | implemented | implemented |
| `TRACE` |  |  |  | implemented |  |
| `TRON` |  |  | implemented |  |  |
| `TROFF` |  |  | implemented |  |  |
| `UNTIL` | reserved |  |  |  |  |
| `USR` |  |  |  | implemented | implemented |
| `VAL` | reserved |  | implemented | implemented | implemented |
| `VERIFY` |  |  |  |  | implemented |
| `VLIN` |  |  |  | implemented |  |
| `VTAB` |  |  |  | implemented |  |
| `WAIT` |  |  |  |  | implemented |
| `XDRAW` |  |  |  | implemented |  |
| `&` | reserved, used token: `AMPERSAND` |  |  | implemented |  |
| `+` | implemented, used token: `PLUS` | implemented | implemented | implemented | implemented |
| `-` | implemented, used token: `MINUS` | implemented | implemented | implemented | implemented |
| `*` | implemented, used token: `MULTIPLY` | implemented | implemented | implemented | implemented |
| `>>` | planned, used token: `SHIFT_RIGHT` |  |  |  |  |
| `<<` | planned, used token: `SHIFT_LEFT` |  |  |  |  |
| `/` | implemented, used token: `DIVIDE` | implemented | implemented | implemented | implemented |
| `;` | implemented |  |  | implemented |  |
| `,` | implemented |  |  | implemented |  |
| `:` | reserved, used token: `COLON` | implemented (as part of a label) |  | implemented | implemented |
| `>` | implemented, used token: `GREATER` | implemented | implemented | implemented | implemented |
| `>=` | implemented, used token: `GREATER_EQUAL` |  |  |
| `<` | implemented, used token: `SMALLER` | implemented | implemented | implemented | implemented |
| `<=` | implemented, used token: `SMALLER_EQUAL` |  |  |  |
| `=` | implemented, used token: `ASSIGN_EQUAL` | implemented | implemented | implemented | implemented |
| `:=` | reserved, used token: `PASCAL_ASSIGN_EQUAL` |  |  |
| `==` | implemented, used token: `COMPARE_EQUAL` |  |  |
| `!=` | implemented, used token: `COMPARE_NOT_EQUAL` |  |  |
| `^` | implemented, used token: `POWER` | reserved | implemented |  | implemented |
| `\'` (`REM` Quote) | implemented | implemented | implemented |  |

### Compatibility Guide

#### Tokenization of alternative implementations

##### Token's used in Interpreters

This reference table maps the token id's of the other Basic dialects. As the original use of Basic was limited to systems with very reduced
memory capabilities, the token were stored as HEX values. This interpreter uses a different approach and stores the token internally
as a list of objects. This reflects also in storing the tokenized and parsed program code.

| Token No. (Hex) | Token No. (Dec) | TRS-80 Level II Basic | Applesoft Basic | Commodore Basic |
|-----------------|-----------------|-----------------------|-----------------|-----------------|
| 80 |	128 | `END` | `END` | `END` |
| 81 |	129 | `FOR` | `FOR` | `FOR` |
| 82 |	130	| `RESET` | `NEXT` | `NEXT` |
| 83 |	131 | `SET` | `DATA` | `DATA` |
| 84 |	132 | `CLS` | `INPUT` | `INPUT#` |
| 85 |  133	| `CMD` | `DEL` | `INPUT` |
| 86 |	134	| `RANDOM` | `DIM` | `DIM` |
| 87 |	135 | `NEXT` | `READ` | `READ` |
| 88 |	136	| `DATA` | `GR` | `LET` |
| 89 |	137	| `INPUT` | `TEXT` | `GOTO` |
| 8A |	138	| `DIM` | `PR#` | `RUN` |
| 8B |	139	| `READ` | `IN#` | `IF` |
| 8C |	140	| `LET` | `CALL` | `RESTORE` |
| 8D |	141	| `GOTO` | `PLOT` | `GOSUB` |
| 8E |	142	| `RUN` | `HLIN` | `RETURN` |
| 8F |	143	| `IF` | `VLIN` | `REM` |
| 90 |	144	| `RESTO` | `HGR2` | `STOP` |
| 91 |	145	| `GOSUB` | `HGR` | `ON` |
| 92 |	146	| `RETURN` | `HCOLOR=` | `WAIT` |
| 93 |	147	| `REM` | `HPLOT` | `LOAD` |
| 94 |	148	| `STOP` | `DRAW` | `SAVE` |
| 95 |	149	| `ELSE` | `XDRAW` | `VERIFY` |
| 96 |	150	| `TRON` | `HTAB` | `DEF` |
| 97 |	151	| `TROFF` | `HOME` | `POKE` |
| 98 |	152	| `DEFSTR` | `ROT=` | `PRINT#` |
| 99 |	153	| `DEFINT` | `SCALE=` | `PRINT` |
| 9A |	154	| `DEFSNG` | `SHLOAD` | `CONT` |
| 9B |	155	| `DEFDBL` | `TRACE` | `LIST` |
| 9C |	156	| `LINE` | `NOTRACE` | `CLR` |
| 9D |	157	| `EDIT` | `NORMAL` | `CMD` |
| 9E |	158	| `ERROR` | `INVERSE` | `SYS` |
| 9F |	159	| `RESUME` | `FLASH` | `OPEN` |
| A0 |	160	| `OUT` | `COLOR=` | `CLOSE` |
| A1 |	161	| `ON` | `POP` | `GET` |
| A2 |	162	| `OPEN` | `VTAB` | `NEW` |
| A3 |	163	| `FIELD` | `HIMEM:` | `TAB(` |
| A4 |	164	| `GET` | `LOMEM:` | `TO` |
| A5 |	165	| `PUT` | `ONERR` | `FN` |
| A6 |	166	| `CLOSE` | `RESUME` | `SPC(` |
| A7 |	167	| `LOAD` | `RECALL` | `THEN` |
| A8 |	168	| `MERGE` | `STORE` | `NOT` |
| A9 |	169	| `NAME` | `SPEED=` | `STEP` |
| AA |	170	| `KILL` | `LET` | `+` |
| AB |	171	| `LSET` | `GOTO` | `-` |
| AC |	172	| `RSET` | `RUN` | `*` |
| AD |	173	| `SAVE` | `IF` | `/` |
| AE |	174	| `SYSTEM` | `RESTORE` | `^` |
| AF |	175	| `LPRINT` | `&` | `AND` |
| B0 |	176	| `DEF` | `GOSUB` | `OR` |
| B1 |	177	| `POKE` | `RETURN` | `>` |
| B2 |	178	| `PRINT` | `REM` | `=` |
| B3 |	179	| `CONT` | `STOP` | `<` |
| B4 |	180	| `LIST` | `ON` | `SGN` |
| B5 |	181	| `LLIST` | `WAIT` | `INT` |
| B6 |	182	| `DELETE` | `LOAD` | `ABS` |
| B7 |	183	| `AUTO` | `SAVE` | `USR` |
| B8 |	184	| `CLEAR` | `DEF FN` | `FRE` |
| B9 |	185	| `CLOAD` | `POKE` | `POS` |
| BA |	186	| `CSAVE` | `PRINT` | `SQR` |
| BB |	187	| `NEW` | `CONT` | `RND` |
| BC |	188	| `TAB` | `LIST` | `LOG` |
| BD |	189	| `TO` | `CLEAR` | `EXP` |
| BE |	190	| `FN` | `GET` | `COS` |
| BF |	191	| `LLIST` | `NEW` | `SIN` |
| C0 |	192	| `DELETE` | `TAB` | `TAN` |
| C1 |	193	| `AUTO` | `TO` | `ATN` |
| C2 |	194	| `ERL` | `FN` | `PEEK` |
| C3 |	195	| `ERR` | `SPC(` | `LEN` |
| C4 |	196	| `STRING$` | `THEN` | `STR$` |
| C5 |	197	| `INSTR` | `AT` | `VAL` |
| C6 |	198	| `POINT` | `NOT` | `ASC` |
| C7 |	199	| `TIME$` | `STEP` | `CHR$` |
| C8 |	200	| `MEM` | `+` | `LEFT$` |
| C9 |	201	| `INKEY$` | `-` | `RIGHT$` |
| CA |	202	| `THEN` | `*` | `MID$` |
| CB |	203	| `NOT` | `/` | `GO` |
| CC |	204	| `STEP` | `;` | |
| CD |	205	| `+` | `AND` | |
| CE |	206	| `-` | `OR` | |
| CF |	207	| `*` | `>` | |
| D0 |	208	| `/` | `=` | |
| D1 |	209	| `^` | `<` | |
| D2 |	210	| `AND` | `SGN` | |
| D3 |	211	| `OR` | `INT` | |
| D4 |	212	| `>` | `ABS` | |
| D5 |	213	| `=` | `USR` | |
| D6 |	214	| `<` | `FRE` | |
| D7 |	215	| `SGN` | `SCRN(` | |
| D8 |	216	| `INT` | `PDL` | |
| D9 |	217	| `ABS` | `POS` | |
| DA |	218	| `FRE` | `SQR` | |
| DB |	219	| `INP` | `RND` | |
| DC |	220	| `POS` | `LOG` | |
| DD |	221	| `SQR` | `EXP` | |
| DE |	222	| `RND` | `COS` | |
| DF |	223	| `LOG` | `SIN` | |
| E0 |	224	| `EXP` | `TAN` | |
| E1 |	225	| `COS` | `ATN` | |
| E2 |	226	| `SIN` | `PEEK` | |
| E3 |	227	| `TAN` | `LEN` | |
| E4 |	228	| `ATN` | `STR$` | |
| E5 |	229	| `PEEK` | `VAL` | |
| E6 |	230	| `CVI` | `ASC` | |
| E7 |	231	| `CVS` | `CHR$` | |
| E8 |	232	| `CVD` | `LEFT$` | |
| E9 |	233	| `EOF` | `RIGHT$` | |
| EA |	234	| `LOC` | `MID$` | |
| EB |	235	| `LOF` | | |
| EC |	236	| `MKI$` | | |
| ED |	237	| `MKS$` | | |
| EE |	238	| `MKD$` | | |
| EF |	239	| `CINT` | | |
| E0 |	240	| `CSNG` | | |
| F1 |	241	| `CDBL` | | |
| F2 |	242	| `FIX` | | |
| F3 |	243	| `LEN` | | |
| F4 |	244	| `STR$` | | |
| F5 |	245	| `VAL` | | |
| F6 |	246	| `ASC` | | |
| F7 |	247	| `CHR$` | | |
| F8 |	248	| `LEFT$` | | |
| F9 |	249	| `RIGHT$` | | | 
| FA |	250	| `MID$` | | |
| FB |	251 | `'` (REM QUOTE) | | |
