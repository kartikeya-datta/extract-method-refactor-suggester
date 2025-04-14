error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/348.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/348.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/348.java
text:
```scala
i@@f (!"java"/*nonNLS*/.equalsIgnoreCase(extension) && !"class"/*nonNLS*/.equalsIgnoreCase(extension)) {

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Element info for PackageFragments.
 */
class PackageFragmentInfo extends OpenableElementInfo {

	/**
	 * A array with all the non-java resources contained by this PackageFragment
	 */
	protected Object[] fNonJavaResources;

/**
 * Create and initialize a new instance of the receiver
 */
public PackageFragmentInfo() {
	fNonJavaResources = null;
}
/**
 * Compute the non-java resources of this package fragment.
 *
 * <p>Package fragments which are folders recognize files based on the
 * type of the fragment
 * <p>Package fragments which are in a jar only recognize .class files (
 * @see JarPackageFragment).
 */
private Object[] computeNonJavaResources(IResource resource) {
	Object[] nonJavaResources = new IResource[5];
	int nonJavaResourcesCounter = 0;
	try{
		IResource[] members = ((IContainer) resource).members();
		for (int i = 0, max = members.length; i < max; i++) {
			IResource child = members[i];
			if (child.getType() != IResource.FOLDER) {
				String extension = child.getProjectRelativePath().getFileExtension();
				if (!"java".equalsIgnoreCase(extension) && !"class".equalsIgnoreCase(extension)) { //$NON-NLS-1$ //$NON-NLS-2$
					if (nonJavaResources.length == nonJavaResourcesCounter) {
						// resize
						System.arraycopy(
							nonJavaResources,
							0,
							(nonJavaResources = new IResource[nonJavaResourcesCounter * 2]),
							0,
							nonJavaResourcesCounter);
					}
					nonJavaResources[nonJavaResourcesCounter++] = child;
				}
			}
		}
		if (nonJavaResourcesCounter == 0) {
			nonJavaResources = NO_NON_JAVA_RESOURCES;
		} else {
			if (nonJavaResources.length != nonJavaResourcesCounter) {
				System.arraycopy(nonJavaResources, 0, (nonJavaResources = new IResource[nonJavaResourcesCounter]), 0, nonJavaResourcesCounter);
			}
		}	
	} catch(CoreException e) {
		nonJavaResources = NO_NON_JAVA_RESOURCES;
		nonJavaResourcesCounter = 0;
	}
	return nonJavaResources;
}
/**
 */
boolean containsJavaResources() {
	return fChildren.length != 0;
}
/**
 * Returns an array of non-java resources contained in the receiver.
 */
Object[] getNonJavaResources(IResource underlyingResource) {
	Object[] nonJavaResources = fNonJavaResources;
	if (nonJavaResources == null) {
		nonJavaResources = computeNonJavaResources(underlyingResource);
		fNonJavaResources = nonJavaResources;
	}
	return nonJavaResources;
}
/**
 * Set the fNonJavaResources to res value
 */
synchronized void setNonJavaResources(Object[] resources) {
	fNonJavaResources = resources;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/348.java