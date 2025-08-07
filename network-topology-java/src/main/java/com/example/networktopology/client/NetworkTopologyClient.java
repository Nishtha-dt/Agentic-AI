package com.example.networktopology.client;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Client for interacting with Network Topology Fuseki SPARQL endpoint
 * Demonstrates how AI agents can query the network topology data
 */
public class NetworkTopologyClient {
    
    private final String endpointUrl;
    
    /**
     * Constructor
     * @param endpointUrl The Fuseki SPARQL endpoint URL
     */
    public NetworkTopologyClient(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
    
    /**
     * Execute a SPARQL query against the endpoint
     */
    public QueryResult executeQuery(String sparqlQuery, String description) {
        System.out.println("\n=== Executing Query: " + description + " ===");
        System.out.println("Endpoint: " + endpointUrl);
        System.out.println("Query: " + sparqlQuery);
        
        List<QuerySolution> results = new ArrayList<>();
        List<String> resultVars = new ArrayList<>();
        
        try (RDFConnection conn = RDFConnectionFactory.connect(endpointUrl)) {
            try (QueryExecution qexec = conn.query(sparqlQuery)) {
                ResultSet resultSet = qexec.execSelect();
                
                if (resultSet.hasNext()) {
                    resultVars = resultSet.getResultVars();
                }
                
                while (resultSet.hasNext()) {
                    QuerySolution solution = resultSet.nextSolution();
                    results.add(solution);
                }
            }
            
            QueryResult queryResult = new QueryResult(description, results, resultVars);
            queryResult.printResults();
            return queryResult;
            
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return new QueryResult(description, results, resultVars);
        }
    }
    
    /**
     * Test connection to the endpoint
     */
    public boolean testConnection() {
        try (RDFConnection conn = RDFConnectionFactory.connect(endpointUrl)) {
            String testQuery = "SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }";
            try (QueryExecution qexec = conn.query(testQuery)) {
                ResultSet results = qexec.execSelect();
                if (results.hasNext()) {
                    QuerySolution solution = results.nextSolution();
                    long count = solution.getLiteral("count").getLong();
                    System.out.println("✓ Connection successful! Total triples: " + count);
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("✗ Connection failed: " + e.getMessage());
            return false;
        }
        return false;
    }
    
    /**
     * Query for outdated routers (firmware < 2.0.0)
     */
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
    
    /**
     * Query for all network devices
     */
    public QueryResult queryAllDevices() {
        String query = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX nt: <http://example.org/network-topology/instances#> " +
            "SELECT ?device ?type ?name WHERE { " +
            "  ?device a ?type . " +
            "  OPTIONAL { ?device :hasName ?name } " +
            "  FILTER(?type != <http://www.w3.org/2002/07/owl#NamedIndividual>) " +
            "} LIMIT 20";
        
        return executeQuery(query, "All Network Devices");
    }
    
    /**
     * Query for network topology structure
     */
    public QueryResult queryNetworkTopology() {
        String query = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX nt: <http://example.org/network-topology/instances#> " +
            "SELECT ?link ?source ?target ?bandwidth WHERE { " +
            "  ?link a :NetworkLink . " +
            "  ?link :connectsFrom ?source . " +
            "  ?link :connectsTo ?target . " +
            "  OPTIONAL { ?link :hasBandwidth ?bandwidth } " +
            "} ORDER BY ?bandwidth";
        
        return executeQuery(query, "Network Topology Structure");
    }
    
    /**
     * Query for high-bandwidth connections
     */
    public QueryResult queryHighBandwidthConnections(double minBandwidth) {
        String query = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX nt: <http://example.org/network-topology/instances#> " +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
            "SELECT ?link ?source ?target ?bandwidth WHERE { " +
            "  ?link a :NetworkLink . " +
            "  ?link :connectsFrom ?source . " +
            "  ?link :connectsTo ?target . " +
            "  ?link :hasBandwidth ?bandwidth . " +
            "  FILTER (xsd:double(?bandwidth) >= " + minBandwidth + ") " +
            "} ORDER BY DESC(?bandwidth)";
        
        return executeQuery(query, "High-Bandwidth Connections (>= " + minBandwidth + " Mbps)");
    }
    
    /**
     * Query for devices in a specific zone
     */
    public QueryResult queryDevicesInZone(String zoneName) {
        String query = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX nt: <http://example.org/network-topology/instances#> " +
            "SELECT ?device ?type WHERE { " +
            "  ?device :locatedIn ?zone . " +
            "  ?zone :hasName \"" + zoneName + "\" . " +
            "  ?device a ?type . " +
            "  FILTER(?type != <http://www.w3.org/2002/07/owl#NamedIndividual>) " +
            "}";
        
        return executeQuery(query, "Devices in Zone: " + zoneName);
    }
    
    /**
     * Run a comprehensive set of AI agent queries
     */
    public void runAgentInsightQueries() {
        System.out.println("\n=== AI Agent Insight Queries via Fuseki Endpoint ===");
        
        // Test connection first
        if (!testConnection()) {
            System.err.println("Cannot connect to Fuseki endpoint. Make sure the server is running.");
            return;
        }
        
        // Execute various queries that AI agents might need
        queryOutdatedRouters();
        queryAllDevices();
        queryNetworkTopology();
        queryHighBandwidthConnections(1000.0);
        queryDevicesInZone("DataCenter");
        queryDevicesInZone("DMZ");
        
        System.out.println("\n=== AI Agent Queries Completed ===");
    }
    
    /**
     * Query result container
     */
    public static class QueryResult {
        private final String description;
        private final List<QuerySolution> solutions;
        private final List<String> resultVars;
        
        public QueryResult(String description, List<QuerySolution> solutions, List<String> resultVars) {
            this.description = description;
            this.solutions = solutions;
            this.resultVars = resultVars;
        }
        
        public void printResults() {
            System.out.println("\nResults for: " + description);
            System.out.println("Found " + solutions.size() + " results:");
            
            if (solutions.isEmpty()) {
                System.out.println("  No results found.");
                return;
            }
            
            for (int i = 0; i < solutions.size(); i++) {
                QuerySolution solution = solutions.get(i);
                System.out.print("  " + (i + 1) + ". ");
                
                for (String var : resultVars) {
                    RDFNode node = solution.get(var);
                    if (node != null) {
                        String value = node.isLiteral() ? 
                            node.asLiteral().getString() : 
                            getLocalName(node.toString());
                        System.out.print(var + ": " + value + " | ");
                    }
                }
                System.out.println();
            }
        }
        
        private String getLocalName(String uri) {
            if (uri.contains("#")) {
                return uri.substring(uri.lastIndexOf("#") + 1);
            } else if (uri.contains("/")) {
                return uri.substring(uri.lastIndexOf("/") + 1);
            }
            return uri;
        }
        
        // Getters
        public String getDescription() { return description; }
        public List<QuerySolution> getSolutions() { return solutions; }
        public List<String> getResultVars() { return resultVars; }
        public int getResultCount() { return solutions.size(); }
    }
    
    /**
     * Demo/test main method
     */
    public static void main(String[] args) {
        String endpointUrl = "http://localhost:3030/network-topology/sparql";
        
        if (args.length > 0) {
            endpointUrl = args[0];
        }
        
        System.out.println("=== Network Topology Fuseki Client Demo ===");
        System.out.println("Connecting to: " + endpointUrl);
        
        NetworkTopologyClient client = new NetworkTopologyClient(endpointUrl);
        client.runAgentInsightQueries();
    }
}
