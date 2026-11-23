1 REM ========================================================================
2 REM Compiler test: SYSTEM function
3 REM Description: Runs an operating system command and returns its output.
4 REM              The compiled program must print exactly what the interpreter prints.
5 REM ========================================================================
10 A$ = SYSTEM("RUN", "echo hello from the system")
20 PRINT A$
30 END
