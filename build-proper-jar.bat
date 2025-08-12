@echo off
echo Building RawInput mod for Minecraft 1.7.10 - Version 1.4.3...

REM Create build directories
mkdir build\classes 2>nul
mkdir build\libs 2>nul

REM Clean previous builds
del /Q build\classes\* 2>nul
del /Q build\libs\*.jar 2>nul

REM Copy and compile Java files
echo Compiling Java source files...
javac -encoding UTF-8 -d build\classes src\main\java\mod\seanld\rawinput\reflect\Fields.java
javac -encoding UTF-8 -d build\classes src\main\java\mod\seanld\rawinput\RawMouseHelper.java
javac -encoding UTF-8 -d build\classes src\main\java\mod\seanld\rawinput\commands\RescanCommand.java
javac -encoding UTF-8 -d build\classes src\main\java\mod\seanld\rawinput\commands\ToggleCommand.java
javac -encoding UTF-8 -d build\classes src\main\java\mod\seanld\rawinput\keybinds\KeybindHandler.java
javac -encoding UTF-8 -d build\classes src\main\java\mod\seanld\rawinput\RawInputHandler.java
javac -encoding UTF-8 -d build\classes src\main\java\mod\seanld\rawinput\RawInput.java

REM Copy resources
echo Copying resources...
xcopy /E /I /Y src\main\resources build\classes 2>nul

REM Create proper jar file
echo Creating proper jar file with compiled classes...
cd build\classes
jar -cf ..\libs\RawInput-1.7.10-1.4.3.jar *

echo Build completed! 
echo Jar file created: build/libs/RawInput-1.7.10-1.4.3.jar
cd ..\..

REM Show file info
dir build\libs

REM Show jar contents
echo.
echo Jar contents:
jar -tf build\libs\RawInput-1.7.10-1.4.3.jar