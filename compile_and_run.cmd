@echo off
echo Compiling Java Files...
echo.
echo.
echo mkdir Classes
echo.
mkdir Classes
echo.
echo.
echo.
for /D %%d in (src\*) do (
    echo Compiling %%d\*.java...
	echo.
	echo javac -d Classes -sourcepath src -cp resources %%d\*.java
    javac -d Classes -sourcepath src -cp resources %%d\*.java
	echo.
	echo.
)


if %ERRORLEVEL% NEQ 0 (
	echo.
	echo.
	echo.
	echo.
    echo Compilation failed.
	timeout /t 10 /nobreak >nul
    exit /b 1
)

echo.
echo.
echo.
echo Compilation successful.
echo.
echo.
timeout /t 3 /nobreak >nul
call run_project.cmd
@echo on

