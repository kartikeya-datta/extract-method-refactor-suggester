error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7196.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7196.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7196.java
text:
```scala
m@@ap = new ConcurrentHashMapV8<>();

/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package jsr166e;
import jsr166e.LongAdder;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

/**
 * A keyed table of adders, that may be useful in computing frequency
 * counts and histograms, or may be used as a form of multiset.  A
 * {@link LongAdder} is associated with each key. Keys are added to
 * the table implicitly upon any attempt to update, or may be added
 * explicitly using method {@link #install}.
 *
 * <p><em>jsr166e note: This class is targeted to be placed in
 * java.util.concurrent.atomic.</em>
 *
 * @since 1.8
 * @author Doug Lea
 */
public class LongAdderTable<K> implements Serializable {
    /** Relies on default serialization */
    private static final long serialVersionUID = 7249369246863182397L;

    /** The underlying map */
    private final ConcurrentHashMapV8<K, LongAdder> map;

    static final class CreateAdder
        implements ConcurrentHashMapV8.Fun<Object, LongAdder> {
        public LongAdder apply(Object unused) { return new LongAdder(); }
    }

    private static final CreateAdder createAdder = new CreateAdder();

    /**
     * Creates a new empty table.
     */
    public LongAdderTable() {
        map = new ConcurrentHashMapV8<K, LongAdder>();
    }

    /**
     * If the given key does not already exist in the table, inserts
     * the key with initial sum of zero; in either case returning the
     * adder associated with this key.
     *
     * @param key the key
     * @return the adder associated with the key
     */
    public LongAdder install(K key) {
        return map.computeIfAbsent(key, createAdder);
    }

    /**
     * Adds the given value to the sum associated with the given
     * key.  If the key does not already exist in the table, it is
     * inserted.
     *
     * @param key the key
     * @param x the value to add
     */
    public void add(K key, long x) {
        map.computeIfAbsent(key, createAdder).add(x);
    }

    /**
     * Increments the sum associated with the given key.  If the key
     * does not already exist in the table, it is inserted.
     *
     * @param key the key
     */
    public void increment(K key) { add(key, 1L); }

    /**
     * Decrements the sum associated with the given key.  If the key
     * does not already exist in the table, it is inserted.
     *
     * @param key the key
     */
    public void decrement(K key) { add(key, -1L); }

    /**
     * Returns the sum associated with the given key, or zero if the
     * key does not currently exist in the table.
     *
     * @param key the key
     * @return the sum associated with the key, or zero if the key is
     * not in the table
     */
    public long sum(K key) {
        LongAdder a = map.get(key);
        return a == null ? 0L : a.sum();
    }

    /**
     * Resets the sum associated with the given key to zero if the key
     * exists in the table.  This method does <em>NOT</em> add or
     * remove the key from the table (see {@link #remove}).
     *
     * @param key the key
     */
    public void reset(K key) {
        LongAdder a = map.get(key);
        if (a != null)
            a.reset();
    }

    /**
     * Resets the sum associated with the given key to zero if the key
     * exists in the table.  This method does <em>NOT</em> add or
     * remove the key from the table (see {@link #remove}).
     *
     * @param key the key
     * @return the previous sum, or zero if the key is not
     * in the table
     */
    public long sumThenReset(K key) {
        LongAdder a = map.get(key);
        return a == null ? 0L : a.sumThenReset();
    }

    /**
     * Returns the sum totalled across all keys.
     *
     * @return the sum totalled across all keys
     */
    public long sumAll() {
        long sum = 0L;
        for (LongAdder a : map.values())
            sum += a.sum();
        return sum;
    }

    /**
     * Resets the sum associated with each key to zero.
     */
    public void resetAll() {
        for (LongAdder a : map.values())
            a.reset();
    }

    /**
     * Totals, then resets, the sums associated with all keys.
     *
     * @return the sum totalled across all keys
     */
    public long sumThenResetAll() {
        long sum = 0L;
        for (LongAdder a : map.values())
            sum += a.sumThenReset();
        return sum;
    }

    /**
     * Removes the given key from the table.
     *
     * @param key the key
     */
    public void remove(K key) { map.remove(key); }

    /**
     * Removes all keys from the table.
     */
    public void removeAll() { map.clear(); }

    /**
     * Returns the current set of keys.
     *
     * @return the current set of keys
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * Returns the current set of key-value mappings.
     *
     * @return the current set of key-value mappings
     */
    public Set<Map.Entry<K,LongAdder>> entrySet() {
        return map.entrySet();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7196.java