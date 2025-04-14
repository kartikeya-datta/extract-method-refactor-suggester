error id: 
file://<WORKSPACE>/build.sbt
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 91
uri: file://<WORKSPACE>/build.sbt
text:
```scala
name := "MethodMetricsExtractor"

version := "0.1"

scalaVersion := "3.6.4"

// Adding the @@custom resolver for ShiftLeft Maven Repo
resolvers += "ShiftLeft Maven Repo" at "https://shiftleft.io/maven"

// Adding the library dependency for joern-cli
libraryDependencies ++= Seq(
  "io.shiftleft" %% "joern-cli" % "1.0.0"
)

```


#### Short summary: 

empty definition using pc, found symbol in pc: 