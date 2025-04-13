error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15327.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15327.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15327.java
text:
```scala
T@@ypeVariablePattern typeVariableMatch = typeVariables.lookupTypeVariable(name);

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
package org.aspectj.weaver.patterns;

import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.IMessage.Kind;
import org.aspectj.weaver.IHasPosition;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.World;

/**
 * A scope that also considers type variables when looking up a type.
 *
 */
public class ScopeWithTypeVariables implements IScope {

	private IScope delegateScope;
	private TypeVariablePatternList typeVariables;
	
	public ScopeWithTypeVariables(TypeVariablePatternList typeVars, IScope delegate) {
		this.delegateScope = delegate;
		this.typeVariables = typeVars;
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#lookupType(java.lang.String, org.aspectj.weaver.IHasPosition)
	 */
	public TypeX lookupType(String name, IHasPosition location) {
		TypeVariable typeVariableMatch = typeVariables.lookupTypeVariable(name);
		if (typeVariableMatch != null) {
			return typeVariableMatch.resolvedType();
		} else {
			return delegateScope.lookupType(name, location);
		}		
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#getWorld()
	 */
	public World getWorld() {
		return delegateScope.getWorld();
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#getEnclosingType()
	 */
	public ResolvedTypeX getEnclosingType() {
		return delegateScope.getEnclosingType();
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#getMessageHandler()
	 */
	public IMessageHandler getMessageHandler() {
		return delegateScope.getMessageHandler();
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#lookupFormal(java.lang.String)
	 */
	public FormalBinding lookupFormal(String name) {
		return delegateScope.lookupFormal(name);
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#getFormal(int)
	 */
	public FormalBinding getFormal(int i) {
		return delegateScope.getFormal(i);
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#getFormalCount()
	 */
	public int getFormalCount() {
		return delegateScope.getFormalCount();
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#getImportedPrefixes()
	 */
	public String[] getImportedPrefixes() {
		return delegateScope.getImportedPrefixes();
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#getImportedNames()
	 */
	public String[] getImportedNames() {
		return delegateScope.getImportedNames();
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#message(org.aspectj.bridge.IMessage.Kind, org.aspectj.weaver.IHasPosition, java.lang.String)
	 */
	public void message(Kind kind, IHasPosition location, String message) {
		delegateScope.message(kind, location, message);
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.IScope#message(org.aspectj.bridge.IMessage.Kind, org.aspectj.weaver.IHasPosition, org.aspectj.weaver.IHasPosition, java.lang.String)
	 */
	public void message(Kind kind, IHasPosition location1,
			IHasPosition location2, String message) {
		delegateScope.message(kind,location1,location2,message);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15327.java