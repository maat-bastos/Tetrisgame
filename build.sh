#!/bin/bash
# Compile the Java files
javac -d bin -sourcepath src src/com/tetris/Main.java

# Run the application
java -cp bin com.tetris.Main