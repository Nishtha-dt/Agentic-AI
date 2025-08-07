package com.example.networktopology.reasoning;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;

import static com.example.networktopology.config.NetworkTopologyConfig.*;

/**
 * Advanced network topology analysis using SPARQL queries
 */
public class NetworkTopologyAnalyzer {
    
    /**
     * Detect redundant paths in the network topology
     */
    public void detectRedundantPaths(InfModel infModel) {
        System.out.println("\n  🔍 Detecting Redundant Paths:");
        
        try {
            String redundantPathQuery = 
                "PREFIX : <" + ONTOLOGY_NS + "> " +
                "PREFIX nt: <" + INSTANCE_NS + "> " +
                "SELECT ?device1 ?device2 (COUNT(?path) AS ?pathCount) WHERE { " +
                "  ?device1 :connectedTo ?device2 . " +
                "  ?device1 :hasInterface ?int1 . " +
                "  ?device2 :hasInterface ?int2 . " +
                "  ?link :connectsInterface ?int1, ?int2 . " +
                "  BIND(?link AS ?path) " +
                "} GROUP BY ?device1 ?device2 " +
                "HAVING (COUNT(?path) > 1)";
            
            Query query = QueryFactory.create(redundantPathQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, infModel);
            
            ResultSet results = qexec.execSelect();
            boolean foundRedundant = false;
            while (results.hasNext()) {
                foundRedundant = true;
                QuerySolution soln = results.nextSolution();
                Resource device1 = soln.getResource("device1");
                Resource device2 = soln.getResource("device2");
                Literal pathCount = soln.getLiteral("pathCount");
                System.out.println("    ⚠️ Redundant paths found between " + 
                                 device1.getLocalName() + " and " + device2.getLocalName() + 
                                 " (" + pathCount.getInt() + " paths)");
            }
            if (!foundRedundant) {
                System.out.println("    ✅ No redundant paths detected");
            }
            qexec.close();
            
        } catch (Exception e) {
            System.err.println("    ✗ Error detecting redundant paths: " + e.getMessage());
        }
    }
    
    /**
     * Analyze unsupported topology configurations
     */
    public void analyzeUnsupportedTopologies(InfModel infModel) {
        System.out.println("\n  🔍 Analyzing Unsupported Topologies:");
        
        try {
            // Check for devices without proper interface configurations
            String unsupportedConfigQuery = 
                "PREFIX : <" + ONTOLOGY_NS + "> " +
                "PREFIX nt: <" + INSTANCE_NS + "> " +
                "SELECT ?device WHERE { " +
                "  ?device a :NetworkDevice . " +
                "  FILTER NOT EXISTS { ?device :hasInterface ?interface } " +
                "}";
            
            Query query = QueryFactory.create(unsupportedConfigQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, infModel);
            
            ResultSet results = qexec.execSelect();
            boolean foundUnsupported = false;
            while (results.hasNext()) {
                foundUnsupported = true;
                QuerySolution soln = results.nextSolution();
                Resource device = soln.getResource("device");
                System.out.println("    ⚠️ Device without interfaces: " + device.getLocalName());
            }
            
            // Check for isolated devices (no connections)
            String isolatedDevicesQuery = 
                "PREFIX : <" + ONTOLOGY_NS + "> " +
                "PREFIX nt: <" + INSTANCE_NS + "> " +
                "SELECT ?device WHERE { " +
                "  ?device a :NetworkDevice . " +
                "  FILTER NOT EXISTS { ?device :connectedTo ?other } " +
                "  FILTER NOT EXISTS { ?other :connectedTo ?device } " +
                "}";
            
            Query isolatedQuery = QueryFactory.create(isolatedDevicesQuery);
            QueryExecution isolatedQexec = QueryExecutionFactory.create(isolatedQuery, infModel);
            
            ResultSet isolatedResults = isolatedQexec.execSelect();
            while (isolatedResults.hasNext()) {
                foundUnsupported = true;
                QuerySolution soln = isolatedResults.nextSolution();
                Resource device = soln.getResource("device");
                System.out.println("    ⚠️ Isolated device (no connections): " + device.getLocalName());
            }
            
            if (!foundUnsupported) {
                System.out.println("    ✅ No unsupported topology configurations detected");
            }
            
            qexec.close();
            isolatedQexec.close();
            
        } catch (Exception e) {
            System.err.println("    ✗ Error analyzing topologies: " + e.getMessage());
        }
    }
    
    /**
     * Find missing links between zones
     */
    public void findMissingZoneLinks(InfModel infModel) {
        System.out.println("\n  🔍 Finding Missing Zone Links:");
        
        try {
            // Find zones that should be connected but aren't
            String missingLinksQuery = 
                "PREFIX : <" + ONTOLOGY_NS + "> " +
                "PREFIX nt: <" + INSTANCE_NS + "> " +
                "SELECT ?zone1 ?zone2 WHERE { " +
                "  ?zone1 a :Zone . " +
                "  ?zone2 a :Zone . " +
                "  FILTER(?zone1 != ?zone2) " +
                "  ?device1 :belongsToZone ?zone1 . " +
                "  ?device2 :belongsToZone ?zone2 . " +
                "  FILTER NOT EXISTS { " +
                "    ?device1 :connectedTo ?device2 " +
                "  } " +
                "  FILTER NOT EXISTS { " +
                "    ?device2 :connectedTo ?device1 " +
                "  } " +
                "}";
            
            Query query = QueryFactory.create(missingLinksQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, infModel);
            
            ResultSet results = qexec.execSelect();
            boolean foundMissing = false;
            while (results.hasNext()) {
                foundMissing = true;
                QuerySolution soln = results.nextSolution();
                Resource zone1 = soln.getResource("zone1");
                Resource zone2 = soln.getResource("zone2");
                System.out.println("    ⚠️ No direct connection between zones: " + 
                                 zone1.getLocalName() + " and " + zone2.getLocalName());
            }
            if (!foundMissing) {
                System.out.println("    ✅ All zones have appropriate connections");
            }
            qexec.close();
            
        } catch (Exception e) {
            System.err.println("    ✗ Error finding missing zone links: " + e.getMessage());
        }
    }
}
