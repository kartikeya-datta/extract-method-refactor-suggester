error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6467.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6467.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6467.java
text:
```scala
public static final V@@ersion V_0_19_0_RC2 = new Version(V_0_19_0_RC2_ID, false);

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

package org.elasticsearch;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.monitor.jvm.JvmInfo;

import java.io.IOException;
import java.io.Serializable;

/**
 */
public class Version implements Serializable {

    // The logic for ID is: XXYYZZAA, where XX is major version, YY is minor version, ZZ is revision, and AA is Beta/RC indicator
    // AA values below 50 are beta builds, and below 99 are RC builds, with 99 indicating a release
    // the (internal) format of the id is there so we can easily do after/before checks on the id

    public static final int V_0_18_0_ID = /*00*/180099;
    public static final Version V_0_18_0 = new Version(V_0_18_0_ID, false);
    public static final int V_0_18_1_ID = /*00*/180199;
    public static final Version V_0_18_1 = new Version(V_0_18_1_ID, false);
    public static final int V_0_18_2_ID = /*00*/180299;
    public static final Version V_0_18_2 = new Version(V_0_18_2_ID, false);
    public static final int V_0_18_3_ID = /*00*/180399;
    public static final Version V_0_18_3 = new Version(V_0_18_3_ID, false);
    public static final int V_0_18_4_ID = /*00*/180499;
    public static final Version V_0_18_4 = new Version(V_0_18_4_ID, false);
    public static final int V_0_18_5_ID = /*00*/180599;
    public static final Version V_0_18_5 = new Version(V_0_18_5_ID, false);
    public static final int V_0_18_6_ID = /*00*/180699;
    public static final Version V_0_18_6 = new Version(V_0_18_6_ID, false);
    public static final int V_0_18_7_ID = /*00*/180799;
    public static final Version V_0_18_7 = new Version(V_0_18_7_ID, false);
    public static final int V_0_18_8_ID = /*00*/180899;
    public static final Version V_0_18_8 = new Version(V_0_18_8_ID, false);

    public static final int V_0_19_0_RC1_ID = /*00*/190051;
    public static final Version V_0_19_0_RC1 = new Version(V_0_19_0_RC1_ID, false);

    public static final int V_0_19_0_RC2_ID = /*00*/190052;
    public static final Version V_0_19_0_RC2 = new Version(V_0_19_0_RC2_ID, true);

    public static final Version CURRENT = V_0_19_0_RC2;

    public static Version readVersion(StreamInput in) throws IOException {
        return fromId(in.readVInt());
    }

    public static Version fromId(int id) {
        switch (id) {
            case V_0_18_0_ID:
                return V_0_18_0;
            case V_0_18_1_ID:
                return V_0_18_1;
            case V_0_18_2_ID:
                return V_0_18_2;
            case V_0_18_3_ID:
                return V_0_18_3;
            case V_0_18_4_ID:
                return V_0_18_4;
            case V_0_18_5_ID:
                return V_0_18_5;
            case V_0_18_6_ID:
                return V_0_18_6;
            case V_0_18_7_ID:
                return V_0_18_7;
            case V_0_18_8_ID:
                return V_0_18_8;

            case V_0_19_0_RC1_ID:
                return V_0_19_0_RC1;
            case V_0_19_0_RC2_ID:
                return V_0_19_0_RC2;
            default:
                return new Version(id, null);
        }
    }

    public static void writeVersion(Version version, StreamOutput out) throws IOException {
        out.writeVInt(version.id);
    }

    public final int id;
    public final byte major;
    public final byte minor;
    public final byte revision;
    public final byte build;
    public final Boolean snapshot;

    Version(int id, @Nullable Boolean snapshot) {
        this.id = id;
        this.major = (byte) ((id / 1000000) % 100);
        this.minor = (byte) ((id / 10000) % 100);
        this.revision = (byte) ((id / 100) % 100);
        this.build = (byte) (id % 100);
        this.snapshot = snapshot;
    }

    public boolean snapshot() {
        return snapshot != null && snapshot;
    }

    public boolean after(Version version) {
        return version.id > id;
    }

    public boolean onOrAfter(Version version) {
        return version.id >= id;
    }

    /**
     * Just the version number (without -SNAPSHOT if snapshot).
     */
    public String number() {
        StringBuilder sb = new StringBuilder();
        sb.append(major).append('.').append(minor).append('.').append(revision);
        if (build < 50) {
            sb.append(".Beta").append(build);
        } else if (build < 99) {
            sb.append(".RC").append(build - 50);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("ElasticSearch Version: " + Version.CURRENT + ", JVM: " + JvmInfo.jvmInfo().vmVersion());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(number());
        if (snapshot()) {
            sb.append("-SNAPSHOT");
        }
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6467.java