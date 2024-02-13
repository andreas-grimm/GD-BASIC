1000 REM SIEVE OF ERATOSTHENES 
1010 REM MODIFED FROM QUITE BASIC MATH PROJECT DEMO
1020 REM 
2010 REM L IS THE LIMIT OF THE SIEVE
2020 REM WE WILL FIND ALL PRIME NUMBERS UP TO L
2030 L# = 1000
2040 REM N IS THE SIEVE ITSELF
2050 REM DIM N(1000)
2060 REM FILL THE SIEVE WITH ALL NUMBERS UP TO L
2070 FOR I# = 1 TO L#
2080 N#(I#) = I#
2090 NEXT
2100 REM START WITH THE FIRST PRIME NUMBER: 2
2110 P# = 2
2120 PRINT P#, " ";
2130 REM "CROSS OUT" MULTIPLES OF P
2140 FOR I# = P# TO L# STEP P#
2150 N#(I#) = 0
2160 NEXT
2170 REM FIND THE NEXT NUMBER NOT CROSSED OUT
2180 P# = P# + 1
2190 IF P# == L# THEN 2220
2200 IF N#(P#) != 0 THEN 2120
2210 GOTO 2180
2220 PRINT
2230 END