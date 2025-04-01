@echo off

REM Setting up variables
set JAVAFX_PATH=C:\path\to\javafx-sdk
set SRC_DIR=src
set OUTPUT_DIR=bin
set MAIN_CLASS=edu.attendance.AttendanceApplication

REM Create output directory if it doesn't exist
if not exist %OUTPUT_DIR% mkdir %OUTPUT_DIR%

echo Compiling Java files...
REM Compile Java files
javac --module-path %JAVAFX_PATH%\lib ^
      --add-modules javafx.controls,javafx.fxml,javafx.media ^
      -d %OUTPUT_DIR% ^
      %SRC_DIR%\edu\attendance\*.java ^
      %SRC_DIR%\edu\attendance\model\*.java ^
      %SRC_DIR%\edu\attendance\controller\*.java ^
      %SRC_DIR%\edu\attendance\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed
    exit /b 1
)

echo Copying resources...
REM Copy resources
if not exist %OUTPUT_DIR%\resources mkdir %OUTPUT_DIR%\resources
xcopy /E /Y %SRC_DIR%\resources %OUTPUT_DIR%\resources\

echo Running application...
REM Run the application
java --module-path %JAVAFX_PATH%\lib ^
     --add-modules javafx.controls,javafx.fxml,javafx.media ^
     -cp %OUTPUT_DIR% %MAIN_CLASS%

echo Done
