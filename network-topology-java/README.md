# Network Topology Ontology - Java Project with Apache Jena

This is a Maven-based Java project that demonstrates how to work with the Network Topology Ontology using Apache Jena.

## Project Structure

```
network-topology-java/
├── pom.xml                           # Maven configuration
├── README.md                         # This file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/networktopology/
│   │   │       └── NetworkTopologyLoader.java
│   │   └── resources/
│   │       ├── network-topology-ontology.owl     # OWL ontology file
│   │       ├── network-topology-ontology.ttl     # Turtle ontology file
│   │       └── simple-rdf-instances.ttl          # Sample RDF instances
│   └── test/
│       └── java/
```

## Prerequisites

- **Java 11 or higher** - The project is configured for Java 11+
- **Apache Maven 3.6+** - For building and dependency management
- **Internet connection** - For downloading dependencies

## Dependencies

The project uses the following main dependencies:
- Apache Jena Core (4.10.0)
- Apache Jena ARQ (SPARQL queries)
- Apache Jena TDB (Triple store)
- Apache Jena Permissions (Reasoning)
- SLF4J (Logging)
- JUnit 5 (Testing)

## Building the Project

### 1. Navigate to the project directory
```bash
cd "d:\AI Project\Jena\network-topology-java"
```

### 2. Download dependencies and compile
```bash
mvn clean compile
```

### 3. Build the complete project
```bash
mvn clean package
```

## Running the Application

### Method 1: Using Maven Exec Plugin
```bash
mvn exec:java
```

### Method 2: Using Maven with specific main class
```bash
mvn exec:java -Dexec.mainClass="com.example.networktopology.NetworkTopologyLoader"
```

### Method 3: Running the compiled JAR
```bash
java -jar target/network-topology-jena-1.0.0.jar
```

### Method 4: Running the shaded JAR (fat JAR with all dependencies)
```bash
java -jar target/network-topology-jena-1.0.0-shaded.jar
```

## What the Application Does

The `NetworkTopologyLoader` class demonstrates four main operations:

### 1. **Load OWL Ontology**
- Loads the `network-topology-ontology.owl` file
- Creates an OntModel with OWL reasoning
- Lists all classes and object properties in the ontology

### 2. **Load Turtle Ontology**
- Loads the `network-topology-ontology.ttl` file
- Counts classes and properties
- Shows basic statistics

### 3. **Load RDF Instances**
- Loads the `simple-rdf-instances.ttl` file
- Executes SPARQL queries to find:
  - All routers with their hostnames and firmware versions
  - All network links and their connected interfaces

### 4. **Combined Model with Reasoning**
- Loads both ontology and instances into a single model
- Applies OWL reasoning
- Validates the model for consistency
- Executes inference queries to discover implicit relationships

## Expected Output

When you run the application, you should see output similar to:

```
=== Network Topology Ontology Loader ===

=== Loading OWL Ontology ===
✓ Ontology loaded successfully!
  Number of statements: 426
  
  Classes in the ontology:
  - NetworkEntity
  - NetworkDevice
  - Router
  - Switch
  - Firewall
  ...

=== Loading Turtle Ontology ===
✓ Turtle ontology loaded successfully!
  Number of statements: 426
  Classes defined: 15
  Properties defined: 25

=== Loading RDF Instances ===
✓ RDF instances loaded successfully!
  Number of statements: 42

  Routers found:
  - Router1 (hostname: router1.example.com, firmware: 1.2.0)
  - Router2 (hostname: router2.example.com, firmware: 1.2.0)

  Network links found:
  - Link1 connects Router1_Interface1 to Router2_Interface1

=== Loading Combined Model with Reasoning ===
✓ Combined model loaded with reasoning!
  Number of statements: 468
✓ Model is valid!

  Network devices with inferred types:
  - Router1 : Router
  - Router2 : Router

=== All operations completed successfully! ===
```

## Troubleshooting

### Common Issues:

1. **Java Version Error**
   ```
   Solution: Ensure you have Java 11+ installed
   Check with: java -version
   ```

2. **Maven Not Found**
   ```
   Solution: Install Apache Maven and add to PATH
   Download from: https://maven.apache.org/download.cgi
   ```

3. **File Not Found Errors**
   ```
   Solution: Ensure all files are in src/main/resources/
   - network-topology-ontology.owl
   - network-topology-ontology.ttl
   - simple-rdf-instances.ttl
   ```

4. **Dependency Download Issues**
   ```
   Solution: Check internet connection and try:
   mvn clean install -U
   ```

5. **Compilation Errors**
   ```
   Solution: Clean and rebuild:
   mvn clean compile
   ```

## Customization

### Adding New Functionality

1. **Add new SPARQL queries** in the respective methods
2. **Modify ontology files** in `src/main/resources/`
3. **Add new reasoning rules** by changing the reasoner type
4. **Create unit tests** in `src/test/java/`

### Configuration

- **Change Java version**: Modify `maven.compiler.source` and `maven.compiler.target` in `pom.xml`
- **Update Jena version**: Change `jena.version` property in `pom.xml`
- **Add new dependencies**: Add them to the `<dependencies>` section in `pom.xml`

## Development Commands

```bash
# Clean the project
mvn clean

# Compile only
mvn compile

# Run tests
mvn test

# Package without tests
mvn package -DskipTests

# Generate project documentation
mvn site

# Check for dependency updates
mvn versions:display-dependency-updates
```

## File Descriptions

- **NetworkTopologyLoader.java**: Main application class with Jena integration
- **network-topology-ontology.owl**: Complete OWL ontology definition
- **network-topology-ontology.ttl**: Same ontology in Turtle format (more readable)
- **simple-rdf-instances.ttl**: Sample RDF data instances (Router1, Router2, Link1, SiteA)
- **pom.xml**: Maven project configuration and dependencies

## Next Steps

1. **Run the application** to see Jena in action
2. **Modify the RDF instances** to add more network devices
3. **Write custom SPARQL queries** for your specific use cases
4. **Extend the ontology** with additional device types or properties
5. **Add unit tests** to validate your ontology and queries
