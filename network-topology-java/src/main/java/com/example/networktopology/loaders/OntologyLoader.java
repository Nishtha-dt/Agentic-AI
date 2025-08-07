package com.example.networktopology.loaders;

import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;

import java.io.FileInputStream;
import java.io.InputStream;

import static com.example.networktopology.config.NetworkTopologyConfig.*;

/**
 * Handles loading of ontology files in different formats
 */
public class OntologyLoader {
    
    /**
     * Load OWL ontology using FileManager
     */
    public OntModel loadOWLOntology() {
        System.out.println("=== Loading OWL Ontology ===");
        
        // Create ontology model
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        
        try {
            // Try to load from resources first, then from file system
            InputStream in = getClass().getClassLoader().getResourceAsStream(ONTOLOGY_OWL_RESOURCE);
            if (in == null) {
                in = new FileInputStream(ONTOLOGY_OWL_RESOURCE);
            }
            
            model.read(in, null, "RDF/XML");
            in.close();
            
            System.out.println("✓ Ontology loaded successfully!");
            System.out.println("  Number of statements: " + model.size());
            
            // List all classes
            System.out.println("\n  Classes in the ontology:");
            model.listClasses().forEach(ontClass -> {
                if (ontClass.getURI() != null && ontClass.getURI().startsWith(ONTOLOGY_NS)) {
                    System.out.println("  - " + ontClass.getLocalName());
                }
            });
            
            // List object properties
            System.out.println("\n  Object Properties:");
            model.listObjectProperties().forEach(prop -> {
                if (prop.getURI() != null && prop.getURI().startsWith(ONTOLOGY_NS)) {
                    System.out.println("  - " + prop.getLocalName());
                }
            });
            
            return model;
            
        } catch (Exception e) {
            System.err.println("✗ Error loading OWL file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load Turtle ontology file
     */
    public Model loadTurtleOntology() {
        System.out.println("\n=== Loading Turtle Ontology ===");
        
        // Create model
        Model model = ModelFactory.createDefaultModel();
        
        try {
            // Try to load from resources first, then from file system
            InputStream in = getClass().getClassLoader().getResourceAsStream(ONTOLOGY_TTL_RESOURCE);
            if (in == null) {
                in = new FileInputStream(ONTOLOGY_TTL_RESOURCE);
            }
            
            // Use RDFDataMgr for more reliable Turtle parsing
            RDFDataMgr.read(model, in, Lang.TURTLE);
            in.close();
            
            System.out.println("✓ Turtle ontology loaded successfully!");
            System.out.println("  Number of statements: " + model.size());
            
            // Count different types of statements
            long classCount = model.listStatements(null, RDF.type, RDFS.Class).toList().size();
            long propertyCount = model.listStatements(null, RDF.type, RDF.Property).toList().size();
            
            System.out.println("  Classes defined: " + classCount);
            System.out.println("  Properties defined: " + propertyCount);
            
            return model;
            
        } catch (Exception e) {
            System.err.println("✗ Error loading Turtle file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load combined ontology and instances into an OntModel with reasoning
     */
    public OntModel loadCombinedOntologyModel(OntModelSpec spec) {
        try {
            // Create ontology model with reasoning
            OntModel ontModel = ModelFactory.createOntologyModel(spec);
            
            // Load ontology
            InputStream ontIn = getClass().getClassLoader().getResourceAsStream(ONTOLOGY_TTL_RESOURCE);
            if (ontIn == null) {
                ontIn = new FileInputStream(ONTOLOGY_TTL_RESOURCE);
            }
            // Load ontology using RDFDataMgr
            RDFDataMgr.read(ontModel, ontIn, Lang.TURTLE);
            ontIn.close();
            
            // Load instances using RDFDataMgr
            InputStream instIn = getClass().getClassLoader().getResourceAsStream(INSTANCES_TTL_RESOURCE);
            if (instIn == null) {
                instIn = new FileInputStream(INSTANCES_TTL_RESOURCE);
            }
            RDFDataMgr.read(ontModel, instIn, Lang.TURTLE);
            instIn.close();
            
            return ontModel;
            
        } catch (Exception e) {
            System.err.println("✗ Error loading combined model: " + e.getMessage());
            return null;
        }
    }
}
