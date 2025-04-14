error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4308.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4308.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4308.java
text:
```scala
r@@eturn builder.bytes().toBytes();

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

package org.elasticsearch.index.gateway;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class CommitPoints implements Iterable<CommitPoint> {

    private final ImmutableList<CommitPoint> commitPoints;

    public CommitPoints(List<CommitPoint> commitPoints) {
        Collections.sort(commitPoints, new Comparator<CommitPoint>() {
            @Override
            public int compare(CommitPoint o1, CommitPoint o2) {
                return (o2.version() < o1.version() ? -1 : (o2.version() == o1.version() ? 0 : 1));
            }
        });
        this.commitPoints = ImmutableList.copyOf(commitPoints);
    }

    public ImmutableList<CommitPoint> commits() {
        return this.commitPoints;
    }

    public boolean hasVersion(long version) {
        for (CommitPoint commitPoint : commitPoints) {
            if (commitPoint.version() == version) {
                return true;
            }
        }
        return false;
    }

    public CommitPoint.FileInfo findPhysicalIndexFile(String physicalName) {
        for (CommitPoint commitPoint : commitPoints) {
            CommitPoint.FileInfo fileInfo = commitPoint.findPhysicalIndexFile(physicalName);
            if (fileInfo != null) {
                return fileInfo;
            }
        }
        return null;
    }

    public CommitPoint.FileInfo findNameFile(String name) {
        for (CommitPoint commitPoint : commitPoints) {
            CommitPoint.FileInfo fileInfo = commitPoint.findNameFile(name);
            if (fileInfo != null) {
                return fileInfo;
            }
        }
        return null;
    }

    @Override
    public Iterator<CommitPoint> iterator() {
        return commitPoints.iterator();
    }

    public static byte[] toXContent(CommitPoint commitPoint) throws Exception {
        XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON).prettyPrint();
        builder.startObject();
        builder.field("version", commitPoint.version());
        builder.field("name", commitPoint.name());
        builder.field("type", commitPoint.type().toString());

        builder.startObject("index_files");
        for (CommitPoint.FileInfo fileInfo : commitPoint.indexFiles()) {
            builder.startObject(fileInfo.name());
            builder.field("physical_name", fileInfo.physicalName());
            builder.field("length", fileInfo.length());
            if (fileInfo.checksum() != null) {
                builder.field("checksum", fileInfo.checksum());
            }
            builder.endObject();
        }
        builder.endObject();

        builder.startObject("translog_files");
        for (CommitPoint.FileInfo fileInfo : commitPoint.translogFiles()) {
            builder.startObject(fileInfo.name());
            builder.field("physical_name", fileInfo.physicalName());
            builder.field("length", fileInfo.length());
            builder.endObject();
        }
        builder.endObject();

        builder.endObject();
        return builder.copiedBytes();
    }

    public static CommitPoint fromXContent(byte[] data) throws Exception {
        XContentParser parser = XContentFactory.xContent(XContentType.JSON).createParser(data);
        try {
            String currentFieldName = null;
            XContentParser.Token token = parser.nextToken();
            if (token == null) {
                // no data...
                throw new IOException("No commit point data");
            }
            long version = -1;
            String name = null;
            CommitPoint.Type type = null;
            List<CommitPoint.FileInfo> indexFiles = Lists.newArrayList();
            List<CommitPoint.FileInfo> translogFiles = Lists.newArrayList();
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token == XContentParser.Token.START_OBJECT) {
                    List<CommitPoint.FileInfo> files = null;
                    if ("index_files".equals(currentFieldName) || "indexFiles".equals(currentFieldName)) {
                        files = indexFiles;
                    } else if ("translog_files".equals(currentFieldName) || "translogFiles".equals(currentFieldName)) {
                        files = translogFiles;
                    } else {
                        throw new IOException("Can't handle object with name [" + currentFieldName + "]");
                    }
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            currentFieldName = parser.currentName();
                        } else if (token == XContentParser.Token.START_OBJECT) {
                            String fileName = currentFieldName;
                            String physicalName = null;
                            long size = -1;
                            String checksum = null;
                            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                                if (token == XContentParser.Token.FIELD_NAME) {
                                    currentFieldName = parser.currentName();
                                } else if (token.isValue()) {
                                    if ("physical_name".equals(currentFieldName) || "physicalName".equals(currentFieldName)) {
                                        physicalName = parser.text();
                                    } else if ("length".equals(currentFieldName)) {
                                        size = parser.longValue();
                                    } else if ("checksum".equals(currentFieldName)) {
                                        checksum = parser.text();
                                    }
                                }
                            }
                            if (physicalName == null) {
                                throw new IOException("Malformed commit, missing physical_name for [" + fileName + "]");
                            }
                            if (size == -1) {
                                throw new IOException("Malformed commit, missing length for [" + fileName + "]");
                            }
                            files.add(new CommitPoint.FileInfo(fileName, physicalName, size, checksum));
                        }
                    }
                } else if (token.isValue()) {
                    if ("version".equals(currentFieldName)) {
                        version = parser.longValue();
                    } else if ("name".equals(currentFieldName)) {
                        name = parser.text();
                    } else if ("type".equals(currentFieldName)) {
                        type = CommitPoint.Type.valueOf(parser.text());
                    }
                }
            }

            if (version == -1) {
                throw new IOException("Malformed commit, missing version");
            }
            if (name == null) {
                throw new IOException("Malformed commit, missing name");
            }
            if (type == null) {
                throw new IOException("Malformed commit, missing type");
            }

            return new CommitPoint(version, name, type, indexFiles, translogFiles);
        } finally {
            parser.close();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4308.java