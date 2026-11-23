1 REM ========================================================================
2 REM Compiler error test: unary minus on a string
3 REM Description: -S$ must stop the program with the error
4 REM              "Cannot apply unary minus to a string". Both the interpreter and the
5 REM              compiled program must stop with that error.
6 REM ========================================================================
10 S$ = "x"
20 PRINT -S$
30 END
