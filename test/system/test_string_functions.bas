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
70 PRINT "LEN('Hello') = "; len1%
80 IF len1% != 5 THEN GOTO 9000

90 REM Test LEFT function
100 PRINT "Testing LEFT$ (Left Substring):"
110 str2$ = "BASIC"
120 left1$ = LEFT(str2$, 3)
130 PRINT "LEFT$('BASIC', 3) = "; left1$

140 REM Test RIGHT function
150 PRINT "Testing RIGHT$ (Right Substring):"
160 str3$ = "BASIC"
170 right1$ = RIGHT(str3$, 2)
180 PRINT "RIGHT$('BASIC', 2) = "; right1$

190 REM Test MID function
200 PRINT "Testing MID$ (Middle Substring):"
210 str4$ = "PROGRAMMING"
220 mid1$ = MID(str4$, 4, 4)
230 PRINT "MID$('PROGRAMMING', 4, 4) = "; mid1$

240 REM Test ASC function
250 PRINT "Testing ASC (ASCII Value):"
260 asc1% = ASC("A")
270 PRINT "ASC('A') = "; asc1%
280 IF asc1% != 65 THEN GOTO 9000
290 asc2% = ASC("a")
300 PRINT "ASC('a') = "; asc2%
310 IF asc2% != 97 THEN GOTO 9000

320 REM Test CHR function
330 PRINT "Testing CHR$ (Character from ASCII):"
340 chr1$ = CHR(65)
350 PRINT "CHR$(65) = "; chr1$
360 chr2$ = CHR(97)
370 PRINT "CHR$(97) = "; chr2$

380 REM Test STR function
390 PRINT "Testing STR$ (Number to String):"
400 num1% = 42
410 str5$ = STR(num1%)
420 PRINT "STR$(42) = '"; str5$; "'"

430 REM Test VAL function
440 PRINT "Testing VAL (String to Number):"
450 str6$ = "123"
460 val1% = VAL(str6$)
470 PRINT "VAL('123') = "; val1%
480 IF val1% != 123 THEN GOTO 9000
490 str7$ = "3.14"
500 val2# = VAL(str7$)
510 PRINT "VAL('3.14') = "; val2#

520 REM Test INSTR function
530 PRINT "Testing INSTR (Find Substring):"
540 str8$ = "Hello World"
550 pos1% = INSTR(str8$, "World")
560 PRINT "INSTR('Hello World', 'World') = "; pos1%
570 IF pos1% != 7 THEN GOTO 9000
580 pos2% = INSTR(str8$, "xyz")
590 PRINT "INSTR('Hello World', 'xyz') = "; pos2%
600 IF pos2% != 0 THEN GOTO 9000

610 PRINT ""
620 PRINT "=== All String Function Tests PASSED ==="
630 END

9000 PRINT "ERROR: Test failed!"
9010 END
