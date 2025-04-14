error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3952.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3952.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3952.java
text:
```scala
private final M@@ap<String, PlaceHolder> table = new Hashtable<String, PlaceHolder>();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.protocol.jms.sampler;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.Message;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Administration of messages.
 *
 */
public class MessageAdmin {
    private static final MessageAdmin SINGLETON = new MessageAdmin();

    private Map<String, PlaceHolder> table = new Hashtable<String, PlaceHolder>();

    private static final Logger log = LoggingManager.getLoggerForClass();

    private MessageAdmin() {
    }

    public static synchronized MessageAdmin getAdmin() {
        return SINGLETON;
    }

    /**
     * @param request
     */
    public void putRequest(String id, Message request) {
        if (log.isDebugEnabled()) {
            log.debug("REQ_ID [" + id + "]");
        }
        table.put(id, new PlaceHolder(request));
    }

    public void putReply(String id, Message reply) {
        PlaceHolder holder = table.get(id);
        if (log.isDebugEnabled()) {
            log.debug("RPL_ID [" + id + "] for holder " + holder);
        }
        if (holder != null) {
            holder.setReply(reply);
            Object obj = holder.getRequest();
            synchronized (obj) {
                obj.notify();
            }

        }
    }

    /**
     * Get the reply message.
     *
     * @param id
     *            the id of the message
     * @return the received message or <code>null</code>
     */
    public Message get(String id) {
        PlaceHolder holder = table.remove(id);
        if (log.isDebugEnabled()) {
            log.debug("GET_ID [" + id + "] for " + holder);
        }
        if (!holder.hasReply()) {
            log.info("Message with " + id + " not found.");
        }
        return (Message) holder.getReply();
    }
}

class PlaceHolder {
    private Object request;

    private Object reply;

    PlaceHolder(Object original) {
        this.request = original;
    }

    void setReply(Object reply) {
        this.reply = reply;
    }

    public Object getReply() {
        return reply;
    }

    public Object getRequest() {
        return request;
    }

    boolean hasReply() {
        return reply != null;
    }

    @Override
    public String toString() {
        return "request=" + request + ", reply=" + reply;
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3952.java