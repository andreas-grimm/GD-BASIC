10 REM Test ON GOTO statement - computed branch based on integer expression
20 PRINT "Testing ON GOTO statement"
30 GOSUB 100
40 GOSUB 200
50 GOSUB 300
60 GOSUB 400
70 PRINT "All tests passed"
80 GOTO 9999

100 REM Test basic ON GOTO with valid indices
110 X% = 1
120 ON X% GOTO 1100, 1200, 1300
130 PRINT "ERROR: Should have jumped to 1100"
140 GOTO 9000
1100 PRINT "  Test 1: ON 1 GOTO -> Correct destination (1100)"
1110 RETURN

200 REM Test ON GOTO with index 2
210 X% = 2
220 ON X% GOTO 2100, 2200, 2300
230 PRINT "ERROR: Should have jumped to 2200"
240 GOTO 9000
2100 PRINT "ERROR: Went to 2100 instead of 2200"
2110 GOTO 9000
2200 PRINT "  Test 2: ON 2 GOTO -> Correct destination (2200)"
2210 RETURN

300 REM Test ON GOTO with out-of-range index (no jump)
310 X% = 5
320 ON X% GOTO 3100, 3200, 3300
330 PRINT "  Test 3: ON 5 GOTO -> Out-of-range, no jump (correct)"
340 RETURN

400 REM Test ON GOTO with expression
410 X% = 1
420 Y% = 1
430 ON X% + Y% GOTO 4100, 4200, 4300
440 PRINT "ERROR: Should have jumped to 4200"
450 GOTO 9000
4100 PRINT "ERROR: Went to 4100"
4110 GOTO 9000
4200 PRINT "  Test 4: ON expression GOTO -> Correct destination"
4210 RETURN
4300 PRINT "ERROR: Went to 4300"
4310 GOTO 9000

9000 PRINT "TEST FAILED"
9999 END