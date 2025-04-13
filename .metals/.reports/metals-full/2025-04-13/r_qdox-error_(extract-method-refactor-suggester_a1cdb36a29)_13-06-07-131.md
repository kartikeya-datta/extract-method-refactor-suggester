error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5211.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5211.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5211.java
text:
```scala
i@@f (isResolved) return;

/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Adrian Colyer			Initial implementation
 * ******************************************************************/
package org.aspectj.weaver;

/**
 * Represents a type variable with bounds
 */
public class TypeVariable {
	
	/**
	 * whether or not the bounds of this type variable have been 
	 * resolved
	 */
	private boolean isResolved = false;
	
	/**
	 * the name of the type variable as recorded in the generic signature
	 */
	private String name;
	
	/**
	 * the upper bound of the type variable (default to Object).
	 * From the extends clause, eg. T extends Number.
	 */
	private TypeX upperBound = TypeX.OBJECT;
	
	/**
	 * any additional upper (interface) bounds.
	 * from the extends clause, e.g. T extends Number & Comparable
	 */
	private TypeX[] additionalInterfaceBounds = new TypeX[0];
	
	/**
	 * any lower bound.
	 * from the super clause, eg T super Foo
	 */
	private TypeX lowerBound = null;
	
	public TypeVariable(String aName) {
		this.name = aName;
	}
	
	public TypeVariable(String aName, TypeX anUpperBound) {
		this(aName);
		this.upperBound = anUpperBound;
	}
	
	public TypeVariable(String aName, TypeX anUpperBound, 
			                        TypeX[] someAdditionalInterfaceBounds) {
		this(aName,anUpperBound);
		this.additionalInterfaceBounds = someAdditionalInterfaceBounds;
	}
	
	public TypeVariable(String aName, TypeX anUpperBound, 
            TypeX[] someAdditionalInterfaceBounds, TypeX aLowerBound) {
		this(aName,anUpperBound,someAdditionalInterfaceBounds);
		this.lowerBound = aLowerBound;
	}
	
	public TypeX getUpperBound() {
		return upperBound;
	}
	
	public TypeX[] getAdditionalInterfaceBounds() {
		return additionalInterfaceBounds;
	}
	
	public TypeX getLowerBound() {
		return lowerBound;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * resolve all the bounds of this type variable
	 */
	public void resolve(World inSomeWorld) {
		if (isResolved) throw new IllegalStateException("already resolved!");
		
		upperBound = upperBound.resolve(inSomeWorld);
		if (lowerBound != null) lowerBound = lowerBound.resolve(inSomeWorld);
		
		for (int i = 0; i < additionalInterfaceBounds.length; i++) {
			additionalInterfaceBounds[i] = additionalInterfaceBounds[i].resolve(inSomeWorld);
		}
		
		isResolved = true;
	}
	
	/**
	 * answer true if the given type satisfies all of the bound constraints of this
	 * type variable.
	 * If type variable has not been resolved then throws IllegalStateException
	 */
	public boolean canBeBoundTo(ResolvedTypeX aCandidateType) {
		if (!isResolved) throw new IllegalStateException("Can't answer binding questions prior to resolving");
		// can be bound iff...
		//  aCandidateType is a subtype of upperBound
		if (!isASubtypeOf(upperBound,aCandidateType)) {
			return false;
		}
		//  aCandidateType is a subtype of all additionalInterfaceBounds
		for (int i = 0; i < additionalInterfaceBounds.length; i++) {
			if (!isASubtypeOf(additionalInterfaceBounds[i], aCandidateType)) {
				return false;
			}
		}
		//  lowerBound is a subtype of aCandidateType
		if ((lowerBound != null) && (!isASubtypeOf(aCandidateType,lowerBound))) {
			return false;
		}
		return true;
	}
	
	private boolean isASubtypeOf(TypeX candidateSuperType, TypeX candidateSubType) {
		ResolvedTypeX superType = (ResolvedTypeX) candidateSuperType;
		ResolvedTypeX subType = (ResolvedTypeX) candidateSubType;
		return superType.isAssignableFrom(subType);
	}

	// only used when resolving circular dependencies
	public void setUpperBound(TypeX aTypeX) {
		this.upperBound = aTypeX;
	}
	
	// good enough approximation
	public String toString() {
		return "T" + upperBound.getSignature();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5211.java