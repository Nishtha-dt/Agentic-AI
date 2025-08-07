# Network Topology Fuseki SPARQL Endpoint

This project provides an Apache Jena Fuseki SPARQL endpoint for the Network Topology ontology, enabling AI agents to query network infrastructure data via HTTP.

## Quick Start

### 1. Start the Fuseki Server

```bash
# Compile the project
mvn clean compile

# Start the standalone Fuseki server (Windows PowerShell)
mvn exec:java '-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher'

# Or start on a custom port (Windows PowerShell)
mvn exec:java '-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher' '-Dexec.args=3333'

# For Linux/Mac/WSL
mvn exec:java -Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher
mvn exec:java -Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher -Dexec.args=3333
```

### 2. Access the Endpoints

Once the server is running, the following endpoints are available:

- **SPARQL Query Endpoint**: `http://localhost:3030/network-topology/sparql`
- **SPARQL Update Endpoint**: `http://localhost:3030/network-topology/update`
- **Graph Store Protocol**: `http://localhost:3030/network-topology/data`
- **Web UI**: `http://localhost:3030`

## AI Agent Integration

### HTTP Requests

AI agents can query the network topology data using HTTP POST requests:

```bash
curl -X POST http://localhost:3030/network-topology/sparql \
  -H "Content-Type: application/sparql-query" \
  -H "Accept: application/sparql-results+json" \
  -d "SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }"
```

### Python Example

```python
import requests
import json

def query_fuseki(sparql_query):
    endpoint = "http://localhost:3030/network-topology/sparql"
    
    response = requests.post(
        endpoint,
        data=sparql_query,
        headers={
            'Content-Type': 'application/sparql-query',
            'Accept': 'application/sparql-results+json'
        }
    )
    
    if response.status_code == 200:
        return response.json()
    else:
        print(f"Error: {response.status_code} - {response.text}")
        return None

# Example queries
queries = {
    "total_triples": "SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }",
    
    "outdated_routers": """
        PREFIX : <http://example.org/network-topology#>
        PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
        SELECT ?router ?firmware WHERE {
          ?router a :Router .
          ?router :hasFirmwareVersion ?firmware .
          FILTER (xsd:double(?firmware) < 2.0)
        }
    """,
    
    "all_devices": """
        PREFIX : <http://example.org/network-topology#>
        SELECT ?device ?type WHERE {
          ?device a ?type .
          FILTER(?type != <http://www.w3.org/2002/07/owl#NamedIndividual>)
        } LIMIT 10
    """,
    
    "network_connections": """
        PREFIX : <http://example.org/network-topology#>
        SELECT ?link ?source ?target ?bandwidth WHERE {
          ?link a :NetworkLink .
          ?link :connectsFrom ?source .
          ?link :connectsTo ?target .
          OPTIONAL { ?link :hasBandwidth ?bandwidth }
        }
    """
}

# Execute queries
for name, query in queries.items():
    print(f"\n--- {name.replace('_', ' ').title()} ---")
    result = query_fuseki(query)
    if result:
        print(json.dumps(result, indent=2))
```

### JavaScript/Node.js Example

```javascript
const axios = require('axios');

async function queryFuseki(sparqlQuery) {
    const endpoint = 'http://localhost:3030/network-topology/sparql';
    
    try {
        const response = await axios.post(endpoint, sparqlQuery, {
            headers: {
                'Content-Type': 'application/sparql-query',
                'Accept': 'application/sparql-results+json'
            }
        });
        return response.data;
    } catch (error) {
        console.error('Query failed:', error.message);
        return null;
    }
}

// Example usage
(async () => {
    const query = `
        PREFIX : <http://example.org/network-topology#>
        PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
        SELECT ?router ?firmware WHERE {
          ?router a :Router .
          ?router :hasFirmwareVersion ?firmware .
          FILTER (xsd:double(?firmware) < 2.0)
        }
    `;
    
    const result = await queryFuseki(query);
    console.log(JSON.stringify(result, null, 2));
})();
```

## Common SPARQL Queries for AI Agents

### 1. Security Analysis

```sparql
# Find routers with outdated firmware
PREFIX : <http://example.org/network-topology#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
SELECT ?router ?firmware WHERE {
  ?router a :Router .
  ?router :hasFirmwareVersion ?firmware .
  FILTER (xsd:double(?firmware) < 2.0)
}
```

