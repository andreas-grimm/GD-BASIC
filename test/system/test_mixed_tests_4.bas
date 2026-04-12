10 rem Growing testfile to develop the basic parser
20 print "Testing an empty line"

30
40 print "Testing: REM and Comment '"
50 rem Testing the REM command
60 ' Testing the Comment tick
100 print "Testing: Assignment of a variable"
110 print "Result of 1 + 2 = ";
120 a# = 1 + 2
130 print a#
140 print "Result of 1 + 2 * 3 = ";
150 a# = 1 + 2 * 3
160 print a#
170 print "Result of 1 + 2 * 3 / 4 = ";
180 a# = 1 + 2 * 3 / 4
190 print a#
200 print "Result of 1 + 2 * 3 / 4 ^ 5 = ";
210 a# = a# ^ 5
220 print a#
230 b# = a# * 2
240 print a#, " x 2 = ", b#;
250 b# = b# * 2
260 print " x2 = ", b#
300 rem Test Case 3: Testing the GOTO
310 print "Testing of the GOTO command"
320 goto 340
330 print "This line should not be printed"
340 print "This line should be printed"
400 rem Test Case 4: Testing the IF
410 print "Testing of the IF command"
420 c# = 2
430 if c# == 3 then
440 print "This line should not be printed"
450 end-if
460 print "Verify this is the first line of the IF statement printed..."
470 if c# == 2 then
480 print "This line should be printed"
490 end-if
999 end
