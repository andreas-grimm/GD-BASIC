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
60 PRINT "MEM = ";
70 PRINT mem%
80 IF mem% < 0 THEN 9000
90 PRINT "MEM returned non-negative value (correct)"
100 REM Test TIME$ function
110 PRINT "Testing TIME$ (Current Time):"
120 time$ = TIME
130 PRINT "TIME$ = ";
140 PRINT time$
150 IF LEN(time$) == 0 THEN 9000
160 PRINT "TIME$ returned a value (correct)"
170 PRINT ""
180 PRINT "=== All System Function Tests PASSED ==="
190 END
9000 PRINT "ERROR: Test failed!"
9010 END
