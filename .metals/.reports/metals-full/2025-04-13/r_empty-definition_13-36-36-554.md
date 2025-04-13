error id: method.
file://<WORKSPACE>/project/src/main/scala/feature_extraction.scala
empty definition using pc, found symbol in pc: method.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -io/shiftleft/semanticcpg/language/cpg/method.
	 -cpg/method.
	 -scala/Predef.cpg.method.
offset: 849
uri: file://<WORKSPACE>/project/src/main/scala/feature_extraction.scala
text:
```scala
// Import the necessary Joern libraries
import io.shiftleft.semanticcpg.language._

object FeatureExtraction {
  def main(args: Array[String]): Unit = {
    val cpg = CpgLoader.load("<WORKSPACE>/data/code-rep-dataset/cpg.bin")// Load your Code Property Graph here, e.g., using Joern APIs

    // 1. Method Length
    val methodLength = cpg.method.map(m => (m.name, m.lineCount))

    // 2. Cyclomatic Complexity
    val cyclomaticComplexity = cpg.method.map(m => (m.name, m.cyclomaticComplexity))

    // 3. Repetitive Code (Duplicate Methods)
    val duplicateMethods = cpg.method
      .groupBy(m => m.code)
      .filter { case (_, methods) => methods.size > 1 }
      .map { case (code, methods) => (code, methods.map(_.name)) }

    // 4. Nesting Levels
    val nestingLevels = cpg.m@@ethod.map(m => (m.name, m.controlStructureDepth))

    // 5. Number of Variables
    val numberOfVariables = cpg.method.map(m => (m.name, m.local.variable.size))

    // Save the extracted features to a CSV
    val features = methodLength.zip(cyclomaticComplexity).zip(nestingLevels).zip(numberOfVariables)
    // Process and write features to file (see save_features.scala)
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: method.