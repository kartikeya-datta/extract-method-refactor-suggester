error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12093.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12093.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12093.java
text:
```scala
i@@f (enclosingAdvice instanceof Advice) return ((Advice)enclosingAdvice).getSignature();

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver;

import java.util.*;

public class IntMap {
	public static final IntMap EMPTY = new IntMap(0) {
		public boolean directlyInAdvice() { return true; }
		public ShadowMunger getEnclosingAdvice() { return null; }  //XXX possible
	};
	
	
	// XXX begin hack to avoid a signature refactoring in Pointcut
	private ResolvedTypeX concreteAspect;
	private ShadowMunger enclosingAdvice;
	private List/*ResolvedPointcutDefinition*/ enclosingDefinition = new ArrayList();
	
	public void pushEnclosingDefinition(ResolvedPointcutDefinition def) {
		enclosingDefinition.add(def);
	}
	
	public void popEnclosingDefinitition() {
		enclosingDefinition.remove(enclosingDefinition.size()-1);
	}
	
	
	public ResolvedPointcutDefinition peekEnclosingDefinitition() {
		return (ResolvedPointcutDefinition)enclosingDefinition.get(enclosingDefinition.size()-1);
	}
	
	
	public boolean directlyInAdvice() {
		return enclosingDefinition.isEmpty();
	}
	
	public ShadowMunger getEnclosingAdvice() {
		return enclosingAdvice;
	}
	
	public void setEnclosingAdvice(ShadowMunger advice) {
		this.enclosingAdvice = advice;
	}
	
	public Member getAdviceSignature() {
		if (enclosingAdvice instanceof Advice) return ((Advice)enclosingAdvice).signature;
		else return null;
	}
	

	public ResolvedTypeX getConcreteAspect() {
		return concreteAspect;
	}

	public void setConcreteAspect(ResolvedTypeX concreteAspect) {
		this.concreteAspect = concreteAspect;
	}
	
	public void copyContext(IntMap bindings) {
		this.enclosingAdvice = bindings.enclosingAdvice;
		this.enclosingDefinition = bindings.enclosingDefinition;
		this.concreteAspect = bindings.concreteAspect;
	}
	
	// XXX end hack to avoid a signature refactoring in Pointcut
	
	
	private static final int MISSING = -1;
	
    private int[] map;

    private IntMap(int[] map) {
        this.map = map;
    }
    public IntMap() {
        map = new int[0];
    }
    public IntMap(int initialCapacity) {
        map = new int[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            map[i] = MISSING;
        }
    }
    
    public void put(int key, int val) {
        /* assert (val >= 0 && key >= 0) */
        if (key >= map.length) {
            int[] tmp = new int[key * 2 + 1]; //??? better expansion function
            System.arraycopy(map, 0, tmp, 0, map.length);
            for (int i = map.length, len = tmp.length; i < len; i++) tmp[i] = MISSING;
            map = tmp;
        }
        map[key] = val;
    }
    
    public int get(int key) {
        return map[key];
    }
    
    public boolean hasKey(int key) {
        return (key < map.length && map[key] != MISSING);
    }
    
    // ---- factory methods
    
    public static IntMap idMap(int size) {
        int[] map = new int[size];
        for (int i = 0; i < size; i++) {
            map[i] = i;
        }
        return new IntMap(map);
    }
    
    // ---- from object
    
    public String toString() {
    	StringBuffer buf = new StringBuffer("[");
    	boolean seenFirst = false;
    	for (int i = 0, len = map.length; i < len; i++) {
			if (map[i] != MISSING) {
				if (seenFirst) {
					buf.append(", ");
				}
				seenFirst = true;
				buf.append(i);
				buf.append(" -> ");
				buf.append(map[i]);
			}
    	}
    	buf.append("]");
    	return buf.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12093.java