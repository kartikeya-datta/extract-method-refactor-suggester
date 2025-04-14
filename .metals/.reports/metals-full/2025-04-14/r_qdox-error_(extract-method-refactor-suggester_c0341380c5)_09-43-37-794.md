error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/125.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/125.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/125.java
text:
```scala
i@@f (kind.isSameOrLessThan(k)) {

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

package org.aspectj.bridge;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.aspectj.util.LangUtil;

/** 
 * Wrap an IMessageHandler to count messages handled.
 * Messages being ignored by the delegate IMessageHandler are not counted.
 */
public class CountingMessageHandler implements IMessageHandler {
    
    public final IMessageHandler delegate;
    public final CountingMessageHandler proxy;
    private final Hashtable counters;
    
    public static CountingMessageHandler makeCountingMessageHandler(IMessageHandler handler) {
    	if (handler instanceof CountingMessageHandler) {
    		return (CountingMessageHandler)handler;
    	} else {
    		return new CountingMessageHandler(handler);
    	}
    }
    
    
    public CountingMessageHandler(IMessageHandler delegate) {
        LangUtil.throwIaxIfNull(delegate, "delegate");
        this.delegate = delegate;
        this.counters = new Hashtable();
        proxy = (delegate instanceof CountingMessageHandler
            ? (CountingMessageHandler) delegate
            : null);
    }
    
    /** @return delegate.handleMessage(IMessage) */
    public boolean handleMessage(IMessage message) throws AbortException {
        if (null != proxy) {
            return proxy.handleMessage(message);
        }
        if (null != message) {
            IMessage.Kind kind = message.getKind();
            if (!isIgnoring(kind)) {
                increment(kind);
            }
        } 
        return delegate.handleMessage(message);
    }

    /** @return delegate.isIgnoring(IMessage.Kind) */
    public boolean isIgnoring(IMessage.Kind kind) {
        return delegate.isIgnoring(kind);
    }
    
    /** @return delegate.toString() */
    public String toString() {
        return delegate.toString();
    }

    /** 
     * Return count of messages seen through this interface.
     * @param kind the IMessage.Kind of the messages to count 
     *         (if null, count all)
     * @param orGreater if true, then count this kind and any 
     *         considered greater by the ordering of 
     *         IMessage.Kind.COMPARATOR
     * @return number of messages of this kind (optionally or greater)
     * @see IMessage.Kind.COMPARATOR
     */
    public int numMessages(IMessage.Kind kind, boolean orGreater) {
        if (null != proxy) {
            return proxy.numMessages(kind, orGreater);
        }
        int result = 0;
        if (null == kind) {            
            for (Enumeration enum = counters.elements(); enum.hasMoreElements();) {
				result += ((IntHolder) enum.nextElement()).count;				
			}
        } else if (!orGreater) {
            result = numMessages(kind);
        } else {
            for (Iterator iter = IMessage.KINDS.iterator(); iter.hasNext();) {
                IMessage.Kind k = (IMessage.Kind) iter.next();
                if (0 >= IMessage.Kind.COMPARATOR.compare(kind, k)) {
                    result += numMessages(k);
                }
            }
        }
        return result;
    }
    
    /** 
     * @return true if 0 is less than 
     * <code>numMessages(IMessage.ERROR, true)</code> 
     */
    public boolean hasErrors() {
        return (0 < numMessages(IMessage.ERROR, true));
    }
    
    private int numMessages(IMessage.Kind kind) {
        if (null != proxy) {
            return proxy.numMessages(kind);
        }
        IntHolder counter = (IntHolder) counters.get(kind);
        return (null == counter ? 0 : counter.count);        
    }

    private void increment(IMessage.Kind kind) {
        if (null != proxy) {
            throw new IllegalStateException("not called when proxying");
        }
        
        IntHolder counter = (IntHolder) counters.get(kind);
        if (null == counter) {
            counter = new IntHolder();
            counters.put(kind, counter);
        }
        counter.count++;
    }

    private static class IntHolder {
        int count;
    }
    
    public void reset() {
    	if (proxy != null) proxy.reset();
    	counters.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/125.java