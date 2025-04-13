error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3450.java
text:
```scala
i@@f (IndexManager.VERBOSE) System.out.println("-> merging index : "/*nonNLS*/+index.getIndexFile());

package org.eclipse.jdt.internal.core.search;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.core.index.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.core.search.indexing.*;
import org.eclipse.jdt.internal.core.search.matching.*;
import org.eclipse.jdt.internal.core.search.processing.*;
import org.eclipse.jdt.internal.core.index.impl.*;

import java.io.*;
import java.util.*;

public class SubTypeSearchJob extends PatternSearchJob {

	Hashtable inputs = new Hashtable(5);
public SubTypeSearchJob(SearchPattern pattern, IJavaSearchScope scope, int detailLevel, IIndexSearchRequestor requestor, IndexManager indexManager, IProgressMonitor progressMonitor) {
	super(pattern, scope, detailLevel, requestor, indexManager, progressMonitor);
}
public SubTypeSearchJob(SearchPattern pattern, IJavaSearchScope scope, IJavaElement focus, int detailLevel, IIndexSearchRequestor requestor, org.eclipse.jdt.internal.core.search.indexing.IndexManager indexManager, IProgressMonitor progressMonitor) {
	super(pattern, scope, focus, detailLevel, requestor, indexManager, progressMonitor);
}
public void closeAll(){

	Enumeration openedInputs = inputs.elements();
	while (openedInputs.hasMoreElements()){
		IndexInput input = (IndexInput) openedInputs.nextElement();
		try {
			input.close();
		} catch(IOException e){
		}
	}
}
/**
 * execute method comment.
 */
public boolean search(IIndex index) {

	if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();

	if (index == null) return COMPLETE;		
	ReadWriteMonitor monitor = indexManager.getMonitorFor(index);
	if (monitor == null) return COMPLETE; // index got deleted since acquired
	try {
		monitor.enterRead(); // ask permission to read

		/* if index has changed, commit these before querying */
		if (index.hasChanged()){
			try {
				monitor.exitRead(); // free read lock
				monitor.enterWrite(); // ask permission to write
				if (IndexManager.VERBOSE) System.out.println("-> merging index : "+index.getIndexFile()); //$NON-NLS-1$
				index.save();
			} catch(IOException e){
				return FAILED;
			} finally {
				monitor.exitWrite(); // finished writing
				monitor.enterRead(); // reaquire read permission
			}
		}
		long start = System.currentTimeMillis();

		IndexInput input;
		if ((input = (IndexInput) inputs.get(index)) == null){
			input = new BlocksIndexInput(index.getIndexFile());
			input.open();
			inputs.put(index, input);
			//System.out.println("Acquiring INPUT for "+index);
		}
		pattern.findIndexMatches(input, requestor, detailLevel, progressMonitor, this.scope);
		executionTime += System.currentTimeMillis() - start;
		return COMPLETE;
	} catch(IOException e){
		return FAILED;
	} finally {
		monitor.exitRead(); // finished reading
	}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3450.java