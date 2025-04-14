error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3730.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3730.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3730.java
text:
```scala
t@@hrow new SearchSourceBuilderException("invalid CIDR mask [" + mask + "] in ip_range aggregation [" + name + "]");

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.elasticsearch.search.aggregations.bucket.range.ipv4;

import org.elasticsearch.search.aggregations.bucket.range.AbstractRangeBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilderException;

import java.util.regex.Pattern;

/**
 *
 */
public class IPv4RangeBuilder extends AbstractRangeBuilder<IPv4RangeBuilder> {

    private static final Pattern MASK_PATTERN = Pattern.compile("[\\.|/]");

    public IPv4RangeBuilder(String name) {
        super(name, InternalIPv4Range.TYPE.name());
    }

    public IPv4RangeBuilder addRange(String key, String from, String to) {
        ranges.add(new Range(key, from, to));
        return this;
    }

    public IPv4RangeBuilder addMaskRange(String mask) {
        return addMaskRange(mask, mask);
    }

    public IPv4RangeBuilder addMaskRange(String key, String mask) {
        long[] fromTo = cidrMaskToMinMax(mask);
        if (fromTo == null) {
            throw new SearchSourceBuilderException("invalid CIDR mask [" + mask + "] in ip_range aggregation [" + getName() + "]");
        }
        ranges.add(new Range(key, fromTo[0] < 0 ? null : fromTo[0], fromTo[1] < 0 ? null : fromTo[1]));
        return this;
    }

    public IPv4RangeBuilder addRange(String from, String to) {
        return addRange(null, from, to);
    }

    public IPv4RangeBuilder addUnboundedTo(String key, String to) {
        ranges.add(new Range(key, null, to));
        return this;
    }

    public IPv4RangeBuilder addUnboundedTo(String to) {
        return addUnboundedTo(null, to);
    }

    public IPv4RangeBuilder addUnboundedFrom(String key, String from) {
        ranges.add(new Range(key, from, null));
        return this;
    }

    public IPv4RangeBuilder addUnboundedFrom(String from) {
        return addUnboundedFrom(null, from);
    }

    /**
     * Computes the min & max ip addresses (represented as long values - same way as stored in index) represented by the given CIDR mask
     * expression. The returned array has the length of 2, where the first entry represents the {@code min} address and the second the {@code max}.
     * A {@code -1} value for either the {@code min} or the {@code max}, represents an unbounded end. In other words:
     *
     * <p>
     * {@code min == -1 == "0.0.0.0" }
     * </p>
     *
     * and
     *
     * <p>
     * {@code max == -1 == "255.255.255.255" }
     * </p>
     *
     * @param cidr
     * @return
     */
    static long[] cidrMaskToMinMax(String cidr) {
        String[] parts = MASK_PATTERN.split(cidr);
        if (parts.length != 5) {
            return null;
        }
        int addr = (( Integer.parseInt(parts[0]) << 24 ) & 0xFF000000)
 (( Integer.parseInt(parts[1]) << 16 ) & 0xFF0000)
 (( Integer.parseInt(parts[2]) << 8 ) & 0xFF00)
  ( Integer.parseInt(parts[3]) & 0xFF);

        int mask = (-1) << (32 - Integer.parseInt(parts[4]));

        int from = addr & mask;
        long longFrom = intIpToLongIp(from);
        if (longFrom == 0) {
            longFrom = -1;
        }

        int to = from + (~mask);
        long longTo = intIpToLongIp(to) + 1; // we have to +1 here as the range is non-inclusive on the "to" side
        if (longTo == InternalIPv4Range.MAX_IP) {
            longTo = -1;
        }

        return new long[] { longFrom, longTo };
    }

    public static long intIpToLongIp(int i) {
        long p1 = ((long) ((i >> 24 ) & 0xFF)) << 24;
        int p2 = ((i >> 16 ) & 0xFF) << 16;
        int p3 = ((i >>  8 ) & 0xFF) << 8;
        int p4 = i & 0xFF;
        return p1 + p2 + p3 + p4;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3730.java