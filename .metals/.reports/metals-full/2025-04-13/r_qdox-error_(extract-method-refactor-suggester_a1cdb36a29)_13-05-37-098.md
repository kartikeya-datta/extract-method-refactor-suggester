error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7427.java
text:
```scala
L@@ist<Results> allResults = new ArrayList<>();

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
package org.elasticsearch.benchmark.scripts.score;

import org.elasticsearch.benchmark.scripts.score.plugin.NativeScriptExamplesPlugin;
import org.elasticsearch.benchmark.scripts.score.script.NativeConstantForLoopScoreScript;
import org.elasticsearch.benchmark.scripts.score.script.NativeConstantScoreScript;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 *
 */
public class ScriptsConstantScoreBenchmark extends BasicScriptBenchmark {

    public static void main(String[] args) throws Exception {

        int minTerms = 49;
        int maxTerms = 50;
        int maxIter = 1000;
        int warmerIter = 1000;

        init(maxTerms);
        List<Results> allResults = new ArrayList<BasicScriptBenchmark.Results>();
        Settings settings = settingsBuilder().put("plugin.types", NativeScriptExamplesPlugin.class.getName()).build();

        String clusterName = ScriptsConstantScoreBenchmark.class.getSimpleName();
        Node node1 = nodeBuilder().clusterName(clusterName).settings(settingsBuilder().put(settings).put("name", "node1")).node();
        Client client = node1.client();
        client.admin().cluster().prepareHealth("test").setWaitForGreenStatus().setTimeout("10s").execute().actionGet();

        indexData(10000, client, true);
        client.admin().cluster().prepareHealth("test").setWaitForGreenStatus().setTimeout("10s").execute().actionGet();

        Results results = new Results();

        results.init(maxTerms - minTerms, "native const script score (log(2) 10X)",
                "Results for native const script score with score = log(2) 10X:", "black", "-.");
        // init script searches
        List<Entry<String, RequestInfo>> searchRequests = initScriptMatchAllSearchRequests(
                NativeConstantForLoopScoreScript.NATIVE_CONSTANT_FOR_LOOP_SCRIPT_SCORE, true);
        // run actual benchmark
        runBenchmark(client, maxIter, results, searchRequests, minTerms, warmerIter);
        allResults.add(results);

        // init native script searches
        results = new Results();
        results.init(maxTerms - minTerms, "mvel const (log(2) 10X)", "Results for mvel const score = log(2) 10X:", "red", "-.");
        searchRequests = initScriptMatchAllSearchRequests("score = 0; for (int i=0; i<10;i++) {score = score + log(2);} return score",
                false);
        // run actual benchmark
        runBenchmark(client, maxIter, results, searchRequests, minTerms, warmerIter);
        allResults.add(results);

        results = new Results();
        results.init(maxTerms - minTerms, "native const script score (2)", "Results for native const script score with score = 2:",
                "black", ":");
        // init native script searches
        searchRequests = initScriptMatchAllSearchRequests(NativeConstantScoreScript.NATIVE_CONSTANT_SCRIPT_SCORE, true);
        // run actual benchmark
        runBenchmark(client, maxIter, results, searchRequests, minTerms, warmerIter);
        allResults.add(results);

        results = new Results();
        results.init(maxTerms - minTerms, "mvel const (2)", "Results for mvel const score = 2:", "red", "--");
        // init native script searches
        searchRequests = initScriptMatchAllSearchRequests("2", false);
        // run actual benchmark
        runBenchmark(client, maxIter, results, searchRequests, minTerms, warmerIter);
        allResults.add(results);

        printOctaveScript(allResults, args);

        client.close();
        node1.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7427.java