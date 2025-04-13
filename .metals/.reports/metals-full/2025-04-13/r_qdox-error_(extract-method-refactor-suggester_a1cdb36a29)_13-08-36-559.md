error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4078.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4078.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4078.java
text:
```scala
r@@eturn builder.contentType().restContentType();

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

package org.elasticsearch.rest;

import org.apache.lucene.util.UnicodeUtil;
import org.elasticsearch.common.thread.ThreadLocals;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

/**
 * @author kimchy (shay.banon)
 */
public class XContentRestResponse extends AbstractRestResponse {

    private static final byte[] END_JSONP;

    static {
        UnicodeUtil.UTF8Result U_END_JSONP = new UnicodeUtil.UTF8Result();
        UnicodeUtil.UTF16toUTF8(");", 0, ");".length(), U_END_JSONP);
        END_JSONP = new byte[U_END_JSONP.length];
        System.arraycopy(U_END_JSONP.result, 0, END_JSONP, 0, U_END_JSONP.length);
    }

    private static ThreadLocal<ThreadLocals.CleanableValue<UnicodeUtil.UTF8Result>> prefixCache = new ThreadLocal<ThreadLocals.CleanableValue<UnicodeUtil.UTF8Result>>() {
        @Override protected ThreadLocals.CleanableValue<UnicodeUtil.UTF8Result> initialValue() {
            return new ThreadLocals.CleanableValue<UnicodeUtil.UTF8Result>(new UnicodeUtil.UTF8Result());
        }
    };

    private final UnicodeUtil.UTF8Result prefixUtf8Result;

    private final Status status;

    private final XContentBuilder builder;

    public XContentRestResponse(RestRequest request, Status status) {
        this.builder = null;
        this.status = status;
        this.prefixUtf8Result = startJsonp(request);
    }

    public XContentRestResponse(RestRequest request, Status status, XContentBuilder builder) throws IOException {
        this.builder = builder;
        this.status = status;
        this.prefixUtf8Result = startJsonp(request);
    }

    @Override public String contentType() {
        return "application/json; charset=UTF-8";
    }

    @Override public boolean contentThreadSafe() {
        return false;
    }

    @Override public byte[] content() throws IOException {
        return builder.unsafeBytes();
    }

    @Override public int contentLength() throws IOException {
        return builder.unsafeBytesLength();
    }

    @Override public Status status() {
        return this.status;
    }

    @Override public byte[] prefixContent() {
        if (prefixUtf8Result != null) {
            return prefixUtf8Result.result;
        }
        return null;
    }

    @Override public int prefixContentLength() {
        if (prefixUtf8Result != null) {
            return prefixUtf8Result.length;
        }
        return -1;
    }

    @Override public byte[] suffixContent() {
        if (prefixUtf8Result != null) {
            return END_JSONP;
        }
        return null;
    }

    @Override public int suffixContentLength() {
        if (prefixUtf8Result != null) {
            return END_JSONP.length;
        }
        return -1;
    }

    private static UnicodeUtil.UTF8Result startJsonp(RestRequest request) {
        String callback = request.param("callback");
        if (callback == null) {
            return null;
        }
        UnicodeUtil.UTF8Result result = prefixCache.get().get();
        UnicodeUtil.UTF16toUTF8(callback, 0, callback.length(), result);
        result.result[result.length] = '(';
        result.length++;
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4078.java