10 REM Test: block IF / ELSE / END-IF, single-line IF, and nested block IF
20 PRINT "=== Testing block IF-ELSE-END-IF ==="
30 errors% = 0
40 x% = 7
50 IF x% > 5 THEN
60   result% = 1
70 ELSE
80   result% = 2
90 END-IF
100 IF result% != 1 THEN errors% = errors% + 1
110 x% = 1
120 IF x% > 5 THEN
130   result% = 1
140 ELSE
150   result% = 2
160 END-IF
170 IF result% != 2 THEN errors% = errors% + 1
180 REM Block IF without ELSE: a false condition skips the block
190 result% = 0
200 IF x% > 5 THEN
210   result% = 9
220 END-IF
230 IF result% != 0 THEN errors% = errors% + 1
240 REM Nested block IF inside a true branch
250 x% = 7
260 y% = 3
270 IF x% > 5 THEN
280   IF y% > 5 THEN
290     result% = 1
300   ELSE
310     result% = 2
320   END-IF
330 ELSE
340   result% = 3
350 END-IF
360 IF result% != 2 THEN errors% = errors% + 1
370 REM Single-line IF with a line number target
380 result% = 0
390 IF x% > 5 THEN 410
400 result% = 5
410 IF result% != 0 THEN errors% = errors% + 1
420 PRINT "Errors found: ";
430 PRINT errors%
440 IF errors% != 0 THEN 9000
450 PRINT "=== All block IF tests PASSED ==="
460 END
9000 PRINT "ERROR: Test failed!"
9010 END
