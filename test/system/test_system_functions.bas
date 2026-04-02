REM ========================================================================
REM Test: System Functions
REM Description: Tests system functions (MEM, TIME$, SYSTEM)
REM Expected: System functions should return appropriate values
REM ========================================================================

10 PRINT "=== Testing System Functions ==="
20 PRINT ""

REM Test MEM function
30 PRINT "Testing MEM (Memory Available):"
40 mem% = MEM
50 PRINT "MEM = "; mem%
60 IF mem% < 0 THEN GOTO 9000
70 PRINT "MEM returned non-negative value (correct)"

REM Test TIME$ function
80 PRINT "Testing TIME$ (Current Time):"
90 time$ = TIME
100 PRINT "TIME$ = "; time$
110 IF LEN(time$) == 0 THEN GOTO 9000
120 PRINT "TIME$ returned a value (correct)"

130 PRINT ""
140 PRINT "=== All System Function Tests PASSED ==="
150 END

9000 PRINT "ERROR: Test failed!"
9010 END
