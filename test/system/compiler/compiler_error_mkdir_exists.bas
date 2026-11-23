1 REM ========================================================================
2 REM Compiler error test: MKDIR of a directory that already exists
3 REM Description: MKDIR "." must stop the program with the error "already exists".
4 REM              Both the interpreter and the compiled program must stop with that error.
5 REM ========================================================================
10 MKDIR "."
20 END
