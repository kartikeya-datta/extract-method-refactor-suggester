error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/506.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/506.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/506.java
text:
```scala
protected M@@ap fCompilerOptions;

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.core.builder.*;

import java.util.*;

/**
 * The abstract superclass of image builders.
 * Provides the building and compilation mechanism
 * in common with the batch and incremental builders.
 */
public abstract class AbstractImageBuilder implements IImageBuilder, ICompilerRequestor {
	protected JavaDevelopmentContextImpl fDC;
	protected StateImpl fOldState;
	protected StateImpl fNewState;
	protected WorkQueue fWorkQueue;
	protected BuilderEnvironment fBuilderEnvironment;
	protected BuildNotifier fNotifier;
	protected org.eclipse.jdt.internal.compiler.Compiler fCompiler;
	protected ConfigurableOption[] fCompilerOptions;
	protected Vector fCompilationResults;
	public int MAX_AT_ONCE = 1000;
/**
 * Creates a new builder.
 */
protected AbstractImageBuilder() {
}
public void acceptResult(CompilationResult result) {
	ConvertedCompilationResult convertedResult = fNewState.convertCompilationResult(result, getBuilderEnvironment().getDefaultPackage());
	PackageElement element = convertedResult.getPackageElement();
	if (!fWorkQueue.hasBeenCompiled(element)) {
		fCompilationResults.addElement(convertedResult);
		fNotifier.compiled((CompilerCompilationUnit) result.getCompilationUnit());
		fWorkQueue.compiled(element);
	}
}
/**
 * Check whether the build has been canceled.
 */
protected void checkCancel() {
	fNotifier.checkCancel();
}
/**
 * Since the image builder is given as a result, let go of
 * any unneeded structures.
 */
protected void cleanUp() {
	fCompiler = null;
	fCompilationResults = null;
	fWorkQueue = null;
	fBuilderEnvironment = null;
	fNotifier = null;
	fNewState.cleanUp();
}
/**
 * Compile the given elements, adding more elements to the work queue 
 * if they are affected by the changes.
 */
protected void compile(Vector vToCompile) {
	int i = 0;
	Vector vToCompileAtOnce = new Vector(Math.min(vToCompile.size(), MAX_AT_ONCE));
	while (i < vToCompile.size()) {
		vToCompileAtOnce.removeAllElements();
		while (i < vToCompile.size() && vToCompileAtOnce.size() < MAX_AT_ONCE) {
			PackageElement unit = (PackageElement) vToCompile.elementAt(i);
			// Although it needed compiling when this method was called,
			// it may have already been compiled due to being brought in
			// by another unit.
			if (fWorkQueue.needsCompile(unit)) {
				SourceEntry sEntry = fNewState.getSourceEntry(unit);
				CompilerCompilationUnit compUnit = new CompilerCompilationUnit(fNewState, sEntry, fNotifier);
				compiling(compUnit);
				vToCompileAtOnce.addElement(compUnit);
				checkCancel();
			}
			++i;
		}
		if (vToCompileAtOnce.size() > 0) {
			CompilerCompilationUnit[] toCompile = new CompilerCompilationUnit[vToCompileAtOnce.size()];
			vToCompileAtOnce.copyInto(toCompile);
			try {
				fDC.inCompiler = true;
				getCompiler().compile(toCompile);
			} finally {
				fDC.inCompiler = false;
			}

			/* Check for cancel immediately after a compile, because the compile may have been
			 * canceled but without propagating the build canceled exception. */
			checkCancel();

			/* store results in new state and get new units to compile */
			ConvertedCompilationResult[] results = getCompilationResults();
			updateState(results);
			checkCancel();
		}
	}
}
/**
 * A unit is being (re)compiled.  Save any previous type structure.
 */
protected void compiling(CompilerCompilationUnit unit) {
}
/**
 * Returns the builder environment to use for this builder.
 */
public BuilderEnvironment getBuilderEnvironment() {
	if (fBuilderEnvironment == null) {
		fBuilderEnvironment = new BuilderEnvironment(this);
		fBuilderEnvironment.setDefaultPackage(fNewState.defaultPackageForProject());
	}
	return fBuilderEnvironment;
}
/**
 * Returns the results of the last compile.
 */
protected ConvertedCompilationResult[] getCompilationResults() {
	ConvertedCompilationResult[] results = new ConvertedCompilationResult[fCompilationResults.size()];
	fCompilationResults.copyInto(results);
	fCompilationResults = null;
	return results;
}
/**
 * Returns the compiler to use.
 */
protected Compiler getCompiler() {
	// Make sure to clear the results before starting a new compile.
	fCompilationResults = new Vector();
	if (fCompiler == null) {
		IErrorHandlingPolicy errorPolicy = DefaultErrorHandlingPolicies.proceedWithAllProblems();
		IProblemFactory problemFactory = ProblemFactory.getProblemFactory(Locale.getDefault());
		fCompiler = new Compiler(getBuilderEnvironment(), errorPolicy, fCompilerOptions, this, problemFactory);
	}
	return fCompiler;
}
/**
 * Returns the state that is being built.
 */
public IState getNewState() {
	return fNewState;
}
/**
 * Returns the old state if the image is being built incrementally
 */
public IState getOldState() {
	return fOldState;
}
/**
 * Returns true if the given source entry must be recompiled, false otherwise.
 */
protected boolean isInvalid(SourceEntry sEntry) {
	return fWorkQueue.needsCompile(fNewState.packageElementFromSourceEntry(sEntry));
}
/**
 * Stores the results of a compilation in the appropriate state tables.
 * Keeps track of what compilation units need to be compiled as a result
 * of the changes.
 */
protected void updateState(ConvertedCompilationResult[] results) {
	// Must store all compilation results, so state has sufficient information 
	// to calculate changes.
	fNewState.putCompilationResults(results);
	
	// Notify listeners 
	fNotifier.notifyElementsChanged(results, fOldState, fNewState);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/506.java