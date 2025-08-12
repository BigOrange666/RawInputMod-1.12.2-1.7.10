@echo off
echo Building RawInput mod for Minecraft 1.7.10 - Version 1.4.3...

REM Create build directories
mkdir build\temp 2>nul
mkdir build\libs 2>nul

REM Clean previous builds
del /Q build\temp\* 2>nul
del /Q build\libs\*.jar 2>nul

REM Copy source files to temp directory
echo Copying source files...
xcopy /E /I /Y src\main\java build\temp
xcopy /E /I /Y src\main\resources build\temp

REM Create jar file with source files (this is what we can do without proper Forge environment)
echo Creating jar file with source files...
cd build\temp
jar -cf ..\libs\RawInput-1.7.10-1.4.3-sources.jar *

echo Build completed! 
echo Source jar file created: build/libs/RawInput-1.7.10-1.4.3-sources.jar
cd ..\..

REM Show file info
dir build\libs

echo.
echo Note: This is a source jar file. For a compiled mod jar, you would need:
echo 1. Proper Minecraft Forge development environment
echo 2. All Minecraft and Forge dependencies
echo 3. Correct compilation with javac -cp "libs/*" ...
echo.
echo For now, this source jar can be used to:
echo - View the modified source code
echo - Import into an IDE for further development
echo - Reference the Java 17+ compatibility changes