// Import the necessary Joern libraries
import io.shiftleft.semanticcpg.language._

object FeatureExtraction {
  def main(args: Array[String]): Unit = {
    // Load the Code Property Graph (CPG)
    val cpg = CpgLoader.load("/Users/kartikeyadatta/Documents/GitHub/extract-method-refactor-suggester/data/code-rep-dataset/cpg.bin")

    // Extract Method Length
    val methodLength = cpg.method.map(m => (m.name, m.lineCount))

    // Extract Cyclomatic Complexity
    val cyclomaticComplexity = cpg.method.map(m => (m.name, m.cyclomaticComplexity))

    // Find Duplicate Methods based on Code
    val duplicateMethods = cpg.method
      .groupBy(m => m.code)
      .filter { case (_, methods) => methods.size > 1 }
      .map { case (code, methods) => (code, methods.map(_.name)) }

    // Extract Nesting Levels
    val nestingLevels = cpg.method.map(m => (m.name, m.controlStructureDepth))

    // Count the Number of Variables in Each Method
    val numberOfVariables = cpg.method.map(m => (m.name, m.local.variable.size))

    // Return all extracted features as a tuple for further processing
    val features = methodLength.zip(cyclomaticComplexity)
      .zip(nestingLevels)
      .zip(numberOfVariables)
      .map {
        case (((methodName, length), (ccMethodName, cyclomatic)), (nestMethodName, nestingLevel)) =>
          (methodName, length, cyclomatic, nestingLevel, numberOfVariables.find(_._1 == methodName).map(_._2).getOrElse(0))
      }

    // Pass extracted features to the labeling function (passing as parameter here)
    MethodLabeling.labelMethods(features)
  }
}
