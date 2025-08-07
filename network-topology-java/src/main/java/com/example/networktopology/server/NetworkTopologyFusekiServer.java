package com.example.networktopology.server;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;

import com.example.networktopology.loaders.OntologyLoader;
import com.example.networktopology.loaders.InstanceLoader;

import java.io.File;

/**
 * Fuseki SPARQL endpoint server for Network Topology data
 * Provides HTTP access for AI agents to query the network topology model
 */
public class NetworkTopologyFusekiServer {
    
    private static final String SERVER_NAME = "NetworkTopology";
    private static final String DATASET_PATH = "/network-topology";
    private static final int DEFAULT_PORT = 3030;
    private static final String TDB_DIRECTORY = "tdb-data";
    
    private FusekiServer server;
    private final OntologyLoader ontologyLoader;
    private final InstanceLoader instanceLoader;
    private final int port;
    
    /**
     * Constructor with default port
     */
    public NetworkTopologyFusekiServer() {
        this(DEFAULT_PORT);
    }
    
    /**
     * Constructor with custom port
     */
    public NetworkTopologyFusekiServer(int port) {
        this.port = port;
        this.ontologyLoader = new OntologyLoader();
        this.instanceLoader = new InstanceLoader();
        
        // Initialize Fuseki logging
        FusekiLogging.setLogging();
    }
    
    /**
     * Initialize and start the Fuseki server
     */
    public void startServer() {
        try {
            System.out.println("=== Starting Network Topology Fuseki Server ===");
            
            // Create or load dataset
            Dataset dataset = createDataset();
            
            // Load data into dataset
            loadNetworkTopologyData(dataset);
            
            // Build and start Fuseki server
            server = FusekiServer.create()
                .port(port)
                .add(DATASET_PATH, dataset)
                .enablePing(true)
                .enableMetrics(true)
                .enableStats(true)
                .build();
            
            server.start();
            
            System.out.println("✓ Fuseki server started successfully!");
            System.out.println("Server URL: http://localhost:" + port);
            System.out.println("Dataset URL: http://localhost:" + port + DATASET_PATH);
            System.out.println("SPARQL Query endpoint: http://localhost:" + port + DATASET_PATH + "/sparql");
            System.out.println("SPARQL Update endpoint: http://localhost:" + port + DATASET_PATH + "/update");
            System.out.println("Graph Store Protocol: http://localhost:" + port + DATASET_PATH + "/data");
            System.out.println("Server UI: http://localhost:" + port + "/$/server");
            System.out.println("Dataset UI: http://localhost:" + port + "/$/datasets" + DATASET_PATH);
            
        } catch (Exception e) {
            System.err.println("Failed to start Fuseki server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stop the Fuseki server
     */
    public void stopServer() {
        if (server != null) {
            System.out.println("Stopping Fuseki server...");
            server.stop();
            System.out.println("✓ Fuseki server stopped.");
        }
    }
    
    /**
     * Create dataset - using in-memory for demo, TDB2 for persistence
     */
    private Dataset createDataset() {
        // Option 1: In-memory dataset (good for demos)
        // return DatasetFactory.createTxnMem();
        
        // Option 2: TDB2 persistent dataset (good for production)
        File tdbDir = new File(TDB_DIRECTORY);
        if (!tdbDir.exists()) {
            tdbDir.mkdirs();
        }
        return TDB2Factory.connectDataset(TDB_DIRECTORY);
    }
    
    /**
     * Load network topology data into the dataset
     */
    private void loadNetworkTopologyData(Dataset dataset) {
        System.out.println("Loading network topology data into Fuseki dataset...");
        
        try {
            // Begin transaction with WRITE mode for TDB2
            dataset.begin(ReadWrite.WRITE);
            
            try {
                // Load ontology data
                Model ontologyModel = ontologyLoader.loadTurtleOntology();
                if (ontologyModel != null) {
                    dataset.getDefaultModel().add(ontologyModel);
                    System.out.println("✓ Loaded ontology data (" + ontologyModel.size() + " triples)");
                }
                
                // Load instance data
                Model instanceModel = instanceLoader.loadRDFInstances();
                if (instanceModel != null) {
                    dataset.getDefaultModel().add(instanceModel);
                    System.out.println("✓ Loaded instance data (" + instanceModel.size() + " triples)");
                }
                
                // Commit transaction
                dataset.commit();
                
                System.out.println("✓ Network topology data loaded successfully!");
                System.out.println("Total triples in dataset: " + dataset.getDefaultModel().size());
                
            } catch (Exception e) {
                // Abort transaction on error
                dataset.abort();
                throw e;
            } finally {
                // End transaction - this handles cleanup properly
                dataset.end();
            }
            
        } catch (Exception e) {
            System.err.println("Error loading data into dataset: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get server information
     */
    public ServerInfo getServerInfo() {
        return new ServerInfo(
            "http://localhost:" + port,
            "http://localhost:" + port + DATASET_PATH + "/sparql",
            "http://localhost:" + port + DATASET_PATH + "/update",
            "http://localhost:" + port + DATASET_PATH + "/data",
            server != null && server.getDataAccessPointRegistry() != null
        );
    }
    
    /**
     * Server information holder
     */
    public static class ServerInfo {
        public final String serverUrl;
        public final String sparqlQueryEndpoint;
        public final String sparqlUpdateEndpoint;
        public final String graphStoreEndpoint;
        public final boolean isRunning;
        
        public ServerInfo(String serverUrl, String sparqlQueryEndpoint, 
                         String sparqlUpdateEndpoint, String graphStoreEndpoint, 
                         boolean isRunning) {
            this.serverUrl = serverUrl;
            this.sparqlQueryEndpoint = sparqlQueryEndpoint;
            this.sparqlUpdateEndpoint = sparqlUpdateEndpoint;
            this.graphStoreEndpoint = graphStoreEndpoint;
            this.isRunning = isRunning;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Fuseki Server Info:\n" +
                "  Server URL: %s\n" +
                "  SPARQL Query: %s\n" +
                "  SPARQL Update: %s\n" +
                "  Graph Store: %s\n" +
                "  Running: %s",
                serverUrl, sparqlQueryEndpoint, sparqlUpdateEndpoint, 
                graphStoreEndpoint, isRunning
            );
        }
    }
    
    /**
     * Main method to run standalone Fuseki server
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        
        // Parse command line arguments for custom port
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0]);
                System.err.println("Using default port: " + DEFAULT_PORT);
                port = DEFAULT_PORT;
            }
        }
        
        NetworkTopologyFusekiServer fusekiServer = new NetworkTopologyFusekiServer(port);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutdown signal received...");
            fusekiServer.stopServer();
        }));
        
        // Start server
        fusekiServer.startServer();
        
        // Keep the server running
        System.out.println("\nPress Ctrl+C to stop the server...");
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
