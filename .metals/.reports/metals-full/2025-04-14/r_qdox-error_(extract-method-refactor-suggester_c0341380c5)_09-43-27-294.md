error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/149.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/149.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/149.java
text:
```scala
g@@etMapper().startPosOffset + codeSnippet.length - 1);

package org.eclipse.jdt.internal.eval;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.classfmt.*;
import org.eclipse.jdt.internal.compiler.util.CharOperation;
import org.eclipse.jdt.internal.compiler.util.Util;

import java.util.*;

/**
 * A code snippet evaluator compiles and returns class file for a code snippet.
 * Or it reports problems against the code snippet. 
 */
public class CodeSnippetEvaluator extends Evaluator implements EvaluationConstants {
	/**
	 * Whether the code snippet support classes should be found in the provided name environment
	 * or on disk.
	 */
	final static boolean DEVELOPMENT_MODE = false;

	/**
	 * The code snippet to evaluate.
	 */
	char[] codeSnippet;

	/**
	 * The code snippet to generated compilation unit mapper
	 */
	CodeSnippetToCuMapper mapper;
/**
 * Creates a new code snippet evaluator.
 */
CodeSnippetEvaluator(char[] codeSnippet, EvaluationContext context, INameEnvironment environment, Map options, IRequestor requestor, IProblemFactory problemFactory) {
	super(context, environment, options, requestor, problemFactory);
	this.codeSnippet = codeSnippet;
}
/**
 * @see org.eclipse.jdt.internal.eval.Evaluator
 */
protected void addEvaluationResultForCompilationProblem(Map resultsByIDs, IProblem problem, char[] cuSource) {
	CodeSnippetToCuMapper mapper = getMapper();
	int pbLineNumber = problem.getSourceLineNumber();
	int evaluationType = mapper.getEvaluationType(pbLineNumber);

	char[] evaluationID = null;
	switch(evaluationType) {
		case EvaluationResult.T_PACKAGE:
			evaluationID = this.context.packageName;
			
			// shift line number, source start and source end
			problem.setSourceLineNumber(1);
			problem.setSourceStart(0);
			problem.setSourceEnd(evaluationID.length - 1);
			break;
			
		case EvaluationResult.T_IMPORT:
			evaluationID = mapper.getImport(pbLineNumber);

			// shift line number, source start and source end
			problem.setSourceLineNumber(1);
			problem.setSourceStart(0);
			problem.setSourceEnd(evaluationID.length - 1);
			break;

		case EvaluationResult.T_CODE_SNIPPET:
			evaluationID = this.codeSnippet;
		
			// shift line number, source start and source end
			problem.setSourceLineNumber(pbLineNumber - this.mapper.lineNumberOffset);
			problem.setSourceStart(problem.getSourceStart() - this.mapper.startPosOffset);
			problem.setSourceEnd(problem.getSourceEnd() - this.mapper.startPosOffset);
			break;
			
		case EvaluationResult.T_INTERNAL:
			evaluationID = cuSource;
			break;
	}

	EvaluationResult result = (EvaluationResult)resultsByIDs.get(evaluationID);
	if (result == null) {
		resultsByIDs.put(evaluationID, new EvaluationResult(evaluationID, evaluationType, new IProblem[] {problem}));
	} else {
		result.addProblem(problem);
	}
}
/**
 * @see org.eclipse.jdt.internal.eval.Evaluator
 */
protected char[] getClassName() {
	return CharOperation.concat(CODE_SNIPPET_CLASS_NAME_PREFIX, Integer.toString(this.context.CODE_SNIPPET_COUNTER + 1).toCharArray());
}
/**
 * @see Evaluator.
 */
Compiler getCompiler(ICompilerRequestor requestor) {
	Compiler compiler = null;
	if (!DEVELOPMENT_MODE) {
		// If we are not developping the code snippet support classes,
		// use a regular compiler and feed its lookup environment with 
		// the code snippet support classes

		compiler = 
			new CodeSnippetCompiler(
				this.environment, 
				DefaultErrorHandlingPolicies.exitAfterAllProblems(), 
				this.options, 
				requestor, 
				this.problemFactory,
				this.context,
				getMapper().startPosOffset,
				getMapper().startPosOffset + codeSnippet.length - 1 + Util.LINE_SEPARATOR_CHARS.length); // 14838
		// Initialize the compiler's lookup environment with the already compiled super classes
		IBinaryType binary = this.context.getRootCodeSnippetBinary();
		if (binary != null) {
			compiler.lookupEnvironment.cacheBinaryType(binary);
		}
		VariablesInfo installedVars = this.context.installedVars;
		if (installedVars != null) {
			ClassFile[] globalClassFiles = installedVars.classFiles;
			for (int i = 0; i < globalClassFiles.length; i++) {
				ClassFileReader binaryType = null;
				try {
					binaryType = new ClassFileReader(globalClassFiles[i].getBytes(), null);
				} catch (ClassFormatException e) {
					e.printStackTrace(); // Should never happen since we compiled this type
				}
				compiler.lookupEnvironment.cacheBinaryType(binaryType);
			}
		}
	} else {
		// If we are developping the code snippet support classes,
		// use a wrapped environment so that if the code snippet classes are not found
		// then a default implementation is provided.

		compiler = new Compiler(
			getWrapperEnvironment(), 
			DefaultErrorHandlingPolicies.exitAfterAllProblems(), 
			this.options, 
			requestor, 
			this.problemFactory);
	}
	return compiler;
}
private CodeSnippetToCuMapper getMapper() {
	if (this.mapper == null) {
		char[] varClassName = null;
		VariablesInfo installedVars = this.context.installedVars;
		if (installedVars != null) {
			char[] superPackageName = installedVars.packageName;
			if (superPackageName != null && superPackageName.length != 0) {
				varClassName = CharOperation.concat(superPackageName, installedVars.className, '.');
			} else {
				varClassName = installedVars.className;
			}
			
		}
		this.mapper = new CodeSnippetToCuMapper(
			this.codeSnippet, 
			this.context.packageName,
			this.context.imports,
			getClassName(),
			varClassName,
			this.context.localVariableNames, 
			this.context.localVariableTypeNames, 
			this.context.localVariableModifiers, 
			this.context.declaringTypeName			
		);

	}
	return this.mapper;
}
/**
 * @see org.eclipse.jdt.internal.eval.Evaluator
 */
protected char[] getSource() {
	return getMapper().cuSource;
}
/**
 * Returns an environment that wraps the client's name environment.
 * This wrapper always considers the wrapped environment then if the name is
 * not found, it search in the code snippet support. This includes the superclass
 * org.eclipse.jdt.internal.eval.target.CodeSnippet as well as the global variable classes.
 */
private INameEnvironment getWrapperEnvironment() {
	return new CodeSnippetEnvironment(this.environment, this.context);
}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/149.java