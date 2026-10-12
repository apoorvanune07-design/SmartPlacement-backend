@echo off
echo ============================================================
echo Starting Smart Placement & Internship Management System Backend
echo ============================================================

REM Configure JAVA_HOME if not set
if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    )
)

REM Add Java & Maven to PATH if needed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Program Files (x86)\apache-maven-3.9.16\bin" (
        set "PATH=C:\Program Files (x86)\apache-maven-3.9.16\bin;%JAVA_HOME%\bin;%PATH%"
    ) else if defined maven_path (
        set "PATH=%maven_path%;%JAVA_HOME%\bin;%PATH%"
    )
)

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Using Maven to run Spring Boot application...
    mvn spring-boot:run
) else (
    echo Maven command 'mvn' not found in PATH.
    echo Please ensure Java 17+ and Maven are installed.
    pause
)
