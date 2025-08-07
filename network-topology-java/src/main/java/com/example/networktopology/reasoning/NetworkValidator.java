package com.example.networktopology.reasoning;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.reasoner.ValidityReport;

import java.util.Iterator;

import static com.example.networktopology.config.NetworkTopologyConfig.*;

/**
 * Validates network constraints and infers network hierarchy
 */
public class NetworkValidator {
    
    /**
     * Validate network constraints and rules
     */
    public void validateNetworkConstraints(InfModel infModel) {
        System.out.println("\n  🔍 Validating Network Constraints:");
        
        try {
            // Validate model consistency
            ValidityReport validity = infModel.validate();
            if (validity.isValid()) {
                System.out.println("    ✅ All network constraints satisfied");
            } else {
                System.out.println("    ⚠️ Network constraint violations found:");
                Iterator<ValidityReport.Report> reports = validity.getReports();
                while (reports.hasNext()) {
                    ValidityReport.Report report = reports.next();
                    System.out.println("      - " + report.getDescription());
                }
            }
            
            // Check for devices with incompatible configurations
            String constraintQuery = 
                "PREFIX : <" + ONTOLOGY_NS + "> " +
                "PREFIX nt: <" + INSTANCE_NS + "> " +
                "SELECT ?device ?interface1 ?interface2 WHERE { " +
                "  ?device :hasInterface ?interface1, ?interface2 . " +
                "  ?interface1 :ipAddress ?ip1 . " +
                "  ?interface2 :ipAddress ?ip2 . " +
                "  FILTER(?interface1 != ?interface2 && ?ip1 = ?ip2) " +
                "}";
            
            Query query = QueryFactory.create(constraintQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, infModel);
            
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Resource device = soln.getResource("device");
                Resource int1 = soln.getResource("interface1");
                Resource int2 = soln.getResource("interface2");
                System.out.println("    ⚠️ IP address conflict on device " + device.getLocalName() + 
                                 ": " + int1.getLocalName() + " and " + int2.getLocalName());
            }
            qexec.close();
            
        } catch (Exception e) {
            System.err.println("    ✗ Error validating constraints: " + e.getMessage());
        }
    }
    
    /**
     * Infer network hierarchy and relationships
     */
    public void inferNetworkHierarchy(InfModel infModel) {
        System.out.println("\n  🔍 Inferring Network Hierarchy:");
        
        try {
            // Find network core devices (highly connected)
            String hierarchyQuery = 
                "PREFIX : <" + ONTOLOGY_NS + "> " +
                "PREFIX nt: <" + INSTANCE_NS + "> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "SELECT ?device ?deviceType (COUNT(?connection) AS ?connectionCount) WHERE { " +
                "  ?device a ?deviceType . " +
                "  ?deviceType rdfs:subClassOf* :NetworkDevice . " +
                "  ?device :connectedTo ?connection . " +
                "} GROUP BY ?device ?deviceType " +
                "ORDER BY DESC(?connectionCount)";
            
            Query query = QueryFactory.create(hierarchyQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, infModel);
            
            ResultSet results = qexec.execSelect();
            System.out.println("    📊 Network hierarchy (by connectivity):");
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Resource device = soln.getResource("device");
                Resource deviceType = soln.getResource("deviceType");
                Literal connectionCount = soln.getLiteral("connectionCount");
                
                String role = "";
                int connections = connectionCount.getInt();
                if (connections >= 3) {
                    role = " (Core Device)";
                } else if (connections == 2) {
                    role = " (Distribution Device)";
                } else {
                    role = " (Access Device)";
                }
                
                System.out.println("      - " + device.getLocalName() + 
                                 " (" + deviceType.getLocalName() + "): " + 
                                 connections + " connections" + role);
            }
            qexec.close();
            
            // Infer critical path dependencies
            analyzeCriticalPaths(infModel);
            
        } catch (Exception e) {
            System.err.println("    ✗ Error inferring hierarchy: " + e.getMessage());
        }
    }
    
    /**
     * Analyze critical single points of failure
     */
    private void analyzeCriticalPaths(InfModel infModel) {
        try {
            String criticalPathQuery = 
                "PREFIX : <" + ONTOLOGY_NS + "> " +
                "PREFIX nt: <" + INSTANCE_NS + "> " +
                "SELECT ?device WHERE { " +
                "  ?device a :Router . " +
                "  ?device :connectedTo ?other . " +
                "  ?other a :NetworkDevice . " +
                "  FILTER NOT EXISTS { " +
                "    ?other :connectedTo ?alternative . " +
                "    FILTER(?alternative != ?device) " +
                "  } " +
                "}";
            
            Query criticalQuery = QueryFactory.create(criticalPathQuery);
            QueryExecution criticalQexec = QueryExecutionFactory.create(criticalQuery, infModel);
            
            ResultSet criticalResults = criticalQexec.execSelect();
            System.out.println("\n    ⚠️ Critical single points of failure:");
            boolean foundCritical = false;
            while (criticalResults.hasNext()) {
                foundCritical = true;
                QuerySolution soln = criticalResults.nextSolution();
                Resource device = soln.getResource("device");
                System.out.println("      - " + device.getLocalName() + " (no redundant paths)");
            }
            if (!foundCritical) {
                System.out.println("      ✅ No critical single points of failure detected");
            }
            criticalQexec.close();
            
        } catch (Exception e) {
            System.err.println("    ✗ Error analyzing critical paths: " + e.getMessage());
        }
    }
}
