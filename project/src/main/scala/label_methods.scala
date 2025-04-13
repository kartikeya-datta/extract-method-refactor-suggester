object MethodLabeling {
  def labelMethods(features: Seq[(String, Int, Int, Int, Int)]): Unit = {
    // Label methods as refactorable or not based on criteria (length > 20 or cyclomatic complexity > 10)
    val labels = features.map { case (name, length, cyclomatic, nesting, variables) =>
      val isRefactorable = if (length > 20 || cyclomatic > 10) 1 else 0
      (name, length, cyclomatic, nesting, variables, isRefactorable)
    }

    // Now pass the labeled data to the save function
    SaveFeatures.saveFeatures(labels)
  }
}
