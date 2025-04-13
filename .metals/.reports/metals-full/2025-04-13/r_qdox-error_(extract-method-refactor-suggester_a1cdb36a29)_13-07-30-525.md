error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[238,2]

error in qdox parser
file content:
```java
offset: 7789
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3565.java
text:
```scala
import org.aspectj.org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.aspectj.ajdt.internal.compiler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.aspectj.bridge.IProgressListener;
import org.aspectj.weaver.IClassFileProvider;
import org.aspectj.weaver.IWeaveRequestor;
import org.aspectj.weaver.bcel.UnwovenClassFile;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

/**
 * @author colyer
 * This class provides the weaver with a source of class files to weave (via the 
 * iterator and IClassFileProvider interfaces). It receives results back from the
 * weaver via the IWeaveRequestor interface.
 */
public class WeaverAdapter implements IClassFileProvider, IWeaveRequestor, Iterator {
	
	private AjCompilerAdapter compilerAdapter;
	private Iterator resultIterator;
	private int classFileIndex = 0;
	private InterimCompilationResult nowProcessing;
	private InterimCompilationResult lastReturnedResult;
	private WeaverMessageHandler weaverMessageHandler;
	private IProgressListener progressListener;
	private boolean finalPhase = false;
	private int localIteratorCounter;
	
	// Fields related to progress monitoring
	private int progressMaxTypes;
	private String progressPhasePrefix;
	private double fromPercent;
	private double toPercent = 100.0;
	private int progressCompletionCount;

	
	public WeaverAdapter(AjCompilerAdapter forCompiler,
						 WeaverMessageHandler weaverMessageHandler,
						 IProgressListener progressListener) { 
		this.compilerAdapter = forCompiler;
		this.weaverMessageHandler = weaverMessageHandler;
		this.progressListener = progressListener;
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.IClassFileProvider#getClassFileIterator()
	 */
	public Iterator getClassFileIterator() {
		classFileIndex = 0;
		localIteratorCounter = 0;
		nowProcessing = null;
		lastReturnedResult = null;
		resultIterator = compilerAdapter.resultsPendingWeave.iterator();
		return this;
	}
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.IClassFileProvider#getRequestor()
	 */
	public IWeaveRequestor getRequestor() {
		return this;
	}
	
	// Iteration
	// ================================================================
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (nowProcessing == null) {
			if (!resultIterator.hasNext()) return false;
			nowProcessing = (InterimCompilationResult) resultIterator.next();
			classFileIndex = 0;
		}
		while (nowProcessing.unwovenClassFiles().length == 0 ) {
			if (!resultIterator.hasNext()) return false;
			nowProcessing = (InterimCompilationResult) resultIterator.next();
		}
		if (classFileIndex < nowProcessing.unwovenClassFiles().length) {
			return true;
		} else {
			classFileIndex = 0;
			if (!resultIterator.hasNext()) return false;
			nowProcessing = (InterimCompilationResult) resultIterator.next();
			while (nowProcessing.unwovenClassFiles().length == 0 ) {
				if (!resultIterator.hasNext()) return false;
				nowProcessing = (InterimCompilationResult) resultIterator.next();
			} 
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		if (!hasNext()) return null;  // sets up indices correctly
		if (finalPhase) {
			if ((lastReturnedResult != null) && (lastReturnedResult != nowProcessing)) {
				// we're done with the lastReturnedResult
				finishedWith(lastReturnedResult);
			}
		}
		localIteratorCounter++;
		lastReturnedResult = nowProcessing;
		weaverMessageHandler.setCurrentResult(nowProcessing.result());
		// weaverMessageHandler.handleMessage(new Message("weaving " + nowProcessing.fileName(),IMessage.INFO, null, null));
		return nowProcessing.unwovenClassFiles()[classFileIndex++];
	}
	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	
	// IWeaveRequestor
	// =====================================================================================

	// weave phases as indicated by bcelWeaver...
	public void processingReweavableState() {
		
		// progress reporting logic
		fromPercent = 50.0; // Assume weaving takes 50% of the progress bar...
	    recordProgress("processing reweavable state");
	}
	
	public void addingTypeMungers() {
		
		// progress reporting logic
		// At this point we have completed one iteration through all the classes/aspects 
		// we'll be dealing with, so let us remember this max value for localIteratorCounter
		// (for accurate progress reporting)
		recordProgress("adding type mungers");
		progressMaxTypes = localIteratorCounter;
	}
	
	public void weavingAspects() {
		
		// progress reporting logic
		progressPhasePrefix="woven aspect ";
		progressCompletionCount=0; // Start counting from *now*
	}
	
	public void weavingClasses() {
		finalPhase = true;
		
		// progress reporting logic
		progressPhasePrefix="woven class ";
	}
	
	public void weaveCompleted() {
		if ((lastReturnedResult != null) && (!lastReturnedResult.result().hasBeenAccepted)) {
			finishedWith(lastReturnedResult);
		}
	}
	


	/* (non-Javadoc)
	 * @see org.aspectj.weaver.IWeaveRequestor#acceptResult(org.aspectj.weaver.bcel.UnwovenClassFile)
	 */
	public void acceptResult(UnwovenClassFile result) {
		char[] key = result.getClassName().replace('.','/').toCharArray();
		removeFromHashtable(lastReturnedResult.result().compiledTypes,key);
		String className = result.getClassName().replace('.', '/');
		AjClassFile ajcf = new AjClassFile(className.toCharArray(),
										   result.getBytes());
		lastReturnedResult.result().record(ajcf.fileName(),ajcf);
		
		if (progressListener != null) {
			progressCompletionCount++;
			
			// Smoothly take progress from 'fromPercent' to 'toPercent'
			recordProgress(
			  fromPercent
			  +((progressCompletionCount/(double)progressMaxTypes)*(toPercent-fromPercent)),
			  progressPhasePrefix+result.getClassName()+" (from "+nowProcessing.fileName()+")");

			if (progressListener.isCancelledRequested()) {
		      throw new AbortCompilation(true,new OperationCanceledException("Weaving cancelled as requested"));
			}
		}
	}

	// helpers...
	// =================================================================
	
	private void finishedWith(InterimCompilationResult result) {
		compilerAdapter.acceptResult(result.result());
	}
	
	private void removeFromHashtable(Hashtable table, char[] key) {
		// jdt uses char[] as a key in the hashtable, which is not very useful as equality is based on being
		// the same array, not having the same content.
		String skey = new String(key);
		char[] victim = null;
		for (Enumeration iter = table.keys(); iter.hasMoreElements();) {
			char[] thisKey = (char[]) iter.nextElement();
			if (skey.equals(new String(thisKey))) {
				victim = thisKey;
				break;
			}
		}
		if (victim != null) {
			table.remove(victim);
		}
	}
	
	private void recordProgress(String message) {
		if (progressListener!=null) {
			progressListener.setText(message);
		}
	}
	
	private void recordProgress(double percentage,String message) {
		if (progressListener!=null) {
			progressListener.setProgress(percentage/100);
			progressListener.setText(message);
		}
	}
}
 N@@o newline at end of file
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3565.java