error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1297.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1297.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1297.java
text:
```scala
S@@ystem.out.println(org.eclipse.jdt.core.tools.classfmt.disassembler.ClassFileDisassembler.disassemble(str));

package org.eclipse.jdt.internal.eval;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.util.*;
import java.util.*;

/**
 * A evaluator builds a compilation unit and compiles it into class files.
 * If the compilation unit has problems, reports the problems using the
 * requestor.
 */
public abstract class Evaluator {
	EvaluationContext context;
	INameEnvironment environment;
	Map options;
	IRequestor requestor;
	IProblemFactory problemFactory;
/**
 * Creates a new evaluator.
 */
Evaluator(EvaluationContext context, INameEnvironment environment, Map options, IRequestor requestor, IProblemFactory problemFactory) {
	this.context = context;
	this.environment = environment;
	this.options = options;
	this.requestor = requestor;
	this.problemFactory = problemFactory;
}
/**
 * Adds the given problem to the corresponding evaluation result in the given table. If the evaluation
 * result doesn't exist yet, adds it in the table. Its evaluation id and evaluation type
 * are computed so that they correspond to the given problem. If it is found to be an internal problem,
 * then the evaluation id of the result is the given compilation unit source.
 */
abstract protected void addEvaluationResultForCompilationProblem(Map resultsByIDs,IProblem problem, char[] cuSource);
/**
 * Returns the evaluation results that converts the given compilation result that has problems.
 * If the compilation result has more than one problem, then the problems are broken down so that
 * each evaluation result has the same evaluation id. 
 */
protected EvaluationResult[] evaluationResultsForCompilationProblems(CompilationResult result, char[] cuSource) {
	// Break down the problems and group them by ids in evaluation results
	IProblem[] problems = result.getProblems();
	HashMap resultsByIDs = new HashMap(5);
	for (int i = 0; i < problems.length; i++) {
		addEvaluationResultForCompilationProblem(resultsByIDs, problems[i], cuSource);
	}

	// Copy results
	int size = resultsByIDs.size();
	EvaluationResult[] evalResults = new EvaluationResult[size];
	Iterator results = resultsByIDs.values().iterator();
	for (int i = 0; i < size; i++) {
		evalResults[i] = (EvaluationResult)results.next();
	}

	return evalResults;
}
/**
 * Compiles and returns the class definitions for the current compilation unit.
 * Returns null if there are any errors.
 */
ClassFile[] getClasses() {
	final char[] source = getSource();
	final ArrayList classDefinitions = new ArrayList();

	// The requestor collects the class definitions and problems
	class CompilerRequestor implements ICompilerRequestor {
		boolean hasErrors = false;
		public void acceptResult(CompilationResult result) {
			if (result.hasProblems()) {
				EvaluationResult[] evalResults = evaluationResultsForCompilationProblems(result, source);
				for (int i = 0; i < evalResults.length; i++) {
					EvaluationResult evalResult = evalResults[i];
					IProblem[] problems = evalResult.getProblems();
					for (int j = 0; j < problems.length; j++) {
						Evaluator.this.requestor.acceptProblem(problems[j], evalResult.getEvaluationID(), evalResult.getEvaluationType());
					}
				}
			}
			if (result.hasErrors()) {
				hasErrors = true;
			} else {
				ClassFile[] classFiles = result.getClassFiles();
				for (int i = 0; i < classFiles.length; i++) {
					ClassFile classFile = classFiles[i];
/* 
			
					char[] filename = classFile.fileName();
					int length = filename.length;
					char[] relativeName = new char[length + 6];
					System.arraycopy(filename, 0, relativeName, 0, length);
					System.arraycopy(".class".toCharArray(), 0, relativeName, length, 6);
					CharOperation.replace(relativeName, '/', java.io.File.separatorChar);
					ClassFile.writeToDisk("d:/test/snippet", new String(relativeName), classFile.getBytes());
					String str = "d:/test/snippet" + "/" + new String(relativeName);
					System.out.println(com.ibm.compiler.java.classfmt.disassembler.ClassFileDisassembler.disassemble(str));				
 */	
					classDefinitions.add(classFile);
				}
			}
		}
	}

	// Compile compilation unit
	CompilerRequestor compilerRequestor = new CompilerRequestor();
	Compiler compiler = getCompiler(compilerRequestor);
	compiler.compile(new ICompilationUnit[] {new ICompilationUnit() {
		public char[] getFileName() {
			 // Name of class is name of CU
			return CharOperation.concat(Evaluator.this.getClassName(), ".java".toCharArray()); //$NON-NLS-1$
		}
		public char[] getContents() {
			return source;
		}
		public char[] getMainTypeName() {
			return Evaluator.this.getClassName();
		}
		public char[][] getPackageName() {
			return null;
		}
	}});
	if (compilerRequestor.hasErrors) {
		return null;
	} else {
		ClassFile[] result = new ClassFile[classDefinitions.size()];
		classDefinitions.toArray(result);
		return result;
	}
}
/**
 * Returns the name of the current class. This is the simple name of the class.
 * This doesn't include the extension ".java" nor the name of the package.
 */
abstract protected char[] getClassName();
/**
 * Creates and returns a compiler for this evaluator.
 */
Compiler getCompiler(ICompilerRequestor requestor) {
	return new Compiler(
		this.environment, 
		DefaultErrorHandlingPolicies.exitAfterAllProblems(), 
		this.options, 
		requestor, 
		this.problemFactory);
}
/**
 * Builds and returns the source for the current compilation unit.
 */
abstract protected char[] getSource();
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1297.java