1 REM ========================================================================
2 REM Test: System Functions
3 REM Description: Tests system functions (MEM, TIME$, SYSTEM)
4 REM Expected: System functions should return appropriate values
5 REM ========================================================================

10 PRINT "=== Testing System Functions ==="
20 PRINT ""

30 REM Test MEM function
40 PRINT "Testing MEM (Memory Available):"
50 mem% = MEM
60 PRINT "MEM = "; mem%
70 IF mem% < 0 THEN GOTO 9000
80 PRINT "MEM returned non-negative value (correct)"

90 REM Test TIME$ function
100 PRINT "Testing TIME$ (Current Time):"
110 time$ = TIME
120 PRINT "TIME$ = "; time$
130 IF LEN(time$) == 0 THEN GOTO 9000
140 PRINT "TIME$ returned a value (correct)"

150 PRINT ""
160 PRINT "=== All System Function Tests PASSED ==="
170 END

9000 PRINT "ERROR: Test failed!"
9010 END
