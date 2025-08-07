# Network Topology SPARQL Project - Complete Learning Guide

This document provides a comprehensive, beginner-friendly explanation of every component in this Apache Jena Fuseki SPARQL project. Perfect for learning semantic web technologies from scratch.

## Table of Contents

1. [Project Overview](#project-overview)
2. [Core Concepts](#core-concepts)
3. [Project Structure](#project-structure)
4. [Maven Configuration](#maven-configuration)
5. [Data Layer](#data-layer)
6. [Server Components](#server-components)
7. [Client Components](#client-components)
8. [Query System](#query-system)
9. [Configuration & Utilities](#configuration--utilities)
10. [Testing & Examples](#testing--examples)
11. [Learning Path](#learning-path)

---

## Project Overview

### What This Project Does

This project creates a **SPARQL endpoint** for network topology data that AI agents can query via HTTP. Think of it as a specialized database for network infrastructure information that speaks the semantic web language.

**Key Components:**
- **Data Storage**: Network topology information (routers, switches, connections)
- **SPARQL Endpoint**: HTTP API for querying the data
- **AI Agent Integration**: Examples for Python, JavaScript, and other languages
- **Semantic Web Standards**: Uses RDF, OWL, and SPARQL

### Why Use SPARQL for Network Topology?

1. **Flexible Queries**: Ask complex questions about network relationships
2. **Standardized**: Uses W3C semantic web standards
3. **AI-Friendly**: Perfect for machine learning and AI agents
4. **Graph-Based**: Natural fit for network topology data

---

## Core Concepts

### 1. RDF (Resource Description Framework)

RDF is the foundation of semantic web data. Everything is described as **triples**:

```
Subject → Predicate → Object
Router1 → hasIP → "192.168.1.1"
Router1 → connectsTo → Switch1
Router1 → hasFirmware → "1.5"
```

### 2. OWL (Web Ontology Language)

OWL defines the "vocabulary" or "schema" for our domain:

```turtle
:Router a owl:Class ;
    rdfs:label "Network Router" .

:hasFirmwareVersion a owl:DatatypeProperty ;
    rdfs:domain :Router ;
    rdfs:range xsd:string .
```

### 3. SPARQL (SPARQL Protocol and RDF Query Language)

SPARQL is SQL for RDF data:

```sparql
SELECT ?router ?firmware WHERE {
  ?router a :Router .
  ?router :hasFirmwareVersion ?firmware .
  FILTER (xsd:double(?firmware) < 2.0)
}
```

### 4. Apache Jena

Apache Jena is a Java framework for semantic web applications:
- **TDB2**: High-performance RDF database
- **Fuseki**: SPARQL server with HTTP endpoints
- **ARQ**: SPARQL query engine

---

## Project Structure

```
network-topology-java/
├── src/main/java/com/example/networktopology/
│   ├── NetworkTopologyLoader.java          # Main application entry point
│   ├── SimpleJenaTest.java                 # Basic Jena functionality demo
│   ├── client/                             # HTTP client examples
│   │   └── NetworkTopologyClient.java     
│   ├── config/                             # Configuration classes
│   │   └── NetworkTopologyConfig.java     
│   ├── launcher/                           # Application launchers
│   │   └── FusekiServerLauncher.java      
│   ├── loaders/                            # Data loading components
│   │   ├── InstanceLoader.java            
│   │   └── OntologyLoader.java            
│   ├── queries/                            # SPARQL query handlers
│   │   └── SPARQLQueryHandler.java        
│   ├── reasoning/                          # Inference and reasoning
│   │   └── NetworkTopologyReasoner.java   
│   ├── server/                             # Fuseki server components
│   │   └── NetworkTopologyFusekiServer.java
│   └── utils/                              # Utility classes
│       └── FileUtils.java                 
├── src/main/resources/
│   ├── network-topology.owl                # Ontology definition
│   └── network-instances.ttl              # Sample data
├── pom.xml                                 # Maven dependencies
└── README files and documentation
```

---

## Maven Configuration

### Understanding pom.xml

```xml
<project>
    <groupId>com.example</groupId>
    <artifactId>network-topology</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
```

**Key Dependencies:**

1. **Apache Jena Core** (`jena-core`): Basic RDF operations
2. **Apache Jena ARQ** (`jena-arq`): SPARQL query engine
3. **Apache Jena TDB2** (`jena-tdb2`): Persistent RDF storage
4. **Apache Jena Fuseki** (`jena-fuseki-main`): HTTP SPARQL server

```xml
<dependency>
    <groupId>org.apache.jena</groupId>
    <artifactId>jena-fuseki-main</artifactId>
    <version>4.10.0</version>
</dependency>
```

---

## Data Layer

### 1. Ontology Definition (`network-topology.owl`)

**Purpose**: Defines the vocabulary for network topology concepts.

```turtle
# Class definitions
:NetworkDevice a owl:Class ;
    rdfs:label "Network Device" .

:Router a owl:Class ;
    rdfs:subClassOf :NetworkDevice ;
    rdfs:label "Router" .

:Switch a owl:Class ;
    rdfs:subClassOf :NetworkDevice ;
    rdfs:label "Switch" .

# Property definitions
:hasFirmwareVersion a owl:DatatypeProperty ;
    rdfs:domain :NetworkDevice ;
    rdfs:range xsd:string ;
    rdfs:label "has firmware version" .

:connectsTo a owl:ObjectProperty ;
    rdfs:domain :NetworkDevice ;
    rdfs:range :NetworkDevice ;
    rdfs:label "connects to" .
```

**What This Means:**
- `Router` and `Switch` are types of `NetworkDevice`
- Every network device can have a firmware version (string)
- Devices can connect to other devices

### 2. Instance Data (`network-instances.ttl`)

**Purpose**: Contains actual network topology data.

```turtle
# Router instances
nt:router1 a :Router ;
    :hasName "Main Router" ;
    :hasIPAddress "192.168.1.1" ;
    :hasFirmwareVersion "1.5" ;
    :locatedIn nt:datacenter1 .

nt:router2 a :Router ;
    :hasName "Backup Router" ;
    :hasIPAddress "192.168.1.2" ;
    :hasFirmwareVersion "2.1" ;
    :locatedIn nt:datacenter1 .

# Network connections
nt:link1 a :NetworkLink ;
    :connectsFrom nt:router1 ;
    :connectsTo nt:switch1 ;
    :hasBandwidth "1000" .
```

### 3. OntologyLoader.java

**Purpose**: Loads the ontology definition into memory.

```java
public class OntologyLoader {
    public Model loadTurtleOntology() {
        try {
            // Create an empty RDF model
            Model model = ModelFactory.createDefaultModel();
            
            // Load the ontology file
            InputStream ontologyStream = getClass()
                .getResourceAsStream("/network-topology.owl");
            
            // Parse and add to model
            model.read(ontologyStream, null, "TURTLE");
            
            return model;
        } catch (Exception e) {
            System.err.println("Failed to load ontology: " + e.getMessage());
            return null;
        }
    }
}
```

**Step-by-Step:**
1. **Create Model**: `ModelFactory.createDefaultModel()` creates an empty RDF graph
2. **Load File**: Reads the .owl file from resources
3. **Parse**: Interprets Turtle syntax and creates RDF triples
4. **Return**: Provides the loaded model for use

### 4. InstanceLoader.java

**Purpose**: Loads actual network data instances.

```java
public class InstanceLoader {
    public Model loadRDFInstances() {
        try {
            Model model = ModelFactory.createDefaultModel();
            InputStream instanceStream = getClass()
                .getResourceAsStream("/network-instances.ttl");
            
            model.read(instanceStream, null, "TURTLE");
            
            System.out.println("✓ RDF instances loaded successfully!");
            System.out.println("  Number of statements: " + model.size());
            
            return model;
        } catch (Exception e) {
            System.err.println("Failed to load instances: " + e.getMessage());
            return null;
        }
    }
}
```

**Key Difference from OntologyLoader:**
- Ontology defines "what can exist"
- Instances define "what actually exists"

---

## Server Components

### 1. NetworkTopologyFusekiServer.java

**Purpose**: Creates an HTTP SPARQL endpoint for AI agents to query.

```java
public class NetworkTopologyFusekiServer {
    private static final String DATASET_PATH = "/network-topology";
    private static final String TDB_DIRECTORY = "tdb-data";
    private int port = 3030;
    private FusekiServer server;
    
    public void startServer() {
        try {
            // Initialize Fuseki logging
            FusekiLogging.init();
            
            // Create persistent dataset
            Dataset dataset = createDataset();
            
            // Load data into dataset
            loadNetworkTopologyData(dataset);
            
            // Build and start Fuseki server
            server = FusekiServer.create()
                .port(port)
                .add(DATASET_PATH, dataset)
                .build();
                
            server.start();
            
            System.out.println("✓ Fuseki server started on port " + port);
            
        } catch (Exception e) {
            System.err.println("Failed to start Fuseki server: " + e.getMessage());
        }
    }
}
```

**What Each Part Does:**

1. **Dataset Creation**: 
   ```java
   private Dataset createDataset() {
       return TDB2Factory.connectDataset(TDB_DIRECTORY);
   }
   ```
   - Creates a persistent RDF database using TDB2
   - Data survives server restarts

2. **Data Loading**:
   ```java
   private void loadNetworkTopologyData(Dataset dataset) {
       dataset.begin(ReadWrite.WRITE);  // Start transaction
       try {
           // Load ontology and instances
           Model ontologyModel = ontologyLoader.loadTurtleOntology();
           Model instanceModel = instanceLoader.loadRDFInstances();
           
           // Add to dataset
           dataset.getDefaultModel().add(ontologyModel);
           dataset.getDefaultModel().add(instanceModel);
           
           dataset.commit();  // Save changes
       } catch (Exception e) {
           dataset.abort();   // Rollback on error
       } finally {
           dataset.end();     // Clean up transaction
       }
   }
   ```

3. **Server Configuration**:
   ```java
   server = FusekiServer.create()
       .port(3030)                    // HTTP port
       .add("/network-topology", dataset)  // Endpoint path
       .build();
   ```

**Transaction Management Explained:**
- **WRITE Transaction**: Required for modifying data
- **Commit**: Permanently saves changes
- **Abort**: Cancels changes if error occurs
- **End**: Releases locks and cleans up

### 2. FusekiServerLauncher.java

**Purpose**: Simple launcher with optional port configuration.

```java
public class FusekiServerLauncher {
    public static void main(String[] args) {
        // Default port
        int port = 3030;
        
        // Check for custom port argument
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0]);
                System.exit(1);
            }
        }
        
        // Create and start server
        NetworkTopologyFusekiServer server = new NetworkTopologyFusekiServer(port);
        server.startServer();
        
        // Keep running until interrupted
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            server.stopServer();
        }
    }
}
```

**Usage Examples:**
```bash
# Default port 3030
mvn exec:java '-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher'

# Custom port 3333
mvn exec:java '-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher' '-Dexec.args=3333'
```

---

## Client Components

### NetworkTopologyClient.java

**Purpose**: Demonstrates how AI agents can query the SPARQL endpoint via HTTP.

```java
public class NetworkTopologyClient {
    private static final String FUSEKI_ENDPOINT = "http://localhost:3030/network-topology/sparql";
    
    public QueryResult queryOutdatedRouters() {
        String query = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX nt: <http://example.org/network-topology/instances#> " +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
            "SELECT ?router ?firmware WHERE { " +
            "  ?router a :Router . " +
            "  ?router :hasFirmwareVersion ?firmware . " +
            "  FILTER (xsd:double(?firmware) < 2.0) " +
            "}";
        
        return executeQuery(query, "Routers with Firmware < 2.0.0");
    }
}
```

**Query Breakdown:**

1. **Prefixes**: Shorthand for long URIs
   ```sparql
   PREFIX : <http://example.org/network-topology#>
   ```
   - `:Router` means `http://example.org/network-topology#Router`

2. **SELECT Clause**: What to return
   ```sparql
   SELECT ?router ?firmware WHERE
   ```
   - Variables start with `?`
   - Returns router and its firmware version

3. **Triple Patterns**: What to match
   ```sparql
   ?router a :Router .
   ?router :hasFirmwareVersion ?firmware .
   ```
   - Find things that are routers
   - Get their firmware versions

4. **Filters**: Conditions to apply
   ```sparql
   FILTER (xsd:double(?firmware) < 2.0)
   ```
   - Convert firmware string to number
   - Only return if less than 2.0

**HTTP Execution:**
```java
private QueryResult executeQuery(String queryString, String description) {
    try (RDFConnection conn = RDFConnectionFactory.connect(FUSEKI_ENDPOINT)) {
        QueryExecution qexec = conn.query(queryString);
        ResultSet results = qexec.execSelect();
        
        // Process results
        List<QuerySolution> solutions = new ArrayList<>();
        List<String> resultVars = results.getResultVars();
        
        while (results.hasNext()) {
            solutions.add(results.nextSolution());
        }
        
        return new QueryResult(description, solutions, resultVars);
    }
}
```

**What Happens:**
1. **Connect**: Opens HTTP connection to Fuseki endpoint
2. **Execute**: Sends SPARQL query via HTTP POST
3. **Process**: Converts results to Java objects
4. **Return**: Provides structured results

---

## Query System

### SPARQLQueryHandler.java

**Purpose**: Provides pre-built SPARQL queries for common network analysis tasks.

```java
public class SPARQLQueryHandler {
    private static final String ONTOLOGY_NS = "http://example.org/network-topology#";
    private static final String INSTANCE_NS = "http://example.org/network-topology/instances#";
    
    public void queryOutdatedRouters(Model model) {
        System.out.println("\n=== SPARQL Query: Routers with Outdated Firmware ===");
        
        String queryString = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
            "SELECT ?router ?firmware WHERE { " +
            "  ?router a :Router . " +
            "  ?router :hasFirmwareVersion ?firmware . " +
            "  FILTER (xsd:double(?firmware) < 2.0) " +
            "}";
        
        executeQuery(model, queryString, "Outdated Routers");
    }
}
```

**Additional Query Examples:**

1. **Network Topology Discovery**:
   ```sparql
   SELECT ?link ?source ?target ?bandwidth WHERE {
     ?link a :NetworkLink .
     ?link :connectsFrom ?source .
     ?link :connectsTo ?target .
     OPTIONAL { ?link :hasBandwidth ?bandwidth }
   } ORDER BY ?bandwidth
   ```

2. **High-Capacity Paths**:
   ```sparql
   SELECT ?link ?from ?to ?bandwidth WHERE {
     ?link a :NetworkLink .
     ?link :connectsFrom ?from .
     ?link :connectsTo ?to .
     ?link :hasBandwidth ?bandwidth .
     FILTER (xsd:double(?bandwidth) >= 1000)
   } ORDER BY DESC(?bandwidth)
   ```

3. **Device Inventory**:
   ```sparql
   SELECT ?device ?type ?name WHERE {
     ?device a ?type .
     OPTIONAL { ?device :hasName ?name }
     FILTER(?type IN (:Router, :Switch, :Server, :Firewall))
   }
   ```

**Query Execution Method:**
```java
private void executeQuery(Model model, String queryString, String description) {
    try {
        // Create query object
        Query query = QueryFactory.create(queryString);
        
        // Execute query on model
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        
        // Print results
        System.out.println("\n" + description + ":");
        ResultSetFormatter.out(System.out, results, query);
        
    } catch (Exception e) {
        System.err.println("Query execution failed: " + e.getMessage());
    }
}
```

---

## Configuration & Utilities

### NetworkTopologyConfig.java

**Purpose**: Centralized configuration management.

```java
public class NetworkTopologyConfig {
    // File paths
    public static final String ONTOLOGY_FILE = "/network-topology.owl";
    public static final String INSTANCES_FILE = "/network-instances.ttl";
    
    // Server configuration
    public static final int DEFAULT_PORT = 3030;
    public static final String DATASET_PATH = "/network-topology";
    public static final String TDB_DIRECTORY = "tdb-data";
    
    // Namespace URIs
    public static final String ONTOLOGY_NS = "http://example.org/network-topology#";
    public static final String INSTANCE_NS = "http://example.org/network-topology/instances#";
    
    // Endpoint URLs
    public static String getSparqlEndpoint(int port) {
        return "http://localhost:" + port + DATASET_PATH + "/sparql";
    }
    
    public static String getUpdateEndpoint(int port) {
        return "http://localhost:" + port + DATASET_PATH + "/update";
    }
}
```

### FileUtils.java

**Purpose**: Utility functions for file operations.

```java
public class FileUtils {
    public static InputStream getResourceAsStream(String resourcePath) {
        InputStream stream = FileUtils.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return stream;
    }
    
    public static boolean resourceExists(String resourcePath) {
        return FileUtils.class.getResource(resourcePath) != null;
    }
    
    public static void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
```

---

## Testing & Examples

### SimpleJenaTest.java

**Purpose**: Basic demonstration of Jena functionality without Fuseki.

```java
public class SimpleJenaTest {
    public static void main(String[] args) {
        System.out.println("=== Simple Jena Test ===");
        
        try {
            // Create empty model
            Model model = ModelFactory.createDefaultModel();
            
            // Add some triples manually
            Resource router1 = model.createResource("http://example.org/router1");
            Property hasIP = model.createProperty("http://example.org/hasIP");
            router1.addProperty(hasIP, "192.168.1.1");
            
            // Print all statements
            StmtIterator iter = model.listStatements();
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                System.out.println(stmt.getSubject() + " " + 
                                 stmt.getPredicate() + " " + 
                                 stmt.getObject());
            }
            
            // Execute a simple query
            String queryString = 
                "SELECT ?s ?p ?o WHERE { ?s ?p ?o }";
            
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet results = qexec.execSelect();
            
            ResultSetFormatter.out(System.out, results, query);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### NetworkTopologyLoader.java

**Purpose**: Main application that demonstrates the complete workflow.

```java
public class NetworkTopologyLoader {
    public static void main(String[] args) {
        System.out.println("=== Network Topology Loader ===");
        
        try {
            // 1. Load data
            OntologyLoader ontologyLoader = new OntologyLoader();
            InstanceLoader instanceLoader = new InstanceLoader();
            
            Model ontologyModel = ontologyLoader.loadTurtleOntology();
            Model instanceModel = instanceLoader.loadRDFInstances();
            
            // 2. Combine models
            Model combinedModel = ModelFactory.createDefaultModel();
            combinedModel.add(ontologyModel);
            combinedModel.add(instanceModel);
            
            // 3. Run queries
            SPARQLQueryHandler queryHandler = new SPARQLQueryHandler();
            queryHandler.queryOutdatedRouters(combinedModel);
            queryHandler.queryNetworkConnections(combinedModel);
            queryHandler.queryHighCapacityPaths(combinedModel, 1000.0);
            
            // 4. Start server (optional)
            if (args.length > 0 && "server".equals(args[0])) {
                NetworkTopologyFusekiServer server = new NetworkTopologyFusekiServer();
                server.startServer();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## Learning Path

### 1. Beginner Level

**Start Here:**
1. Run `SimpleJenaTest.java` to understand basic RDF operations
2. Explore the ontology file (`network-topology.owl`)
3. Look at instance data (`network-instances.ttl`)
4. Run basic SPARQL queries with `SPARQLQueryHandler`

**Key Concepts to Master:**
- RDF triples (Subject-Predicate-Object)
- Turtle syntax
- Basic SPARQL SELECT queries
- Prefixes and namespaces

### 2. Intermediate Level

**Next Steps:**
1. Start the Fuseki server with `FusekiServerLauncher`
2. Use the HTTP client in `NetworkTopologyClient`
3. Explore the web UI at `http://localhost:3030`
4. Modify queries to answer different questions

**Key Concepts to Master:**
- HTTP SPARQL endpoints
- Query execution over HTTP
- Filters and functions in SPARQL
- Optional patterns

### 3. Advanced Level

**Advanced Topics:**
1. Modify the ontology to add new concepts
2. Create custom reasoning rules
3. Implement SPARQL UPDATE operations
4. Add authentication and security

**Key Concepts to Master:**
- OWL reasoning and inference
- SPARQL UPDATE (INSERT, DELETE)
- Performance optimization
- Production deployment

### 4. Expert Level

**Expert Challenges:**
1. Integrate with machine learning pipelines
2. Build federated SPARQL queries
3. Implement custom Jena components
4. Create domain-specific reasoners

### Common SPARQL Patterns

1. **Basic Pattern Matching**:
   ```sparql
   ?device a :Router .
   ?device :hasName ?name .
   ```

2. **Filtering**:
   ```sparql
   FILTER (?bandwidth > 1000)
   FILTER (regex(?name, "Router"))
   ```

3. **Optional Patterns**:
   ```sparql
   OPTIONAL { ?device :hasDescription ?desc }
   ```

4. **Aggregation**:
   ```sparql
   SELECT (COUNT(?router) as ?count) WHERE {
     ?router a :Router .
   }
   ```

5. **Property Paths**:
   ```sparql
   ?device1 :connectsTo+ ?device2 .  # One or more connections
   ```

### Debugging Tips

1. **Query Syntax Errors**:
   - Check prefixes are defined
   - Ensure proper turtle syntax
   - Verify namespace URIs

2. **Empty Results**:
   - Check data was loaded properly
   - Verify class and property names
   - Use COUNT queries to check data exists

3. **Server Issues**:
   - Check port availability
   - Verify TDB2 directory permissions
   - Look for transaction conflicts

### Resources for Further Learning

1. **Apache Jena Documentation**: https://jena.apache.org/
2. **SPARQL Specification**: https://www.w3.org/TR/sparql11-query/
3. **OWL Primer**: https://www.w3.org/TR/owl2-primer/
4. **Turtle Specification**: https://www.w3.org/TR/turtle/

This project provides a complete foundation for building semantic web applications with Apache Jena and SPARQL. Each component is designed to be educational while remaining practical for real-world use cases.
