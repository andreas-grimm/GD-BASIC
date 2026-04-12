10 REM ========================================================================
20 REM Test: File I/O
30 REM Description: This test validates FOPEN, FPRINT, FINPUT, FCLOSE, and EOF.
40 REM Expected: Files are created, written, read back, and closed/deleted.
50 REM ========================================================================
60 PRINT "=== Testing File I/O ==="
70 PRINT ""
80 FILE$ = "test_io.txt"
90 FILE_ID = 1
100
110 PRINT "Testing FOPEN for writing..."
120 FOPEN 1 "test_io.txt" "write"
130
140 PRINT "Testing FPRINT to write data..."
150 FPRINT 1 "Line 1: Hello World"
160 FPRINT 1 "Line 2: 12345"
170 FPRINT 1 "Line 3: Last Line"
180
190 PRINT "Testing FCLOSE..."
200 FCLOSE 1 "KEEP"
210
220 PRINT "Testing FOPEN for reading..."
230 FOPEN 1 "test_io.txt" "read"
240
250 PRINT "Testing FINPUT and EOF..."
260 FINPUT 1 L1$
270 IF L1$ != "Line 1: Hello World" THEN
271 PRINT "ERROR: Line 1 mismatch, found: <", L1$, ">"
272 GOTO 9000
273 END-IF
280 PRINT "Read Line 1: ", L1$
290
300 FINPUT 1 L2$
310 IF L2$ != "Line 2: 12345" THEN
311 PRINT "ERROR: Line 2 mismatch"
312 GOTO 9000
313 END-IF
320 PRINT "Read Line 2: ", L2$
330
340 FINPUT 1 L3$
350 IF L3$ != "Line 3: Last Line" THEN
351 PRINT "ERROR: Line 3 mismatch"
352 GOTO 9000
353 END-IF
360 PRINT "Read Line 3: ", L3$
370
380 PRINT "Testing EOF function (expecting 1)..."
390 REM Force a read to trigger EOF
400 FINPUT 1 L4$
410 ONE% = 1
411 IF EOF(1) != ONE% THEN
412 PRINT "ERROR: EOF not detected"
413 GOTO 9010
414 END-IF
420 PRINT "EOF reached successfully."
430
440 PRINT "Testing FCLOSE with DELETE..."
450 FCLOSE 1 "DELETE"
460
470 PRINT ""
480 PRINT "=== File I/O Tests PASSED ==="
490 END
500
9000 PRINT "ERROR: Data mismatch during read!"
9010 PRINT "ERROR: EOF not detected or reported incorrectly!"
9020 END
