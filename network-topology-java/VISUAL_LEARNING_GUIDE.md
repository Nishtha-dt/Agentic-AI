# Network Topology SPARQL Project - Visual Learning Guide

*A Complete Visual Guide to Apache Jena, Fuseki, and Semantic Web Technologies*

---

## 📖 Table of Contents

1. [🎯 Project Overview](#-project-overview)
2. [🧠 Semantic Web Fundamentals](#-semantic-web-fundamentals)
3. [🏗️ Apache Jena Architecture](#️-apache-jena-architecture)
4. [📊 Project Structure Diagram](#-project-structure-diagram)
5. [🔄 Data Flow Visualization](#-data-flow-visualization)
6. [🌐 SPARQL Query Process](#-sparql-query-process)
7. [🚀 Component Deep Dive](#-component-deep-dive)
8. [💻 Hands-On Examples](#-hands-on-examples)
9. [🎓 Learning Progression](#-learning-progression)
10. [🔧 Troubleshooting Guide](#-troubleshooting-guide)

---

## 🎯 Project Overview

### What We're Building

```
┌─────────────────────────────────────────────────────────────┐
│                Network Topology SPARQL System              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  🤖 AI Agents  ←→  🌐 HTTP API  ←→  📊 SPARQL Engine       │
│                                                             │
│                         ↕                                   │
│                                                             │
│              🗄️ TDB2 Database (Network Data)                │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Key Value Propositions:**
- 🔍 **Semantic Queries**: Ask complex questions about network relationships
- 🚀 **AI-Ready**: Perfect integration with machine learning systems
- 📈 **Scalable**: Built on enterprise-grade Apache Jena framework
- 🌍 **Standards-Based**: Uses W3C semantic web technologies

---

## 🧠 Semantic Web Fundamentals

### 1. The RDF Triple Model

Every piece of information is stored as a **triple**:

```
Subject  →  Predicate  →  Object
   │           │           │
   ▼           ▼           ▼
Router1  →  hasIP  →  "192.168.1.1"
Router1  →  connectsTo  →  Switch1
Router1  →  hasFirmware  →  "1.5"
```

**Visual Representation:**
```
        hasIP
Router1 ────────→ "192.168.1.1"
   │
   │ connectsTo
   └─────────→ Switch1
   │
   │ hasFirmware
   └─────────→ "1.5"
```

### 2. Network Topology as a Graph

```
    Data Center 1                    Data Center 2
┌─────────────────────┐          ┌─────────────────────┐
│                     │          │                     │
│  🔄 Router1         │   Link   │         Router2 🔄  │
│  (FW: 1.5)         │◄────────►│         (FW: 2.1)   │
│       │             │          │             │       │
│       │             │          │             │       │
│   🔀 Switch1        │          │        Switch2 🔀   │
│       │             │          │             │       │
│   💻 Server1        │          │        Server2 💻   │
│                     │          │                     │
└─────────────────────┘          └─────────────────────┘
```

### 3. From Graph to RDF Triples

The above network becomes these triples:
```turtle
# Router definitions
nt:router1 a :Router ;
    :hasName "Main Router" ;
    :hasFirmwareVersion "1.5" ;
    :locatedIn nt:datacenter1 .

nt:router2 a :Router ;
    :hasName "Backup Router" ;
    :hasFirmwareVersion "2.1" ;
    :locatedIn nt:datacenter2 .

# Network connections
nt:link1 a :NetworkLink ;
    :connectsFrom nt:router1 ;
    :connectsTo nt:router2 ;
    :hasBandwidth "1000" .
```

---

## 🏗️ Apache Jena Architecture

### Core Components Overview

```
┌───────────────────────────────────────────────────────────┐
│                   Apache Jena Framework                  │
├───────────────────────────────────────────────────────────┤
│                                                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │   Fuseki    │  │     ARQ     │  │    TDB2     │      │
│  │ HTTP Server │  │SPARQL Engine│  │  Database   │      │
│  └─────────────┘  └─────────────┘  └─────────────┘      │
│         │                 │                 │           │
│         └─────────────────┼─────────────────┘           │
│                           │                             │
│         ┌─────────────────┼─────────────────┐           │
│         │                 │                 │           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │    Model    │  │  Reasoning  │  │  Ontology   │      │
│  │   (RDF)     │  │   Engine    │  │   (OWL)     │      │
│  └─────────────┘  └─────────────┘  └─────────────┘      │
│                                                           │
└───────────────────────────────────────────────────────────┘
```

### Component Responsibilities

| Component | Purpose | Key Features |
|-----------|---------|--------------|
| **Fuseki** | HTTP SPARQL Server | REST API, Web UI, Authentication |
| **ARQ** | SPARQL Query Engine | Query parsing, optimization, execution |
| **TDB2** | Native RDF Database | High performance, ACID transactions |
| **Model** | RDF Graph Interface | In-memory operations, serialization |
| **Reasoning** | Inference Engine | Rule-based reasoning, OWL support |

---

## 📊 Project Structure Diagram

### Directory Structure with Purpose

```
network-topology-java/
│
├── 📁 src/main/java/com/example/networktopology/
│   │
│   ├── 🚀 NetworkTopologyLoader.java          ← Main entry point
│   ├── 🧪 SimpleJenaTest.java                 ← Basic demo
│   │
│   ├── 📁 client/                             ← HTTP client examples
│   │   └── 🌐 NetworkTopologyClient.java     ← AI agent integration
│   │
│   ├── 📁 config/                             ← Configuration
│   │   └── ⚙️ NetworkTopologyConfig.java     ← Centralized settings
│   │
│   ├── 📁 launcher/                           ← Application starters
│   │   └── 🎯 FusekiServerLauncher.java      ← Server bootstrap
│   │
│   ├── 📁 loaders/                            ← Data loading
│   │   ├── 📚 OntologyLoader.java            ← Schema loader
│   │   └── 📊 InstanceLoader.java            ← Data loader
│   │
│   ├── 📁 queries/                            ← SPARQL operations
│   │   └── 🔍 SPARQLQueryHandler.java        ← Pre-built queries
│   │
│   ├── 📁 reasoning/                          ← AI reasoning
│   │   └── 🧠 NetworkTopologyReasoner.java   ← Inference engine
│   │
│   ├── 📁 server/                             ← HTTP server
│   │   └── 🖥️ NetworkTopologyFusekiServer.java ← Main server
│   │
│   └── 📁 utils/                              ← Helper functions
│       └── 🔧 FileUtils.java                 ← File operations
│
├── 📁 src/main/resources/
│   ├── 📄 network-topology.owl                ← Ontology (Schema)
│   └── 📄 network-instances.ttl              ← Sample data
│
├── 📄 pom.xml                                 ← Maven dependencies
└── 📚 Documentation files
```

### Class Interaction Diagram

```
┌─────────────────┐    creates    ┌─────────────────┐
│ FusekiServer    │──────────────►│ TDB2 Dataset    │
│ Launcher        │               │                 │
└─────────────────┘               └─────────────────┘
         │                                 ▲
         │ uses                            │ loads data
         ▼                                 │
┌─────────────────┐    delegates   ┌─────────────────┐
│ NetworkTopology │──────────────►│ OntologyLoader  │
│ FusekiServer    │               │ InstanceLoader  │
└─────────────────┘               └─────────────────┘
         ▲                                 │
         │ queries                         │ provides data
         │                                 ▼
┌─────────────────┐    executes    ┌─────────────────┐
│ NetworkTopology │──────────────►│ SPARQLQuery     │
│ Client          │               │ Handler         │
└─────────────────┘               └─────────────────┘
```

---

## 🔄 Data Flow Visualization

### 1. Server Startup Process

```
START
  │
  ▼
┌─────────────────┐
│ 1. Initialize   │ ──── FusekiLogging.init()
│    Logging      │
└─────────────────┘
  │
  ▼
┌─────────────────┐
│ 2. Create       │ ──── TDB2Factory.connectDataset()
│    TDB2         │
│    Dataset      │
└─────────────────┘
  │
  ▼
┌─────────────────┐
│ 3. Load         │ ──── OntologyLoader.loadTurtleOntology()
│    Ontology     │
└─────────────────┘
  │
  ▼
┌─────────────────┐
│ 4. Load         │ ──── InstanceLoader.loadRDFInstances()
│    Instance     │
│    Data         │
└─────────────────┘
  │
  ▼
┌─────────────────┐
│ 5. Start        │ ──── FusekiServer.create().port().add().build().start()
│    HTTP Server  │
└─────────────────┘
  │
  ▼
┌─────────────────┐
│ 6. Ready for    │ ──── http://localhost:3030/network-topology/sparql
│    Queries      │
└─────────────────┘
```

### 2. Query Execution Flow

```
AI Agent Request
       │
       ▼
┌─────────────────┐
│ HTTP POST       │ ──── Content-Type: application/sparql-query
│ to Fuseki       │
└─────────────────┘
       │
       ▼
┌─────────────────┐
│ ARQ SPARQL      │ ──── Parse query string
│ Parser          │      Validate syntax
└─────────────────┘
       │
       ▼
┌─────────────────┐
│ Query           │ ──── Optimize query plan
│ Optimizer       │      Choose execution strategy
└─────────────────┘
       │
       ▼
┌─────────────────┐
│ TDB2 Query      │ ──── Execute against stored triples
│ Execution       │      Apply filters and joins
└─────────────────┘
       │
       ▼
┌─────────────────┐
│ Result          │ ──── Format as JSON/XML/CSV
│ Serialization   │      Return HTTP response
└─────────────────┘
       │
       ▼
   AI Agent Response
```

### 3. Transaction Management

```
Dataset Transaction Lifecycle
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│  BEGIN(ReadWrite.WRITE)                                     │
│         │                                                   │
│         ▼                                                   │
│  ┌─────────────┐                                           │
│  │   ACTIVE    │ ◄─── Load ontology data                   │
│  │ TRANSACTION │ ◄─── Load instance data                   │
│  └─────────────┘                                           │
│         │                                                   │
│         ├─── SUCCESS ──► COMMIT() ──► Changes saved        │
│         │                                                   │
│         └─── ERROR ────► ABORT() ──► Changes discarded     │
│                           │                                 │
│                           ▼                                 │
│                        END() ──► Release locks             │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🌐 SPARQL Query Process

### Anatomy of a SPARQL Query

```sparql
PREFIX : <http://example.org/network-topology#>           ← Namespace shortcuts
PREFIX nt: <http://example.org/network-topology/instances#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>          ← Data type functions

SELECT ?router ?firmware WHERE {                          ← What to return
  ?router a :Router .                                     ← Pattern: find routers
  ?router :hasFirmwareVersion ?firmware .                 ← Pattern: get firmware
  FILTER (xsd:double(?firmware) < 2.0)                   ← Condition: version < 2.0
}                                                         ← End query block
```

### Query Execution Visualization

```
Query: "Find outdated routers"
│
├─ Step 1: Parse Prefixes
│  ├─ : = http://example.org/network-topology#
│  ├─ nt: = http://example.org/network-topology/instances#
│  └─ xsd: = http://www.w3.org/2001/XMLSchema#
│
├─ Step 2: Find Pattern Matches
│  │
│  ├─ Pattern 1: ?router a :Router
│  │  │
│  │  ├─ Match: nt:router1 a :Router
│  │  ├─ Match: nt:router2 a :Router
│  │  └─ Match: nt:router3 a :Router
│  │
│  └─ Pattern 2: ?router :hasFirmwareVersion ?firmware
│     │
│     ├─ nt:router1 :hasFirmwareVersion "1.5"
│     ├─ nt:router2 :hasFirmwareVersion "2.1"
│     └─ nt:router3 :hasFirmwareVersion "1.8"
│
├─ Step 3: Apply Filters
│  │
│  ├─ xsd:double("1.5") < 2.0 ✓ (Include nt:router1)
│  ├─ xsd:double("2.1") < 2.0 ✗ (Exclude nt:router2)
│  └─ xsd:double("1.8") < 2.0 ✓ (Include nt:router3)
│
└─ Step 4: Return Results
   │
   ├─ Result 1: router=nt:router1, firmware="1.5"
   └─ Result 2: router=nt:router3, firmware="1.8"
```

### Common Query Patterns

#### 1. Basic Pattern Matching
```sparql
# Find all devices and their types
SELECT ?device ?type WHERE {
  ?device a ?type .
}
```

#### 2. Property Paths
```sparql
# Find devices connected through multiple hops
SELECT ?device1 ?device2 WHERE {
  ?device1 :connectsTo+ ?device2 .  # + means one or more connections
}
```

#### 3. Optional Patterns
```sparql
# Get devices with optional descriptions
SELECT ?device ?name ?description WHERE {
  ?device :hasName ?name .
  OPTIONAL { ?device :hasDescription ?description }
}
```

#### 4. Aggregation
```sparql
# Count devices by type
SELECT ?type (COUNT(?device) as ?count) WHERE {
  ?device a ?type .
} GROUP BY ?type
```

---

## 🚀 Component Deep Dive

### 1. OntologyLoader.java - The Schema Keeper

**Purpose**: Loads the "vocabulary" that defines what concepts exist in our network domain.

```java
public class OntologyLoader {
    public Model loadTurtleOntology() {
        // 🏗️ Create empty RDF model (like an empty database)
        Model model = ModelFactory.createDefaultModel();
        
        // 📖 Read ontology file from resources
        InputStream ontologyStream = getClass()
            .getResourceAsStream("/network-topology.owl");
        
        // 🔄 Parse Turtle syntax and populate model
        model.read(ontologyStream, null, "TURTLE");
        
        return model;
    }
}
```

**What the Ontology Contains:**
```turtle
# 📝 Class Definitions (What types of things exist?)
:NetworkDevice a owl:Class ;
    rdfs:label "Network Device" .

:Router a owl:Class ;
    rdfs:subClassOf :NetworkDevice ;
    rdfs:label "Router" .

# 🔗 Property Definitions (How are things related?)
:hasFirmwareVersion a owl:DatatypeProperty ;
    rdfs:domain :NetworkDevice ;      # Who has this property?
    rdfs:range xsd:string ;           # What type is the value?
    rdfs:label "has firmware version" .

:connectsTo a owl:ObjectProperty ;
    rdfs:domain :NetworkDevice ;      # What connects?
    rdfs:range :NetworkDevice ;       # What does it connect to?
    rdfs:label "connects to" .
```

### 2. InstanceLoader.java - The Data Provider

**Purpose**: Loads actual network data (the real routers, switches, etc.).

```java
public class InstanceLoader {
    public Model loadRDFInstances() {
        Model model = ModelFactory.createDefaultModel();
        
        // 📊 Load actual network data
        InputStream instanceStream = getClass()
            .getResourceAsStream("/network-instances.ttl");
        
        model.read(instanceStream, null, "TURTLE");
        
        // 📈 Report what was loaded
        System.out.println("✓ Loaded " + model.size() + " triples");
        
        return model;
    }
}
```

**What Instance Data Looks Like:**
```turtle
# 🔄 Actual router instances
nt:router1 a :Router ;
    :hasName "Main Router" ;
    :hasIPAddress "192.168.1.1" ;
    :hasFirmwareVersion "1.5" ;
    :locatedIn nt:datacenter1 .

# 🔀 Actual switch instances
nt:switch1 a :Switch ;
    :hasName "Core Switch" ;
    :hasPortCount "48" ;
    :locatedIn nt:datacenter1 .

# 🌐 Actual network connections
nt:link1 a :NetworkLink ;
    :connectsFrom nt:router1 ;
    :connectsTo nt:switch1 ;
    :hasBandwidth "1000" ;
    :hasProtocol "Ethernet" .
```

### 3. NetworkTopologyFusekiServer.java - The HTTP Gateway

**Purpose**: Creates a web server that AI agents can query via HTTP.

```java
public class NetworkTopologyFusekiServer {
    private static final int DEFAULT_PORT = 3030;
    private FusekiServer server;
    
    public void startServer() {
        // 🚀 Initialize Fuseki logging system
        FusekiLogging.init();
        
        // 🗄️ Create persistent database
        Dataset dataset = TDB2Factory.connectDataset("tdb-data");
        
        // 📊 Load our network data
        loadNetworkTopologyData(dataset);
        
        // 🌐 Create HTTP server
        server = FusekiServer.create()
            .port(DEFAULT_PORT)                    // Listen on port 3030
            .add("/network-topology", dataset)     // Expose dataset at this path
            .build();
        
        // ▶️ Start the server
        server.start();
        
        System.out.println("🚀 Server ready at http://localhost:3030");
    }
}
```

**Server Endpoints Created:**
```
🌐 Web UI:          http://localhost:3030
🔍 SPARQL Query:    http://localhost:3030/network-topology/sparql
✏️ SPARQL Update:   http://localhost:3030/network-topology/update
📊 Graph Store:     http://localhost:3030/network-topology/data
```

### 4. NetworkTopologyClient.java - The AI Agent

**Purpose**: Shows how AI agents can query the server via HTTP.

```java
public class NetworkTopologyClient {
    private static final String ENDPOINT = 
        "http://localhost:3030/network-topology/sparql";
    
    public QueryResult queryOutdatedRouters() {
        // 🔍 Build SPARQL query
        String query = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
            "SELECT ?router ?firmware WHERE { " +
            "  ?router a :Router . " +
            "  ?router :hasFirmwareVersion ?firmware . " +
            "  FILTER (xsd:double(?firmware) < 2.0) " +
            "}";
        
        // 🌐 Execute over HTTP
        return executeQuery(query, "Outdated Routers");
    }
    
    private QueryResult executeQuery(String queryString, String description) {
        // 🔌 Connect to Fuseki endpoint
        try (RDFConnection conn = RDFConnectionFactory.connect(ENDPOINT)) {
            // 📨 Send HTTP POST with SPARQL query
            QueryExecution qexec = conn.query(queryString);
            ResultSet results = qexec.execSelect();
            
            // 📊 Process results
            // ... convert to Java objects ...
            
            return new QueryResult(description, solutions, resultVars);
        }
    }
}
```

### 5. SPARQLQueryHandler.java - The Query Library

**Purpose**: Provides pre-built queries for common network analysis tasks.

```java
public class SPARQLQueryHandler {
    
    // 🔍 Find routers with old firmware
    public void queryOutdatedRouters(Model model) {
        String query = buildQuery("outdated-routers", 
            "SELECT ?router ?firmware WHERE {",
            "  ?router a :Router .",
            "  ?router :hasFirmwareVersion ?firmware .",
            "  FILTER (xsd:double(?firmware) < 2.0)",
            "}");
        
        executeQuery(model, query, "🚨 Security Alert: Outdated Routers");
    }
    
    // 🌐 Discover network topology
    public void queryNetworkConnections(Model model) {
        String query = buildQuery("network-topology",
            "SELECT ?link ?source ?target ?bandwidth WHERE {",
            "  ?link a :NetworkLink .",
            "  ?link :connectsFrom ?source .",
            "  ?link :connectsTo ?target .",
            "  OPTIONAL { ?link :hasBandwidth ?bandwidth }",
            "} ORDER BY ?bandwidth");
        
        executeQuery(model, query, "🗺️ Network Topology Map");
    }
    
    // 📈 Find high-performance connections
    public void queryHighCapacityPaths(Model model, double minBandwidth) {
        String query = buildQuery("high-capacity",
            "SELECT ?link ?from ?to ?bandwidth WHERE {",
            "  ?link a :NetworkLink .",
            "  ?link :connectsFrom ?from .",
            "  ?link :connectsTo ?to .",
            "  ?link :hasBandwidth ?bandwidth .",
            "  FILTER (xsd:double(?bandwidth) >= " + minBandwidth + ")",
            "} ORDER BY DESC(?bandwidth)");
        
        executeQuery(model, query, "🚀 High-Performance Connections");
    }
}
```

---

## 💻 Hands-On Examples

### Example 1: Starting the System

```bash
# 📁 Navigate to project directory
cd network-topology-java

# 🔨 Compile everything
mvn clean compile

# 🚀 Start the Fuseki server
mvn exec:java '-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher'
```

**Expected Output:**
```
🔄 Loading network topology data...
✓ Loaded ontology data (313 triples)
✓ Loaded instance data (32 triples)
✓ Network topology data loaded successfully!
🚀 Fuseki server started on port 3030
📊 SPARQL endpoint: http://localhost:3030/network-topology/sparql
🌐 Web UI: http://localhost:3030
```

### Example 2: Testing with cURL

```bash
# 🔍 Count all triples in the database
curl -X POST http://localhost:3030/network-topology/sparql \
  -H "Content-Type: application/sparql-query" \
  -H "Accept: application/sparql-results+json" \
  -d "SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }"
```

**Response:**
```json
{
  "head": { "vars": [ "count" ] },
  "results": {
    "bindings": [
      {
        "count": {
          "type": "literal",
          "datatype": "http://www.w3.org/2001/XMLSchema#integer",
          "value": "345"
        }
      }
    ]
  }
}
```

### Example 3: Finding Outdated Routers

```bash
# 🚨 Security query: Find routers with firmware < 2.0
curl -X POST http://localhost:3030/network-topology/sparql \
  -H "Content-Type: application/sparql-query" \
  -H "Accept: application/sparql-results+json" \
  -d "PREFIX : <http://example.org/network-topology#>
      PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
      SELECT ?router ?firmware WHERE {
        ?router a :Router .
        ?router :hasFirmwareVersion ?firmware .
        FILTER (xsd:double(?firmware) < 2.0)
      }"
```

### Example 4: Python AI Agent

```python
import requests
import json

class NetworkTopologyAgent:
    def __init__(self, endpoint="http://localhost:3030/network-topology/sparql"):
        self.endpoint = endpoint
    
    def query_sparql(self, query):
        """Execute SPARQL query and return results"""
        response = requests.post(
            self.endpoint,
            data=query,
            headers={
                'Content-Type': 'application/sparql-query',
                'Accept': 'application/sparql-results+json'
            }
        )
        
        if response.status_code == 200:
            return response.json()
        else:
            raise Exception(f"Query failed: {response.status_code} - {response.text}")
    
    def find_security_risks(self):
        """🚨 Find routers with outdated firmware"""
        query = """
        PREFIX : <http://example.org/network-topology#>
        PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
        SELECT ?router ?firmware WHERE {
          ?router a :Router .
          ?router :hasFirmwareVersion ?firmware .
          FILTER (xsd:double(?firmware) < 2.0)
        }
        """
        
        results = self.query_sparql(query)
        
        print("🚨 Security Alert: Outdated Routers")
        for binding in results['results']['bindings']:
            router = binding['router']['value']
            firmware = binding['firmware']['value']
            print(f"  - {router}: firmware {firmware}")
    
    def analyze_network_performance(self):
        """📈 Find high-bandwidth connections"""
        query = """
        PREFIX : <http://example.org/network-topology#>
        PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
        SELECT ?link ?source ?target ?bandwidth WHERE {
          ?link a :NetworkLink .
          ?link :connectsFrom ?source .
          ?link :connectsTo ?target .
          ?link :hasBandwidth ?bandwidth .
          FILTER (xsd:double(?bandwidth) >= 1000)
        } ORDER BY DESC(?bandwidth)
        """
        
        results = self.query_sparql(query)
        
        print("🚀 High-Performance Network Links")
        for binding in results['results']['bindings']:
            link = binding['link']['value']
            source = binding['source']['value']
            target = binding['target']['value']
            bandwidth = binding['bandwidth']['value']
            print(f"  - {source} ←→ {target}: {bandwidth} Mbps")

# 🤖 Use the AI agent
if __name__ == "__main__":
    agent = NetworkTopologyAgent()
    
    try:
        agent.find_security_risks()
        print()
        agent.analyze_network_performance()
    except Exception as e:
        print(f"❌ Error: {e}")
```

### Example 5: JavaScript/Node.js Integration

```javascript
const axios = require('axios');

class NetworkTopologyClient {
    constructor(endpoint = 'http://localhost:3030/network-topology/sparql') {
        this.endpoint = endpoint;
    }
    
    async querySparql(query) {
        try {
            const response = await axios.post(this.endpoint, query, {
                headers: {
                    'Content-Type': 'application/sparql-query',
                    'Accept': 'application/sparql-results+json'
                }
            });
            return response.data;
        } catch (error) {
            throw new Error(`SPARQL query failed: ${error.message}`);
        }
    }
    
    async getNetworkInventory() {
        const query = `
            PREFIX : <http://example.org/network-topology#>
            SELECT ?device ?type ?name WHERE {
              ?device a ?type .
              OPTIONAL { ?device :hasName ?name }
              FILTER(?type IN (:Router, :Switch, :Server, :Firewall))
            }
        `;
        
        const results = await this.querySparql(query);
        
        console.log('📊 Network Device Inventory');
        console.log('═'.repeat(50));
        
        const inventory = {};
        results.results.bindings.forEach(binding => {
            const type = binding.type.value.split('#')[1]; // Extract class name
            const device = binding.device.value;
            const name = binding.name ? binding.name.value : 'Unnamed';
            
            if (!inventory[type]) inventory[type] = [];
            inventory[type].push({ device, name });
        });
        
        Object.keys(inventory).forEach(type => {
            console.log(`\n${type}s (${inventory[type].length}):`);
            inventory[type].forEach(item => {
                console.log(`  • ${item.name} (${item.device})`);
            });
        });
    }
    
    async findConnectivityPaths() {
        const query = `
            PREFIX : <http://example.org/network-topology#>
            SELECT ?path ?source ?target WHERE {
              ?path a :NetworkLink .
              ?path :connectsFrom ?source .
              ?path :connectsTo ?target .
            }
        `;
        
        const results = await this.querySparql(query);
        
        console.log('\n🌐 Network Connectivity Map');
        console.log('═'.repeat(50));
        
        results.results.bindings.forEach(binding => {
            const source = binding.source.value.split('#')[1];
            const target = binding.target.value.split('#')[1];
            console.log(`${source} ←→ ${target}`);
        });
    }
}

// 🚀 Usage example
(async () => {
    const client = new NetworkTopologyClient();
    
    try {
        await client.getNetworkInventory();
        await client.findConnectivityPaths();
    } catch (error) {
        console.error('❌ Error:', error.message);
    }
})();
```

---

## 🎓 Learning Progression

### 🌱 Beginner Level (Week 1-2)

**Goal**: Understand the basics of RDF and SPARQL

**Activities**:
1. **Run SimpleJenaTest.java**
   ```bash
   mvn exec:java '-Dexec.mainClass=com.example.networktopology.SimpleJenaTest'
   ```
   
2. **Explore the data files**:
   - Open `src/main/resources/network-topology.owl`
   - Open `src/main/resources/network-instances.ttl`
   - Understand the Turtle syntax

3. **Try basic SPARQL queries**:
   ```sparql
   # Count everything
   SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }
   
   # List all classes
   SELECT DISTINCT ?class WHERE { ?s a ?class }
   
   # Find all routers
   SELECT ?router WHERE { ?router a :Router }
   ```

**Key Concepts to Master**:
- ✅ RDF triples (Subject-Predicate-Object)
- ✅ Turtle syntax basics
- ✅ SPARQL SELECT queries
- ✅ Prefixes and namespaces

### 🌿 Intermediate Level (Week 3-4)

**Goal**: Work with HTTP endpoints and filters

**Activities**:
1. **Start the Fuseki server**:
   ```bash
   mvn exec:java '-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher'
   ```

2. **Use the web UI**: Visit `http://localhost:3030`

3. **Run the HTTP client**:
   ```bash
   mvn exec:java '-Dexec.mainClass=com.example.networktopology.client.NetworkTopologyClient'
   ```

4. **Try advanced queries**:
   ```sparql
   # Routers with specific firmware
   SELECT ?router ?firmware WHERE {
     ?router a :Router .
     ?router :hasFirmwareVersion ?firmware .
     FILTER (xsd:double(?firmware) >= 2.0)
   }
   
   # Optional patterns
   SELECT ?device ?name ?description WHERE {
     ?device :hasName ?name .
     OPTIONAL { ?device :hasDescription ?description }
   }
   ```

**Key Concepts to Master**:
- ✅ HTTP SPARQL endpoints
- ✅ Filters and functions
- ✅ Optional patterns
- ✅ Data type conversions (xsd:double, xsd:int)

### 🌳 Advanced Level (Week 5-6)

**Goal**: Modify data and create custom queries

**Activities**:
1. **Add new network devices**:
   - Edit `network-instances.ttl`
   - Add new routers, switches, or servers
   - Restart server and test

2. **Create custom ontology concepts**:
   - Edit `network-topology.owl`
   - Add new device types or properties
   - Test with queries

3. **Build aggregation queries**:
   ```sparql
   # Count devices by type
   SELECT ?type (COUNT(?device) as ?count) WHERE {
     ?device a ?type .
   } GROUP BY ?type
   
   # Average bandwidth
   SELECT (AVG(xsd:double(?bandwidth)) as ?avgBandwidth) WHERE {
     ?link :hasBandwidth ?bandwidth .
   }
   ```

**Key Concepts to Master**:
- ✅ Ontology modification
- ✅ Data insertion and updates
- ✅ Aggregation functions
- ✅ GROUP BY and HAVING

### 🌲 Expert Level (Week 7-8)

**Goal**: Integration and production concepts

**Activities**:
1. **Build AI agent integrations**:
   - Use the Python example
   - Create your own language bindings
   - Implement error handling

2. **Performance optimization**:
   - Test with larger datasets
   - Understand TDB2 indexing
   - Monitor query performance

3. **Security and deployment**:
   - Add authentication
   - Configure HTTPS
   - Deploy to cloud platforms

**Key Concepts to Master**:
- ✅ Production deployment
- ✅ Performance optimization
- ✅ Security configuration
- ✅ Integration patterns

---

## 🔧 Troubleshooting Guide

### 🚨 Common Issues and Solutions

#### 1. "Port 3030 already in use"

**Symptoms**:
```
Exception in thread "main" org.apache.jena.fuseki.FusekiException: 
Address already in use: bind
```

**Solutions**:
```bash
# Option 1: Use a different port
mvn exec:java '-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher' '-Dexec.args=3333'

# Option 2: Kill existing process
# Windows:
netstat -ano | findstr :3030
taskkill /PID <process_id> /F

# Linux/Mac:
lsof -ti:3030 | xargs kill -9
```

#### 2. "Unresolved prefixed name: xsd:double"

**Symptoms**:
```
org.apache.jena.query.QueryParseException: Unresolved prefixed name: xsd:double
```

**Solution**: Add the XSD prefix to your query:
```sparql
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
SELECT ?router ?firmware WHERE {
  ?router a :Router .
  ?router :hasFirmwareVersion ?firmware .
  FILTER (xsd:double(?firmware) < 2.0)  # Now xsd: is defined
}
```

#### 3. "No results from queries"

**Diagnostic Steps**:

1. **Check data loading**:
   ```sparql
   # Count total triples
   SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }
   ```

2. **List all classes**:
   ```sparql
   SELECT DISTINCT ?class WHERE { ?s a ?class }
   ```

3. **Check specific instances**:
   ```sparql
   SELECT ?s ?p ?o WHERE { 
     ?s a <http://example.org/network-topology#Router> 
   } LIMIT 10
   ```

#### 4. "Transaction lock errors"

**Symptoms**:
```
java.lang.IllegalMonitorStateException: attempt to unlock read lock, 
not locked by current thread
```

**Solution**: Ensure proper transaction management:
```java
// ✅ Correct pattern
dataset.begin(ReadWrite.WRITE);
try {
    // ... do work ...
    dataset.commit();
} catch (Exception e) {
    dataset.abort();
    throw e;
} finally {
    dataset.end();  // Always cleanup
}
```

#### 5. "Maven compilation errors"

**Common Issues**:

1. **Missing dependencies**: Run `mvn clean install`
2. **Wrong Java version**: Ensure Java 11+ is installed
3. **IDE issues**: Refresh Maven project in your IDE

**Check Java version**:
```bash
java -version
mvn -version
```

### 🔍 Debugging Tools

#### 1. Fuseki Web UI
- Visit: `http://localhost:3030`
- Use the query editor to test SPARQL
- Check dataset statistics
- View server logs

#### 2. Enable Debug Logging
Add to your Java startup:
```bash
-Dlog4j.logger.org.apache.jena=DEBUG
```

#### 3. Query Validation
Use online SPARQL validators:
- https://sparql.org/query-validator.html
- https://www.w3.org/2009/sparql/docs/query-validator.html

### 📊 Performance Tips

#### 1. Query Optimization
```sparql
# ❌ Inefficient (broad filter at end)
SELECT ?device ?type WHERE {
  ?device a ?type .
  ?device :hasName ?name .
  FILTER (regex(?name, "Router"))
}

# ✅ Efficient (specific type first)
SELECT ?device ?name WHERE {
  ?device a :Router .
  ?device :hasName ?name .
  FILTER (regex(?name, "Main"))
}
```

#### 2. Use LIMIT for Exploration
```sparql
# Always use LIMIT when exploring large datasets
SELECT ?s ?p ?o WHERE { 
  ?s ?p ?o 
} LIMIT 100
```

#### 3. Index-Friendly Patterns
```sparql
# ✅ Good: starts with specific bound values
?router a :Router .
?router :hasFirmwareVersion ?firmware .

# ❌ Avoid: starts with unbound variables
?property rdfs:domain ?router .
?router a :Router .
```

---

## 🌟 Advanced Use Cases

### 🤖 AI Agent Scenarios

#### 1. Network Security Monitoring
```python
class SecurityMonitor:
    def __init__(self, sparql_endpoint):
        self.agent = NetworkTopologyAgent(sparql_endpoint)
    
    def daily_security_check(self):
        # Find outdated firmware
        outdated = self.agent.find_security_risks()
        
        # Check for unauthorized devices
        unknown_devices = self.agent.find_unknown_devices()
        
        # Generate security report
        return self.generate_report(outdated, unknown_devices)
```

#### 2. Performance Optimization
```javascript
class PerformanceAnalyzer {
    async findBottlenecks() {
        // Find low-bandwidth links
        const slowLinks = await this.querySlowConnections();
        
        // Find overloaded devices
        const overloaded = await this.queryHighUtilization();
        
        // Suggest optimizations
        return this.suggestImprovements(slowLinks, overloaded);
    }
}
```

#### 3. Capacity Planning
```sparql
# Find devices nearing capacity limits
PREFIX : <http://example.org/network-topology#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
SELECT ?device ?currentLoad ?maxCapacity 
       ((xsd:double(?currentLoad) / xsd:double(?maxCapacity)) * 100 as ?utilizationPercent)
WHERE {
  ?device :hasCurrentLoad ?currentLoad .
  ?device :hasMaxCapacity ?maxCapacity .
  FILTER ((xsd:double(?currentLoad) / xsd:double(?maxCapacity)) > 0.8)
} ORDER BY DESC(?utilizationPercent)
```

### 🔮 Future Enhancements

#### 1. Real-time Data Integration
- Connect to network monitoring systems (SNMP, NetFlow)
- Stream live network data into TDB2
- Update network state in real-time

#### 2. Machine Learning Integration
- Export network graphs for ML algorithms
- Predict network failures
- Anomaly detection in network behavior

#### 3. Federated Queries
- Query multiple network domains
- Integrate with external knowledge bases
- Cross-reference with threat intelligence

#### 4. Advanced Reasoning
- Implement custom OWL rules
- Automatic network topology inference
- Configuration validation

---

## 📚 Additional Resources

### 📖 Essential Reading
1. **Apache Jena Documentation**: https://jena.apache.org/documentation/
2. **SPARQL 1.1 Specification**: https://www.w3.org/TR/sparql11-query/
3. **OWL 2 Web Ontology Language**: https://www.w3.org/TR/owl2-overview/
4. **RDF 1.1 Concepts**: https://www.w3.org/TR/rdf11-concepts/

### 🎥 Video Tutorials
1. **Apache Jena Tutorial Series**: Search for "Apache Jena tutorial" on YouTube
2. **SPARQL Query Language**: W3C SPARQL tutorials
3. **Semantic Web Basics**: Stanford CS224W or similar courses

### 🛠️ Tools and Utilities
1. **Protégé**: Ontology editor (https://protege.stanford.edu/)
2. **SPARQL Playground**: Online query testing
3. **RDF Validators**: Turtle syntax validators
4. **GraphDB**: Alternative triple store

### 🌐 Community
1. **Apache Jena Mailing Lists**: jena-users@apache.org
2. **Stack Overflow**: Tag your questions with "apache-jena"
3. **Semantic Web Community**: Join W3C community groups
4. **GitHub**: Contribute to open source projects

---

## 🏁 Conclusion

This project demonstrates the power of semantic web technologies for network topology management. By combining Apache Jena's robust framework with modern HTTP APIs, we've created a system that:

✅ **Enables flexible querying** of network infrastructure data  
✅ **Provides AI-ready APIs** for machine learning integration  
✅ **Uses standardized technologies** for long-term maintainability  
✅ **Scales from development to production** environments  

The modular architecture makes it easy to:
- 🔄 **Extend** with new device types and properties
- 🌐 **Integrate** with existing network management systems  
- 📊 **Analyze** complex network relationships
- 🤖 **Automate** network operations with AI agents

Whether you're building network monitoring tools, implementing AI-driven network optimization, or exploring semantic web technologies, this project provides a solid foundation for your semantic web journey.

**Happy coding! 🚀**

---

*This documentation is a living document. As you explore and extend the project, consider contributing improvements back to help others learn these powerful technologies.*
