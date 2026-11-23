10 REM Test: BCD overflow - more than 40 digits before the decimal point must stop with an error
20 B# = 9999999999999999999999999999999999999999
30 C# = B# * 10
40 PRINT C#
50 END
