1 REM ========================================================================
2 REM Regression: PI to 32 digits with BCD (#) variables
3 REM Description: Calculates PI twice, first with the Gauss-Legendre
4 REM              iteration and then with Machin's formula, using decimal
5 REM              (BCD) variables with 40 significant digits. Each iteration
6 REM              or series term prints its progress. Both results are checked
7 REM              against the known 32-digit value.
8 REM Expected:    PI = 3.1415926535897932384626433832795 (32 significant digits)
9 REM Ends with:   "PASSED" on success, or GOTO 9000 on failure
10 REM ========================================================================
20 PRINT "=== Testing PI to 32 digits (BCD) ==="
30 PRINT ""
40 REM Convergence tolerance: 1E-35 written as a decimal literal
50 tol# = 0.00000000000000000000000000000000001
60 two# = 2
70 PRINT "--- Algorithm 1: Gauss-Legendre iteration ---"
80 GOSUB 1000
90 PRINT "Gauss-Legendre result (32 digits): ", LEFT$(STR$(gl#), 33)
100 PRINT ""
110 PRINT "--- Algorithm 2: Machin's formula ---"
120 GOSUB 2000
130 PRINT "Machin result (32 digits): ", LEFT$(STR$(mc#), 33)
140 PRINT ""
150 PRINT "--- Checking both results ---"
155 REM Decimal literals are read as REAL (double) first, so only the
156 REM 32-digit string of each result is compared, not a literal value.
160 expect$ = "3.1415926535897932384626433832795"
170 s1$ = LEFT$(STR$(gl#), 33)
180 IF s1$ != expect$ THEN 9000
190 PRINT "Gauss-Legendre matches the 32-digit value: OK"
200 s2$ = LEFT$(STR$(mc#), 33)
210 IF s2$ != expect$ THEN 9000
220 PRINT "Machin matches the 32-digit value: OK"
240 PRINT ""
250 PRINT "=== All PI (BCD) tests PASSED ==="
260 END
1000 REM ------------------------------------------------------------------
1001 REM Subroutine: Gauss-Legendre iteration. Result in gl#.
1002 REM   a0 = 1, b0 = 1/SQR(2), t0 = 1/4, p0 = 1
1003 REM   each step: a' = (a+b)/2, b' = SQR(a*b),
1004 REM              t' = t - p*(a-a')^2, p' = 2p
1005 REM   estimate:  pi = (a+b)^2 / (4t)
1006 REM ------------------------------------------------------------------
1010 PRINT "Start values: a = 1, b = 1/SQR(2), t = 0.25, p = 1"
1020 a# = 1
1030 b# = 1 / SQR(two#)
1040 t# = 0.25
1050 p# = 1
1060 prev# = 0
1070 conv# = 1
1080 it% = 0
1090 WHILE conv# >= tol# AND it% < 10
1100   it% = it% + 1
1110   an# = (a# + b#) / 2
1120   bn# = SQR(a# * b#)
1130   d# = a# - an#
1140   t# = t# - p# * d# * d#
1150   p# = p# * 2
1160   a# = an#
1170   b# = bn#
1180   gl# = (a# + b#) * (a# + b#) / (4 * t#)
1190   conv# = ABS(gl# - prev#)
1200   prev# = gl#
1210   PRINT "Gauss-Legendre iteration ", it%, ": pi = ", STR$(gl#)
1220   PRINT "    change since last iteration = ", STR$(conv#)
1230 END-WHILE
1240 PRINT "Gauss-Legendre finished after ", it%, " iterations"
1250 RETURN
2000 REM ------------------------------------------------------------------
2001 REM Subroutine: Machin's formula, pi = 16*atan(1/5) - 4*atan(1/239).
2002 REM Result in mc#. Uses the series in subroutine 3000.
2010 PRINT "Machin: pi = 16 * arctan(1/5) - 4 * arctan(1/239)"
2020 xv# = 5
2030 GOSUB 3000
2040 a5# = atn#
2050 PRINT "arctan(1/5) finished: ", STR$(a5#)
2060 xv# = 239
2070 GOSUB 3000
2080 a239# = atn#
2090 PRINT "arctan(1/239) finished: ", STR$(a239#)
2100 mc# = 16 * a5# - 4 * a239#
2110 RETURN
3000 REM ------------------------------------------------------------------
3001 REM Subroutine: arctan(1/xv#) by its series
3002 REM   atan(1/x) = 1/x - 1/(3x^3) + 1/(5x^5) - ...
3003 REM Input: xv#. Result: atn#. Stops when a term is below tol#.
3004 REM ------------------------------------------------------------------
3010 pw# = 1 / xv#
3020 x2# = xv# * xv#
3030 atn# = 0
3040 sgn# = 1
3050 k% = 0
3060 term# = 1
3070 WHILE ABS(term#) >= tol#
3080   term# = pw# / (2 * k% + 1)
3090   atn# = atn# + sgn# * term#
3100   sgn# = 0 - sgn#
3110   pw# = pw# / x2#
3120   PRINT "  arctan(1/", xv#, ") term ", k%, ": sum = ", STR$(atn#)
3130   k% = k% + 1
3140 END-WHILE
3150 RETURN
9000 PRINT "ERROR: Test failed!"
9010 END
