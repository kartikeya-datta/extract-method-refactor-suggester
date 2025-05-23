error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1921.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1921.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1921.java
text:
```scala
t@@his.objectMapper = defaultObjectMapper();

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

package org.elasticsearch.benchmark.micro.deps.jackson;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.util.Preconditions;
import org.elasticsearch.util.StopWatch;
import org.elasticsearch.util.io.FastStringReader;
import org.elasticsearch.util.io.Streams;
import org.elasticsearch.util.io.StringBuilderWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static org.elasticsearch.util.json.Jackson.*;

/**
 * A simple Jackson type benchmark to check how well it converts to different types it supports
 * such as Map and JsonNode.
 *
 * @author kimchy (Shay Banon)
 */
@SuppressWarnings({"unchecked"})
public class JacksonTypesBenchmark {

    private final String jsonString;

    private final int factor;

    private final int cycles;

    private final ObjectMapper objectMapper;

    private final JsonType[] types;

    public JacksonTypesBenchmark(String jsonString) throws IOException {
        Preconditions.checkNotNull(jsonString, "jsonString must have a value");
        this.jsonString = jsonString;
        this.objectMapper = newObjectMapper();
        this.factor = 10;
        this.cycles = 10000;

        // warm things up
        JsonType[] types = buildTypes();
        for (JsonType type : types) {
            type.runRead(1000);
            type.runWrite(1000);
        }

        this.types = buildTypes();
    }

    /**
     * Runs the test. Will run <tt>factor * cycles</tt> iterations interleaving the
     * different type operations by <tt>factor</tt>.
     */
    public void run() throws IOException {
        // interleave the type tests so GC won't be taken into account
        for (int i = 0; i < factor; i++) {
            for (JsonType type : types) {
                type.runRead(cycles);
                type.runWrite(cycles);
            }
        }

        System.out.println("Run [" + (cycles * factor) + "] iterations");
        System.out.println("==============================");
        for (JsonType type : types) {
            System.out.println("------------------------------");
            System.out.println("Type [" + type.type.getSimpleName() + "]");
            System.out.println(type.readStopWatch.shortSummary());
            System.out.println(type.writeStopWatch.shortSummary());
            System.out.println("------------------------------");
        }
    }

    /**
     * Builds the types that we are going to test.
     */
    private JsonType[] buildTypes() throws IOException {
        JsonType[] types = new JsonType[2];
        types[0] = new JsonType(jsonString, objectMapper, Map.class);
        types[1] = new JsonType(jsonString, objectMapper, JsonNode.class);
        return types;
    }

    /**
     * Represents a test for a specific type, allowing to runRead and runWrite
     * on it and finally getting the results from the write/read stop watches.
     */
    private static class JsonType {
        final StopWatch readStopWatch = new StopWatch("read").keepTaskList(false);
        final StopWatch writeStopWatch = new StopWatch("write").keepTaskList(false);
        final String jsonString;
        final ObjectMapper objectMapper;
        final Class type;
        final Object master;

        protected JsonType(String jsonString, ObjectMapper objectMapper, Class type) throws IOException {
            this.jsonString = jsonString;
            this.objectMapper = objectMapper;
            this.type = type;
            this.master = objectMapper.readValue(new FastStringReader(jsonString), type);
        }

        void runRead(int cycles) throws IOException {
            readStopWatch.start();
            for (int i = 0; i < cycles; i++) {
                objectMapper.readValue(new FastStringReader(jsonString), type);
            }
            readStopWatch.stop();
        }

        void runWrite(int cycles) throws IOException {
            writeStopWatch.start();
            for (int i = 0; i < cycles; i++) {
                StringBuilderWriter builderWriter = StringBuilderWriter.Cached.cached();
                objectMapper.writeValue(builderWriter, master);
                builderWriter.toString();
            }
            writeStopWatch.stop();
        }
    }

    public static void main(String[] args) throws Exception {
        JacksonTypesBenchmark benchmark = new JacksonTypesBenchmark(
                Streams.copyToString(new InputStreamReader(JacksonTypesBenchmark.class.getResourceAsStream("/org/elasticsearch/benchmark/micro/deps/jackson/test1.json"))));
        benchmark.run();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1921.java