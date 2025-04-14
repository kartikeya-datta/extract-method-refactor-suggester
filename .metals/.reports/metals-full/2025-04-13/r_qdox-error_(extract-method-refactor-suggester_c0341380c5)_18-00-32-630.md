error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12911.java
text:
```scala
l@@ast = iterator.next();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.iterators;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.ResettableIterator;

/**
 * Implements a <code>MapIterator</code> using a Map entrySet.
 * Reverse iteration is not supported.
 * <pre>
 * MapIterator it = map.mapIterator();
 * while (it.hasNext()) {
 *   Object key = it.next();
 *   Object value = it.getValue();
 *   it.setValue(newValue);
 * }
 * </pre>
 *  
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class EntrySetMapIterator<K, V> implements MapIterator<K, V>, ResettableIterator<K> {
    
    private final Map<K, V> map;
    private Iterator<Map.Entry<K, V>> iterator;
    private Map.Entry<K, V> last;
    private boolean canRemove = false;
    
    /**
     * Constructor.
     * 
     * @param map  the map to iterate over
     */
    public EntrySetMapIterator(Map<K, V> map) {
        super();
        this.map = map;
        this.iterator = map.entrySet().iterator();
    }

    //-----------------------------------------------------------------------    
    /**
     * Checks to see if there are more entries still to be iterated.
     *
     * @return <code>true</code> if the iterator has more elements
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Gets the next <em>key</em> from the <code>Map</code>.
     *
     * @return the next key in the iteration
     * @throws java.util.NoSuchElementException if the iteration is finished
     */
    public K next() {
        last = (Map.Entry<K, V>) iterator.next();
        canRemove = true;
        return last.getKey();
    }

    //-----------------------------------------------------------------------
    /**
     * Removes the last returned key from the underlying <code>Map</code>.
     * <p>
     * This method can be called once per call to <code>next()</code>.
     *
     * @throws UnsupportedOperationException if remove is not supported by the map
     * @throws IllegalStateException if <code>next()</code> has not yet been called
     * @throws IllegalStateException if <code>remove()</code> has already been called
     *  since the last call to <code>next()</code>
     */
    public void remove() {
        if (canRemove == false) {
            throw new IllegalStateException("Iterator remove() can only be called once after next()");
        }
        iterator.remove();
        last = null;
        canRemove = false;
    }
    
    //-----------------------------------------------------------------------
    /**
     * Gets the current key, which is the key returned by the last call
     * to <code>next()</code>.
     *
     * @return the current key
     * @throws IllegalStateException if <code>next()</code> has not yet been called
     */
    public K getKey() {
        if (last == null) {
            throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
        }
        return last.getKey();
    }

    /**
     * Gets the current value, which is the value associated with the last key
     * returned by <code>next()</code>.
     *
     * @return the current value
     * @throws IllegalStateException if <code>next()</code> has not yet been called
     */
    public V getValue() {
        if (last == null) {
            throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
        }
        return last.getValue();
    }

    /**
     * Sets the value associated with the current key.
     *
     * @param value  the new value
     * @return the previous value
     * @throws UnsupportedOperationException if setValue is not supported by the map
     * @throws IllegalStateException if <code>next()</code> has not yet been called
     * @throws IllegalStateException if <code>remove()</code> has been called since the
     *  last call to <code>next()</code>
     */
    public V setValue(V value) {
        if (last == null) {
            throw new IllegalStateException("Iterator setValue() can only be called after next() and before remove()");
        }
        return last.setValue(value);
    }

    //-----------------------------------------------------------------------
    /**
     * Resets the state of the iterator.
     */
    public void reset() {
        iterator = map.entrySet().iterator();
        last = null;
        canRemove = false;
    }
    
    /**
     * Gets the iterator as a String.
     * 
     * @return a string version of the iterator
     */    
    @Override
    public String toString() {
        if (last != null) {
            return "MapIterator[" + getKey() + "=" + getValue() + "]";
        }
        return "MapIterator[]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12911.java