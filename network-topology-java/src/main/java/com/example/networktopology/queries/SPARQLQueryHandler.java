package com.example.networktopology.queries;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;

import static com.example.networktopology.config.NetworkTopologyConfig.*;

/**
 * Handles SPARQL queries for network topology analysis and agent insights
 */
public class SPARQLQueryHandler {

    /**
     * Query for routers with firmware version less than 2.0.0
     */
    public void queryOutdatedRouters(Model model) {
        System.out.println("\n=== SPARQL Query: Routers with Firmware < 2.0.0 ===");
        
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
    
    /**
     * Query for devices by type
     */
    public void queryDevicesByType(Model model, String deviceType) {
        System.out.println("\n=== SPARQL Query: All " + deviceType + " Devices ===");
        
        String queryString = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "SELECT ?device ?name WHERE { " +
            "  ?device a :" + deviceType + " . " +
            "  OPTIONAL { ?device :hasName ?name } " +
            "}";
        
        executeQuery(model, queryString, deviceType + " Devices");
    }
    
    /**
     * Query for network connections and bandwidth
     */
    public void queryNetworkConnections(Model model) {
        System.out.println("\n=== SPARQL Query: Network Connections and Bandwidth ===");
        
        String queryString = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "SELECT ?link ?from ?to ?bandwidth WHERE { " +
            "  ?link a :NetworkLink . " +
            "  ?link :connectsFrom ?from . " +
            "  ?link :connectsTo ?to . " +
            "  OPTIONAL { ?link :hasBandwidth ?bandwidth } " +
            "}";
        
        executeQuery(model, queryString, "Network Connections");
    }
    
    /**
     * Query for devices in specific zones
     */
    public void queryDevicesInZone(Model model, String zoneName) {
        System.out.println("\n=== SPARQL Query: Devices in Zone '" + zoneName + "' ===");
        
        String queryString = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "SELECT ?device ?type WHERE { " +
            "  ?device :locatedIn ?zone . " +
            "  ?zone :hasName \"" + zoneName + "\" . " +
            "  ?device a ?type . " +
            "  FILTER(?type != <http://www.w3.org/2002/07/owl#NamedIndividual>) " +
            "}";
        
        executeQuery(model, queryString, "Devices in " + zoneName);
    }
    
    /**
     * Query for high-capacity network paths
     */
    public void queryHighCapacityPaths(Model model, double minBandwidth) {
        System.out.println("\n=== SPARQL Query: High-Capacity Paths (>= " + minBandwidth + " Mbps) ===");
        
        String queryString = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
            "SELECT ?link ?from ?to ?bandwidth WHERE { " +
            "  ?link a :NetworkLink . " +
            "  ?link :connectsFrom ?from . " +
            "  ?link :connectsTo ?to . " +
            "  ?link :hasBandwidth ?bandwidth . " +
            "  FILTER (xsd:double(?bandwidth) >= " + minBandwidth + ") " +
            "} ORDER BY DESC(?bandwidth)";
        
        executeQuery(model, queryString, "High-Capacity Paths");
    }
    
    /**
     * Query for security-related device configurations
     */
    public void querySecurityConfiguration(Model model) {
        System.out.println("\n=== SPARQL Query: Security Configuration Analysis ===");
        
        String queryString = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "SELECT ?device ?type ?firmware ?security WHERE { " +
            "  ?device a ?type . " +
            "  OPTIONAL { ?device :hasFirmwareVersion ?firmware } " +
            "  OPTIONAL { ?device :hasSecurityLevel ?security } " +
            "  FILTER(?type = :Router || ?type = :Switch || ?type = :Firewall) " +
            "}";
        
        executeQuery(model, queryString, "Security Configuration");
    }
    
    /**
     * Custom SPARQL query execution
     */
    public void executeCustomQuery(Model model, String customQuery, String description) {
        System.out.println("\n=== Custom SPARQL Query: " + description + " ===");
        executeQuery(model, customQuery, description);
    }
    
    /**
     * Generic query executor with result formatting
     */
    private void executeQuery(Model model, String queryString, String queryName) {
        try {
            Query query = QueryFactory.create(queryString);
            
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                
                int resultCount = 0;
                System.out.println("Query: " + queryName);
                System.out.println("Results:");
                
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    resultCount++;
                    
                    System.out.print("  " + resultCount + ". ");
                    
                    // Print all variables in the solution
                    results.getResultVars().forEach(var -> {
                        RDFNode node = soln.get(var);
                        if (node != null) {
                            String value = node.isLiteral() ? 
                                node.asLiteral().getString() : 
                                getLocalName(node.toString());
                            System.out.print(var + ": " + value + " | ");
                        }
                    });
                    System.out.println();
                }
                
                if (resultCount == 0) {
                    System.out.println("  No results found.");
                }
                
                System.out.println("Total results: " + resultCount);
                
            }
        } catch (Exception e) {
            System.err.println("Error executing SPARQL query '" + queryName + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Extract local name from URI for cleaner display
     */
    private String getLocalName(String uri) {
        if (uri.contains("#")) {
            return uri.substring(uri.lastIndexOf("#") + 1);
        } else if (uri.contains("/")) {
            return uri.substring(uri.lastIndexOf("/") + 1);
        }
        return uri;
    }
    
    /**
     * Run a comprehensive set of agent insight queries
     */
    public void runAgentInsightQueries(Model model) {
        System.out.println("\n=== Running Comprehensive Agent Insight Queries ===");
        
        // 1. Security analysis
        queryOutdatedRouters(model);
        querySecurityConfiguration(model);
        
        // 2. Network topology analysis
        queryNetworkConnections(model);
        queryHighCapacityPaths(model, 1000.0); // >= 1000 Mbps
        
        // 3. Device inventory
        queryDevicesByType(model, "Router");
        queryDevicesByType(model, "Switch");
        queryDevicesByType(model, "Server");
        
        // 4. Zone-based analysis
        queryDevicesInZone(model, "DataCenter");
        queryDevicesInZone(model, "DMZ");
        
        System.out.println("\n=== Agent Insight Queries Completed ===");
    }
}
