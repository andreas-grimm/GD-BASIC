1 REM ========================================================================
2 REM Compiler test: file input and output, and the file functions
3 REM Description: FOPEN, FPRINT, FCLOSE, FINPUT, FGET, FPEEK, FREWIND, FCOPY, FRENAME, FDELETE,
4 REM              FEXISTS, EOF, FISOPEN, FGETSIZE, FLINECOUNT. The files are removed at the end.
5 REM              The compiled program must print exactly what the interpreter prints.
6 REM ========================================================================
10 FOPEN 1 "compiler_io_test.txt" "write"
20 FPRINT 1 "First line"
30 FPRINT 1, "Second line"
40 FCLOSE 1 ""
50 PRINT FEXISTS("compiler_io_test.txt")
60 FOPEN 2 "compiler_io_test.txt" "read"
70 FINPUT 2, L$
80 PRINT L$
90 FINPUT 2 L2$
100 PRINT L2$
110 PRINT EOF(2)
120 FREWIND 2
130 FGET 2, C$
140 PRINT C$
150 FPEEK 2, P$
160 PRINT P$
170 PRINT FISOPEN(2)
180 PRINT FGETSIZE(2)
190 PRINT FLINECOUNT(2)
200 FCLOSE 2 ""
210 FOPEN 3 "compiler_io_copy.txt" "write"
220 FOPEN 4 "compiler_io_test.txt" "read"
230 FCOPY 4, 3
240 FCLOSE 4 ""
250 FCLOSE 3 ""
260 PRINT FEXISTS("compiler_io_copy.txt")
270 FOPEN 5 "compiler_io_copy.txt" "read"
280 FINPUT 5, C$
290 PRINT C$
300 FCLOSE 5 "DELETE"
310 FOPEN 6 "compiler_io_r1.txt" "write"
320 FPRINT 6 "rename me"
330 FRENAME 6, "compiler_io_r2.txt"
340 FCLOSE 6 ""
350 PRINT FEXISTS("compiler_io_r2.txt")
360 FOPEN 7 "compiler_io_r2.txt" "read"
370 FCLOSE 7 "DELETE"
380 FOPEN 8 "compiler_io_test.txt" "read"
390 FCLOSE 8 "DELETE"
400 PRINT FEXISTS("compiler_io_test.txt")
410 END
