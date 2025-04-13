error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3390.java
text:
```scala
v@@oid newTranslog(long id);

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

package org.elasticsearch.index.translog;

import org.apache.lucene.index.Term;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.lease.Releasable;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.util.concurrent.NotThreadSafe;
import org.elasticsearch.common.util.concurrent.ThreadSafe;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.shard.IndexShardComponent;
import org.elasticsearch.index.shard.service.IndexShard;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author kimchy (Shay Banon)
 */
@ThreadSafe
public interface Translog extends IndexShardComponent {

    /**
     * Returns the id of the current transaction log.
     */
    long currentId();

    /**
     * Returns the number of operations in the transaction log.
     */
    int size();

    /**
     * The estimated memory size this translog is taking.
     */
    ByteSizeValue estimateMemorySize();

    /**
     * Creates a new transaction log internally. Note, users of this class should make
     * sure that no operations are performed on the trans log when this is called.
     */
    void newTranslog();

    /**
     * Adds a create operation to the transaction log.
     */
    void add(Operation operation) throws TranslogException;

    /**
     * Snapshots the current transaction log allowing to safely iterate over the snapshot.
     */
    Snapshot snapshot() throws TranslogException;

    /**
     * Snapshots the delta between the current state of the translog, and the state defined
     * by the provided snapshot. If a new translog has been created after the provided snapshot
     * has been take, will return a snapshot on the current trasnlog.
     */
    Snapshot snapshot(Snapshot snapshot);

    /**
     * Closes the transaction log.
     */
    void close();

    /**
     * A snapshot of the transaction log, allows to iterate over all the transaction log operations.
     */
    @NotThreadSafe
    static interface Snapshot extends Iterable<Operation>, Releasable, Streamable {

        /**
         * The id of the translog the snapshot was taken with.
         */
        long translogId();

        /**
         * The number of translog operations in the snapshot.
         */
        int size();

        Iterable<Operation> skipTo(int skipTo);
    }

    /**
     * A generic interface representing an operation performed on the transaction log.
     * Each is associated with a type.
     */
    static interface Operation extends Streamable {
        static enum Type {
            CREATE((byte) 1),
            SAVE((byte) 2),
            DELETE((byte) 3),
            DELETE_BY_QUERY((byte) 4);

            private final byte id;

            private Type(byte id) {
                this.id = id;
            }

            public byte id() {
                return this.id;
            }

            public static Type fromId(byte id) {
                switch (id) {
                    case 1:
                        return CREATE;
                    case 2:
                        return SAVE;
                    case 3:
                        return DELETE;
                    case 4:
                        return DELETE_BY_QUERY;
                    default:
                        throw new IllegalArgumentException("No type mapped for [" + id + "]");
                }
            }
        }

        Type opType();

        long estimateSize();

        void execute(IndexShard indexShard) throws ElasticSearchException;
    }

    static class Create implements Operation {
        private String id;
        private String type;
        private byte[] source;

        public Create() {
        }

        public Create(Engine.Create create) {
            this(create.type(), create.id(), create.source());
        }

        public Create(String type, String id, byte[] source) {
            this.id = id;
            this.type = type;
            this.source = source;
        }

        @Override public Type opType() {
            return Type.CREATE;
        }

        @Override public long estimateSize() {
            return ((id.length() + type.length()) * 2) + source.length + 12;
        }

        public String id() {
            return this.id;
        }

        public byte[] source() {
            return this.source;
        }

        public String type() {
            return this.type;
        }

        @Override public void execute(IndexShard indexShard) throws ElasticSearchException {
            indexShard.create(type, id, source);
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            in.readVInt(); // version
            id = in.readUTF();
            type = in.readUTF();
            source = new byte[in.readVInt()];
            in.readFully(source);
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeVInt(0); // version
            out.writeUTF(id);
            out.writeUTF(type);
            out.writeVInt(source.length);
            out.writeBytes(source);
        }
    }

    static class Index implements Operation {
        private String id;
        private String type;
        private byte[] source;

        public Index() {
        }

        public Index(Engine.Index index) {
            this(index.type(), index.id(), index.source());
        }

        public Index(String type, String id, byte[] source) {
            this.type = type;
            this.id = id;
            this.source = source;
        }

        @Override public Type opType() {
            return Type.SAVE;
        }

        @Override public long estimateSize() {
            return ((id.length() + type.length()) * 2) + source.length + 12;
        }

        public String type() {
            return this.type;
        }

        public String id() {
            return this.id;
        }

        public byte[] source() {
            return this.source;
        }

        @Override public void execute(IndexShard indexShard) throws ElasticSearchException {
            indexShard.index(type, id, source);
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            in.readVInt(); // version
            id = in.readUTF();
            type = in.readUTF();
            source = new byte[in.readVInt()];
            in.readFully(source);
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeVInt(0); // version
            out.writeUTF(id);
            out.writeUTF(type);
            out.writeVInt(source.length);
            out.writeBytes(source);
        }
    }

    static class Delete implements Operation {
        private Term uid;

        public Delete() {
        }

        public Delete(Engine.Delete delete) {
            this(delete.uid());
        }

        public Delete(Term uid) {
            this.uid = uid;
        }

        @Override public Type opType() {
            return Type.DELETE;
        }

        @Override public long estimateSize() {
            return ((uid.field().length() + uid.text().length()) * 2) + 20;
        }

        public Term uid() {
            return this.uid;
        }

        @Override public void execute(IndexShard indexShard) throws ElasticSearchException {
            indexShard.delete(uid);
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            in.readVInt(); // version
            uid = new Term(in.readUTF(), in.readUTF());
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeVInt(0); // version
            out.writeUTF(uid.field());
            out.writeUTF(uid.text());
        }
    }

    static class DeleteByQuery implements Operation {
        private byte[] source;
        @Nullable private String queryParserName;
        private String[] types = Strings.EMPTY_ARRAY;

        public DeleteByQuery() {
        }

        public DeleteByQuery(Engine.DeleteByQuery deleteByQuery) {
            this(deleteByQuery.source(), deleteByQuery.queryParserName(), deleteByQuery.types());
        }

        public DeleteByQuery(byte[] source, @Nullable String queryParserName, String... types) {
            this.queryParserName = queryParserName;
            this.source = source;
            this.types = types;
        }

        @Override public Type opType() {
            return Type.DELETE_BY_QUERY;
        }

        @Override public long estimateSize() {
            return source.length + ((queryParserName == null ? 0 : queryParserName.length()) * 2) + 8;
        }

        public String queryParserName() {
            return this.queryParserName;
        }

        public byte[] source() {
            return this.source;
        }

        public String[] types() {
            return this.types;
        }

        @Override public void execute(IndexShard indexShard) throws ElasticSearchException {
            indexShard.deleteByQuery(source, queryParserName, types);
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            in.readVInt(); // version
            source = new byte[in.readVInt()];
            in.readFully(source);
            if (in.readBoolean()) {
                queryParserName = in.readUTF();
            }
            int typesSize = in.readVInt();
            if (typesSize > 0) {
                types = new String[typesSize];
                for (int i = 0; i < typesSize; i++) {
                    types[i] = in.readUTF();
                }
            }
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeVInt(0); // version
            out.writeVInt(source.length);
            out.writeBytes(source);
            if (queryParserName == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                out.writeUTF(queryParserName);
            }
            out.writeVInt(types.length);
            for (String type : types) {
                out.writeUTF(type);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3390.java