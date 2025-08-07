package com.example.networktopology;

import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.*;

import com.example.networktopology.config.NetworkTopologyConfig;
import com.example.networktopology.loaders.OntologyLoader;
import com.example.networktopology.loaders.InstanceLoader;
import com.example.networktopology.reasoning.BasicReasoner;
import com.example.networktopology.reasoning.NetworkTopologyAnalyzer;
import com.example.networktopology.reasoning.NetworkValidator;
import com.example.networktopology.utils.ModelExplorer;
import com.example.networktopology.queries.SPARQLQueryHandler;
import com.example.networktopology.server.NetworkTopologyFusekiServer;
import com.example.networktopology.client.NetworkTopologyClient;

/**
 * Main application for loading and analyzing Network Topology Ontology
 * 
 * This refactored class demonstrates modular design with:
 * 1. Separate loaders for different file types
 * 2. Dedicated reasoning components
 * 3. Specialized analysis tools
 * 4. Clean separation of concerns
 */
public class NetworkTopologyLoader {
    
    private final OntologyLoader ontologyLoader;
    private final InstanceLoader instanceLoader;
    private final BasicReasoner basicReasoner;
    private final NetworkTopologyAnalyzer analyzer;
    private final NetworkValidator validator;
    private final ModelExplorer explorer;
    private final SPARQLQueryHandler queryHandler;
    
    /**
     * Constructor - initialize all components
     */
    public NetworkTopologyLoader() {
        this.ontologyLoader = new OntologyLoader();
        this.instanceLoader = new InstanceLoader();
        this.basicReasoner = new BasicReasoner();
        this.analyzer = new NetworkTopologyAnalyzer();
        this.validator = new NetworkValidator();
        this.explorer = new ModelExplorer();
        this.queryHandler = new SPARQLQueryHandler();
    }
    
