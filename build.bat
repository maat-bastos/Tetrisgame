@echo off
REM Compile the Java files
javac -d bin -sourcepath src src/com/tetris/Main.java

REM Run the application
java -cp bin com.tetris.Main