      ' Test File - Do Not Change This File Without Changing JUnit
      count = 5

      count = count * 2

      ' stop looping if we're done
top:  if count = 0 then end
      print "Hello, world!"
      
      ' decrement and restart the loop
      count = count - 1
      goto top
end: