error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2233.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2233.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,66]

error in qdox parser
file content:
```java
offset: 66
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2233.java
text:
```scala
"Overlapping ranges passed to normalize: see CASSANDRA-2641: " + p@@revious + " and " + unwrapped;

package org.apache.cassandra.dht;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import org.apache.cassandra.io.ICompactSerializer2;
import org.apache.cassandra.utils.Pair;

public abstract class AbstractBounds implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static AbstractBoundsSerializer serializer = new AbstractBoundsSerializer();

    public static ICompactSerializer2<AbstractBounds> serializer()
    {
        return serializer;
    }

    private enum Type
    {
        RANGE,
        BOUNDS
    }

    public final Token left;
    public final Token right;

    protected transient final IPartitioner partitioner;

    public AbstractBounds(Token left, Token right, IPartitioner partitioner)
    {
        this.left = left;
        this.right = right;
        this.partitioner = partitioner;
    }

    /**
     * Given token T and AbstractBounds ?L,R], returns Pair(?L,T], ?T,R])
     * (where ? means that the same type of Bounds is returned -- Range or Bounds -- as the original.)
     * The original AbstractBounds must contain the token T.
     * If the split would cause one of the left or right side to be empty, it will be null in the result pair.
     */
    public Pair<AbstractBounds,AbstractBounds> split(Token token)
    {
        assert left.equals(token) || contains(token);
        AbstractBounds lb = createFrom(token);
        // we contain this token, so only one of the left or right can be empty
        AbstractBounds rb = lb != null && token.equals(right) ? null : new Range(token, right);
        return new Pair<AbstractBounds,AbstractBounds>(lb, rb);
    }

    @Override
    public int hashCode()
    {
        return 31 * left.hashCode() + right.hashCode();
    }

    public abstract boolean equals(Object obj);

    public abstract boolean contains(Token start);

    /** @return A clone of this AbstractBounds with a new right Token, or null if an identical range would be created. */
    public abstract AbstractBounds createFrom(Token right);

    public abstract List<AbstractBounds> unwrap();

    /**
     * @return A copy of the given list of non-intersecting bounds with all bounds unwrapped, sorted by bound.left.
     * This method does not allow overlapping ranges as input.
     */
    public static List<AbstractBounds> normalize(Collection<? extends AbstractBounds> bounds)
    {
        // unwrap all
        List<AbstractBounds> output = new ArrayList<AbstractBounds>();
        AbstractBounds previous = null;
        for (AbstractBounds bound : bounds)
        {
            List<AbstractBounds> unwrapped = bound.unwrap();
            assert previous == null || previous.right.compareTo(unwrapped.get(0).left) <= 0 :
                "Overlapping ranges passed to normalize: see CASSANDRA-2461: " + previous + " and " + unwrapped;
            output.addAll(unwrapped);
            previous = unwrapped.get(unwrapped.size() - 1);
        }

        // sort by left
        Collections.sort(output, new Comparator<AbstractBounds>()
        {
            public int compare(AbstractBounds b1, AbstractBounds b2)
            {
                return b1.left.compareTo(b2.left);
            }
        });
        return output;
    }

    private static class AbstractBoundsSerializer implements ICompactSerializer2<AbstractBounds>
    {
        public void serialize(AbstractBounds range, DataOutput out) throws IOException
        {
            out.writeInt(range instanceof Range ? Type.RANGE.ordinal() : Type.BOUNDS.ordinal());
            Token.serializer().serialize(range.left, out);
            Token.serializer().serialize(range.right, out);
        }

        public AbstractBounds deserialize(DataInput in) throws IOException
        {
            if (in.readInt() == Type.RANGE.ordinal())
                return new Range(Token.serializer().deserialize(in), Token.serializer().deserialize(in));
            return new Bounds(Token.serializer().deserialize(in), Token.serializer().deserialize(in));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2233.java