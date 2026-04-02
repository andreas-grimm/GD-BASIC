1 REM ========================================================================
2 REM Test: GOTO, GOSUB, and RETURN Statements
3 REM Description: Tests program flow control with GOTO, GOSUB, and RETURN
4 REM Expected: Program flow should jump correctly and subroutines should work
5 REM ========================================================================

10 PRINT "=== Testing GOTO, GOSUB, and RETURN ==="
20 PRINT ""

30 REM Test GOTO
40 PRINT "Testing GOTO:"
50 GOTO 80
60 PRINT "ERROR: This line should be skipped"
70 GOTO 9000
80 PRINT "GOTO worked correctly"

90 REM Test GOSUB and RETURN
100 PRINT "Testing GOSUB and RETURN:"
110 PRINT "Before subroutine call"
120 GOSUB 1000
130 PRINT "After subroutine call"

140 REM Test multiple GOSUB calls
150 PRINT "Testing Multiple GOSUB Calls:"
160 GOSUB 2000
170 GOSUB 2000
180 PRINT "Multiple calls completed"

190 REM Test nested GOSUB
200 PRINT "Testing Nested GOSUB:"
210 GOSUB 3000
220 PRINT "Nested GOSUB completed"

230 PRINT ""
240 PRINT "=== All GOTO/GOSUB/RETURN Tests PASSED ==="
250 END

1000 REM Subroutine 1
1010 PRINT "  Inside subroutine 1000"
1020 RETURN

2000 REM Subroutine 2
2010 PRINT "  Inside subroutine 2000"
2020 RETURN

3000 REM Subroutine 3 (calls another subroutine)
3010 PRINT "  Inside subroutine 3000"
3020 GOSUB 3100
3030 PRINT "  Back in subroutine 3000"
3040 RETURN

3100 PRINT "    Inside nested subroutine 3100"
3110 RETURN

9000 PRINT "ERROR: Test failed!"
9010 END
