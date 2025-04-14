error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14340.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14340.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14340.java
text:
```scala
public abstract S@@hadowMunger parameterizeWith(ResolvedType declaringType,Map typeVariableMap);

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver;

import java.util.Collection;
import java.util.Map;

import org.aspectj.asm.AsmManager;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.util.PartialOrder;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;

/**
 * For every shadow munger, nothing can be done with it until it is concretized.  Then...
 * 
 * (Then we call fast match.)
 * 
 * For every shadow munger, for every shadow, 
 * first match is called, 
 * then (if match returned true) the shadow munger is specialized for the shadow, 
 *     which may modify state.  
 * Then implement is called. 
 */

public abstract class ShadowMunger implements PartialOrder.PartialComparable, IHasPosition {
	protected Pointcut pointcut;
	
	// these three fields hold the source location of this munger
	protected int start, end;
	protected ISourceContext sourceContext;
	private ISourceLocation sourceLocation;
	private String handle = null;
	private ResolvedType declaringType;  // the type that declared this munger.

	
	public ShadowMunger(Pointcut pointcut, int start, int end, ISourceContext sourceContext) {
		this.pointcut = pointcut;
		this.start = start;
		this.end = end;
		this.sourceContext = sourceContext;
	}
	
	public abstract ShadowMunger concretize(ResolvedType fromType, World world, PerClause clause);	

    public abstract void specializeOn(Shadow shadow);
    public abstract void implementOn(Shadow shadow);
	
	/**
	 * All overriding methods should call super
	 */
    public boolean match(Shadow shadow, World world) {
    	return pointcut.match(shadow).maybeTrue();
    }
    
    public abstract ShadowMunger parameterizeWith(Map typeVariableMap); 
    
	public int fallbackCompareTo(Object other) {
		return toString().compareTo(toString());
	}
	
	public int getEnd() {
		return end;
	}

	public int getStart() {
		return start;
	}
	
    public ISourceLocation getSourceLocation() {
    	if (sourceLocation == null) {
	    	if (sourceContext != null) {
				sourceLocation = sourceContext.makeSourceLocation(this);
	    	}
    	}
    	return sourceLocation;
    }

	public String getHandle() {
		if (null == handle) {
			ISourceLocation sl = getSourceLocation();
			if (sl != null) {
				handle = AsmManager.getDefault().getHandleProvider().createHandleIdentifier(
				            sl.getSourceFile(),
				            sl.getLine(),
				            sl.getColumn(),
							sl.getOffset());
			}
		}
		return handle;
	}

	// ---- fields
	
    public static final ShadowMunger[] NONE = new ShadowMunger[0];



	public Pointcut getPointcut() {
		return pointcut;
	}
	
	// pointcut may be updated during rewriting...
	public void setPointcut(Pointcut pointcut) {
		this.pointcut = pointcut;
	}

	/**
	 * Invoked when the shadow munger of a resolved type are processed.
	 * @param aType
	 */
	public void setDeclaringType(ResolvedType aType) {
		this.declaringType = aType;
	}
	
	public ResolvedType getDeclaringType() {
		return this.declaringType;
	}
	
	/**
	 * @return a Collection of ResolvedType for all checked exceptions that
	 *          might be thrown by this munger
	 */
	public abstract Collection getThrownExceptions();

    /**
     * Does the munger has to check that its exception are accepted by the shadow ?
     * ATAJ: It s not the case for @AJ around advice f.e. that can throw Throwable, even if the advised
     * method does not throw any exceptions. 
     * @return true if munger has to check that its exceptions can be throwned based on the shadow
     */
    public abstract boolean mustCheckExceptions();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14340.java