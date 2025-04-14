error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16718.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16718.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16718.java
text:
```scala
public i@@nt compareTo(DeweyDecimal other) {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.util;

import java.util.StringTokenizer;

/**
 * Utility class to contain version numbers in "Dewey Decimal"
 * syntax.  Numbers in the "Dewey Decimal" syntax consist of positive
 * decimal integers separated by periods ".".  For example, "2.0" or
 * "1.2.3.4.5.6.7".  This allows an extensible number to be used to
 * represent major, minor, micro, etc versions.  The version number
 * must begin with a number.
 *
 */
public class DeweyDecimal implements Comparable<DeweyDecimal> {

    /** Array of components that make up DeweyDecimal */
    private final int[] components;

    /**
     * Construct a DeweyDecimal from an array of integer components.
     *
     * @param components an array of integer components.
     */
    public DeweyDecimal(final int[] components) {
        this.components = new int[components.length];
        System.arraycopy(components, 0, this.components, 0, components.length);
    }

    /**
     * Construct a DeweyDecimal from string in DeweyDecimal format.
     *
     * @param string the string in dewey decimal format
     * @exception NumberFormatException if string is malformed
     */
    public DeweyDecimal(final String string)
        throws NumberFormatException {
        final StringTokenizer tokenizer = new StringTokenizer(string, ".", true);
        final int size = tokenizer.countTokens();

        components = new int[ (size + 1) / 2 ];

        for (int i = 0; i < components.length; i++) {
            final String component = tokenizer.nextToken();
            if (component.length() == 0) {
                throw new NumberFormatException("Empty component in string");
            }

            components[ i ] = Integer.parseInt(component);

            //Strip '.' token
            if (tokenizer.hasMoreTokens()) {
                tokenizer.nextToken();

                //If it ended in a dot, throw an exception
                if (!tokenizer.hasMoreTokens()) {
                    throw new NumberFormatException("DeweyDecimal ended in a '.'");
                }
            }
        }
    }

    /**
     * Return number of components in <code>DeweyDecimal</code>.
     *
     * @return the number of components in dewey decimal
     */
    public int getSize() {
        return components.length;
    }

    /**
     * Return the component at specified index.
     *
     * @param index the index of components
     * @return the value of component at index
     */
    public int get(final int index) {
        return components[ index ];
    }

    /**
     * Return <code>true</code> if this <code>DeweyDecimal</code> is
     * equal to the other <code>DeweyDecimal</code>.
     *
     * @param other the other DeweyDecimal
     * @return true if equal to other DeweyDecimal, false otherwise
     */
    public boolean isEqual(final DeweyDecimal other) {
        final int max = Math.max(other.components.length, components.length);

        for (int i = 0; i < max; i++) {
            final int component1 = (i < components.length) ? components[ i ] : 0;
            final int component2 = (i < other.components.length) ? other.components[ i ] : 0;

            if (component2 != component1) {
                return false;
            }
        }

        return true; // Exact match
    }

    /**
     * Return <code>true</code> if this <code>DeweyDecimal</code> is
     * less than the other <code>DeweyDecimal</code>.
     *
     * @param other the other DeweyDecimal
     * @return true if less than other DeweyDecimal, false otherwise
     */
    public boolean isLessThan(final DeweyDecimal other) {
        return !isGreaterThanOrEqual(other);
    }

    /**
     * Return <code>true</code> if this <code>DeweyDecimal</code> is
     * less than or equal to the other <code>DeweyDecimal</code>.
     *
     * @param other the other DeweyDecimal
     * @return true if less than or equal to other DeweyDecimal, false otherwise
     */
    public boolean isLessThanOrEqual(final DeweyDecimal other) {
        return !isGreaterThan(other);
    }

    /**
     * Return <code>true</code> if this <code>DeweyDecimal</code> is
     * greater than the other <code>DeweyDecimal</code>.
     *
     * @param other the other DeweyDecimal
     * @return true if greater than other DeweyDecimal, false otherwise
     */
    public boolean isGreaterThan(final DeweyDecimal other) {
        final int max = Math.max(other.components.length, components.length);

        for (int i = 0; i < max; i++) {
            final int component1 = (i < components.length) ? components[ i ] : 0;
            final int component2 = (i < other.components.length) ? other.components[ i ] : 0;

            if (component2 > component1) {
                return false;
            }
            if (component2 < component1) {
                return true;
            }
        }

        return false; // Exact match
    }

    /**
     * Return <code>true</code> if this <code>DeweyDecimal</code> is
     * greater than or equal to the other <code>DeweyDecimal</code>.
     *
     * @param other the other DeweyDecimal
     * @return true if greater than or equal to other DeweyDecimal, false otherwise
     */
    public boolean isGreaterThanOrEqual(final DeweyDecimal other) {
        final int max = Math.max(other.components.length, components.length);

        for (int i = 0; i < max; i++) {
            final int component1 = (i < components.length) ? components[ i ] : 0;
            final int component2 = (i < other.components.length) ? other.components[ i ] : 0;

            if (component2 > component1) {
                return false;
            }
            if (component2 < component1) {
                return true;
            }
        }

        return true; // Exact match
    }

    /**
     * Return string representation of <code>DeweyDecimal</code>.
     *
     * @return the string representation of DeweyDecimal.
     */
    @Override public String toString() {
        final StringBuffer sb = new StringBuffer();

        for (int i = 0; i < components.length; i++) {
            if (i != 0) {
                sb.append('.');
            }
            sb.append(components[ i ]);
        }

        return sb.toString();
    }

    @Override public int compareTo(DeweyDecimal other) {
        final int max = Math.max(other.components.length, components.length);
        for (int i = 0; i < max; i++) {
            final int component1 = (i < components.length) ? components[ i ] : 0;
            final int component2 = (i < other.components.length) ? other.components[ i ] : 0;
            if (component1 != component2) {
                return component1 - component2;
            }
        }
        return 0;
    }

    @Override public int hashCode() {
        return toString().hashCode();
    }

    @Override public boolean equals(Object o) {
        return o instanceof DeweyDecimal && isEqual((DeweyDecimal) o);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16718.java