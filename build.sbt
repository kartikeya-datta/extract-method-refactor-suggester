name := "MethodMetricsExtractor"

version := "0.1"

scalaVersion := "3.6.4"

// Adding the custom resolver for ShiftLeft Maven Repo
resolvers += "ShiftLeft Maven Repo" at "https://shiftleft.io/maven"

// Adding the library dependency for joern-cli
libraryDependencies += "io.joern" %% "joern-cli" % "4.0.3203"
