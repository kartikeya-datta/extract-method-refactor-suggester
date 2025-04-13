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


## Status
🔧 Work in progress. Stay tuned!
