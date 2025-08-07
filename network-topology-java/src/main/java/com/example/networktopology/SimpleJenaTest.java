package com.example.networktopology;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;

import java.io.InputStream;

/**
 * Simple test to verify Jena can read our files
 */
public class SimpleJenaTest {
    
    public static void main(String[] args) {
        System.out.println("=== Simple Jena Test ===");
        
        // Test 1: Load Turtle file using RDFDataMgr
        try {
            Model model = ModelFactory.createDefaultModel();
            InputStream in = SimpleJenaTest.class.getClassLoader()
                .getResourceAsStream("network-topology-ontology.ttl");
            
            if (in != null) {
                RDFDataMgr.read(model, in, Lang.TURTLE);
                System.out.println("✓ Turtle file loaded successfully using RDFDataMgr");
                System.out.println("  Statements: " + model.size());
                in.close();
            } else {
                System.err.println("✗ Could not find turtle file");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error loading turtle: " + e.getMessage());
        }
        
        // Test 2: Load instance file
        try {
            Model model = ModelFactory.createDefaultModel();
            InputStream in = SimpleJenaTest.class.getClassLoader()
                .getResourceAsStream("simple-rdf-instances.ttl");
            
            if (in != null) {
                RDFDataMgr.read(model, in, Lang.TURTLE);
                System.out.println("✓ Instance file loaded successfully using RDFDataMgr");
                System.out.println("  Statements: " + model.size());
                in.close();
            } else {
                System.err.println("✗ Could not find instance file");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error loading instances: " + e.getMessage());
        }
        
        // Test 3: Try basic Model.read with TURTLE
        try {
            Model model = ModelFactory.createDefaultModel();
            InputStream in = SimpleJenaTest.class.getClassLoader()
                .getResourceAsStream("simple-rdf-instances.ttl");
            
            if (in != null) {
                model.read(in, null, "TURTLE");
                System.out.println("✓ Basic Model.read with TURTLE works");
                System.out.println("  Statements: " + model.size());
                in.close();
            } else {
                System.err.println("✗ Could not find instance file for basic test");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error with basic Model.read: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
