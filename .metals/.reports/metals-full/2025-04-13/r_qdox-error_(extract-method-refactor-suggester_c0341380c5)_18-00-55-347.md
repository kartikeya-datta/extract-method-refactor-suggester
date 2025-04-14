error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/877.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/877.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/877.java
text:
```scala
public static final C@@ommitPoint NULL = new CommitPoint(-1, "_null_", Type.GENERATED, ImmutableList.<CommitPoint.FileInfo>of(), ImmutableList.<CommitPoint.FileInfo>of());

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

package org.elasticsearch.index.gateway;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.index.store.StoreFileMetaData;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author kimchy (shay.banon)
 */
public class CommitPoint {

    public static CommitPoint NULL = new CommitPoint(-1, "_null_", Type.GENERATED, ImmutableList.<CommitPoint.FileInfo>of(), ImmutableList.<CommitPoint.FileInfo>of());

    public static class FileInfo {
        private final String name;
        private final String physicalName;
        private final long length;
        private final String checksum;

        public FileInfo(String name, String physicalName, long length, String checksum) {
            this.name = name;
            this.physicalName = physicalName;
            this.length = length;
            this.checksum = checksum;
        }

        public String name() {
            return name;
        }

        public String physicalName() {
            return this.physicalName;
        }

        public long length() {
            return length;
        }

        @Nullable public String checksum() {
            return checksum;
        }

        public boolean isSame(StoreFileMetaData md) {
            if (checksum != null && md.checksum() != null) {
                return checksum.equals(md.checksum());
            }
            return length == md.length();
        }
    }

    public static enum Type {
        GENERATED,
        SAVED
    }

    private final long version;

    private final String name;

    private final Type type;

    private final ImmutableList<FileInfo> indexFiles;

    private final ImmutableList<FileInfo> translogFiles;

    public CommitPoint(long version, String name, Type type, List<FileInfo> indexFiles, List<FileInfo> translogFiles) {
        this.version = version;
        this.name = name;
        this.type = type;
        this.indexFiles = ImmutableList.copyOf(indexFiles);
        this.translogFiles = ImmutableList.copyOf(translogFiles);
    }

    public long version() {
        return version;
    }

    public String name() {
        return this.name;
    }

    public Type type() {
        return this.type;
    }

    public ImmutableList<FileInfo> indexFiles() {
        return this.indexFiles;
    }

    public ImmutableList<FileInfo> translogFiles() {
        return this.translogFiles;
    }

    public boolean containPhysicalIndexFile(String physicalName) {
        return findPhysicalIndexFile(physicalName) != null;
    }

    public CommitPoint.FileInfo findPhysicalIndexFile(String physicalName) {
        for (FileInfo file : indexFiles) {
            if (file.physicalName().equals(physicalName)) {
                return file;
            }
        }
        return null;
    }

    public CommitPoint.FileInfo findNameFile(String name) {
        CommitPoint.FileInfo fileInfo = findNameIndexFile(name);
        if (fileInfo != null) {
            return fileInfo;
        }
        return findNameTranslogFile(name);
    }

    public CommitPoint.FileInfo findNameIndexFile(String name) {
        for (FileInfo file : indexFiles) {
            if (file.name().equals(name)) {
                return file;
            }
        }
        return null;
    }

    public CommitPoint.FileInfo findNameTranslogFile(String name) {
        for (FileInfo file : translogFiles) {
            if (file.name().equals(name)) {
                return file;
            }
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/877.java