error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12934.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12934.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12934.java
text:
```scala
C@@SVRecord(final String[] values, final Map<String, Integer> mapping, final String comment, final long recordNumber) {

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

package org.apache.commons.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * A CSV record
 * 
 * @version $Id: $
 */
public class CSVRecord implements Serializable, Iterable<String> {

    private static final long serialVersionUID = 1L;

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The values of the record */
    private final String[] values;

    /** The column name to index mapping. */
    private final Map<String, Integer> mapping;

    /** The accumulated comments (if any) */
    private final String comment;
    
    /** The record number. */
    private final long recordNumber;

    CSVRecord(final String[] values, final Map<String, Integer> mapping, final String comment, long recordNumber) {
        this.recordNumber = recordNumber;
        this.values = values != null ? values : EMPTY_STRING_ARRAY;
        this.mapping = mapping;
        this.comment = comment;
    }

    /**
     * Returns a value by index.
     *
     * @param i
     *            the index of the column retrieved
     */
    public String get(final int i) {
        return values[i];
    }

    /**
     * Returns a value by name.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return the column value, or {@code null} if the column name is not found
     * @throws IllegalStateException
     *             if no header mapping was provided
     */
    public String get(final String name) {
        if (mapping == null) {
            throw new IllegalStateException("No header mapping was specified, the record values can't be accessed by name");
        }
        final Integer index = mapping.get(name);
        return index != null ? values[index.intValue()] : null;
    }

    /**
     * Checks whether a given column is mapped.
     * 
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given columns is mapped.
     */
    public boolean isMapped(final String name) {
        return mapping != null ? mapping.containsKey(name) : false;
    }
    
    /**
     * Checks whether a given columns is mapped and has a value.
     * 
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given columns is mapped.
     */
    public boolean isSet(final String name) {
        return isMapped(name) && mapping.get(name).intValue() < values.length;
    }
    
    public Iterator<String> iterator() {
        return Arrays.asList(values).iterator();
    }

    String[] values() {
        return values;
    }

    public String getComment() {
        return comment;
    }

    public long getRecordNumber() {
        return recordNumber;
    }

    /**
     * Returns the number of values in this record.
     */
    public int size() {
        return values.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12934.java