REM ========================================================================
REM Test: GOTO, GOSUB, and RETURN Statements
REM Description: Tests program flow control with GOTO, GOSUB, and RETURN
REM Expected: Program flow should jump correctly and subroutines should work
REM ========================================================================

10 PRINT "=== Testing GOTO, GOSUB, and RETURN ==="
20 PRINT ""

REM Test GOTO
30 PRINT "Testing GOTO:"
40 GOTO 70
50 PRINT "ERROR: This line should be skipped"
60 GOTO 9000
70 PRINT "GOTO worked correctly"

REM Test GOSUB and RETURN
80 PRINT "Testing GOSUB and RETURN:"
90 PRINT "Before subroutine call"
100 GOSUB 1000
110 PRINT "After subroutine call"

REM Test multiple GOSUB calls
120 PRINT "Testing Multiple GOSUB Calls:"
130 GOSUB 2000
140 GOSUB 2000
150 PRINT "Multiple calls completed"

REM Test nested GOSUB
160 PRINT "Testing Nested GOSUB:"
170 GOSUB 3000
180 PRINT "Nested GOSUB completed"

190 PRINT ""
200 PRINT "=== All GOTO/GOSUB/RETURN Tests PASSED ==="
210 END

REM Subroutine 1
1000 PRINT "  Inside subroutine 1000"
1010 RETURN

REM Subroutine 2
2000 PRINT "  Inside subroutine 2000"
2010 RETURN

REM Subroutine 3 (calls another subroutine)
3000 PRINT "  Inside subroutine 3000"
3010 GOSUB 3100
3020 PRINT "  Back in subroutine 3000"
3030 RETURN

3100 PRINT "    Inside nested subroutine 3100"
3110 RETURN

9000 PRINT "ERROR: Test failed!"
9010 END
