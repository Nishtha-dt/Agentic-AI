package com.example.networktopology.launcher;

import com.example.networktopology.server.NetworkTopologyFusekiServer;

/**
 * Standalone launcher for Network Topology Fuseki Server
 * Use this to start the SPARQL endpoint server for AI agents
 */
public class FusekiServerLauncher {
    
    public static void main(String[] args) {
        System.out.println("=== Network Topology Fuseki Server Launcher ===");
        
        int port = 3030;
        
        // Parse command line arguments
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                System.out.println("Using custom port: " + port);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0]);
                System.err.println("Using default port: " + port);
            }
        }
        
        // Create and start the server
        NetworkTopologyFusekiServer server = new NetworkTopologyFusekiServer(port);
        
        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nReceived shutdown signal...");
            server.stopServer();
            System.out.println("Server stopped gracefully.");
        }));
        
        // Start the server
        server.startServer();
        
        // Print usage information for AI agents
        printAIAgentInstructions(port);
        
        // Keep the server running
        System.out.println("\n=== Server is running ===");
        System.out.println("Press Ctrl+C to stop the server");
        
        try {
            // Keep the main thread alive
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Server interrupted.");
        }
    }
    
    /**
     * Print instructions for AI agents
     */
    private static void printAIAgentInstructions(int port) {
        String baseUrl = "http://localhost:" + port;
        String datasetUrl = baseUrl + "/network-topology";
        String sparqlUrl = datasetUrl + "/sparql";
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("AI AGENT CONNECTION INSTRUCTIONS");
        System.out.println("=".repeat(60));
        
        System.out.println("\n🔗 ENDPOINTS:");
        System.out.println("  SPARQL Query:  " + sparqlUrl);
        System.out.println("  SPARQL Update: " + datasetUrl + "/update");
        System.out.println("  Graph Store:   " + datasetUrl + "/data");
        System.out.println("  Web UI:        " + baseUrl);
        
        System.out.println("\n📡 SAMPLE SPARQL QUERIES:");
        
        System.out.println("\n1. Count all triples:");
        System.out.println("   SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }");
        
        System.out.println("\n2. Find routers with old firmware:");
        System.out.println("   PREFIX : <http://example.org/network-topology#>");
        System.out.println("   SELECT ?router ?firmware WHERE {");
        System.out.println("     ?router a :Router .");
        System.out.println("     ?router :hasFirmwareVersion ?firmware .");
        System.out.println("     FILTER (xsd:double(?firmware) < 2.0)");
        System.out.println("   }");
        
        System.out.println("\n3. List all network devices:");
        System.out.println("   PREFIX : <http://example.org/network-topology#>");
        System.out.println("   SELECT ?device ?type WHERE {");
        System.out.println("     ?device a ?type .");
        System.out.println("     FILTER(?type != <http://www.w3.org/2002/07/owl#NamedIndividual>)");
        System.out.println("   } LIMIT 10");
        
        System.out.println("\n🌐 HTTP REQUEST EXAMPLES:");
        
        System.out.println("\nUsing cURL:");
        System.out.println("curl -X POST " + sparqlUrl + " \\");
        System.out.println("  -H \"Content-Type: application/sparql-query\" \\");
        System.out.println("  -H \"Accept: application/sparql-results+json\" \\");
        System.out.println("  -d \"SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }\"");
        
        System.out.println("\nUsing Python requests:");
        System.out.println("import requests");
        System.out.println("query = 'SELECT (COUNT(*) as ?count) WHERE { ?s ?p ?o }'");
        System.out.println("response = requests.post(");
        System.out.println("    '" + sparqlUrl + "',");
        System.out.println("    data=query,");
        System.out.println("    headers={");
        System.out.println("        'Content-Type': 'application/sparql-query',");
        System.out.println("        'Accept': 'application/sparql-results+json'");
        System.out.println("    }");
        System.out.println(")");
        System.out.println("print(response.json())");
        
        System.out.println("\n📚 ONTOLOGY NAMESPACES:");
        System.out.println("  Ontology: http://example.org/network-topology#");
        System.out.println("  Instances: http://example.org/network-topology/instances#");
        
        System.out.println("\n" + "=".repeat(60));
    }
}
