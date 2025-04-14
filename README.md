# extract-method-refactor-suggester
Here I am intending to make a model using which we can optimize the java code by looking for extract method refactoring opportunities in Java code using Code Property Graphs (CPGs) and multi-view graph embeddings.



# Extract Method Refactor Suggester

A machine learning-based project to detect and recommend Extract Method refactoring opportunities in Java code using Code Property Graphs (CPGs) and multi-view graph embeddings.

## Structure
- data/: Input Java code and datasets
- embeddings/: CPG-based code embeddings
- scripts/: Parsing and processing scripts
- models/: ML model training/evaluation
- notebooks/: Exploratory analysis
- results/: Visualizations, evaluation outputs


## 🔧 Setup Instructions for Joern
1. Download Joern from: https://github.com/joernio/joern/releases
2. Extract it to `~/tools/joern`
3. Add it to PATH or use `./joern` directly from that folder

Task	Command
- Parse Java to CPG	./joern-parse /path/to/code
- Launch Shell	./joern
- Import CPG	importCpg("cpg.bin.zip")
- Explore	cpg.method.name.l, cpg.call.code.l, etc.

Steps Performed in the CPG Creation Notebook
1) Describe the Dataset:
The notebook outlines the goal of creating a Code Property Graph (CPG) from a Java dataset downloaded from the CodRep repository (https://github.com/ASSERT-KTH/CodRep).
2) Convert File Formats:
Converts text files (.txt) containing Java code in the dataset (located at /Users/path.../data/code-rep-dataset/Dataset5/Tasks) to Java files (.java) for processing.
3) Generate the CPG:
Uses Joern to create a CPG file (cpg.bin) from the Java dataset, representing the codebase as a graph for analysis.
4) Load the CPG:
Loads the generated CPG file (intended at /Users/kartikeyadatta/Documents/GitHub/extract-method-refactor-suggester/data/code-rep-dataset/cpg.bin) for querying and feature extraction.
5) Extract Method Features:
Iterates through each method in the CPG and collects the following characteristics:
Method Name: The name of the method.
Number of Parameters: How many parameters the method accepts.
Line Number: The source code line where the method is defined.
Number of Calls: The count of method calls made within the method.
Number of Local Variables: The number of variables defined locally in the method.
Number of Return Statements: How many return statements the method contains.
Number of Invocations: The count of method calls (similar to number of calls).
Body Size: The total number of nodes in the method’s abstract syntax tree (AST), indicating method complexity.
Number of Control Structures: The count of control structures (e.g., if, for, while) in the method.
Full Name: The fully qualified name of the method, including its class and package.
6) Save Features to CSV:
Stores the extracted features in a CSV file (features_output.csv) at /Users/kartikeyadatta/Documents/GitHub/extract-method-refactor-suggester/features_output.csv.
The CSV includes a header row naming each feature, followed by one row per method with its corresponding values.


## Status
🔧 Work in progress. Stay tuned!


As Data Obtaining is done. I have got a CSV file which states differnt java methods with their features. Now my task is to build a machine learning model that can analyze Java methods and suggest potential refactoring opportunities based on features extracted from the code. Specifically, my focus is on Extract Method refactoring, which involves identifying large, complex methods that could be broken down into smaller, more manageable method. 

