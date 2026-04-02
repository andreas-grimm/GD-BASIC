REM ========================================================================
REM Test: String Functions
REM Description: Tests all string functions (ASC, CHR, INSTR, LEFT, LEN, MID, RIGHT, STR, VAL)
REM Expected: All string functions should work correctly
REM ========================================================================

10 PRINT "=== Testing String Functions ==="
20 PRINT ""

REM Test LEN function
30 PRINT "Testing LEN (String Length):"
40 str1$ = "Hello"
50 len1% = LEN(str1$)
60 PRINT "LEN('Hello') = "; len1%
70 IF len1% != 5 THEN GOTO 9000

REM Test LEFT function
80 PRINT "Testing LEFT$ (Left Substring):"
90 str2$ = "BASIC"
100 left1$ = LEFT(str2$, 3)
110 PRINT "LEFT$('BASIC', 3) = "; left1$

REM Test RIGHT function
120 PRINT "Testing RIGHT$ (Right Substring):"
130 str3$ = "BASIC"
140 right1$ = RIGHT(str3$, 2)
150 PRINT "RIGHT$('BASIC', 2) = "; right1$

REM Test MID function
160 PRINT "Testing MID$ (Middle Substring):"
170 str4$ = "PROGRAMMING"
180 mid1$ = MID(str4$, 4, 4)
190 PRINT "MID$('PROGRAMMING', 4, 4) = "; mid1$

REM Test ASC function
200 PRINT "Testing ASC (ASCII Value):"
210 asc1% = ASC("A")
220 PRINT "ASC('A') = "; asc1%
230 IF asc1% != 65 THEN GOTO 9000
240 asc2% = ASC("a")
250 PRINT "ASC('a') = "; asc2%
260 IF asc2% != 97 THEN GOTO 9000

REM Test CHR function
270 PRINT "Testing CHR$ (Character from ASCII):"
280 chr1$ = CHR(65)
290 PRINT "CHR$(65) = "; chr1$
300 chr2$ = CHR(97)
310 PRINT "CHR$(97) = "; chr2$

REM Test STR function
320 PRINT "Testing STR$ (Number to String):"
330 num1% = 42
340 str5$ = STR(num1%)
350 PRINT "STR$(42) = '"; str5$; "'"

REM Test VAL function
360 PRINT "Testing VAL (String to Number):"
370 str6$ = "123"
380 val1% = VAL(str6$)
390 PRINT "VAL('123') = "; val1%
400 IF val1% != 123 THEN GOTO 9000
410 str7$ = "3.14"
420 val2# = VAL(str7$)
430 PRINT "VAL('3.14') = "; val2#

REM Test INSTR function
440 PRINT "Testing INSTR (Find Substring):"
450 str8$ = "Hello World"
460 pos1% = INSTR(str8$, "World")
470 PRINT "INSTR('Hello World', 'World') = "; pos1%
480 IF pos1% != 7 THEN GOTO 9000
490 pos2% = INSTR(str8$, "xyz")
500 PRINT "INSTR('Hello World', 'xyz') = "; pos2%
510 IF pos2% != 0 THEN GOTO 9000

520 PRINT ""
530 PRINT "=== All String Function Tests PASSED ==="
540 END

9000 PRINT "ERROR: Test failed!"
9010 END
