error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3502.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3502.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3502.java
text:
```scala
i@@nitsOnReturn = initsOnReturn.mergedWith(flowInfo.unconditionalInits());

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.flow;

import java.util.ArrayList;

import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.codegen.ObjectCache;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

/**
 * Reflects the context of code analysis, keeping track of enclosing
 *	try statements, exception handlers, etc...
 */
public class ExceptionHandlingFlowContext extends FlowContext {
	
	public ReferenceBinding[] handledExceptions;
	
	public final static int BitCacheSize = 32; // 32 bits per int
	int[] isReached;
	int[] isNeeded;
	UnconditionalFlowInfo[] initsOnExceptions;
	ObjectCache indexes = new ObjectCache();
	boolean isMethodContext;

	public UnconditionalFlowInfo initsOnReturn;

	// for dealing with anonymous constructor thrown exceptions
	public ArrayList extendedExceptions;
	
	public ExceptionHandlingFlowContext(
		FlowContext parent,
		AstNode associatedNode,
		ReferenceBinding[] handledExceptions,
		BlockScope scope,
		UnconditionalFlowInfo flowInfo) {

		super(parent, associatedNode);
		isMethodContext = scope == scope.methodScope();
		this.handledExceptions = handledExceptions;
		int count = handledExceptions.length, cacheSize = (count / BitCacheSize) + 1;
		this.isReached = new int[cacheSize]; // none is reached by default
		this.isNeeded = new int[cacheSize]; // none is needed by default
		this.initsOnExceptions = new UnconditionalFlowInfo[count];
		for (int i = 0; i < count; i++) {
			this.indexes.put(handledExceptions[i], i); // key type  -> value index
			boolean isUnchecked =
				(scope.compareUncheckedException(handledExceptions[i]) != NotRelated);
			int cacheIndex = i / BitCacheSize, bitMask = 1 << (i % BitCacheSize);
			if (isUnchecked) {
				isReached[cacheIndex] |= bitMask;
				this.initsOnExceptions[i] = flowInfo.copy().unconditionalInits();
			} else {
				this.initsOnExceptions[i] = FlowInfo.DEAD_END;
			}
		}
		System.arraycopy(this.isReached, 0, this.isNeeded, 0, cacheSize);
		this.initsOnReturn = FlowInfo.DEAD_END;	
	}

	public void complainIfUnusedExceptionHandlers(
		AstNode[] exceptionHandlers,
		BlockScope scope,
		TryStatement tryStatement) {
		// report errors for unreachable exception handlers
		for (int i = 0, count = handledExceptions.length; i < count; i++) {
			int index = indexes.get(handledExceptions[i]);
			int cacheIndex = index / BitCacheSize;
			int bitMask = 1 << (index % BitCacheSize);
			if ((isReached[cacheIndex] & bitMask) == 0) {
				scope.problemReporter().unreachableExceptionHandler(
					handledExceptions[index],
					exceptionHandlers[index]);
			} else {
				if ((isNeeded[cacheIndex] & bitMask) == 0) {
					scope.problemReporter().maskedExceptionHandler(
						handledExceptions[index],
						exceptionHandlers[index]);
				}
			}
		}
		// will optimized out unnecessary catch block during code gen
		tryStatement.preserveExceptionHandler = isNeeded;
	}

	public String individualToString() {
		
		StringBuffer buffer = new StringBuffer("Exception flow context"); //$NON-NLS-1$
		int length = handledExceptions.length;
		for (int i = 0; i < length; i++) {
			int cacheIndex = i / BitCacheSize;
			int bitMask = 1 << (i % BitCacheSize);
			buffer.append('[').append(handledExceptions[i].readableName());
			if ((isReached[cacheIndex] & bitMask) != 0) {
				if ((isNeeded[cacheIndex] & bitMask) == 0) {
					buffer.append("-masked"); //$NON-NLS-1$
				} else {
					buffer.append("-reached"); //$NON-NLS-1$
				}
			} else {
				buffer.append("-not reached"); //$NON-NLS-1$
			}
			buffer.append('-').append(initsOnExceptions[i].toString()).append(']');
		}
		buffer.append("[initsOnReturn -").append(initsOnReturn.toString()).append(']'); //$NON-NLS-1$
		return buffer.toString();
	}

	public UnconditionalFlowInfo initsOnException(ReferenceBinding exceptionType) {
		
		int index;
		if ((index = indexes.get(exceptionType)) < 0) {
			return FlowInfo.DEAD_END;
		}
		return initsOnExceptions[index];
	}

	public UnconditionalFlowInfo initsOnReturn(){
		return this.initsOnReturn;
	}
	
	public void recordHandlingException(
		ReferenceBinding exceptionType,
		UnconditionalFlowInfo flowInfo,
		TypeBinding raisedException,
		AstNode invocationSite,
		boolean wasAlreadyDefinitelyCaught) {
			
		int index = indexes.get(exceptionType);
		// if already flagged as being reached (unchecked exception handler)
		int cacheIndex = index / BitCacheSize;
		int bitMask = 1 << (index % BitCacheSize);
		if (!wasAlreadyDefinitelyCaught) {
			this.isNeeded[cacheIndex] |= bitMask;
		}
		this.isReached[cacheIndex] |= bitMask;
		
		initsOnExceptions[index] =
			initsOnExceptions[index] == FlowInfo.DEAD_END
				? flowInfo.copy().unconditionalInits()
				: initsOnExceptions[index].mergedWith(flowInfo);
	}
	
	public void recordReturnFrom(FlowInfo flowInfo) {

		if (!flowInfo.isReachable()) return; 
		if (initsOnReturn == FlowInfo.DEAD_END) {
			initsOnReturn = flowInfo.copy().unconditionalInits();
		} else {
			initsOnReturn.mergedWith(flowInfo.unconditionalInits());
		}
	}
	
	/*
	 * Compute a merged list of unhandled exception types (keeping only the most generic ones).
	 * This is necessary to add synthetic thrown exceptions for anonymous type constructors (JLS 8.6).
	 */
	public void mergeUnhandledException(TypeBinding newException){
		
		if (this.extendedExceptions == null){
			this.extendedExceptions = new ArrayList(5);
			for (int i = 0; i < this.handledExceptions.length; i++){
				this.extendedExceptions.add(this.handledExceptions[i]);
			}
		}
		
		boolean isRedundant = false;
		
		for(int i = this.extendedExceptions.size()-1; i >= 0; i--){
			switch(Scope.compareTypes(newException, (TypeBinding)this.extendedExceptions.get(i))){
				case MoreGeneric :
					this.extendedExceptions.remove(i);
					break;
				case EqualOrMoreSpecific :
					isRedundant = true;
					break;
				case NotRelated :
					break;
			}
		}
		if (!isRedundant){
			this.extendedExceptions.add(newException);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3502.java