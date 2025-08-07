# Network Topology Ontology - Refactored Modular Design

## Overview

This project has been refactored from a single large Java class into a modular, maintainable structure. The original `NetworkTopologyLoader.java` file (600+ lines) has been split into specialized components with clear responsibilities.

## New Project Structure

```
src/main/java/com/example/networktopology/
├── config/
│   └── NetworkTopologyConfig.java          # Configuration constants
├── loaders/
│   ├── OntologyLoader.java                  # Ontology file loading
│   └── InstanceLoader.java                  # RDF instance loading
├── reasoning/
│   ├── BasicReasoner.java                   # Basic reasoning operations
│   ├── NetworkTopologyAnalyzer.java         # Advanced topology analysis
│   └── NetworkValidator.java                # Constraint validation & hierarchy
├── utils/
│   └── ModelExplorer.java                   # Model exploration utilities
└── NetworkTopologyLoader.java               # Main application (refactored)
```

## Key Benefits of Refactoring

### 1. **Separation of Concerns**
- **Configuration**: All constants and configuration in one place
- **Loading**: Separate loaders for different file types and purposes
- **Reasoning**: Modular reasoning components for different analysis types
- **Utilities**: Reusable helper methods

### 2. **Reduced Complexity**
- **Original**: Single 600+ line class with multiple responsibilities
- **Refactored**: 7 focused classes, each under 150 lines
- **Easier to understand**: Each class has a single, clear purpose

### 3. **Better Maintainability**
- **Focused Classes**: Changes to specific functionality affect only relevant classes
- **Clear Dependencies**: Easy to see what each component depends on
- **Testing**: Each component can be unit tested independently

### 4. **Improved Reusability**
- **Modular Components**: Can be used in different combinations
- **Interface-based**: Easy to extend or replace components
- **Configuration-driven**: Easy to modify behavior without code changes

## Component Descriptions

### NetworkTopologyConfig
- Contains all namespace URIs and file paths
- Centralized configuration management
- Easy to modify for different environments

### OntologyLoader
- Handles loading of OWL and Turtle ontology files
- Provides both simple Model and OntModel loading
- Supports resource-based and file system loading

### InstanceLoader
- Loads RDF instance data
- Provides specialized query methods for routers and network links
- Handles display of loaded instance data

### BasicReasoner
- Creates reasoning models with different OWL reasoners
- Performs basic inference and validation
- Queries for inferred device types

### NetworkTopologyAnalyzer
- Advanced SPARQL-based network analysis
- Detects redundant paths
- Analyzes unsupported topologies
- Finds missing zone links

### NetworkValidator
- Validates network constraints and rules
- Infers network hierarchy based on connectivity
- Identifies critical single points of failure

### ModelExplorer
- Utility methods for exploring RDF models
- Displays model statistics and information
- Reusable across different components

### NetworkTopologyLoader (Refactored)
- **Main orchestrator** that coordinates all components
- **Simplified main method** with clear demonstration flow
- **Dependency injection** pattern for component management
- **Clean separation** between file loading, reasoning, and analysis

## Usage Examples

### Simple Ontology Loading
```java
OntologyLoader loader = new OntologyLoader();
OntModel model = loader.loadOWLOntology();
```

### Instance Data Analysis
```java
InstanceLoader instanceLoader = new InstanceLoader();
Model instances = instanceLoader.loadRDFInstances();
instanceLoader.displayRouters(instances);
instanceLoader.displayNetworkLinks(instances);
```

### Advanced Reasoning
```java
BasicReasoner reasoner = new BasicReasoner();
NetworkTopologyAnalyzer analyzer = new NetworkTopologyAnalyzer();

InfModel infModel = reasoner.createAdvancedReasoningModel(ontModel);
analyzer.detectRedundantPaths(infModel);
analyzer.analyzeUnsupportedTopologies(infModel);
```

## Migration from Original Code

### What Changed
1. **Constants moved** to `NetworkTopologyConfig`
2. **File loading split** between `OntologyLoader` and `InstanceLoader`
3. **Reasoning split** into `BasicReasoner`, `NetworkTopologyAnalyzer`, and `NetworkValidator`
4. **Utilities extracted** to `ModelExplorer`
5. **Main class simplified** to orchestrate components

### What Stayed the Same
- **All original functionality** is preserved
- **Same Maven dependencies** and project structure
- **Same resource files** and configuration
- **Same output** and behavior when run

### Backward Compatibility
- The refactored `NetworkTopologyLoader.main()` produces the same output
- Individual components can be used independently
- Easy to add wrapper methods for backward compatibility if needed

## Development Benefits

### For Adding New Features
- **Add new analyzer**: Create new class in `reasoning` package
- **Add new file format**: Extend appropriate loader class
- **Add new queries**: Add methods to relevant analyzer class

### For Testing
- **Unit test each component** independently
- **Mock dependencies** easily with interfaces
- **Test specific functionality** without loading entire system

### For Debugging
- **Isolate issues** to specific components
- **Smaller code units** are easier to debug
- **Clear data flow** between components

## Best Practices Demonstrated

1. **Single Responsibility Principle**: Each class has one clear purpose
2. **Dependency Injection**: Components are injected rather than hard-coded
3. **Configuration Management**: Centralized configuration constants
4. **Error Handling**: Appropriate error handling in each component
5. **Resource Management**: Proper closing of input streams and query executions
6. **Logging**: Clear console output for monitoring and debugging

This refactored structure makes the code much more maintainable, testable, and extensible while preserving all original functionality.
