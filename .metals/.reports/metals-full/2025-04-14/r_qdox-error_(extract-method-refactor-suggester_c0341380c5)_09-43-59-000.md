error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/898.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/898.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/898.java
text:
```scala
r@@eturn false;

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
package org.apache.commons.collections.trie;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

import org.apache.commons.collections.Trie;

/**
 * This class provides some basic {@link Trie} functionality and 
 * utility methods for actual {@link Trie} implementations.
 * 
 * @since 4.0
 * @version $Id$
 */
abstract class AbstractTrie<K, V> extends AbstractMap<K, V> 
        implements Trie<K, V>, Serializable {
    
    private static final long serialVersionUID = 5826987063535505652L;
    
    /**
     * The {@link KeyAnalyzer} that's being used to build the 
     * PATRICIA {@link Trie}.
     */
    protected final KeyAnalyzer<? super K> keyAnalyzer;
    
    /** 
     * Constructs a new {@link Trie} using the given {@link KeyAnalyzer}.
     */
    public AbstractTrie(final KeyAnalyzer<? super K> keyAnalyzer) {
        if (keyAnalyzer == null) {
            throw new NullPointerException("keyAnalyzer");
        }
        
        this.keyAnalyzer = keyAnalyzer;
    }
    
    /**
     * Returns the {@link KeyAnalyzer} that constructed the {@link Trie}.
     */
    public KeyAnalyzer<? super K> getKeyAnalyzer() {
        return keyAnalyzer;
    }
    
    /**
     * {@inheritDoc}
     */
    public K selectKey(final K key) {
        final Map.Entry<K, V> entry = select(key);
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }
    
    /**
     * {@inheritDoc}
     */
    public V selectValue(final K key) {
        final Map.Entry<K, V> entry = select(key);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("Trie[").append(size()).append("]={\n");
        for (final Map.Entry<K, V> entry : entrySet()) {
            buffer.append("  ").append(entry).append("\n");
        }
        buffer.append("}\n");
        return buffer.toString();
    }
    
    /**
     * A utility method to cast keys. It actually doesn't
     * cast anything. It's just fooling the compiler!
     */
    @SuppressWarnings("unchecked")
    final K castKey(final Object key) {
        return (K)key;
    }
    
    /**
     * Returns the length of the given key in bits
     * 
     * @see KeyAnalyzer#lengthInBits(Object)
     */
    final int lengthInBits(final K key) {
        if (key == null) {
            return 0;
        }
        
        return keyAnalyzer.lengthInBits(key);
    }
    
    /**
     * Returns the number of bits per element in the key
     * 
     * @see KeyAnalyzer#bitsPerElement()
     */
    final int bitsPerElement() {
        return keyAnalyzer.bitsPerElement();
    }
    
    /**
     * Returns whether or not the given bit on the 
     * key is set or false if the key is null.
     * 
     * @see KeyAnalyzer#isBitSet(Object, int, int)
     */
    final boolean isBitSet(final K key, final int bitIndex, final int lengthInBits) {
        if (key == null) { // root's might be null!
            return false;
        }
        return keyAnalyzer.isBitSet(key, bitIndex, lengthInBits);
    }
    
    /**
     * Utility method for calling {@link KeyAnalyzer#bitIndex(Object, int, int, Object, int, int)}
     */
    final int bitIndex(final K key, final K foundKey) {
        return keyAnalyzer.bitIndex(key, 0, lengthInBits(key), 
                foundKey, 0, lengthInBits(foundKey));
    }
    
    /**
     * An utility method for calling {@link KeyAnalyzer#compare(Object, Object)}
     */
    final boolean compareKeys(final K key, final K other) {
        if (key == null) {
            return other == null;
        } else if (other == null) {
            return key == null;
        }
        
        return keyAnalyzer.compare(key, other) == 0;
    }
    
    /**
     * Returns true if both values are either null or equal
     */
    static boolean compare(final Object a, final Object b) {
        return a == null ? b == null : a.equals(b);
    }
    
    /**
     * A basic implementation of {@link Entry}
     */
    abstract static class BasicEntry<K, V> implements Map.Entry<K, V>, Serializable {
        
        private static final long serialVersionUID = -944364551314110330L;

        protected K key;
        
        protected V value;
        
        private final int hashCode;
        
        public BasicEntry(final K key) {
            this.key = key;
            this.hashCode = key != null ? key.hashCode() : 0;
        }
        
        public BasicEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
            
            this.hashCode = (key != null ? key.hashCode() : 0)
                    ^ (value != null ? value.hashCode() : 0);
        }
        
        /**
         * Replaces the current key and value with the provided
         * key &amp; value
         */
        public V setKeyValue(final K key, final V value) {
            this.key = key;
            return setValue(value);
        }
        
        /**
         * {@inheritDoc}
         */
        public K getKey() {
            return key;
        }
        
        /**
         * {@inheritDoc}
         */
        public V getValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        public V setValue(final V value) {
            final V previous = this.value;
            this.value = value;
            return previous;
        }
        
        @Override
        public int hashCode() {
            return hashCode;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Map.Entry)) {
                return false;
            }
            
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>)o;
            if (compare(key, other.getKey()) 
                    && compare(value, other.getValue())) {
                return true;
            }
            return false;
        }
        
        @Override
        public String toString() {
            return key + "=" + value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/898.java