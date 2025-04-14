error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10476.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10476.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10476.java
text:
```scala
r@@eturn (CFlow)stack.elementAt(0);

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.runtime.internal;

import org.aspectj.lang.NoAspectBoundException;
import org.aspectj.runtime.CFlow;

import java.util.Stack;
import java.util.Hashtable;
import java.util.Enumeration;

public class CFlowStack {
    private Hashtable stacks = new Hashtable();
    private Thread cached_thread;
    private Stack cached_stack;
    private int change_count = 0;
    private static final int COLLECT_AT = 20000;
    private static final int MIN_COLLECT_AT = 100;

    private synchronized Stack getThreadStack() {
        if (Thread.currentThread() != cached_thread) {
            cached_thread = Thread.currentThread();
            cached_stack = (Stack)stacks.get(cached_thread);
            if (cached_stack == null) {
                cached_stack = new Stack();
                stacks.put(cached_thread, cached_stack);
            }
            change_count++;
            // Collect more often if there are many threads, but not *too* often
            int size = Math.max(1, stacks.size()); // should be >1 b/c always live threads, but...
            if (change_count > Math.max(MIN_COLLECT_AT, COLLECT_AT/size)) {
                Stack dead_stacks = new Stack();
                for (Enumeration e = stacks.keys(); e.hasMoreElements(); ) {
                    Thread t = (Thread)e.nextElement();
                    if (!t.isAlive()) dead_stacks.push(t);
                }
                for (Enumeration e = dead_stacks.elements(); e.hasMoreElements(); ) {
                    Thread t = (Thread)e.nextElement();
                    stacks.remove(t);
                }
                change_count = 0;
            }
        }
        return cached_stack;
    }

	//XXX dangerous, try to remove
    public void push(Object obj) {
        getThreadStack().push(obj);
    }

    public void pushInstance(Object obj) {
        getThreadStack().push(new CFlow(obj));
    }

    public void push(Object[] obj) {
        getThreadStack().push(new CFlowPlusState(obj));
    }

    public void pop() {
        getThreadStack().pop();
    }

    public Object peek() {
        Stack stack = getThreadStack();
        if (stack.isEmpty()) throw new org.aspectj.lang.NoAspectBoundException();
        return (Object)stack.peek();
    }
    
    public Object get(int index) {
        CFlow cf = peekCFlow();
        return (null == cf ? null : cf.get(index));
    }

    public Object peekInstance() {
    	CFlow cf = peekCFlow();
    	if (cf != null ) return cf.getAspect();
    	else throw new NoAspectBoundException();
    }

    public CFlow peekCFlow() {
        Stack stack = getThreadStack();
        if (stack.isEmpty()) return null;
        return (CFlow)stack.peek();
    }

    public CFlow peekTopCFlow() {
        Stack stack = getThreadStack();
        if (stack.isEmpty()) return null;
        return (CFlow)stack.get(0);
    }

    public boolean isValid() {
        return !getThreadStack().isEmpty();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10476.java