package com.example.networktopology.config;

/**
 * Configuration constants for the Network Topology application
 */
public class NetworkTopologyConfig {
    
    // Namespace URIs
    public static final String ONTOLOGY_NS = "http://example.org/network-topology#";
    public static final String INSTANCE_NS = "http://example.org/network-topology/instances#";
    
    // File paths (adjust these to match your file locations)
    public static final String ONTOLOGY_OWL_FILE = "src/main/resources/network-topology-ontology.owl";
    public static final String ONTOLOGY_TTL_FILE = "src/main/resources/network-topology-ontology.ttl";
    public static final String INSTANCES_TTL_FILE = "src/main/resources/simple-rdf-instances.ttl";
    
    // Resource file names
    public static final String ONTOLOGY_OWL_RESOURCE = "network-topology-ontology.owl";
    public static final String ONTOLOGY_TTL_RESOURCE = "network-topology-ontology.ttl";
    public static final String INSTANCES_TTL_RESOURCE = "simple-rdf-instances.ttl";
    
    private NetworkTopologyConfig() {
        // Utility class, no instantiation
    }
}
