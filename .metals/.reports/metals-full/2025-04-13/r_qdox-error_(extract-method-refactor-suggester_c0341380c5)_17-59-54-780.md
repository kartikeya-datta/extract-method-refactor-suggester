error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17794.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17794.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[139,80]

error in qdox parser
file content:
```java
offset: 3422
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17794.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.util.value;


import java.io.Serializable;

import wicket.util.lang.Primitives;

/**
 * A base class for value classes based on a Java int primitive which want to implement
 * standard operations on that value without the pain of aggregating a Int object.
 * @author Jonathan Locke
 */
public class IntValue implements Comparable, Serializable
{
	/** serialVersionUID */
	private static final long serialVersionUID = 1458111047822645764L;
	
	// The int value
    protected final int value;

    /**
     * Constructor
     * @param value The int value
     */
    public IntValue(final int value)
    {
        this.value = value;
    }

    /**
     * @param that The value to compare against
     * @return True if this value is greater than that value
     */
    public final boolean greaterThan(final IntValue that)
    {
        return this.value > that.value;
    }

    /**
     * @param value The value to compare against
     * @return True if this value is greater than the given value
     */
    public final boolean greaterThan(final int value)
    {
        return this.value > value;
    }

    /**
     * @param that The value to compare against
     * @return True if this value is less than that value
     */
    public final boolean lessThan(final IntValue that)
    {
        return this.value < that.value;
    }

    /**
     * @param that The value to compare against
     * @return True if this value is less than that value
     */
    public final boolean lessThan(final int that)
    {
        return this.value < that;
    }

    /**
     * @param that The value to compare against
     * @return True if this value is equal to that value
     */
    public final boolean equals(final Object that)
    {
        if (that instanceof IntValue)
        {
            return this.value == ((IntValue) that).value;
        }

        return false;
    }

    /**
     * @return Hashcode for this object
     */
    public final int hashCode()
    {
        return Primitives.hashCode(value);
    }

    /**
     * @param object The object to compare with
     * @return 0 if equal, -1 if less than or 1 if greater than
     */
    public final int compareTo(final Object object)
    {
        final IntValue that = (IntValue) object;

        if (this.value < that.value)
        {
            return -1;
        }

        if (this.value > that.value)
        {
            return 1;
        }

        return 0;
    }

    /**
     * Converts this byte count to a string
     * @return The string for this byte count
     */
    public String toString()
    {
        return String.valueOf(value);
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17794.java