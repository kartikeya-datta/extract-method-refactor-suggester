error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5418.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5418.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5418.java
text:
```scala
l@@ogger.trace("ThreadLocal with key of type [{0}] (value [{1}]) and a value of type [{2}] (value [{3}]):  The ThreadLocal has been forcibly removed.", args);

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.util;

import org.elasticsearch.util.logging.ESLogger;
import org.elasticsearch.util.logging.Loggers;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author kimchy (shay.banon)
 */
public class ThreadLocals {

    private static final ESLogger logger = Loggers.getLogger(ThreadLocals.class);

    public static class CleanableValue<T> {

        private T value;

        public CleanableValue(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }
    }

    public static void clearReferencesThreadLocals() {
        try {
            Thread[] threads = getThreads();
            // Make the fields in the Thread class that store ThreadLocals
            // accessible
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            Field inheritableThreadLocalsField = Thread.class.getDeclaredField("inheritableThreadLocals");
            inheritableThreadLocalsField.setAccessible(true);
            // Make the underlying array of ThreadLoad.ThreadLocalMap.Entry objects
            // accessible
            Class<?> tlmClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = tlmClass.getDeclaredField("table");
            tableField.setAccessible(true);

            for (int i = 0; i < threads.length; i++) {
                Object threadLocalMap;
                if (threads[i] != null) {
                    // Clear the first map
                    threadLocalMap = threadLocalsField.get(threads[i]);
                    clearThreadLocalMap(threadLocalMap, tableField);
                    // Clear the second map
                    threadLocalMap =
                            inheritableThreadLocalsField.get(threads[i]);
                    clearThreadLocalMap(threadLocalMap, tableField);
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to clean thread locals", e);
        }
    }


    /*
     * Clears the given thread local map object. Also pass in the field that
     * points to the internal table to save re-calculating it on every
     * call to this method.
     */

    private static void clearThreadLocalMap(Object map, Field internalTableField) throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        if (map != null) {
            Method mapRemove = map.getClass().getDeclaredMethod("remove", ThreadLocal.class);
            mapRemove.setAccessible(true);
            Object[] table = (Object[]) internalTableField.get(map);
            int staleEntriesCount = 0;
            if (table != null) {
                for (int j = 0; j < table.length; j++) {
                    if (table[j] != null) {
                        boolean remove = false;
                        // Check the key
                        Object key = ((Reference<?>) table[j]).get();
                        // Check the value
                        Field valueField = table[j].getClass().getDeclaredField("value");
                        valueField.setAccessible(true);
                        Object value = valueField.get(table[j]);
                        if ((value != null && CleanableValue.class.isAssignableFrom(value.getClass()))) {
                            remove = true;
                        }
                        if (remove) {
                            Object[] args = new Object[4];
                            if (key != null) {
                                args[0] = key.getClass().getCanonicalName();
                                args[1] = key.toString();
                            }
                            args[2] = value.getClass().getCanonicalName();
                            args[3] = value.toString();
                            if (logger.isDebugEnabled()) {
                                logger.debug("ThreadLocal with key of type [{0}] (value [{1}]) and a value of type [{2}] (value [{3}]):  The ThreadLocal has been forcibly removed.", args);
                            }
                            if (key == null) {
                                staleEntriesCount++;
                            } else {
                                mapRemove.invoke(map, key);
                            }
                        }
                    }
                }
            }
            if (staleEntriesCount > 0) {
                Method mapRemoveStale = map.getClass().getDeclaredMethod("expungeStaleEntries");
                mapRemoveStale.setAccessible(true);
                mapRemoveStale.invoke(map);
            }
        }
    }

    /*
     * Get the set of current threads as an array.
     */

    private static Thread[] getThreads() {
        // Get the current thread group
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        // Find the root thread group
        while (tg.getParent() != null) {
            tg = tg.getParent();
        }

        int threadCountGuess = tg.activeCount() + 50;
        Thread[] threads = new Thread[threadCountGuess];
        int threadCountActual = tg.enumerate(threads);
        // Make sure we don't miss any threads
        while (threadCountActual == threadCountGuess) {
            threadCountGuess *= 2;
            threads = new Thread[threadCountGuess];
            // Note tg.enumerate(Thread[]) silently ignores any threads that
            // can't fit into the array
            threadCountActual = tg.enumerate(threads);
        }

        return threads;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5418.java