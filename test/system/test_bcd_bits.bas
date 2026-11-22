10 REM Test: shift operands are integers (owner decision, 2026-11-22). A BCD value with a fraction is not a whole number.
20 REM Negative test: the program must stop with an error when a fractional BCD value is shifted.
30 PRINT "=== Testing that a fractional BCD shift is rejected ==="
40 a# = 3.9
50 b# = a# << 2
60 PRINT "ERROR: Test failed - the shift of a BCD value was not rejected"
70 END
