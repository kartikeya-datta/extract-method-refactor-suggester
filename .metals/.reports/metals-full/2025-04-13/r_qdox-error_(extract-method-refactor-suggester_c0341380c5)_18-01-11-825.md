error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/530.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/530.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/530.java
text:
```scala
A@@ssert.isTrue(false, "Internal Error - Lazy building has been disabled"/*nonNLS*/);

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.Vector;

import org.eclipse.jdt.internal.compiler.ConfigurableOption;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.core.Assert;
import org.eclipse.jdt.internal.core.Util;
import org.eclipse.jdt.internal.core.builder.IDelta;
import org.eclipse.jdt.internal.core.builder.IImageBuilder;
import org.eclipse.jdt.internal.core.builder.IImageContext;
import org.eclipse.jdt.internal.core.builder.IPackage;

/**
 * The batch image builder - builds a state from scratch.
 */
public class BatchImageBuilder extends AbstractImageBuilder implements IImageBuilder, ICompilerRequestor {
	/**
	 * A flag indicating we are doing a batch build rather than
	 * background lazy builds.
	 */
	protected boolean fDoingBatchBuild = false;
/**
 * Creates a new batch image builder on the given new state.
 * The builder will compile everything in the state's project.
 */
public BatchImageBuilder(StateImpl state) {
	this(state, JavaDevelopmentContextImpl.getDefaultCompilerOptions());
}
/**
 * Creates a new batch image builder on the given new state.
 * The batch builder will build all classes within the state's project.
 * This constructor has been created for testing purposes.  This allows
 * tests to control the compiler options used by the batch build.
 */
protected BatchImageBuilder(StateImpl state, ConfigurableOption[] options) {
	fDC = (JavaDevelopmentContextImpl) state.getDevelopmentContext();
	state.setCompilerOptions(options);
	fCompilerOptions = options;
	fNewState = state;
	fWorkQueue = new WorkQueue();
}
/**
 * Builds the entire image from scratch, based on the provided workspace.
 */
public void build() {
	fDoingBatchBuild = true;
	fNotifier = new BuildNotifier(fDC, true);
	getBuilderEnvironment().setNotifier(fNotifier);
	fNotifier.begin();
	try {
		fNewState.readClassPath();
		fNotifier.subTask(Util.bind("build.scrubbingOutput"/*nonNLS*/));
		fNewState.getBinaryOutput().scrubOutput();
		fNotifier.updateProgressDelta(0.05f);
		fNotifier.subTask(Util.bind("build.analyzingPackages"/*nonNLS*/));
		fNewState.buildInitialPackageMap();
		fNotifier.updateProgressDelta(0.05f);

		/* Force build all in build context */
		fNotifier.subTask(Util.bind("build.analyzingSources"/*nonNLS*/));
		IPackage[] pkgs = fNewState.getPackageMap().getAllPackagesAsArray();
		for (int i = 0; i < pkgs.length; ++i) {
			fNotifier.checkCancel();
			SourceEntry[] entries = fNewState.getSourceEntries(pkgs[i]);
			if (entries != null) {
				for (int j = 0; j < entries.length; ++j) {
					SourceEntry sEntry = entries[j];
					if (sEntry.isSource()) {
						PackageElement element = fNewState.packageElementFromSourceEntry(sEntry);
						fWorkQueue.add(element);
					}
				}
			}
		}
		fNotifier.updateProgressDelta(0.05f);
		Vector vToCompile = fWorkQueue.getElementsToCompile();
		if (vToCompile.size() > 0) {
			fNotifier.setProgressPerCompilationUnit(0.75f / vToCompile.size());
			compile(vToCompile);
		}
		/* Copy resources to binary output */
		new ProjectResourceCopier(fNewState.getJavaProject(), fDC, fNotifier, 0.10f).copyAllResourcesOnClasspath();
		
		fNotifier.done();
	} finally {
		cleanUp();
	}
}
/**
 * Returns an image delta between old and new states in the image context.
 * This does not apply to the batch builder.
 * @see IImageBuilder
 */
public IDelta getImageDelta(IImageContext imageContext) {
	return null;
}
/**
 * Builds a given compilation unit.
 */
public void lazyBuild(PackageElement unit) {
	//		String msg = "Attempt to lazy build " + unit.getPackage().getName() + "." + unit.getFileName();
	//		System.err.println(msg + ". " + "Lazy building has been disabled.");
	Assert.isTrue(false, Util.bind("build.lazyBuildDisabled"/*nonNLS*/));
}
/**
 * Returns a string describe the builder
 * @see IImageBuilder
 */
public String toString() {
	return "batch image builder for:\n\tnew state: "/*nonNLS*/ + getNewState();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/530.java