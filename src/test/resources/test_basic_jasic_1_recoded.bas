10 rem Test File - Do Not Change This File Without Changing JUnit
20 count = 5
30 count = count * 2
40 rem stop looping if we are done
50 if count = 0 then
60 goto 120
70 end-if
80 print "Hello, world!"
90 rem decrement and restart the loop
100 count = count - 1
110 goto 50
120 end