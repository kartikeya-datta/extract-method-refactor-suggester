error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8606.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8606.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8606.java
text:
```scala
r@@eturn Status.CONT;

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

import org.apache.thrift.TException;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author kimchy (shay.banon)
 */
public class ThriftRestImpl extends AbstractComponent implements Rest.Iface {

    private final RestController restController;

    @Inject public ThriftRestImpl(Settings settings, RestController restController) {
        super(settings);
        this.restController = restController;
    }

    @Override public org.elasticsearch.thrift.RestResponse execute(RestRequest request) throws TException {
        if (logger.isTraceEnabled()) {
            logger.trace("thrift message {}", request);
        }
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<RestResponse> ref = new AtomicReference<RestResponse>();
        restController.dispatchRequest(new ThriftRestRequest(request), new RestChannel() {
            @Override public void sendResponse(RestResponse response) {
                ref.set(response);
                latch.countDown();
            }
        });
        try {
            latch.await();
            return convert(ref.get());
        } catch (Exception e) {
            throw new TException("failed to generate response", e);
        }
    }

    private org.elasticsearch.thrift.RestResponse convert(RestResponse response) throws IOException {
        org.elasticsearch.thrift.RestResponse tResponse = new org.elasticsearch.thrift.RestResponse(getStatus(response.status()));
        if (response.contentLength() > 0) {
            tResponse.setBody(ByteBuffer.wrap(response.content(), 0, response.contentLength()));
        }
        return tResponse;
    }

    private Status getStatus(RestResponse.Status status) {
        switch (status) {
            case CONTINUE:
                return Status.CONTINUE;
            case SWITCHING_PROTOCOLS:
                return Status.SWITCHING_PROTOCOLS;
            case OK:
                return Status.OK;
            case CREATED:
                return Status.CREATED;
            case ACCEPTED:
                return Status.ACCEPTED;
            case NON_AUTHORITATIVE_INFORMATION:
                return Status.NON_AUTHORITATIVE_INFORMATION;
            case NO_CONTENT:
                return Status.NO_CONTENT;
            case RESET_CONTENT:
                return Status.RESET_CONTENT;
            case PARTIAL_CONTENT:
                return Status.PARTIAL_CONTENT;
            case MULTI_STATUS:
                // no status for this??
                return Status.INTERNAL_SERVER_ERROR;
            case MULTIPLE_CHOICES:
                return Status.MULTIPLE_CHOICES;
            case MOVED_PERMANENTLY:
                return Status.MOVED_PERMANENTLY;
            case FOUND:
                return Status.FOUND;
            case SEE_OTHER:
                return Status.SEE_OTHER;
            case NOT_MODIFIED:
                return Status.NOT_MODIFIED;
            case USE_PROXY:
                return Status.USE_PROXY;
            case TEMPORARY_REDIRECT:
                return Status.TEMPORARY_REDIRECT;
            case BAD_REQUEST:
                return Status.BAD_REQUEST;
            case UNAUTHORIZED:
                return Status.UNAUTHORIZED;
            case PAYMENT_REQUIRED:
                return Status.PAYMENT_REQUIRED;
            case FORBIDDEN:
                return Status.FORBIDDEN;
            case NOT_FOUND:
                return Status.NOT_FOUND;
            case METHOD_NOT_ALLOWED:
                return Status.METHOD_NOT_ALLOWED;
            case NOT_ACCEPTABLE:
                return Status.NOT_ACCEPTABLE;
            case PROXY_AUTHENTICATION:
                return Status.INTERNAL_SERVER_ERROR;
            case REQUEST_TIMEOUT:
                return Status.REQUEST_TIMEOUT;
            case CONFLICT:
                return Status.CONFLICT;
            case GONE:
                return Status.GONE;
            case LENGTH_REQUIRED:
                return Status.LENGTH_REQUIRED;
            case PRECONDITION_FAILED:
                return Status.PRECONDITION_FAILED;
            case REQUEST_ENTITY_TOO_LARGE:
                return Status.REQUEST_ENTITY_TOO_LARGE;
            case REQUEST_URI_TOO_LONG:
                return Status.REQUEST_URI_TOO_LONG;
            case UNSUPPORTED_MEDIA_TYPE:
                return Status.UNSUPPORTED_MEDIA_TYPE;
            case REQUESTED_RANGE_NOT_SATISFIED:
                return Status.INTERNAL_SERVER_ERROR;
            case EXPECTATION_FAILED:
                return Status.EXPECTATION_FAILED;
            case UNPROCESSABLE_ENTITY:
                return Status.BAD_REQUEST;
            case LOCKED:
                return Status.BAD_REQUEST;
            case FAILED_DEPENDENCY:
                return Status.BAD_REQUEST;
            case INTERNAL_SERVER_ERROR:
                return Status.INTERNAL_SERVER_ERROR;
            case NOT_IMPLEMENTED:
                return Status.NOT_IMPLEMENTED;
            case BAD_GATEWAY:
                return Status.BAD_GATEWAY;
            case SERVICE_UNAVAILABLE:
                return Status.SERVICE_UNAVAILABLE;
            case GATEWAY_TIMEOUT:
                return Status.GATEWAY_TIMEOUT;
            case HTTP_VERSION_NOT_SUPPORTED:
                return Status.INTERNAL_SERVER_ERROR;
            default:
                return Status.INTERNAL_SERVER_ERROR;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8606.java