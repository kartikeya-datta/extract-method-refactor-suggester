error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4177.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4177.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4177.java
text:
```scala
O@@bject[] entries = new Object[fNum << 1];

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

package org.apache.xerces.util;


/**
 * This class is an unsynchronized hash table primary used for String
 * to Object mapping.
 * <p>
 * The hash code uses the same algorithm as SymbolTable class.
 * 
 * @author Elena Litani
 * @version $Id$
 */
public class SymbolHash {

    //
    // Constants
    //

    /** Default table size. */
    protected int fTableSize = 101;

    //
    // Data
    //

    /** Buckets. */
    protected Entry[] fBuckets; 

    /** Number of elements. */
    protected int fNum = 0;

    //
    // Constructors
    //

    /** Constructs a key table with the default size. */
    public SymbolHash() {
        fBuckets = new Entry[fTableSize];
    }

    /**
     * Constructs a key table with a given size.
     * 
     * @param size  the size of the key table.
     */
    public SymbolHash(int size) {
        fTableSize = size;
        fBuckets = new Entry[fTableSize];
    }

    //
    // Public methods
    //

    /**
     * Adds the key/value mapping to the key table. If the key already exists, 
     * the previous value associated with this key is overwritten by the new
     * value.
     * 
     * @param key
     * @param value 
     */
    public void put(Object key, Object value) {
        int bucket = (key.hashCode() & 0x7FFFFFFF) % fTableSize;
        Entry entry = search(key, bucket);

        // replace old value
        if (entry != null) {
            entry.value = value;
        }
        // create new entry
        else {
            entry = new Entry(key, value, fBuckets[bucket]);
            fBuckets[bucket] = entry;
            fNum++;
        }
    }

    /**
     * Get the value associated with the given key.
     * 
     * @param key
     * @return the value associated with the given key.
     */
    public Object get(Object key) {
        int bucket = (key.hashCode() & 0x7FFFFFFF) % fTableSize;
        Entry entry = search(key, bucket);
        if (entry != null) {
            return entry.value;
        }
        return null;
    }

    /**
     * Get the number of key/value pairs stored in this table.
     * 
     * @return the number of key/value pairs stored in this table.
     */
    public int getLength() {
        return fNum;
    }
    
    /**
     * Add all values to the given array. The array must have enough entry.
     * 
     * @param elements  the array to store the elements
     * @param from      where to start store element in the array
     * @return          number of elements copied to the array
     */
    public int getValues(Object[] elements, int from) {
        for (int i=0, j=0; i<fTableSize && j<fNum; i++) {
            for (Entry entry = fBuckets[i]; entry != null; entry = entry.next) {
                elements[from+j] = entry.value;
                j++;
            }
        }
        return fNum;
    }

    /**
     * Return key/value pairs of all entries in the map
     */
    public Object[] getEntries() {
        Object[] entries = new String[fNum << 1];
        for (int i=0, j=0; i<fTableSize && j<fNum << 1; i++) {
            for (Entry entry = fBuckets[i]; entry != null; entry = entry.next) {
                entries[j] = entry.key;
                entries[++j] = entry.value; 
                j++;
            }
        }
        return entries;
    }

    /**
     * Make a clone of this object.
     */
    public SymbolHash makeClone() {
        SymbolHash newTable = new SymbolHash(fTableSize);
        newTable.fNum = fNum;
        for (int i = 0; i < fTableSize; i++) {
            if (fBuckets[i] != null)
                newTable.fBuckets[i] = fBuckets[i].makeClone();
        }
        return newTable;
    }
    
    /**
     * Remove all key/value assocaition. This tries to save a bit of GC'ing
     * by at least keeping the fBuckets array around.
     */
    public void clear() {
        for (int i=0; i<fTableSize; i++) {
            fBuckets[i] = null;
        }
        fNum = 0;
    } // clear():  void

    protected Entry search(Object key, int bucket) {
        // search for identical key
        for (Entry entry = fBuckets[bucket]; entry != null; entry = entry.next) {
            if (key.equals(entry.key))
                return entry;
        }
        return null;
    }
    
    //
    // Classes
    //

    /**
     * This class is a key table entry. Each entry acts as a node
     * in a linked list.
     */
    protected static final class Entry {
        // key/value
        public Object key;
        public Object value;
        /** The next entry. */
        public Entry next;

        public Entry() {
            key = null;
            value = null;
            next = null;
        }

        public Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
        public Entry makeClone() {
            Entry entry = new Entry();
            entry.key = key;
            entry.value = value;
            if (next != null)
                entry.next = next.makeClone();
            return entry;
        }
    } // entry

} // class SymbolHash

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4177.java