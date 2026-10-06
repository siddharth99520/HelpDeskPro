@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script for Windows
@REM
@REM Automatically downloads and installs Apache Maven if not found locally.
@REM ----------------------------------------------------------------------------

@echo off
setlocal

set "MAVEN_VERSION=3.9.6"
set "MAVEN_HOME=%~dp0.mvn\apache-maven-%MAVEN_VERSION%"

@REM Check if Maven is already on PATH
where mvn >nul 2>&1
if %ERRORLEVEL% equ 0 (
    mvn %*
    goto end
)

@REM Check if we have a local Maven installation
if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    "%MAVEN_HOME%\bin\mvn.cmd" %*
    goto end
)

@REM Download and install Maven locally
echo Maven not found. Downloading Apache Maven %MAVEN_VERSION%...
if not exist "%~dp0.mvn" mkdir "%~dp0.mvn"

set "DOWNLOAD_URL=https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip"
set "ZIP_FILE=%~dp0.mvn\maven.zip"

powershell -Command "Write-Host 'Downloading from %DOWNLOAD_URL%...'; [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '%ZIP_FILE%'"
if %ERRORLEVEL% neq 0 (
    echo ERROR: Failed to download Maven. Please install Maven manually.
    exit /b 1
)

echo Extracting Maven...
powershell -Command "Expand-Archive -Path '%ZIP_FILE%' -DestinationPath '%~dp0.mvn' -Force"
if %ERRORLEVEL% neq 0 (
    echo ERROR: Failed to extract Maven.
    exit /b 1
)

del "%ZIP_FILE%"
echo Maven %MAVEN_VERSION% installed to %MAVEN_HOME%

"%MAVEN_HOME%\bin\mvn.cmd" %*

:end
endlocal
