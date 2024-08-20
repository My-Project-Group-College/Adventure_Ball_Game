@echo off
echo Running the project...
echo.
echo.
echo java -cp "Classes;resources" main.MainClass
echo.
echo.
java -cp "Classes;resources" main.MainClass

if %ERRORLEVEL% NEQ 0 (
	echo.
	echo.
	echo.
	echo.
    echo Failed to run the project.
	timeout /t 10 /nobreak >nul
    exit /b 1
)
echo.
echo.
echo.
echo Thanks For Playing.
echo Closing Now...
timeout /t 5 /nobreak >nul
@echo on
