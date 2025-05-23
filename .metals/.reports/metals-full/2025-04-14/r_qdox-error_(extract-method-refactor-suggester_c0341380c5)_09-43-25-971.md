error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7905.java
text:
```scala
i@@f (indexSize != 0 && indexSize == reusedIndexSize) {

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.elasticsearch.action.admin.indices.status;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

/**
 * @author kimchy (shay.banon)
 */
public class GatewayRecoveryStatus {

    public enum Stage {
        INIT((byte) 0),
        RETRY((byte) 1),
        INDEX((byte) 2),
        TRANSLOG((byte) 3),
        FINALIZE((byte) 4),
        DONE((byte) 5);

        private final byte value;

        Stage(byte value) {
            this.value = value;
        }

        public byte value() {
            return value;
        }

        public static Stage fromValue(byte value) {
            if (value == 0) {
                return INIT;
            } else if (value == 1) {
                return RETRY;
            } else if (value == 2) {
                return INDEX;
            } else if (value == 3) {
                return TRANSLOG;
            } else if (value == 4) {
                return FINALIZE;
            } else if (value == 5) {
                return DONE;
            }
            throw new ElasticSearchIllegalArgumentException("No stage found for [" + value + ']');
        }
    }

    final Stage stage;

    final long startTime;

    final long time;

    final long throttlingTime;

    final long indexThrottlingTime;

    final long indexSize;

    final long reusedIndexSize;

    final long recoveredIndexSize;

    final long recoveredTranslogOperations;

    public GatewayRecoveryStatus(Stage stage, long startTime, long time, long throttlingTime, long indexThrottlingTime, long indexSize, long reusedIndexSize,
                                 long recoveredIndexSize, long recoveredTranslogOperations) {
        this.stage = stage;
        this.startTime = startTime;
        this.time = time;
        this.throttlingTime = throttlingTime;
        this.indexThrottlingTime = indexThrottlingTime;
        this.indexSize = indexSize;
        this.reusedIndexSize = reusedIndexSize;
        this.recoveredIndexSize = recoveredIndexSize;
        this.recoveredTranslogOperations = recoveredTranslogOperations;
    }

    public Stage stage() {
        return this.stage;
    }

    public long startTime() {
        return this.startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public TimeValue time() {
        return TimeValue.timeValueMillis(time);
    }

    public TimeValue getTime() {
        return time();
    }

    public TimeValue throttlingTime() {
        return TimeValue.timeValueMillis(throttlingTime);
    }

    public TimeValue getThrottlingTime() {
        return throttlingTime();
    }

    public TimeValue indexThrottlingTime() {
        return TimeValue.timeValueMillis(indexThrottlingTime);
    }

    public TimeValue getIndexThrottlingTime() {
        return indexThrottlingTime();
    }

    public ByteSizeValue indexSize() {
        return new ByteSizeValue(indexSize);
    }

    public ByteSizeValue getIndexSize() {
        return indexSize();
    }

    public ByteSizeValue reusedIndexSize() {
        return new ByteSizeValue(reusedIndexSize);
    }

    public ByteSizeValue getReusedIndexSize() {
        return reusedIndexSize();
    }

    public ByteSizeValue expectedRecoveredIndexSize() {
        return new ByteSizeValue(indexSize - reusedIndexSize);
    }

    public ByteSizeValue getExpectedRecoveredIndexSize() {
        return expectedRecoveredIndexSize();
    }

    /**
     * How much of the index has been recovered.
     */
    public ByteSizeValue recoveredIndexSize() {
        return new ByteSizeValue(recoveredIndexSize);
    }

    /**
     * How much of the index has been recovered.
     */
    public ByteSizeValue getRecoveredIndexSize() {
        return recoveredIndexSize();
    }

    public int indexRecoveryProgress() {
        if (recoveredIndexSize == 0) {
            if (indexSize == reusedIndexSize) {
                return 100;
            }
            return 0;
        }
        return (int) (((double) recoveredIndexSize) / expectedRecoveredIndexSize().bytes() * 100);
    }

    public int getIndexRecoveryProgress() {
        return indexRecoveryProgress();
    }

    public long recoveredTranslogOperations() {
        return recoveredTranslogOperations;
    }

    public long getRecoveredTranslogOperations() {
        return recoveredTranslogOperations();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7905.java