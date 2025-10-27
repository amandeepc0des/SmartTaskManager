@echo off
echo Building SmartTaskManager...

REM Clean and create directories
if exist out rmdir /s /q out
mkdir out

REM Compile Java files
javac -d out src/com/taskmanager/*.java src/com/taskmanager/model/*.java src/com/taskmanager/model/enums/*.java src/com/taskmanager/service/*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    exit /b 1
)

REM Create JAR file
jar cfe SmartTaskManager.jar com.taskmanager.Main -C out .

if %errorlevel% neq 0 (
    echo JAR creation failed!
    exit /b 1
)

echo Build successful! SmartTaskManager.jar created.
echo.
echo To run: java -jar SmartTaskManager.jar