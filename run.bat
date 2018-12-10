@echo off  
set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%
if "%OS%" == "Windows_NT" set PROGNAME=%~nx0%
set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_111"
set JAVAC_JAR=%JAVA_HOME%\lib\tools.jar
setlocal enabledelayedexpansion
set "JAVA=%JAVA_HOME%\bin\java"
set OPTS=-Xms512M -Xmx1024M  -XX:+AggressiveOpts -XX:+UseParallelGC -XX:NewSize=64M
set LIBPATH=.\lib
set CP=%DIRNAME%;
rem set MAIN=com.arcsoft.nosql.data.task.mixture.ClientFileOfCloudQuery
set MAIN=com.ford.cevdm.tcp.TcpServerTest
rem set MAIN= com.arcsoft.nosql.data.test.MigrateMain

for /f %%i in ('dir/b %LIBPATH%\*.jar^|sort') do (set CP=!CP!%LIBPATH%\%%i;)

echo ===============================================================================  
echo.  
echo   Engine Startup Environment  
echo.  
echo   JAVA: %JAVA%  
echo.  
echo   JAVA_OPTS: %OPTS%  
echo.  
echo   CLASSPATH: %CP%  
echo.  
echo ===============================================================================  
echo.  
  
java %OPTS% -classpath %CP% %MAIN% 
rem java %OPTS% -classpath %CP% %MAIN% arg1 arg2
rem pause
rem exit