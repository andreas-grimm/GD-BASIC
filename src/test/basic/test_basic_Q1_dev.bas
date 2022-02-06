10 rem Growing testfile to develop the basic parser
20 print "Testing an empty line"

30
40 print "Testing: REM and Comment '"
50 rem Testing the REM command
60 ' Testing the Comment tick
110 print "Testing: Assignment of a variable"
120 print "Result of 1 + 2 * 3 / 4 ^ 5 = ";
130 a# = 1 + 2 * 3 / 4 ^ 5
140 print a#
150 b# = a# * 2
160 print a#, " x 2 = ", b#;
170 b# = b# * 2
180 print " x2 = ", b#
200 rem Test Case 3: Testing the GOTO
210 print "Testing of the GOTO command"
220 goto 240
230 print "This line should not be printed"
240 print "This line should be printed"
300 rem Test Case 4: Testing the IF
310 print "Testing of the IF command"
320 c# = 2
330 if c# == 3 then
340 print "This line should not be printed"
350 end-if
360 print "Verify this is the first line of the IF statement printed..."
370 if c# == 2 then
380 print "This line should be printed"
390 end-if
999 end
