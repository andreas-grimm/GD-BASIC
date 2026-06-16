1 REM ========================================================================
2 REM Test: String Functions
3 REM Description: Tests all string functions (ASC, CHR, INSTR, LEFT, LEN, MID, RIGHT, STR, VAL)
4 REM Expected: All string functions should work correctly
5 REM ========================================================================
10 PRINT "=== Testing String Functions ==="
20 PRINT ""
30 REM Test LEN function
40 PRINT "Testing LEN (String Length):"
50 str1$ = "Hello"
60 len1% = LEN(str1$)
70 PRINT "LEN('Hello') = ";
80 PRINT len1%
90 IF len1% != 5 THEN 9000
100 REM Test LEFT function
110 PRINT "Testing LEFT$ (Left Substring):"
120 str2$ = "BASIC"
130 left1$ = LEFT(str2$, 3)
140 PRINT "LEFT$('BASIC', 3) = ";
150 PRINT left1$
160 REM Test RIGHT function
170 PRINT "Testing RIGHT$ (Right Substring):"
180 str3$ = "BASIC"
190 right1$ = RIGHT(str3$, 2)
200 PRINT "RIGHT$('BASIC', 2) = ";
210 PRINT right1$
220 REM Test MID function
230 PRINT "Testing MID$ (Middle Substring):"
240 str4$ = "PROGRAMMING"
250 mid1$ = MID(str4$, 4, 4)
260 PRINT "MID$('PROGRAMMING', 4, 4) = ";
270 PRINT mid1$
280 REM Test ASC function
290 PRINT "Testing ASC (ASCII Value):"
300 asc1% = ASC("A")
310 PRINT "ASC('A') = ";
320 PRINT asc1%
330 IF asc1% != 65 THEN 9000
340 asc2% = ASC("a")
350 PRINT "ASC('a') = ";
360 PRINT asc2%
370 IF asc2% != 97 THEN 9000
380 REM Test CHR function
390 PRINT "Testing CHR$ (Character from ASCII):"
400 chr1$ = CHR(65)
410 PRINT "CHR$(65) = ";
420 PRINT chr1$
430 chr2$ = CHR(97)
440 PRINT "CHR$(97) = ";
450 PRINT chr2$
460 REM Test STR function
470 PRINT "Testing STR$ (Number to String):"
480 num1% = 42
490 str5$ = STR(num1%)
500 PRINT "STR$(42) = '";
510 PRINT str5$;
520 PRINT "'"
530 REM Test VAL function
540 PRINT "Testing VAL (String to Number):"
550 str6$ = "123"
560 val1% = VAL(str6$)
570 PRINT "VAL('123') = ";
580 PRINT val1%
590 IF val1% != 123 THEN 9000
600 str7$ = "3.14"
610 val2 = VAL(str7$)
620 PRINT "VAL('3.14') = ";
630 PRINT val2
640 REM Test INSTR function
650 PRINT "Testing INSTR (Find Substring):"
660 str8$ = "Hello World"
670 pos1% = INSTR(str8$, "World")
680 PRINT "INSTR('Hello World', 'World') = ";
690 PRINT pos1%
700 IF pos1% != 7 THEN 9000
710 pos2% = INSTR(str8$, "xyz")
720 PRINT "INSTR('Hello World', 'xyz') = ";
730 PRINT pos2%
740 IF pos2% != 0 THEN 9000
750 PRINT ""
760 PRINT "=== All String Function Tests PASSED ==="
770 END
9000 PRINT "ERROR: Test failed!"
9010 END
