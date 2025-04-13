error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3640.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3640.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3640.java
text:
```scala
t@@his.params = request.getParameters() == null ? new HashMap<String, String>() : request.getParameters();

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

package org.elasticsearch.thrift;

import org.elasticsearch.common.Unicode;
import org.elasticsearch.common.collect.ImmutableSet;
import org.elasticsearch.rest.support.AbstractRestRequest;
import org.elasticsearch.rest.support.RestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author kimchy (shay.banon)
 */
public class ThriftRestRequest extends AbstractRestRequest implements org.elasticsearch.rest.RestRequest {

    private final org.elasticsearch.thrift.RestRequest request;

    private final String path;

    private final Map<String, String> params;

    public ThriftRestRequest(org.elasticsearch.thrift.RestRequest request) {
        this.request = request;
        this.params = request.getParams() == null ? new HashMap<String, String>() : request.getParams();

        int pathEndPos = request.getUri().indexOf('?');
        if (pathEndPos < 0) {
            this.path = request.getUri();
        } else {
            this.path = request.getUri().substring(0, pathEndPos);
            RestUtils.decodeQueryString(request.getUri(), pathEndPos + 1, params);
        }
    }

    @Override public Method method() {
        switch (request.getMethod()) {
            case GET:
                return Method.GET;
            case POST:
                return Method.POST;
            case PUT:
                return Method.PUT;
            case DELETE:
                return Method.DELETE;
            case HEAD:
                return Method.HEAD;
            case OPTIONS:
                return Method.OPTIONS;
        }
        return null;
    }

    @Override public String uri() {
        return request.getUri();
    }

    @Override public String path() {
        return this.path;
    }

    @Override public boolean hasContent() {
        return request.getBody() != null && request.getBody().remaining() > 0;
    }

    @Override public boolean contentUnsafe() {
        return false;
    }

    @Override public byte[] contentByteArray() {
        return request.getBody().array();
    }

    @Override public int contentByteArrayOffset() {
        return request.getBody().arrayOffset();
    }

    @Override public int contentLength() {
        return request.getBody().remaining();
    }

    @Override public String contentAsString() {
        return Unicode.fromBytes(contentByteArray(), contentByteArrayOffset(), contentLength());
    }

    @Override public Set<String> headerNames() {
        if (request.getHeaders() == null) {
            return ImmutableSet.of();
        }
        return request.getHeaders().keySet();
    }

    @Override public String header(String name) {
        if (request.getHeaders() == null) {
            return null;
        }
        return request.getHeaders().get(name);
    }

    @Override public String cookie() {
        return null;
    }

    @Override public boolean hasParam(String key) {
        return params.containsKey(key);
    }

    @Override public String param(String key) {
        return params.get(key);
    }

    @Override public Map<String, String> params() {
        return params;
    }

    @Override public String param(String key, String defaultValue) {
        String value = params.get(key);
        if (value == null) {
            return value;
        }
        return defaultValue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3640.java