1 REM ========================================================================
2 REM Compiler test: directories (MKDIR, RMDIR, DIREXISTS, GETCWD)
3 REM Description: Creates and removes directories, and writes a file inside one of them.
4 REM              GETCWD returns the directory set by CHDIR (empty until CHDIR is used).
5 REM              The directories are removed at the end.
6 REM              The compiled program must print exactly what the interpreter prints.
7 REM ========================================================================
10 MKDIR "compiler_io_dir"
20 PRINT DIREXISTS("compiler_io_dir")
30 RMDIR "compiler_io_dir"
40 PRINT DIREXISTS("compiler_io_dir")
50 MKDIR "compiler_io_dir2"
60 FOPEN 1 "compiler_io_dir2/inside.txt" "write"
70 FPRINT 1 "inside"
80 FCLOSE 1 "DELETE"
90 RMDIR "compiler_io_dir2", TRUE
100 PRINT DIREXISTS("compiler_io_dir2")
105 CHDIR "."
110 PRINT LEN(GETCWD()) > 0
120 END
