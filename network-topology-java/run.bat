@echo off
REM Network Topology Fuseki Setup Script for Windows
REM Makes it easy to start different components

echo === Network Topology Fuseki Setup ===
echo.

if "%1"=="" goto usage
if "%1"=="server" goto server
if "%1"=="client" goto client  
if "%1"=="main" goto main
if "%1"=="compile" goto compile
if "%1"=="clean" goto clean
goto usage

:usage
echo Usage: %0 [command]
echo.
echo Commands:
echo   server    - Start Fuseki SPARQL endpoint server
echo   client    - Run client demo (requires server to be running)
echo   main      - Run full application with all demos
echo   compile   - Compile the project
echo   clean     - Clean and compile
echo.
echo Examples:
echo   %0 server           # Start Fuseki server on port 3030
echo   %0 client           # Test client connectivity
echo   %0 main             # Run complete demo
echo.
goto end

:compile
echo 🔨 Compiling project...
mvn compile
if %errorlevel% equ 0 (
    echo ✅ Compilation successful!
) else (
    echo ❌ Compilation failed!
    exit /b 1
)
goto end

:clean
echo 🧹 Cleaning and compiling project...
mvn clean compile
if %errorlevel% equ 0 (
    echo ✅ Clean compilation successful!
) else (
    echo ❌ Clean compilation failed!
    exit /b 1
)
goto end

:server
call :compile
if %errorlevel% neq 0 goto end
echo 🚀 Starting Fuseki SPARQL endpoint server...
echo 📡 Server will be available at: http://localhost:3030
echo 🔍 SPARQL endpoint: http://localhost:3030/network-topology/sparql
echo 💾 Data will be persisted in tdb-data\ directory
echo.
echo Press Ctrl+C to stop the server
echo.
mvn exec:java "-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher"
goto end

:client
call :compile
if %errorlevel% neq 0 goto end
echo 🔗 Running client demo...
echo 📡 Connecting to: http://localhost:3030/network-topology/sparql
echo ⚠️  Make sure the Fuseki server is running first!
echo.
mvn exec:java "-Dexec.mainClass=com.example.networktopology.client.NetworkTopologyClient"
goto end

:main
call :compile
if %errorlevel% neq 0 goto end
echo 🏃 Running complete application demo...
echo 📊 This includes all components: loading, querying, reasoning, and Fuseki demo
echo.
mvn exec:java "-Dexec.mainClass=com.example.networktopology.NetworkTopologyLoader"
goto end

:end
