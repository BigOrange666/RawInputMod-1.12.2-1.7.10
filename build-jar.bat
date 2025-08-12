@echo off
echo Building RawInput mod for Minecraft 1.7.10 - Version 1.4.3...

REM Create build directories
mkdir build\classes 2>nul
mkdir build\libs 2>nul

REM Copy all source files
echo Copying source files...
xcopy /E /I /Y src\main\java build\classes
xcopy /E /I /Y src\main\resources build\classes

REM Create jar file with version 1.4.3
echo Creating jar file for Minecraft 1.7.10 - Version 1.4.3...
cd build\classes
jar -cf ..\libs\RawInput-1.7.10-1.4.3.jar *

echo Build completed! 
echo Jar file created: build/libs/RawInput-1.7.10-1.4.3.jar
cd ..\..

REM Show file info
dir build\libs