error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8608.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8608.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8608.java
text:
```scala
S@@tringBuilder outBuffer = new StringBuilder();

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
package org.apache.commons.math.stat;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.exception.util.LocalizedFormats;

/**
 * Maintains a frequency distribution.
 * <p>
 * Accepts int, long, char or Comparable values.  New values added must be
 * comparable to those that have been added, otherwise the add method will
 * throw an IllegalArgumentException.</p>
 * <p>
 * Integer values (int, long, Integer, Long) are not distinguished by type --
 * i.e. <code>addValue(Long.valueOf(2)), addValue(2), addValue(2l)</code> all have
 * the same effect (similarly for arguments to <code>getCount,</code> etc.).</p>
 * <p>
 * char values are converted by <code>addValue</code> to Character instances.
 * As such, these values are not comparable to integral values, so attempts
 * to combine integral types with chars in a frequency distribution will fail.
 * </p>
 * <p>
 * The values are ordered using the default (natural order), unless a
 * <code>Comparator</code> is supplied in the constructor.</p>
 *
 * @version $Revision$ $Date$
 */
public class Frequency implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -3845586908418844111L;

    /** underlying collection */
    private final TreeMap<Comparable<?>, Long> freqTable;

    /**
     * Default constructor.
     */
    public Frequency() {
        freqTable = new TreeMap<Comparable<?>, Long>();
    }

    /**
     * Constructor allowing values Comparator to be specified.
     *
     * @param comparator Comparator used to order values
     */
    @SuppressWarnings("unchecked") // TODO is the cast OK?
    public Frequency(Comparator<?> comparator) {
        freqTable = new TreeMap<Comparable<?>, Long>((Comparator<? super Comparable<?>>) comparator);
    }

    /**
     * Return a string representation of this frequency
     * distribution.
     *
     * @return a string representation.
     */
    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("Value \t Freq. \t Pct. \t Cum Pct. \n");
        Iterator<Comparable<?>> iter = freqTable.keySet().iterator();
        while (iter.hasNext()) {
            Comparable<?> value = iter.next();
            outBuffer.append(value);
            outBuffer.append('\t');
            outBuffer.append(getCount(value));
            outBuffer.append('\t');
            outBuffer.append(nf.format(getPct(value)));
            outBuffer.append('\t');
            outBuffer.append(nf.format(getCumPct(value)));
            outBuffer.append('\n');
        }
        return outBuffer.toString();
    }

    /**
     * Adds 1 to the frequency count for v.
     * <p>
     * If other objects have already been added to this Frequency, v must
     * be comparable to those that have already been added.
     * </p>
     *
     * @param v the value to add.
     * @throws IllegalArgumentException if <code>v</code> is not comparable with previous entries
     */
    public void addValue(Comparable<?> v){
        Comparable<?> obj = v;
        if (v instanceof Integer) {
           obj = Long.valueOf(((Integer) v).longValue());
        }
        try {
            Long count = freqTable.get(obj);
            if (count == null) {
                freqTable.put(obj, Long.valueOf(1));
            } else {
                freqTable.put(obj, Long.valueOf(count.longValue() + 1));
            }
        } catch (ClassCastException ex) {
            //TreeMap will throw ClassCastException if v is not comparable
            throw MathRuntimeException.createIllegalArgumentException(
                  LocalizedFormats.INSTANCES_NOT_COMPARABLE_TO_EXISTING_VALUES,
                  v.getClass().getName());
        }
    }

    /**
     * Adds 1 to the frequency count for v.
     *
     * @param v the value to add.
     */
    public void addValue(int v) {
        addValue(Long.valueOf(v));
    }

    /**
     * Adds 1 to the frequency count for v.
     *
     * @param v the value to add.
     */
    public void addValue(long v) {
        addValue(Long.valueOf(v));
    }

    /**
     * Adds 1 to the frequency count for v.
     *
     * @param v the value to add.
     */
    public void addValue(char v) {
        addValue(Character.valueOf(v));
    }

    /** Clears the frequency table */
    public void clear() {
        freqTable.clear();
    }

    /**
     * Returns an Iterator over the set of values that have been added.
     * <p>
     * If added values are integral (i.e., integers, longs, Integers, or Longs),
     * they are converted to Longs when they are added, so the objects returned
     * by the Iterator will in this case be Longs.</p>
     *
     * @return values Iterator
     */
    public Iterator<Comparable<?>> valuesIterator() {
        return freqTable.keySet().iterator();
    }

    //-------------------------------------------------------------------------

    /**
     * Returns the sum of all frequencies.
     *
     * @return the total frequency count.
     */
    public long getSumFreq() {
        long result = 0;
        Iterator<Long> iterator = freqTable.values().iterator();
        while (iterator.hasNext())  {
            result += iterator.next().longValue();
        }
        return result;
    }

    /**
     * Returns the number of values = v.
     * Returns 0 if the value is not comparable.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     */
    public long getCount(Comparable<?> v) {
        if (v instanceof Integer) {
            return getCount(((Integer) v).longValue());
        }
        long result = 0;
        try {
            Long count =  freqTable.get(v);
            if (count != null) {
                result = count.longValue();
            }
        } catch (ClassCastException ex) {
            // ignore and return 0 -- ClassCastException will be thrown if value is not comparable
        }
        return result;
    }

    /**
     * Returns the number of values = v.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     */
    public long getCount(int v) {
        return getCount(Long.valueOf(v));
    }

    /**
     * Returns the number of values = v.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     */
    public long getCount(long v) {
        return getCount(Long.valueOf(v));
    }

    /**
     * Returns the number of values = v.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     */
    public long getCount(char v) {
        return getCount(Character.valueOf(v));
    }

    //-------------------------------------------------------------

    /**
     * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns <code>Double.NaN</code> if no values have been added.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public double getPct(Comparable<?> v) {
        final long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return (double) getCount(v) / (double) sumFreq;
    }

    /**
     * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public double getPct(int v) {
        return getPct(Long.valueOf(v));
    }

    /**
     * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public double getPct(long v) {
        return getPct(Long.valueOf(v));
    }

    /**
     * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public double getPct(char v) {
        return getPct(Character.valueOf(v));
    }

    //-----------------------------------------------------------------------------------------

    /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup.
     * @return the proportion of values equal to v
     */
    public long getCumFreq(Comparable<?> v) {
        if (getSumFreq() == 0) {
            return 0;
        }
        if (v instanceof Integer) {
            return getCumFreq(((Integer) v).longValue());
        }
        @SuppressWarnings("unchecked") // OK, freqTable is Comparable<?>
        Comparator<Comparable<?>> c = (Comparator<Comparable<?>>) freqTable.comparator();
        if (c == null) {
            c = new NaturalComparator();
        }
        long result = 0;

        try {
            Long value = freqTable.get(v);
            if (value != null) {
                result = value.longValue();
            }
        } catch (ClassCastException ex) {
            return result;   // v is not comparable
        }

        if (c.compare(v, freqTable.firstKey()) < 0) {
            return 0;  // v is comparable, but less than first value
        }

        if (c.compare(v, freqTable.lastKey()) >= 0) {
            return getSumFreq();    // v is comparable, but greater than the last value
        }

        Iterator<Comparable<?>> values = valuesIterator();
        while (values.hasNext()) {
            Comparable<?> nextValue = values.next();
            if (c.compare(v, nextValue) > 0) {
                result += getCount(nextValue);
            } else {
                return result;
            }
        }
        return result;
    }

     /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public long getCumFreq(int v) {
        return getCumFreq(Long.valueOf(v));
    }

     /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public long getCumFreq(long v) {
        return getCumFreq(Long.valueOf(v));
    }

    /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public long getCumFreq(char v) {
        return getCumFreq(Character.valueOf(v));
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns <code>Double.NaN</code> if no values have been added.
     * Returns 0 if at least one value has been added, but v is not comparable
     * to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     */
    public double getCumPct(Comparable<?> v) {
        final long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return (double) getCumFreq(v) / (double) sumFreq;
    }

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     */
    public double getCumPct(int v) {
        return getCumPct(Long.valueOf(v));
    }

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     */
    public double getCumPct(long v) {
        return getCumPct(Long.valueOf(v));
    }

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     */
    public double getCumPct(char v) {
        return getCumPct(Character.valueOf(v));
    }

    /**
     * A Comparator that compares comparable objects using the
     * natural order.  Copied from Commons Collections ComparableComparator.
     */
    private static class NaturalComparator<T extends Comparable<T>> implements Comparator<Comparable<T>>, Serializable {

        /** Serializable version identifier */
        private static final long serialVersionUID = -3852193713161395148L;

        /**
         * Compare the two {@link Comparable Comparable} arguments.
         * This method is equivalent to:
         * <pre>(({@link Comparable Comparable})o1).{@link Comparable#compareTo compareTo}(o2)</pre>
         *
         * @param  o1 the first object
         * @param  o2 the second object
         * @return  result of comparison
         * @throws NullPointerException when <i>o1</i> is <code>null</code>,
         *         or when <code>((Comparable)o1).compareTo(o2)</code> does
         * @throws ClassCastException when <i>o1</i> is not a {@link Comparable Comparable},
         *         or when <code>((Comparable)o1).compareTo(o2)</code> does
         */
        @SuppressWarnings("unchecked") // cast to (T) may throw ClassCastException, see Javadoc
        public int compare(Comparable<T> o1, Comparable<T> o2) {
            return o1.compareTo((T) o2);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result +
                 ((freqTable == null) ? 0 : freqTable.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Frequency))
            return false;
        Frequency other = (Frequency) obj;
        if (freqTable == null) {
            if (other.freqTable != null)
                return false;
        } else if (!freqTable.equals(other.freqTable))
            return false;
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8608.java