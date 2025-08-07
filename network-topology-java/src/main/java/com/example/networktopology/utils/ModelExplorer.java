package com.example.networktopology.utils;

import org.apache.jena.rdf.model.*;

import static com.example.networktopology.config.NetworkTopologyConfig.*;

/**
 * Utility methods for exploring and displaying model information
 */
public class ModelExplorer {
    
    /**
     * Helper method to explore the model
     */
    public void exploreModel(Model model, String title) {
        System.out.println("\n=== " + title + " ===");
        
        if (model == null) {
            System.out.println("Model is null");
            return;
        }
        
        // Print some statistics
        System.out.println("Total statements: " + model.size());
        
        // List subjects
        System.out.println("\nSubjects in the model:");
        model.listSubjects().forEach(resource -> {
            if (resource.getURI() != null && 
                (resource.getURI().startsWith(ONTOLOGY_NS) || 
                 resource.getURI().startsWith(INSTANCE_NS))) {
                System.out.println("- " + resource.getLocalName());
            }
        });
    }
    
    /**
     * Display model statistics
     */
    public void displayModelStatistics(Model model, String modelName) {
        if (model == null) {
            System.out.println(modelName + " model is null");
            return;
        }
        
        System.out.println("\n" + modelName + " Statistics:");
        System.out.println("  Total statements: " + model.size());
        
        // Count subjects, predicates, objects
        long subjectCount = model.listSubjects().toList().size();
        System.out.println("  Unique subjects: " + subjectCount);
        
        // List namespaces
        System.out.println("  Namespaces used:");
        model.getNsPrefixMap().forEach((prefix, uri) -> {
            System.out.println("    " + prefix + ": " + uri);
        });
    }
}
