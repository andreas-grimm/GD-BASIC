# The Official gricom.eu Java Style Guide

This style guide is different from other you may see, because the focus is
centered on readability for print and the web. We created this style guide to
keep the code in our tutorials consistent.

Our overarching goals are __conciseness__, __readability__ and __simplicity__.

You should also check out out [Swift](https://github.com/raywenderlich/swift-style-guide)
and [Objective-C](https://github.com/raywenderlich/objective-c-style-guide)
style guides too.

## Checkstyle

All rules in the styleguide are verified using the Checkstyle tool during the build process.

## Inspiration

This style-guide is somewhat of a mash-up between the existing Java language
style guides, style guide for C and C++ coding, and a tutorial-readability focused Swift style-guide. The language
guidance is drawn from the
[Android contributors style guide](https://source.android.com/source/code-style.html)
and the
[Google Java Style Guide](https://google-styleguide.googlecode.com/svn/trunk/javaguide.html).

## Nomenclature

On the whole, naming should follow Java standards.

### Packages

Package names are all __lower-case__, multiple words concatenated together,
without hypenation or underscores:

__BAD__:

> eu.GriCom.funky_widget

__GOOD__:

> eu.gricom.funkywidget


### Classes & Interfaces

Written in __UpperCamelCase__. For example 'ResourceHandler'. 

### Methods

Written in __lowerCamelCase__. For example 'setValue'.

### Fields

Written in __lowerCamelCase__.

Static fields should be written in __uppercase__, with an underscore separating
words:

> public static final int THE_ANSWER = 42;

Field naming should follow Hungarian Notation and naming conventions in the following form:

- each field starts with a description of it's type, common descriptions are "str" for string, "i" for integer, "o" of undetermined object, "b" for boolean, and "l" for long.
- collections add the type of collection before the type: "v" for Vector, "a" for array, and "l" for list
- maps are considered objects
- member variables (variables that are global in an object) start with an underscore "_".

For example:
>  public static final int SOME_CONSTANT = 42;
>
>  private static MyClass _oSingleton;
>  
>  int iPrivate;
>
>  String strPrivate;
>
>  Vector<String> vstrProtected;

### Variables & Parameters

Written in __lowerCamelCase__.

Single character values to be avoided except for temporary looping variables.

### Misc

In code, acronyms should be treated as words. For example:

__BAD:__

> XMLHTTPRequest

> String URL

> findPostByID

__GOOD:__

> XmlHttpRequest

> String url

> findPostById


## Project Naming

Each project is designated a 4 character project name. The first character is designated "G" for internal projects. Alternative characters can be reserved as needed. The second character is designated a "D" for projects resulting in common libraries, development tools, utilities. The character "C" is designated for projects related with commercial issues. The character "R" is designated to projects related with reference and master data issues. The character "X" in the second position is reserved for projects that do not fall into any of those categories.

Each project shall have a README.md file which describes the purpose of the project and also contains the modification history and planning. Mandatory on published projects is also the LICENSE.md file containing the license governing the release of the software and the STYLEGUIDE.md file (this file) describing the used coding conventions. The latest version of each of these files is in the GDXX project. Every release should adopt the latest version of the files.

## Versioning and Version Control

The version control system of choice is GIT. The latest tested and released version of each project is in the Master branch of the software repository, the Release Candidate is in the Test branch, and the daily build is in the Development branch. Projects with more than one developer are using Feature Branches.

Each project has a version number. THe version number consists of three digits, designated for <major>.<minor>.<bugfix>. A major vrsion number < 0 designates not published code.

### Access Level Modifiers

Access level modifiers should be explicitly defined for classes, methods and
member variables.

### Fields & Variables

Prefer single declaration per line.

__BAD:__

> String username, twitterHandle;

__GOOD:__

> String username;

> String twitterHandle;

### Classes

Exactly one class per source file, although inner classes are encouraged where
scoping appropriate.


### Enum Classes

Enum classes should be avoided where possible, due to a large memory overhead.
Static constants are preferred.

Enum classes without methods may be formatted without line-breaks, as follows:

> private enum CompassDirection { EAST, NORTH, WEST, SOUTH }

## Spacing

Spacing is especially important in code, as code needs to be
easily readable as part of the tutorial. Java does not lend itself well to this.

### Indentation

Indentation is using spaces - never tabs.

#### Blocks

Indentation for blocks uses 2 spaces (not the default 4):

#### Line Wraps

Indentation for line wraps should use 4 spaces (not the default 8):

### Line Length

Lines should be no longer than 100 characters long.

### Vertical Spacing

There should be exactly one blank line between methods to aid in visual clarity 
and organization. Whitespace within methods should separate functionality, but 
having too many sections in a method often means you should refactor into
several methods.

## Getters and Setters

For external access to fields in classes, getters and setters are preferred to
direct access of the fields. Fields should rarely be public.

However, it is encouraged to use the field directly when accessing internally
(i.e. from inside the class). This is a performance optimization recommended
by Google: http://developer.android.com/training/articles/perf-tips.html#GettersSetters

## Brace Style

Only trailing closing-braces are awarded their own line. All others appear the
same line as preceding code

Conditional statements are always required to be enclosed with braces,
irrespective of the number of lines required.

## Switch Statements

Switch statements fall-through by default, but this can be unintuitive. If you
require this behavior, comment it.

Alway include the default case.

## Annotations

Standard annotations should be used - in particular @overwrite. This should
appear the line before the function declaration.


## XML Guidance

The use of XMML is discouraged, the data format of choice is JSON. The format for configuration files is YAML

## Language

Use British English spelling.

## Copyright Statement

The Copyright statement should have minimal length, copyrights and related issues are documented in the LICENSE.md file