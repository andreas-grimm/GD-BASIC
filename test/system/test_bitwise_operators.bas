10 REM Test: Bitwise Operators
20 REM Description: Tests bitwise shift operators (<<, >>) on INTEGER values.
30 REM Shifts are defined for INTEGER values only. Number literals are REAL, so the operands are INTEGER variables.
40 REM Rewritten in Phase 7 (the old version shifted REAL literals, which is not defined).
50 PRINT "=== Testing Bitwise Operators ==="
60 PRINT ""
70 PRINT "Testing Left Shift (<<):"
80 four% = 4
90 one% = 1
100 a% = four% << one%
110 PRINT "4 << 1 = ";
120 PRINT a%
130 IF a% != 8 THEN 9000
140 three% = 3
150 two% = 2
160 b% = three% << two%
170 PRINT "3 << 2 = ";
180 PRINT b%
190 IF b% != 12 THEN 9000
200 PRINT "Testing Right Shift (>>):"
210 sixteen% = 16
220 c% = sixteen% >> one%
230 PRINT "16 >> 1 = ";
240 PRINT c%
250 IF c% != 8 THEN 9000
260 twenty% = 20
270 d% = twenty% >> two%
280 PRINT "20 >> 2 = ";
290 PRINT d%
300 IF d% != 5 THEN 9000
310 PRINT ""
311 REM Whole-number literals are REAL in the source but are integers in a shift (owner decision, 2026-11-22)
312 e% = 1 << 3
313 PRINT "1 << 3 (literals) = ";
314 PRINT e%
315 IF e% != 8 THEN 9000
316 f% = 256 >> 4
317 PRINT "256 >> 4 (literals) = ";
318 PRINT f%
319 IF f% != 16 THEN 9000
320 PRINT "=== All Bitwise Operator Tests PASSED ==="
330 END
9000 PRINT "ERROR: Test failed!"
9010 END