### 2. Network Topology Discovery

```sparql
# Get network topology structure
PREFIX : <http://example.org/network-topology#>
SELECT ?link ?source ?target ?bandwidth WHERE {
  ?link a :NetworkLink .
  ?link :connectsFrom ?source .
  ?link :connectsTo ?target .
  OPTIONAL { ?link :hasBandwidth ?bandwidth }
} ORDER BY ?bandwidth
```

### 3. Device Inventory

```sparql
# List all network devices by type
PREFIX : <http://example.org/network-topology#>
SELECT ?device ?type ?name WHERE {
  ?device a ?type .
  OPTIONAL { ?device :hasName ?name }
  FILTER(?type IN (:Router, :Switch, :Server, :Firewall))
}
```

### 4. Performance Analysis

```sparql
# Find high-bandwidth connections
PREFIX : <http://example.org/network-topology#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
SELECT ?link ?source ?target ?bandwidth WHERE {
  ?link a :NetworkLink .
  ?link :connectsFrom ?source .
  ?link :connectsTo ?target .
  ?link :hasBandwidth ?bandwidth .
  FILTER (xsd:double(?bandwidth) >= 1000)
} ORDER BY DESC(?bandwidth)
```

### 5. Zone-based Analysis

```sparql
# Find devices in specific zones
PREFIX : <http://example.org/network-topology#>
SELECT ?device ?type ?zone WHERE {
  ?device :locatedIn ?zone .
  ?device a ?type .
  ?zone :hasName ?zoneName .
  FILTER(?type != <http://www.w3.org/2002/07/owl#NamedIndividual>)
}
```

## Ontology Namespaces

- **Ontology Classes/Properties**: `http://example.org/network-topology#`
- **Instance Data**: `http://example.org/network-topology/instances#`

## Running Components Separately

### 1. Just the Fuseki Server

```bash
# Windows PowerShell
mvn exec:java '-Dexec.mainClass=com.example.networktopology.server.NetworkTopologyFusekiServer'

# Linux/Mac/WSL
mvn exec:java -Dexec.mainClass=com.example.networktopology.server.NetworkTopologyFusekiServer
```

### 2. Just the Client Demo

```bash
# Windows PowerShell
mvn exec:java '-Dexec.mainClass=com.example.networktopology.client.NetworkTopologyClient'

# Linux/Mac/WSL
mvn exec:java -Dexec.mainClass=com.example.networktopology.client.NetworkTopologyClient
```

### 3. Full Application with Endpoint Demo

```bash
# Windows PowerShell
mvn exec:java '-Dexec.mainClass=com.example.networktopology.NetworkTopologyLoader'

# Linux/Mac/WSL
mvn exec:java -Dexec.mainClass=com.example.networktopology.NetworkTopologyLoader
```

## Data Persistence

The Fuseki server uses TDB2 for persistent storage. Data is stored in the `tdb-data` directory and will persist between server restarts.

## Troubleshooting

### Port Already in Use

If port 3030 is already in use, start the server on a different port:

```bash
# Windows PowerShell
mvn exec:java '-Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher' '-Dexec.args=3333'

# Linux/Mac/WSL
mvn exec:java -Dexec.mainClass=com.example.networktopology.launcher.FusekiServerLauncher -Dexec.args=3333
```

### Connection Issues

1. Ensure the Fuseki server is running
2. Check firewall settings
3. Verify the endpoint URL is correct
4. Use the web UI at `http://localhost:3030` to test queries manually

### Empty Results

If queries return no results:
1. Check that data was loaded correctly
2. Verify namespace prefixes in queries
3. Use the count query to check total triples: `SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }`

## Integration with AI Frameworks

This Fuseki endpoint can be integrated with various AI frameworks:

- **LangChain**: Use SPARQL tools to query the endpoint
- **AutoGen**: Create agents that query network topology data
- **Custom AI Agents**: Use HTTP requests to access network insights
- **Knowledge Graphs**: Integrate with larger knowledge graph systems

## Security Considerations

For production use:
1. Configure authentication and authorization
2. Use HTTPS endpoints
3. Implement rate limiting
4. Monitor query patterns for security issues
5. Restrict network access as needed
