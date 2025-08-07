package com.example.networktopology.reasoning;

import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;

import java.util.Iterator;

import static com.example.networktopology.config.NetworkTopologyConfig.*;

/**
 * Handles basic reasoning operations on network topology models
 */
public class BasicReasoner {
    
    /**
     * Load combined model with reasoning and perform validation
     */
    public InfModel createReasoningModel(OntModel ontModel) {
        System.out.println("\n=== Loading Combined Model with Reasoning ===");
        
        try {
            System.out.println("✓ Combined model loaded with reasoning!");
            System.out.println("  Number of statements: " + ontModel.size());
            
            // Validate the model
            Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
            InfModel infModel = ModelFactory.createInfModel(reasoner, ontModel);
            ValidityReport validity = infModel.validate();
            
            if (validity.isValid()) {
                System.out.println("✓ Model is valid!");
            } else {
                System.out.println("⚠ Model validation issues:");
                Iterator<ValidityReport.Report> reports = validity.getReports();
                while (reports.hasNext()) {
                    ValidityReport.Report report = reports.next();
                    System.out.println("  - " + report.getDescription());
                }
            }
            
            return infModel;
            
        } catch (Exception e) {
            System.err.println("✗ Error loading combined model: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Query with inference to find network devices with inferred types
     */
    public void queryInferredDeviceTypes(InfModel infModel) {
        if (infModel == null) return;
        
        // Query with inference
        String inferenceQuery = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
            "SELECT ?device ?type WHERE { " +
            "  ?device a ?type . " +
            "  ?type rdfs:subClassOf* :NetworkDevice . " +
            "  FILTER(?type != :NetworkDevice) " +
            "}";
        
        Query query = QueryFactory.create(inferenceQuery);
        QueryExecution qexec = QueryExecutionFactory.create(query, infModel);
        
        System.out.println("\n  Network devices with inferred types:");
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Resource device = soln.getResource("device");
            Resource type = soln.getResource("type");
            if (device.getLocalName() != null && type.getLocalName() != null) {
                System.out.println("  - " + device.getLocalName() + " : " + type.getLocalName());
            }
        }
        qexec.close();
    }
    
    /**
     * Create an advanced reasoning model with full OWL reasoning
     */
    public InfModel createAdvancedReasoningModel(OntModel ontModel) {
        try {
            // Create inference model with OWL reasoner
            Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
            InfModel infModel = ModelFactory.createInfModel(reasoner, ontModel);
            
            System.out.println("✓ Advanced reasoning model loaded!");
            System.out.println("  Total statements with inference: " + infModel.size());
            
            return infModel;
            
        } catch (Exception e) {
            System.err.println("✗ Error creating advanced reasoning model: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
