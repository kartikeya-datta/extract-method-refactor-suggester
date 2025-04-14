error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/859.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/859.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/859.java
text:
```scala
r@@eturn "SourceElementTable(" + fPackageTable + ")"; //$NON-NLS-1$ //$NON-NLS-2$

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.core.Assert;
import org.eclipse.jdt.internal.core.builder.*;
import org.eclipse.jdt.internal.core.util.LookupTable;

import java.util.Enumeration;

/**
 * The source element table contains all elements of the workspace that are
 * visible to the image builder.  It is implemented as nested hashtables.  The
 * first hashtable is keyed by non state-specific package handle.  The second
 * table is keyed by name of source file name (e.g., "Object.class", or "Foo.java"),
 * and has SourceEntry objects as values.
 */
class SourceElementTable extends StateTables{
	LookupTable fPackageTable = new LookupTable(11);
	/**
	 * Returns true if the package is in the table, false otherwise.
	 */
	boolean containsPackage(IPackage pkg) {
		return fPackageTable.containsKey(pkg);
	}
	/**
	 * Creates a copy of the table.
	 */
	SourceElementTable copy() {
		try {
			SourceElementTable copy = (SourceElementTable) super.clone();
			copy.fPackageTable = new LookupTable(fPackageTable.size() * 2 + 1);
			for (Enumeration e = fPackageTable.keys(); e.hasMoreElements();) {
				IPackage pkg = (IPackage) e.nextElement();
				LookupTable pkgTable = (LookupTable) fPackageTable.get(pkg);
				copy.fPackageTable.put(pkg, pkgTable.clone());
			}
			return copy;
		}
		catch (CloneNotSupportedException e) {
			// Should not happen.
			throw new Error();
		}
	}
	/**
	 * Returns the table for a package.  Returns null if no such table exists.
	 */
	LookupTable getPackageTable(IPackage pkg) {
		return (LookupTable) fPackageTable.get(pkg);
	}
/**
 * Returns the source entries in the given package.  
 * Returns null if no entries exist for that package.
 */
SourceEntry[] getSourceEntries(IPackage pkg) {
	LookupTable pkgTable = getPackageTable(pkg);
	if (pkgTable == null) {
		return null;
	}
	int i = 0;
	SourceEntry[] results = new SourceEntry[pkgTable.size()];
	for (Enumeration e = pkgTable.elements(); e.hasMoreElements();) {
		results[i++] = (SourceEntry) e.nextElement();
	}
	return results;
}
/**
 * Returns the source entry for a package and file name.  Returns null if
 * no entry exists.
 */
SourceEntry getSourceEntry(IPackage pkg, String fileName) {
	/* make sure package is not state specific */
	Assert.isTrue(!pkg.isStateSpecific());
	LookupTable pkgTable = getPackageTable(pkg);
	if (pkgTable != null) {
		return (SourceEntry) pkgTable.get(fileName);
	}
	return null;
}
	/**
	 * Returns the number of packages in the table.  
	 */
	int numPackages() {
		return fPackageTable.size();
	}
	/**
	 * Adds the table for a package to the table.
	 */
	void putPackageTable(IPackage pkg, LookupTable pkgTable) {
		fPackageTable.put(pkg, pkgTable);
	}
	/**
	 * Adds one source entry in the source element table
	 */
	public void putSourceEntry(IPackage pkg, SourceEntry sourceEntry) {
		LookupTable pkgTable = getPackageTable(pkg);
		if (pkgTable == null){
			putPackageTable(pkg, pkgTable = new LookupTable());
		}
		pkgTable.put(sourceEntry.getFileName(), sourceEntry);
	}
	/**
	 * Removes the source entries for a package.
	 */
	void removePackage(IPackage pkg) {
		fPackageTable.remove(pkg);
	}
/**
 * Removes the source entry for a source element.  Returns the
 * removed element or null if it didn't exist
 */
SourceEntry removeSourceEntry(IPackage pkg, String fileName) {
	LookupTable pkgTable = getPackageTable(pkg);
	if (pkgTable != null) {
		return (SourceEntry) pkgTable.remove(fileName);
	}
	return null;
}
	/**
	 * Returns a String that represents the value of this object.
	 * @return a string representation of the receiver
	 */
	public String toString() {
		return "SourceElementTable("/*nonNLS*/ + fPackageTable + ")"/*nonNLS*/;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/859.java