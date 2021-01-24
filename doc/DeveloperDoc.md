This document contains the documentation of the different concepts used for the implementation of the 
interprester. It duplicates in some areas the language documentation, 
which cannot be avoided.

# Developer Documentation

This documentation has been written as a notebook or scrapbook of the
developer(s) of the interpreter - mainly to document the ideas and concepts
used in the implementation. It is a living document, under version control,
and should be extended as the code growths and needs some additional documentation.

## Markdown
All documentation is written using the MarkDown format. The documentation of the
format can be found here: [www.markdownguide.org](https://www.markdownguide.org/basic-syntax/),
 with the extended syntax documented here [Extended Syntax](https://www.markdownguide.org/extended-syntax/),
or here: [github.com](https://guides.github.com/features/mastering-markdown/)

## Installing and Using the Interpreter
The interpreter is delivered source code only. To use it, the package is using Apache Maven to compile.

Use the `git clone` command to retrieve the latest version of the interpreter. The package will download into any location, there is 
no need for a special filesystem set-up. The package has been tested using Mac OS X and Linux, but the installation should support
Windows as well.

For the compilation, the project is using *Apache Maven* as the build manager tool. The implementation is tested with Maven 3.6.3, but
any newer version of Maven should work as well. The command line to run Maven is:

    mvn clean compile dependency:tree dependency:copy-dependencies package

During the build process, the interpreter is compiled, but the process also runs a number of JUNIT test cases, and performs
a static code analysis using Checkstyle. The definition of the syntax for Checkstyle is in the `./etc` directory, Checkstyle
is working with the code style guide of the project. The code style guide is located in the projects root directory, named `STYLEGUIDE.md`.
If you want to participate, please adopt your contributions to the look and feel of the project. Code submissions not reflecting
the styleguide will be rejected.

A wrapper around this command can be found in the `./bin`-directory: `./bin/build`. A number of test
scripts are added: `bin/test` is running the _JASIC_ test programs, `bin/test2` runs the same program logic as a _BASIC_
program. `bin/dev` is used to run the development _BASIC_ program that is used to implement and unit test the _BASIC_
version of the interpreter.

At this stage, the interpreter is using a command line interface (`CLI`). The syntax of the `CLI` is:

    java -jar target/BASIC-<version-SNAPSHOT>.jar <parameter> <program name>.<.bas|.jas>

Possible optional parameter supported are:
* "`-h`" - help (This screen), no further arguments
* "`-q`" - quiet mode, disables all interpeter messages and only displays the output of the program
* "`-v`" - verbose level: parameter defines the debug level, e.g. `info`, `debug`. See the following example.
* "`-b`" - language type: `J` = Jasic, `B` = Basic, default is set to `B`

The only mandatory parameter is:
* "`-i`" - mandatory name of the input file, with the name of the input file as an argument

_Example_:

    java -jar target/BASIC-0.0.2-SNAPSHOT.jar -v"debug|info" -i src/test/resources/testfile_basic.bas

## Participation in the Development of the Interpreter
As of version `0.1.0` (planned) the interpeter is avaialble in a public _GITHUB_ (`github.com`) repository. It is covered by
a modified version of the __CDDL 1.0 License__, see `LICENSE.md` in the project root directory.

The project is following open source project standard processes. Anybody with an interest to participate can and may clone or
fork a copy of the project. Deliveries back to the author can be done using pull requests. Refer to the `git` documentation
to gain information on the process.

A `GIST` space might be opened at a later stage, right now the project is using a _Jira_ space to plan additional functionality
and release dates. The _Jira_ project can be found here:

[Jira Link](https://gricom.atlassian.net/jira/software/projects/BASIC)

## Before Starting Modifications...


## General Structure of the Interpreter

![Interpreter Structure](https://github.com/andreas-grimm/Interpreters/blob/feature_tokenizer/doc/Process%20Flow.jpg?raw=true)

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


### Parser
The parser translates the program, representated as a list of token, to a list of statement objects. These statement objects
contain all logic of the different commands, so the runtime part of the interpreter does barely contain any logic.

### Runtime

## How to add new BASIC Commands to the Interpreter
New commands, unlike new structures, do not require a change in the Tokenizer. The main part to add new commands
is in the [BasicParser](https://github.com/andreas-grimm/Interpreters/blob/feature_tokenizer/doc/DeveloperDoc.md#BasicParser-Class)
class. In this class, the method __parse__ contains the logic to convert the identified token into the sequence of commands 
that will be executed in the interpreter.

### Adding new token to the Tokenizer

- Step 1: Add the new reserved word into the list of reserved words (`eu.gricom.interpreter.basic.tokenizer.ReservedWords.java`)
  and the token file (`eu.gricom.interpeter.basic.tokenizer.TokenType.java`).
- Step 2: Verify that the BASIC tokenizer (`eu.gricom.interpreter.basic.BasicLexer.java`) can process the added new tokens and reserved word.

## Main Classes

### Parser Package

#### JasicParser

#### BasicParser

### Tokenizer Package

#### Token.java

#### Tokenizer.java

#### JasicLexer.java

#### TokenizeState.java

#### BasicLexer.java

#### TokenType.java
The content of the TokenType file is limited to the definition of the available token types.

## Package Structure

### The MemoryManager Package

### The VariableType Package

#### Value Interface

#### Variable Type Boolean

#### Variable Type Integer

#### Variable Type Real

#### Mathematical Functions

#### Variable Type String

### The Statements Package

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

##### `GOTO` Statement
The `GOTO` Statement - or non-conditional jump - moves the execution of the running Basic program
to a different location in the program. The executable functionality is located in class 
`eu.gricom.interpreter.basic.statements.GotoStatement`. The class is an implementation of the 
`eu.gricom.interpreter.basic.statements.Statement` interface. 

The implementation of the functionality is realized by using two different classes: 
`eu.gricom.interpreter.basic.statements.LabelStatement` for the JASIC programs, and 
`eu.gricom.interpreter.basic.statements.LineNumberStatement` for the BASIC program. These classes are used to identify
the target for the jump, which is either a Label (see `LabelStatement`), or a line number.

When using labels, the actual label is identified by the parsing process and is added to the list of locations in the
`LabelStatement` class. The class then has a second method to retrieve the location of the label in the program, and the
program pointer can be set to the location in the program. While the label functionality is relatively simple, the 
`LineNumberStatement` needs some extra effort to implement the use of line numbers. The class uses two internal hashmaps 
to build the logical connection between the BASIC source code lines and the executeable statements, using the identified 
token as a link. As the target of the jump is a location, not a command or token, the class has to perform some look-ups
to determine the target of the jump. The link looks as follows:

Basic Source Code Line -> Token -> Executable Statement

When the `GOTO` command is executed, it will first look whether the argument with the command is located in the list of 
the labels. If there is no reference in the `Label` object (which will only be populated for JASIC programs), the flow
will use the `LineNumberStatement` object to determine the target of the jump. For this, the `GOTO` statement
will use the token sequence number (an attribute of the `Statement` classes) to retrieve the line number in the BASIC 
program.


The argument in the `GOTO` command is either a string (and then in the JASIC handling of the flow), or (alternatively) a 
number - which makes it part of the BASIC implementation.

Limitations of the current implementation:
* As the comments commands `REM` and `'` are not reflected in the executables, those commands can not be justed as targets 
  for a jump. The current functionality needs to be extended in such a way that if the target line is not found in the
  reference list in `LineNumberStatement`, then the flow needs to find the command with the next higher statement / token / 
  source line number. [link](https://gricom.atlassian.net/browse/BASIC-58)
* The performance of the retrieval of the BASIC source line is non-optimal and should be improved.

##### `IF-THEN` Statement
The `IF-THEN` statement implements the main control statement in the programming language. The main structure of the control
statement consists of 4 token: `IF`, `THEN`, `ELSE`, `END-IF`. In the JASIC implementation the structure only consist of
two token: `IF` and `THEN`.

The structure of the command in BASIC is:
`IF` [condition] `THEN` [commands] `ELSE` [commands] `END-IF`

The same structure in JASIC is:
`IF` [condition] `THEN` [jump target]

The implementation of the structure for BASIC is similar to the JASIC implementation. 

Limitations of the current implementation:
* With this version of the interpreter, the negative case of the control statement (`ELSE`) is not yet implemented 
  [link](https://gricom.atlassian.net/browse/BASIC-64)
* Potential change of the logic (to be determined): If the BASIC `IF-THEN` command has no token, but a number following 
  the `THEN` statement, the command could execute a jump to the line number in the statement.

##### `INPUT` Statement

##### `PRINT` Statement

## Appendix

### Reserved Words

BASIC uses a number of reserved words for the implementation of the program. The following list provides
a full list of all reserved words for both dialects, BASIC and Jasic. To help programmers using different
Basic dialcts, the list also contains the reserved words of three major other Basic dialects. The semantics
of the keywords can vary - refer to the language manual for the use of the reserved words.

| Reserved Word |  GD-Basic | Jasic | TRS-80 Level II Basic | Applesoft Basic | Commodore Basic | Notes |
|---------------|-----------|-------|-----------------------|-----------------|-----------------|-------|
| `ABS` | reserved |  | implemented | implemented | implemented |
| `AND` | reserved |  | implemented | implemented | implemented |
| `ASC` | reserved |  | implemented | implemented | implemented |
| `AT` |  |  |  | implemented |  | 
| `ATN` | reserved |  | implemented | implemented | implemented |
| `AUTO` |  |  | implemented |  |  |
| `CALL` | reserved |  |  | implemented |  |
| `CDBL` | reserved |  | implemented |  |  |
| `CHR$` | reserved, used token: `CHR` |  | implemented | implemented | implemented |
| `CINT` | reserved |  | implemented |  |  |
| `CLEAR` |  |  | implemented | implemented |  |
| `CLOSE` | reserved |  | implemented | implemented | implemented |
| `CLOAD` |  |  | implemented |  |  |
| `CLS` | reserved |  | implemented |  |  |
| `CLR` |  |  |  |  | implemented |
| `CSAVE` |  |  | implemented | | |
| `CMD` | reserved |  | implemented | | implemented |
| `COLOR=` |  |  |  | implemented |  |
| `CONT` | reserved |  | implemented | implemented | implemented |
| `COS` | reserved |  | implemented | implemented | implemented |
| `CSNG` | reserved |  | implemented |  |  |
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
| `DIM` | reserved |  | implemented | implemented | implemented |
| `DRAW` |  |  |  | implemented |  |
| `EDIT` |  |  | implemented |  |  |
| `ELSE` | reserved |  | implemented | implemented | implemented |
| `END` | implemented |  | implemented | implemented | implemented |
| `END-IF` | implemented |  |  |  |  |
| `EOF` | reserved |  | implemented |  |  |
| `EOL` | reserved |  |  |  |  |
| `EOP` | implemented | implemented |  |  |  | `EOP` is a token used to mark the end of the program. It is not the implementation of a reserved word.
| `ERL` | reserved |  | implemented |  |  |
| `ERR` | reserved |  | implemented |  |
| `ERROR` |  |  | implemented |  |  |
| `EXP` | reserved |  | implemented | implemented | implemented |
| `FIELD` |  |  | implemented |  |  |
| `FIX` | | not planned | implemented | | |
| `FLASH` |  |  | implemented |  |  |
| `FN` |  |  | implemented | implemented | implemented |
| `FOR` | reserved |  | implemented | implemented | implemented |
| `FRE` | reserved |  | implemented | implemented | implemented | 
| `GET` |  |  | implemented | implemented | implemented |
| `GO` |  |  |  |  | implemented |
| `GOSUB` | reserved |  | implemented | implemented | implemented |
| `GOTO` | implemented | implemented | implemented | implemented | implemented |
| `GR` |  |  |  | implemented |  |
| `HCOLOR=` |  |  |  | implemented |  |
| `HGR` |  |  |  | implemented |  |
| `HGR2` |  |  |  | implemented |  |
| `HIMEM:` |  |  |  | implemented |  |
| `HLIN` |  |  |  | implemented |  |
| `HOME` |  |  |  | implemented |  |
| `HTAB` |  |  |  | implemented |  |
| `HPLOT` |  |  |  | implemented |  |
| `IF` | implemented | implemented | implemented | implemented | implemented |
| `IN #` |  |  |  | implemented |  |
| `INKEY$` |  |  | implemented |  |  |
| `INP` |  |  | implemented |  |  |
| `INSTR` | reserved |  | implemented |  |  |
| `INT` | reserved |  | implemented | implemented | implemented |
| `INVERSE` |  |  |  | implemented |  |
| `INPUT` | implemented | implemented | implemented | implemented | implemented |
| `INPUT#` |  |  |  |  | implemented |
| `KILL` |  |  | implemented |  |  |
| `LEFT$` | reserved, used token: `LEFT` |  | implemented | implemented | implemented |
| `LEN` | reserved |  | implemented | implemented | implemented |
| `LET` | reserved |  | implemented | implemented | implemented |
| `LINE` | implemented | implemented | implemented (different function) | | | The `LINE` token is used to mark empty program lines, the token has no responding reserved word.
| `LIST` |  |  | implemented | implemented | implemented |
| `LLIST` |  |  | implemented |  |  |
| `LOAD` |  |  | implemented |  | implemented |
| `LOC` |  |  | implemented |  |  |
| `LOF` |  |  | implemented |  |  |
| `LOG` | reserved |  | implemented | implemented | implemented |
| `LOMEM:` |  |  |  | implemented |  |
| `LPRINT` |  |  | implemented |  |  |
| `LSET` |  |  | implemented |  |  |
| `MEM` | reserved |  | implemented |  |
| `MERGE` |  |  | implemented |  |  |
| `MID$` | reserved, used token: `MID` |  | implemented | implemented | implemented |
| `MKD$` |  |  | implemented | | |
| `MKI$` |  |  | implemented | | |
| `MKS$` |  |  | implemented | | |
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
| `RETURN` | reserved |  | implemented | implemented | implemented |
| `RESTO` |  |  | implemented |  |  |
| `RESTORE` |  |  |  | implemented | implemented |
| `RIGHT$` | reserved, used token: `RIGHT` |  | implemented | implemented | implemented |
| `RND` | reserved |  | implemented | implemented | implemented |
| `ROT=` |  |  |  | implemented |  |
| `RSET` |  |  | implemented |  |  |
| `RUN` |  |  | implemented | implemented | implemented |
| `SAVE` |  |  | implemented |  | implemented |
| `SCALE=` |  |  |  | implemented |  |
| `SET` |  |  | implemented |  |  |
| `SCRN(` |  |  |  | implemented |  |
| `SGN` | reserved |  | implemented | implemented | implemented |
| `SHLOAD` |  |  |  | implemented |  |
| `SIN` | reserved |  | implemented | implemented | implemented |
| `SPC(` |  |  |  | implemented | implemented |
| `SPEED=` |  |  |  | implemented |  |
| `SQR` | reserved |  | implemented | implemented | implemented |
| `STEP` | reserved |  | implemented | implemented | implemented |
| `STOP` | reserved |  | implemented | implemented | implemented |
| `STORE` |  |  |  | implemented |  |
| `STRING$` | reserved, used token: `TOSTRING` |  | implemented |  |  |
| `STR$` | reserved, used token: `STR` |  | implemented | implemented | implemented |
| `SYSTEM` | reserved |  | implemented |  |  |
| `SYS` |  |  |  |  | implemented |
| `TAB` | reserved |  | implemented | implemented | implemented |
| `TAN` | reserved |  | implemented | implemented | implemented |
| `TEXT` |  |  |  | implemented |  |
| `THEN` | implemented | implemented | implemented | implemented | implemented |
| `TIME$` | reserved, used token: `TIME` |  | implemented |  |  |
| `TO` | reserved |  | implemented | implemented | implemented |
| `TRACE` |  |  |  | implemented |  |
| `TRON` |  |  | implemented |  |  |
| `TROFF` |  |  | implemented |  |  |
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
| `/` | implemented, used token: `DIVIDE` | implemented | implemented | implemented | implemented |
| `;` |  |  |  | implemented |  |
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

### Compartibility Guide

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
