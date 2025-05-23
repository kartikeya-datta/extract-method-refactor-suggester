error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1105.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1105.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1105.java
text:
```scala
o@@ut.writeOptionalString(writtenBy == null ? null : writtenBy.toString());

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

package org.elasticsearch.index.store;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.lucene.Lucene;

import java.io.IOException;

/**
 *
 */
public class StoreFileMetaData implements Streamable {

    private String name;

    // the actual file size on "disk", if compressed, the compressed size
    private long length;

    private String checksum;

    private Version writtenBy;

    private BytesRef hash;

    private StoreFileMetaData() {
    }

    public StoreFileMetaData(String name, long length) {
        this(name, length, null);
    }

    public StoreFileMetaData(String name, long length, String checksum) {
        this(name, length, checksum, null, null);
    }

    public StoreFileMetaData(String name, long length, String checksum, Version writtenBy) {
        this(name, length, checksum, writtenBy, null);
    }

    public StoreFileMetaData(String name, long length, String checksum, Version writtenBy, BytesRef hash) {
        this.name = name;
        this.length = length;
        this.checksum = checksum;
        this.writtenBy = writtenBy;
        this.hash = hash == null ? new BytesRef() : hash;
    }


    /**
     * Returns the name of this file
     */
    public String name() {
        return name;
    }

    /**
     * the actual file size on "disk", if compressed, the compressed size
     */
    public long length() {
        return length;
    }

    /**
     * Returns a string representation of the files checksum. Since Lucene 4.8 this is a CRC32 checksum written
     * by lucene. Previously we use Adler32 on top of Lucene as the checksum algorithm, if {@link #hasLegacyChecksum()} returns
     * <code>true</code> this is a Adler32 checksum.
     * @return
     */
    @Nullable
    public String checksum() {
        return this.checksum;
    }

    /**
     * Returns <code>true</code> iff the length and the checksums are the same. otherwise <code>false</code>
     */
    public boolean isSame(StoreFileMetaData other) {
        if (checksum == null || other.checksum == null) {
            return false;
        }
        return length == other.length && checksum.equals(other.checksum) && hash.equals(other.hash);
    }

    public static StoreFileMetaData readStoreFileMetaData(StreamInput in) throws IOException {
        StoreFileMetaData md = new StoreFileMetaData();
        md.readFrom(in);
        return md;
    }

    @Override
    public String toString() {
        return "name [" + name + "], length [" + length + "], checksum [" + checksum + "], writtenBy [" + writtenBy + "]" ;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        name = in.readString();
        length = in.readVLong();
        checksum = in.readOptionalString();
        if (in.getVersion().onOrAfter(org.elasticsearch.Version.V_1_3_0)) {
            String versionString = in.readOptionalString();
            writtenBy = Lucene.parseVersionLenient(versionString, null);
        }
        if (in.getVersion().onOrAfter(org.elasticsearch.Version.V_1_4_0)) {
            hash = in.readBytesRef();
        } else {
            hash = new BytesRef();
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeVLong(length);
        out.writeOptionalString(checksum);
        if (out.getVersion().onOrAfter(org.elasticsearch.Version.V_1_3_0)) {
            out.writeOptionalString(writtenBy == null ? null : writtenBy.name());
        }
        if (out.getVersion().onOrAfter(org.elasticsearch.Version.V_1_4_0)) {
            out.writeBytesRef(hash);
        }
    }

    /**
     * Returns the Lucene version this file has been written by or <code>null</code> if unknown
     */
    public Version writtenBy() {
        return writtenBy;
    }

    /**
     * Returns <code>true</code>  iff the checksum is not <code>null</code> and if the file has NOT been written by
     * a Lucene version greater or equal to Lucene 4.8
     */
    public boolean hasLegacyChecksum() {
        return checksum != null && ((writtenBy != null  && writtenBy.onOrAfter(Version.LUCENE_4_8)) == false);
    }

    /**
     * Returns a variable length hash of the file represented by this metadata object. This can be the file
     * itself if the file is small enough. If the length of the hash is <tt>0</tt> no hash value is available
     */
    public BytesRef hash() {
        return hash;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1105.java