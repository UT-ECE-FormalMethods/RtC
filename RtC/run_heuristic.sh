#!/bin/bash

# defining the Java executable path
JAVA_EXEC="C:\Program Files\Java\jdk-19\bin\java.exe"

# defining the classpath that appears in IntelliJ IDEA run config
CLASSPATH="E:\university\Research\Formal-Methods-Lab\RtC-git\RtC\RtC\target\classes;C:\Users\LENOVO\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.16.1\jackson-databind-2.16.1.jar;C:\Users\LENOVO\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.16.1\jackson-annotations-2.16.1.jar;C:\Users\LENOVO\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.16.1\jackson-core-2.16.1.jar"

# defining the main class to run
MAIN_CLASS="org.example.Main"

# executing the program for heuristicType values from 1 to 7
for i in {0..8}
do
    "$JAVA_EXEC" -classpath "$CLASSPATH" "$MAIN_CLASS" "$i"
    echo "Execution with heuristicType=$i completed."
done

echo "All executions completed."
