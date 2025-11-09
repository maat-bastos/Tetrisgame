@echo off
echo Limpando diretorios anteriores...
if exist bin rmdir /s /q bin
if exist dist rmdir /s /q dist
mkdir bin
mkdir dist
mkdir dist\lib

echo Compilando o jogo...
"C:\Program Files\Java\jdk-17\bin\javac" -encoding UTF-8 -d bin -cp "lib/*" src/com/tetris/Main.java src/com/tetris/controller/*.java src/com/tetris/db/*.java src/com/tetris/model/*.java src/com/tetris/view/*.java

echo Copiando recursos...
xcopy /s /y src\com\tetris\view\resources\*.* bin\com\tetris\view\resources\
copy lib\*.jar dist\lib\

echo Criando o JAR...
cd bin
"C:\Program Files\Java\jdk-17\bin\jar" cfm ..\dist\Tetris.jar ..\manifest.txt com\tetris\*.class com\tetris\*\*.class com\tetris\view\resources\*.*
cd ..

echo Executavel criado com sucesso em dist\Tetris.jar
echo Para executar o jogo, va ate a pasta dist e execute: java -jar Tetris.jar