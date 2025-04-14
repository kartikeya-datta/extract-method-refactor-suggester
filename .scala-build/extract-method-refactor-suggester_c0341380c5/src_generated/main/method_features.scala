

final class method_features$_ {
def args = method_features_sc.args$
def scriptPath = """method_features.sc"""
/*<script>*/
import io.shiftleft.semanticcpg.language._

object MethodMetricsExtractor {
  def main(args: Array[String]): Unit = {
    // Assuming you are running this in the Joern environment
    val cpg = io.shiftleft.semanticcpg.CpgLoader.loadCpg("path_to_your_cpg.bin")

    val out = new java.io.PrintWriter("method_features.csv")

    // Writing the header of the CSV
    out.println("method_name,file_name,line_count,parameter_count,return_count,local_var_count,call_count,if_count,loop_count,try_catch_count,literal_count,nesting_depth,cyclomatic_complexity,fan_in,fan_out")

    // Utility function to safely extract integer values
    def safeInt(opt: Option[Int]): Int = opt.getOrElse(0)

    // Iterating over each method in the CPG
    cpg.method.foreach { m =>
      val name = m.name
      val file = m.file.name.headOption.getOrElse("NA")

      val lineCount = safeInt(m.endLine) - safeInt(m.lineNumber) + 1
      val paramCount = m.parameter.size
      val returnCount = m.methodReturn.code.l.count(_.contains("return"))
      val localVarCount = m.local.size
      val callCount = m.call.size
      val ifCount = m.controlStructure.isIf.size
      val loopCount = m.controlStructure.name(".*(for|while).*").size
      val tryCatchCount = m.controlStructure.name(".*(try|catch).*").size
      val literalCount = m.literal.size

      val allBlocks = m.ast.isBlock.l
      val nestingDepth = allBlocks.map(b => b.ast.isBlock.size).maxOption.getOrElse(1) - 1

      // Cyclomatic complexity formula: 1 + number of decisions (if, loops, try-catch)
      val cyclomaticComplexity = 1 + ifCount + loopCount + tryCatchCount + returnCount

      val fanOut = callCount
      val fanIn = m.calledBy.size

      // Writing the metrics to the CSV
      out.println(s"$name,$file,$lineCount,$paramCount,$returnCount,$localVarCount,$callCount,$ifCount,$loopCount,$tryCatchCount,$literalCount,$nestingDepth,$cyclomaticComplexity,$fanIn,$fanOut")
    }

    // Closing the file after writing the metrics
    out.close()

    println("Metrics extraction complete. Output saved to 'method_features.csv'.")
  }
}

/*</script>*/ /*<generated>*//*</generated>*/
}

object method_features_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new method_features$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export method_features_sc.script as `method_features`

