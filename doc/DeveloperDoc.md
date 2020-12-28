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

## General Structure of the Interpreter

### Tokenizer

### Parser

### Runtime

## Package Structure

### The MemoryManager Package

### The VariableType Package

#### Value Interface

#### Variable Type Boolean

#### Variable Type Integer

#### Variable Type Real

##### Mathematical Functions

#### Variable Type String

### The Statements Package

#### Expressions

##### OperatorExpression
The __OperatorExpression__ class implements the different functions implemented as operators. This class
originally contained not only the definition of the operators, but also the implementation. As of this
version, the operators are now in the [VariableTypes](#The VariableType Package). By using the 
[common interface](#Value Interface), the __OperatorExpression__ is now independent of the actual values 
and does not need to be modified in case additional types (like [integers](#Variable Type Integer) or
[booleans](#Variable Type Boolean)) are added in a later version.

#### Statements
