error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2786.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2786.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2786.java
text:
```scala
public v@@oid errors(String... errors) {

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

package org.elasticsearch.action.bench;

import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.*;

/**
 * Benchmark response.
 *
 * A benchmark response will contain a mapping of names to results for each competition.
 */
public class BenchmarkResponse extends ActionResponse implements Streamable, ToXContent {

    private String benchmarkName;
    private State state = State.RUNNING;
    private boolean verbose;
    private String[] errors = Strings.EMPTY_ARRAY;

    Map<String, CompetitionResult> competitionResults;

    public BenchmarkResponse() {
        competitionResults = new HashMap<>();
    }

    public BenchmarkResponse(String benchmarkName, Map<String, CompetitionResult> competitionResults) {
        this.benchmarkName = benchmarkName;
        this.competitionResults = competitionResults;
    }

    /**
     * Benchmarks can be in one of:
     *  RUNNING     - executing normally
     *  COMPLETE    - completed normally
     *  ABORTED     - aborted
     *  FAILED      - execution failed
     */
    public static enum State {
        RUNNING((byte) 0),
        COMPLETE((byte) 1),
        ABORTED((byte) 2),
        FAILED((byte) 3);

        private final byte id;
        private static final State[] STATES = new State[State.values().length];

        static {
            for (State state : State.values()) {
                assert state.id() < STATES.length && state.id() >= 0;
                STATES[state.id] = state;
            }
        }

        State(byte id) {
            this.id = id;
        }

        public byte id() {
            return id;
        }

        public static State fromId(byte id) throws ElasticsearchIllegalArgumentException {
            if (id < 0 || id >= STATES.length) {
                throw new ElasticsearchIllegalArgumentException("No mapping for id [" + id + "]");
            }
            return STATES[id];
        }
    }

    /**
     * Name of the benchmark
     * @return  Name of the benchmark
     */
    public String benchmarkName() {
        return benchmarkName;
    }

    /**
     * Sets the benchmark name
     * @param benchmarkName Benchmark name
     */
    public void benchmarkName(String benchmarkName) {
        this.benchmarkName = benchmarkName;
    }

    /**
     * Benchmark state
     * @return  Benchmark state
     */
    public State state() {
        return state;
    }

    /**
     * Sets the state of the benchmark
     * @param state State
     */
    public void state(State state) {
        this.state = state;
    }

    /**
     * Possibly replace the existing state with the new state depending on the severity
     * of the new state. More severe states, such as FAILED, will over-write less severe
     * ones, such as COMPLETED.
     * @param newState  New candidate state
     * @return          The merged state
     */
    public State mergeState(State newState) {
        if (state.compareTo(newState) < 0) {
            state = newState;
        }
        return state;
    }

    /**
     * Map of competition names to competition results
     * @return  Map of competition names to competition results
     */
    public Map<String, CompetitionResult> competitionResults() {
        return competitionResults;
    }

    /**
     * Whether to report verbose statistics
     */
    public boolean verbose() {
        return verbose;
    }

    /**
     * Sets whether to report verbose statistics
     */
    public void verbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Whether the benchmark encountered error conditions
     * @return  Whether the benchmark encountered error conditions
     */
    public boolean hasErrors() {
        return (errors != null && errors.length > 0);
    }

    /**
     * Error messages
     * @return  Error messages
     */
    public String[] errors() {
        return this.errors;
    }

    /**
     * Sets error messages
     * @param errors    Error messages
     */
    public void errors(String[] errors) {
        this.errors = (errors == null) ? Strings.EMPTY_ARRAY : errors;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.field(Fields.STATUS, state.toString());
        if (errors != null) {
            builder.array(Fields.ERRORS, errors);
        }
        builder.startObject(Fields.COMPETITORS);
        if (competitionResults != null) {
            for (Map.Entry<String, CompetitionResult> entry : competitionResults.entrySet()) {
                entry.getValue().verbose(verbose);
                entry.getValue().toXContent(builder, params);
            }
        }
        builder.endObject();
        return builder;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        benchmarkName = in.readString();
        state = State.fromId(in.readByte());
        errors = in.readStringArray();
        int size = in.readVInt();
        for (int i = 0; i < size; i++) {
            String s = in.readString();
            CompetitionResult cr = new CompetitionResult();
            cr.readFrom(in);
            competitionResults.put(s, cr);
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(benchmarkName);
        out.writeByte(state.id());
        out.writeStringArray(errors);
        out.write(competitionResults.size());
        for (Map.Entry<String, CompetitionResult> entry : competitionResults.entrySet()) {
            out.writeString(entry.getKey());
            entry.getValue().writeTo(out);
        }
    }

    @Override
    public String toString() {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().prettyPrint();
            builder.startObject();
            toXContent(builder, EMPTY_PARAMS);
            builder.endObject();
            return builder.string();
        } catch (IOException e) {
            return "{ \"error\" : \"" + e.getMessage() + "\"}";
        }
    }

    static final class Fields {
        static final XContentBuilderString STATUS = new XContentBuilderString("status");
        static final XContentBuilderString ERRORS = new XContentBuilderString("errors");
        static final XContentBuilderString COMPETITORS = new XContentBuilderString("competitors");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2786.java