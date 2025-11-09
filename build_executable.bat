@echo off
echo Compilando o jogo...
if not exist bin mkdir bin
"C:\Program Files\Java\jdk-17\bin\javac" -encoding UTF-8 -d bin -cp "lib/*" src/com/tetris/Main.java src/com/tetris/controller/*.java src/com/tetris/db/*.java src/com/tetris/model/*.java src/com/tetris/view/*.java

echo Criando o executavel...
cd bin
"C:\Program Files\Java\jdk-17\bin\jar" cfm ../Tetris.jar ../manifest.txt com/tetris/*.class com/tetris/*/*.class com/tetris/view/resources/*
cd ..

echo Copiando recursos...
if not exist dist mkdir dist
if not exist dist\com\tetris\view\resources mkdir dist\com\tetris\view\resources
copy Tetris.jar dist\
if not exist dist\lib mkdir dist\lib
copy lib\*.jar dist\lib\
xcopy /s /y bin\com\tetris\view\resources\*.* dist\com\tetris\view\resources\

echo Executavel criado com sucesso em dist\Tetris.jar