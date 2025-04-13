error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13503.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13503.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13503.java
text:
```scala
final P@@honeticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true);

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
package org.apache.commons.codec.language.bm;

import org.junit.Test;

/**
 * Tests performance for {@link PhoneticEngine}.
 * <p>
 * See <a href="https://issues.apache.org/jira/browse/CODEC-174">[CODEC-174] Improve performance of Beider Morse
 * encoder</a>.
 * </p>
 * <p>
 * Results for November 7, 2013, project SVN revision 1539678.
 * </p>
 * <p>
 * Environment:
 * </p>
 * <ul>
 * <li>java version "1.7.0_45"</li>
 * <li>Java(TM) SE Runtime Environment (build 1.7.0_45-b18)</li>
 * <li>Java HotSpot(TM) 64-Bit Server VM (build 24.45-b08, mixed mode)</li>
 * <li>OS name: "windows 7", version: "6.1", arch: "amd64", family: "windows")</li>
 * </ul>
 * <ol>
 * <li>Time for encoding 80,000 times the input 'Angelo': 33,039 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 32,297 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 32,857 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': <b>31,561 millis.</b></li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 32,665 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 32,215 millis.</li>
 * </ol>
 * <p>
 * On this file's revision 1539678, with patch <a
 * href="https://issues.apache.org/jira/secure/attachment/12611963/CODEC-174-change-rules-storage-to-Map.patch"
 * >CODEC-174-change-rules-storage-to-Map</a>:
 * </p>
 * <ol>
 * <li>Time for encoding 80,000 times the input 'Angelo': 18,196 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,858 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,644 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': <b>13,591 millis.</b></li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,861 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,696 millis.</li>
 * </ol>
 * <p>
 * Patch applied, committed revision 1539783.
 * </p>
 * <p>
 * On this file's revision 1539783, with patch <a
 * href="https://issues.apache.org/jira/secure/attachment/12611962/CODEC-174-delete-subsequence-cache.patch"
 * >CODEC-174-delete-subsequence-cache.patch</a>:
 * </p>
 * <ol>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,547 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': <b>13,501 millis.</b></li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,528 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 17,110 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,910 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 16,969 millis.</li>
 * </ol>
 * <p>
 * Patch not applied.
 * </p>
 * <p>
 * On this file's revision 1539787, with patch <a
 * href="https://issues.apache.org/jira/secure/attachment/12612178/CODEC-174-reuse-set-in-PhonemeBuilder.patch"
 * >CODEC-174-reuse-set-in-PhonemeBuilder.patch</a>:
 * </p>
 * <ol>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,724 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,451 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,742 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': <b>13,186 millis.</b></li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 13,600 millis.</li>
 * <li>Time for encoding 80,000 times the input 'Angelo': 16,405 millis.</li>
 * </ol>
 * <p>
 * Patch applied, committed revision 1539788.
 * </p>
 */
public class PhoneticEnginePerformanceTest {

    private static final int LOOP = 80000;

    @Test
    public void test() {
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true);
        final String input = "Angelo";
        final long startMillis = System.currentTimeMillis();
        for (int i = 0; i < LOOP; i++) {
            engine.encode(input);
        }
        final long totalMillis = System.currentTimeMillis() - startMillis;
        System.out.println(String.format("Time for encoding %,d times the input '%s': %,d millis.", LOOP, input,
                totalMillis));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13503.java