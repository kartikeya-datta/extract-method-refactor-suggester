error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3428.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3428.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3428.java
text:
```scala
public static final T@@hreadLocalStack<Map<String, EntityManager>> nonTxStack = new ThreadLocalStack<Map<String, EntityManager>>();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.jpa.container;

import static org.jboss.as.jpa.JpaLogger.ROOT_LOGGER;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 * Close the non tx invocations on transaction scoped entity manager
 *
 * @author Scott Marlow
 */
public class NonTxEmCloser {
    /**
     * Each thread will have its own list of SB invocations in progress.
     * Key = scoped persistence unit name
     */
    public static ThreadLocalStack<Map<String, EntityManager>> nonTxStack = new ThreadLocalStack<Map<String, EntityManager>>();

    /**
     * entered new session bean invocation, start new collection for tracking transactional entity managers created
     * without a JTA transaction.
     */
    public static void pushCall() {
        nonTxStack.push(null);          // to conserve memory/cpu cycles, push a null placeholder that will only get replaced
        // with a Map if we actually need it (in add() below).
    }

    /**
     * current session bean invocation is ending, close any transactional entity managers created without a JTA
     * transaction.
     */
    public static void popCall() {
        Map<String, EntityManager> emStack = nonTxStack.pop();
        if (emStack != null) {
            for (EntityManager entityManager : emStack.values()) {
                try {
                    entityManager.close();
                } catch (RuntimeException safeToIgnore) {
                    if (ROOT_LOGGER.isTraceEnabled()) {
                        ROOT_LOGGER.trace("Could not close (non-transactional) container managed entity manager." +
                            "  This shouldn't impact application functionality (only read " +
                            "operations occur in non-transactional mode)", safeToIgnore);
                    }
                }
            }
        }
    }

    /**
     * Return the transactional entity manager for the specified scoped persistence unit name
     *
     * @param puScopedName
     * @return
     */
    public static EntityManager get(String puScopedName) {
        Map<String, EntityManager> map = nonTxStack.get();
        if (map != null) {
            return map.get(puScopedName);
        }
        return null;
    }

    public static void add(String puScopedName, EntityManager entityManager) {
        Map<String, EntityManager> map = nonTxStack.get();
        if (map == null && nonTxStack.getList() != null) {
            // replace null with a collection to hold the entity managers.
            map = new HashMap<String, EntityManager>();
            nonTxStack.replace(map);    // replace top of stack (currently null) with new collection
        }
        if (map != null) {
            map.put(puScopedName, entityManager);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3428.java