error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2837.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2837.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2837.java
text:
```scala
s@@uper.remove(source);

/* *******************************************************************
 * Copyright (c) 2003 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Mik Kersten     initial implementation 
 * ******************************************************************/

package org.aspectj.asm.internal;

import java.util.*;

import org.aspectj.asm.*;

/**
 * TODO: add a remove, and a clear all
 * 
 * @author Mik Kersten
 * 
 */
public class RelationshipMap extends HashMap implements IRelationshipMap {
	
	private IHierarchy hierarchy;
	
	public RelationshipMap(IHierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public List get(String handle) {
		List relationships = (List)super.get(handle);
		if (relationships == null) {
			return null;
		} else {
			return relationships;
		}
	}
	
	public List get(IProgramElement source) {
		return get(source.getHandleIdentifier());
	}

	public IRelationship get(String source, IRelationship.Kind kind, String relationshipName) {
		List relationships = get(source);
		if (relationships == null) {
			relationships = new ArrayList();
			IRelationship rel = new Relationship(relationshipName, kind, source, new ArrayList());
			relationships.add(rel);
			super.put(source, relationships);
			return rel;
		} else {
			for (Iterator it = relationships.iterator(); it.hasNext(); ) {
				IRelationship curr = (IRelationship)it.next();
				if (curr.getKind() == kind && curr.getName().equals(relationshipName)) {
					return curr;
				}
			}
		}
		return null;
	}

	public IRelationship get(IProgramElement source, IRelationship.Kind kind, String relationshipName) {
		return get(source.getHandleIdentifier(), kind, relationshipName);
	}
	
	public void remove(String source, IRelationship relationship) {
		List list = (List)super.get(source);
		if (list != null) {
			boolean matched = false;
			for (Iterator it = list.iterator(); it.hasNext(); ) {
				IRelationship curr = (IRelationship)it.next();
				if (curr.getName().equals(relationship.getName())) {
					curr.getTargets().addAll(relationship.getTargets());
					matched = true;
				}
			}
			if (!matched) list.remove(relationship);
		}		
	}

	public void removeAll(String source) {
		List list = (List)super.remove(source);	
	}
	
	public void put(String source, IRelationship relationship) {
		System.err.println(">> for: " + source + ", put::" + relationship);
		
		List list = (List)super.get(source);
		if (list == null) {
			list = new ArrayList();
			list.add(relationship);
			super.put(source, list);
		} else {
			boolean matched = false;
			for (Iterator it = list.iterator(); it.hasNext(); ) {
				IRelationship curr = (IRelationship)it.next();
				if (curr.getName().equals(relationship.getName())
					&& curr.getKind() == relationship.getKind()) {
					curr.getTargets().addAll(relationship.getTargets());
					matched = true;
				}
			}
			if (matched) list.add(relationship);
		}
	}

	public void put(IProgramElement source, IRelationship relationship) {
		put(source.getHandleIdentifier(), relationship);
	}

	public void clear() {
		super.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2837.java