    public static void main(String[] args) {
        NetworkTopologyLoader loader = new NetworkTopologyLoader();
        
        try {
            System.out.println("=== Network Topology Ontology Loader (Modular Version) ===\n");
            
            // Method 1: Load and display OWL ontology
            loader.demonstrateOWLLoading();
            
            // Method 2: Load and display Turtle ontology
            loader.demonstrateTurtleLoading();
            
            // Method 3: Load and display RDF instances
            loader.demonstrateInstanceLoading();
            
            // Method 4: Combined model with basic reasoning
            loader.demonstrateBasicReasoning();
            
            // Method 5: SPARQL Queries for Agent Insight
            loader.demonstrateSPARQLQueries();
            
            // Method 6: Fuseki SPARQL Endpoint Demo
            loader.demonstrateFusekiEndpoint();
            
            // Method 7: Advanced network topology analysis
            loader.demonstrateAdvancedAnalysis();
            
            System.out.println("\n=== All operations completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Error in main execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrate OWL ontology loading
     */
    public void demonstrateOWLLoading() {
        OntModel owlModel = ontologyLoader.loadOWLOntology();
        if (owlModel != null) {
            explorer.displayModelStatistics(owlModel, "OWL Ontology");
        }
    }
    
    /**
     * Demonstrate Turtle ontology loading
     */
    public void demonstrateTurtleLoading() {
        Model turtleModel = ontologyLoader.loadTurtleOntology();
        if (turtleModel != null) {
            explorer.displayModelStatistics(turtleModel, "Turtle Ontology");
        }
    }
    
    /**
     * Demonstrate RDF instance loading
     */
    public void demonstrateInstanceLoading() {
        Model instanceModel = instanceLoader.loadRDFInstances();
        if (instanceModel != null) {
            instanceLoader.displayRouters(instanceModel);
            instanceLoader.displayNetworkLinks(instanceModel);
            explorer.displayModelStatistics(instanceModel, "RDF Instances");
        }
    }
    
    /**
     * Demonstrate basic reasoning capabilities
     */
    public void demonstrateBasicReasoning() {
        // Load combined ontology and instances
        OntModel combinedModel = ontologyLoader.loadCombinedOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        
        if (combinedModel != null) {
            // Create reasoning model
            InfModel reasoningModel = basicReasoner.createReasoningModel(combinedModel);
            
            if (reasoningModel != null) {
                // Query for inferred device types
                basicReasoner.queryInferredDeviceTypes(reasoningModel);
            }
        }
    }
    
    /**
     * Demonstrate SPARQL queries for agent insight
     */
    public void demonstrateSPARQLQueries() {
        System.out.println("\n=== SPARQL Queries for Agent Insight ===");
        
        // Load combined model with instances
        Model instanceModel = instanceLoader.loadRDFInstances();
        
        if (instanceModel != null) {
            // Run the specific query requested by the user
            queryHandler.queryOutdatedRouters(instanceModel);
            
            // Run comprehensive agent insight queries
            queryHandler.runAgentInsightQueries(instanceModel);
            
            // Demonstrate custom queries
            demonstrateCustomQueries(instanceModel);
        }
    }
    
    /**
     * Demonstrate Fuseki SPARQL endpoint for AI agents
     */
    public void demonstrateFusekiEndpoint() {
        System.out.println("\n=== Fuseki SPARQL Endpoint Demo ===");
        
        try {
            // Create and start Fuseki server in a separate thread
            NetworkTopologyFusekiServer fusekiServer = new NetworkTopologyFusekiServer(3030);
            
            Thread serverThread = new Thread(() -> {
                fusekiServer.startServer();
            });
            serverThread.setDaemon(true);
            serverThread.start();
            
            // Wait a moment for server to start
            Thread.sleep(3000);
            
            // Demonstrate client connectivity
            String endpointUrl = "http://localhost:3030/network-topology/sparql";
            NetworkTopologyClient client = new NetworkTopologyClient(endpointUrl);
            
            System.out.println("\n--- Testing AI Agent Connectivity ---");
            client.runAgentInsightQueries();
            
            // Display endpoint information
            NetworkTopologyFusekiServer.ServerInfo serverInfo = fusekiServer.getServerInfo();
            System.out.println("\n--- Endpoint Information for AI Agents ---");
            System.out.println(serverInfo);
            
            System.out.println("\n--- Example cURL Commands for AI Agents ---");
            printCurlExamples(endpointUrl);
            
            // Note: In a real scenario, you'd want to keep the server running
            // For demo purposes, we'll stop it after demonstration
            System.out.println("\nNote: Fuseki server is running in the background.");
            System.out.println("In production, you would keep this server running for AI agents to access.");
            
        } catch (Exception e) {
            System.err.println("Error demonstrating Fuseki endpoint: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Print example cURL commands for AI agents
     */
    private void printCurlExamples(String endpointUrl) {
        System.out.println("\n1. Query for outdated routers:");
        System.out.println("curl -X POST " + endpointUrl + " \\");
        System.out.println("  -H \"Content-Type: application/sparql-query\" \\");
        System.out.println("  -H \"Accept: application/sparql-results+json\" \\");
        System.out.println("  -d 'PREFIX : <http://example.org/network-topology#>"); 
        System.out.println("      SELECT ?router ?firmware WHERE {");
        System.out.println("        ?router a :Router .");
        System.out.println("        ?router :hasFirmwareVersion ?firmware .");
        System.out.println("        FILTER (xsd:double(?firmware) < 2.0)");
        System.out.println("      }'");
        
        System.out.println("\n2. Query for all network devices:");
        System.out.println("curl -X POST " + endpointUrl + " \\");
        System.out.println("  -H \"Content-Type: application/sparql-query\" \\");
        System.out.println("  -H \"Accept: application/sparql-results+json\" \\");
        System.out.println("  -d 'PREFIX : <http://example.org/network-topology#>"); 
        System.out.println("      SELECT ?device ?type WHERE {");
        System.out.println("        ?device a ?type .");
        System.out.println("        FILTER(?type != <http://www.w3.org/2002/07/owl#NamedIndividual>)");
        System.out.println("      } LIMIT 10'");
        
        System.out.println("\n3. Count total triples:");
        System.out.println("curl -X POST " + endpointUrl + " \\");
        System.out.println("  -H \"Content-Type: application/sparql-query\" \\");
        System.out.println("  -H \"Accept: application/sparql-results+json\" \\");
        System.out.println("  -d 'SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }'");
    }
    
    /**
     * Demonstrate custom SPARQL queries
     */
    private void demonstrateCustomQueries(Model model) {
        System.out.println("\n=== Custom SPARQL Query Examples ===");
        
        // Query 1: Find all devices with specific properties
        String customQuery1 = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX nt: <http://example.org/network-topology/instances#> " +
            "SELECT ?device ?type ?name WHERE { " +
            "  ?device a ?type . " +
            "  OPTIONAL { ?device :hasName ?name } " +
            "  FILTER(?type != <http://www.w3.org/2002/07/owl#NamedIndividual>) " +
            "} LIMIT 10";
        
        queryHandler.executeCustomQuery(model, customQuery1, "All Network Devices");
        
        // Query 2: Network topology structure
        String customQuery2 = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX nt: <http://example.org/network-topology/instances#> " +
            "SELECT ?link ?source ?target ?bandwidth WHERE { " +
            "  ?link a :NetworkLink . " +
            "  ?link :connectsFrom ?source . " +
            "  ?link :connectsTo ?target . " +
            "  OPTIONAL { ?link :hasBandwidth ?bandwidth } " +
            "} ORDER BY ?bandwidth";
        
        queryHandler.executeCustomQuery(model, customQuery2, "Network Topology Structure");
        
        // Query 3: Security analysis (the exact query from user request)
        String userRequestedQuery = 
            "PREFIX : <http://example.org/network-topology#> " +
            "PREFIX nt: <http://example.org/network-topology/instances#> " +
            "SELECT ?router WHERE { " +
            "  ?router a :Router . " +
            "  ?router :hasFirmwareVersion ?v . " +
            "  FILTER (?v < \"2.0.0\") " +
            "}";
        
        queryHandler.executeCustomQuery(model, userRequestedQuery, "User Requested Query - Routers with Firmware < 2.0.0");
    }
    
    /**
     * Demonstrate advanced network topology analysis
     */
    public void demonstrateAdvancedAnalysis() {
        System.out.println("\n=== Advanced Network Topology Reasoning ===");
        
        // Load combined model for advanced reasoning
        OntModel ontModel = ontologyLoader.loadCombinedOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
        
        if (ontModel != null) {
            // Create advanced reasoning model
            InfModel advancedModel = basicReasoner.createAdvancedReasoningModel(ontModel);
            
            if (advancedModel != null) {
                // Perform various network topology analyses
                analyzer.detectRedundantPaths(advancedModel);
                analyzer.analyzeUnsupportedTopologies(advancedModel);
                analyzer.findMissingZoneLinks(advancedModel);
                validator.validateNetworkConstraints(advancedModel);
                validator.inferNetworkHierarchy(advancedModel);
            }
        }
    }
}
