package com.example.networktopology.loaders;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;

import java.io.FileInputStream;
import java.io.InputStream;

import static com.example.networktopology.config.NetworkTopologyConfig.*;

/**
 * Handles loading of RDF instance data
 */
public class InstanceLoader {
    
    /**
     * Load RDF instances from Turtle file
     */
    public Model loadRDFInstances() {
        System.out.println("\n=== Loading RDF Instances ===");
        
        // Create model
        Model model = ModelFactory.createDefaultModel();
        
        try {
            // Try to load from resources first, then from file system
            InputStream in = getClass().getClassLoader().getResourceAsStream(INSTANCES_TTL_RESOURCE);
            if (in == null) {
                in = new FileInputStream(INSTANCES_TTL_RESOURCE);
            }
            
            // Use RDFDataMgr for more reliable Turtle parsing
            RDFDataMgr.read(model, in, Lang.TURTLE);
            in.close();
            
            System.out.println("✓ RDF instances loaded successfully!");
            System.out.println("  Number of statements: " + model.size());
            
            return model;
            
        } catch (Exception e) {
            System.err.println("✗ Error loading RDF instances: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Display routers from the loaded model
     */
    public void displayRouters(Model model) {
        if (model == null) return;
        
        // Query for routers
        String queryString = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "SELECT ?router ?hostname ?firmware WHERE { " +
            "  ?router a :Router ; " +
            "          :hostname ?hostname ; " +
            "          :firmwareVersion ?firmware . " +
            "}";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        
        System.out.println("\n  Routers found:");
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Resource router = soln.getResource("router");
            Literal hostname = soln.getLiteral("hostname");
            Literal firmware = soln.getLiteral("firmware");
            System.out.println("  - " + router.getLocalName() + 
                             " (hostname: " + hostname.getString() + 
                             ", firmware: " + firmware.getString() + ")");
        }
        qexec.close();
    }
    
    /**
     * Display network links from the loaded model
     */
    public void displayNetworkLinks(Model model) {
        if (model == null) return;
        
        // Query for links
        String linkQuery = 
            "PREFIX : <" + ONTOLOGY_NS + "> " +
            "PREFIX nt: <" + INSTANCE_NS + "> " +
            "SELECT ?link ?interface1 ?interface2 WHERE { " +
            "  ?link a :NetworkLink ; " +
            "        :connectsInterface ?interface1, ?interface2 . " +
            "  FILTER(?interface1 != ?interface2) " +
            "}";
        
        Query linkQ = QueryFactory.create(linkQuery);
        QueryExecution linkQexec = QueryExecutionFactory.create(linkQ, model);
        
        System.out.println("\n  Network links found:");
        ResultSet linkResults = linkQexec.execSelect();
        while (linkResults.hasNext()) {
            QuerySolution soln = linkResults.nextSolution();
            Resource link = soln.getResource("link");
            Resource int1 = soln.getResource("interface1");
            Resource int2 = soln.getResource("interface2");
            System.out.println("  - " + link.getLocalName() + 
                             " connects " + int1.getLocalName() + 
                             " to " + int2.getLocalName());
        }
        linkQexec.close();
    }
}
