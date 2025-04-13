import java.io._

object SaveFeatures {
  def saveFeatures(featuresAndLabels: Seq[(String, Int, Int, Int, Int, Int)]): Unit = {
    // Define the path where the CSV will be saved
    val writer = new PrintWriter(new File("/Users/kartikeyadatta/Documents/GitHub/extract-method-refactor-suggester/data/method_features.csv"))
    
    // Write header to the CSV
    writer.write("method_name,length,cyclomatic_complexity,nesting_level,num_variables,is_refactorable\n")
    
    // Write each method's features and label to the CSV
    featuresAndLabels.foreach { case (name, length, cyclomatic, nesting, variables, label) =>
      writer.write(s"$name,$length,$cyclomatic,$nesting,$variables,$label\n")
    }

    // Close the writer
    writer.close()
    println(s"Features and labels saved to: /Users/kartikeyadatta/Documents/GitHub/extract-method-refactor-suggester/data/method_features.csv")
  }
}
