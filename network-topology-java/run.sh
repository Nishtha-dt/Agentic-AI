#!/bin/bash

# Network Topology Fuseki Setup Script
# Makes it easy to start different components

echo "=== Network Topology Fuseki Setup ==="
echo

# Function to show usage
show_usage() {
    echo "Usage: $0 [command]"
    echo
    echo "Commands:"
    echo "  server    - Start Fuseki SPARQL endpoint server"
    echo "  client    - Run client demo (requires server to be running)"
    echo "  main      - Run full application with all demos"
    echo "  compile   - Compile the project"
    echo "  clean     - Clean and compile"
    echo
    echo "Examples:"
    echo "  $0 server           # Start Fuseki server on port 3030"
    echo "  $0 client           # Test client connectivity"
    echo "  $0 main             # Run complete demo"
    echo
}

# Function to compile project
compile_project() {
    echo "🔨 Compiling project..."
    mvn compile
    if [ $? -eq 0 ]; then
        echo "✅ Compilation successful!"
    else
        echo "❌ Compilation failed!"
        exit 1
    fi
}

# Function to clean and compile
clean_compile() {
    echo "🧹 Cleaning and compiling project..."
    mvn clean compile
    if [ $? -eq 0 ]; then
        echo "✅ Clean compilation successful!"
    else
        echo "❌ Clean compilation failed!"
        exit 1
    fi
}

# Function to start Fuseki server
start_server() {
    echo "🚀 Starting Fuseki SPARQL endpoint server..."
    echo "📡 Server will be available at: http://localhost:3030"
    echo "🔍 SPARQL endpoint: http://localhost:3030/network-topology/sparql"
    echo "💾 Data will be persisted in tdb-data/ directory"
    echo
    echo "Press Ctrl+C to stop the server"
    echo
    mvn exec:java -Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher
}

# Function to run client demo
run_client() {
    echo "🔗 Running client demo..."
    echo "📡 Connecting to: http://localhost:3030/network-topology/sparql"
    echo "⚠️  Make sure the Fuseki server is running first!"
    echo
    mvn exec:java -Dexec.mainClass=com.example.networktopology.client.NetworkTopologyClient
}

# Function to run main application
run_main() {
    echo "🏃 Running complete application demo..."
    echo "📊 This includes all components: loading, querying, reasoning, and Fuseki demo"
    echo
    mvn exec:java -Dexec.mainClass=com.example.networktopology.NetworkTopologyLoader
}

# Main script logic
case "$1" in
    "server")
        compile_project
        start_server
        ;;
    "client")
        compile_project
        run_client
        ;;
    "main")
        compile_project
        run_main
        ;;
    "compile")
        compile_project
        ;;
    "clean")
        clean_compile
        ;;
    *)
        show_usage
        ;;
esac
