error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6373.java
text:
```scala
r@@eturn new NameEnvironmentAnswer(new ResourceCompilationUnit(file, file.getLocationURI()), null /* no access restriction */);

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.util.SimpleLookupTable;
import org.eclipse.jdt.internal.core.builder.ClasspathLocation;
import org.eclipse.jdt.internal.core.util.ResourceCompilationUnit;
import org.eclipse.jdt.internal.core.util.Util;

public class ClasspathSourceDirectory extends ClasspathLocation {

	IContainer sourceFolder;
	SimpleLookupTable directoryCache;
	SimpleLookupTable missingPackageHolder = new SimpleLookupTable();
	char[][] fullExclusionPatternChars;
	char[][] fulInclusionPatternChars;

ClasspathSourceDirectory(IContainer sourceFolder, char[][] fullExclusionPatternChars, char[][] fulInclusionPatternChars) {
	this.sourceFolder = sourceFolder;
	this.directoryCache = new SimpleLookupTable(5);
	this.fullExclusionPatternChars = fullExclusionPatternChars;
	this.fulInclusionPatternChars = fulInclusionPatternChars;
}

public void cleanup() {
	this.directoryCache = null;
}

SimpleLookupTable directoryTable(String qualifiedPackageName) {
	SimpleLookupTable dirTable = (SimpleLookupTable) directoryCache.get(qualifiedPackageName);
	if (dirTable == missingPackageHolder) return null; // package exists in another classpath directory or jar
	if (dirTable != null) return dirTable;

	try {
		IResource container = sourceFolder.findMember(qualifiedPackageName); // this is a case-sensitive check
		if (container instanceof IContainer) {
			IResource[] members = ((IContainer) container).members();
			dirTable = new SimpleLookupTable();
			for (int i = 0, l = members.length; i < l; i++) {
				IResource m = members[i];
				String name;
				if (m.getType() == IResource.FILE) {
					int index = Util.indexOfJavaLikeExtension(name = m.getName());
					if (index >= 0) {
						String fullPath = m.getFullPath().toString();
						if (!org.eclipse.jdt.internal.compiler.util.Util.isExcluded(fullPath.toCharArray(), this.fulInclusionPatternChars, this.fullExclusionPatternChars, false/*not a folder path*/)) {
							dirTable.put(name.substring(0, index), m);
						}
					}
				}
			}
			directoryCache.put(qualifiedPackageName, dirTable);
			return dirTable;
		}
	} catch(CoreException ignored) {
		// treat as if missing
	}
	directoryCache.put(qualifiedPackageName, missingPackageHolder);
	return null;
}

public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof ClasspathSourceDirectory)) return false;

	return sourceFolder.equals(((ClasspathSourceDirectory) o).sourceFolder);
} 

public NameEnvironmentAnswer findClass(String sourceFileWithoutExtension, String qualifiedPackageName, String qualifiedSourceFileWithoutExtension) {
	SimpleLookupTable dirTable = directoryTable(qualifiedPackageName);
	if (dirTable != null && dirTable.elementSize > 0) {
		IFile file = (IFile) dirTable.get(sourceFileWithoutExtension);
		if (file != null) {
			return new NameEnvironmentAnswer(new ResourceCompilationUnit(file), null /* no access restriction */);
		}
	}
	return null;
}

public IPath getProjectRelativePath() {
	return sourceFolder.getProjectRelativePath();
}

public boolean isPackage(String qualifiedPackageName) {
	return directoryTable(qualifiedPackageName) != null;
}

public void reset() {
	this.directoryCache = new SimpleLookupTable(5);
}

public String toString() {
	return "Source classpath directory " + sourceFolder.getFullPath().toString(); //$NON-NLS-1$
}

public String debugPathString() {
	return this.sourceFolder.getFullPath().toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6373.java