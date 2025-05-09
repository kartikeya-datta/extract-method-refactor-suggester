error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1341.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1341.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1341.java
text:
```scala
l@@og.tracef("%s handling request %d(%d)", ManagementChannel.this, header.getBatchId(), header.getRequestId());

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.protocol.mgmt;

import static org.jboss.as.protocol.old.ProtocolUtils.expectHeader;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.as.protocol.ProtocolChannel;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.SimpleDataInput;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.CloseHandler;
import org.jboss.remoting3.MessageInputStream;
import org.xnio.IoUtils;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ManagementChannel extends ProtocolChannel {

    private final RequestReceiver requestReceiver = new RequestReceiver();
    private final ResponseReceiver responseReceiver = new ResponseReceiver();

    ManagementChannel(String name, Channel channel) {
        super(name, channel);
    }

    public void setOperationHandler(final ManagementOperationHandler handler) {
        requestReceiver.setOperationHandler(handler);
    }

    @Override
    protected void doHandle(final Channel channel, final MessageInputStream message) {
        log.tracef("%s handling incoming data", this);
        final SimpleDataInput input = new SimpleDataInput(Marshalling.createByteInput(message));
        try {
            ManagementProtocolHeader header = ManagementProtocolHeader.parse(input);
            if (header.isRequest()) {
                requestReceiver.handleRequest((ManagementRequestHeader)header, input);
            } else {
                responseReceiver.handleResponse((ManagementResponseHeader)header, input);
            }
            //Consume the rest of the output if any
            while (input.read() != -1) {
            }
        } catch (Exception e) {
            log.tracef(e, "%s error handling incoming data", this);
        } finally {
            log.tracef("%s done handling incoming data", this);
            IoUtils.safeClose(input);
            IoUtils.safeClose(message);
        }
    }

    void executeRequest(ManagementRequest<?> request, ManagementResponseHandler<?> responseHandler) throws IOException {
        responseReceiver.registerResponseHandler(request.getCurrentRequestId(), responseHandler);
        FlushableDataOutputImpl output = FlushableDataOutputImpl.create(this.writeMessage());
        Key closeKey = null;
        try {
            CloseHandler<Channel> closeHandler = request.getRequestCloseHandler();
            if (closeHandler != null) {
                closeKey = addCloseHandler(closeHandler);
            }
            final ManagementRequestHeader managementRequestHeader = new ManagementRequestHeader(ManagementProtocol.VERSION, request.getCurrentRequestId(), request.getBatchId(), request.getRequestCode());
            managementRequestHeader.write(output);

            request.writeRequest(this, output);
        } finally {
            IoUtils.safeClose(output);
            if (closeKey != null) {
                closeKey.remove();
            }
        }
    }

    void throwFormattedException(Exception e) throws IOException {
        //e.printStackTrace();
        if (e instanceof IOException) {
            throw (IOException)e;
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        }
        throw new IOException(e);

    }

    private class RequestReceiver {
        private volatile ManagementOperationHandler operationHandler;

        private void handleRequest(final ManagementRequestHeader header, final DataInput input) throws IOException {
            log.tracef("%s handling request %d", ManagementChannel.this, header.getBatchId());
            final FlushableDataOutputImpl output = FlushableDataOutputImpl.create(writeMessage());

            Exception error = null;
            try {
                final ManagementRequestHandler requestHandler;
                try {
                    //Read request
                    requestHandler = getRequestHandler(header);
                    requestHandler.setContext(new ManagementRequestContext(ManagementChannel.this, header));
                    requestHandler.readRequest(input);
                    expectHeader(input, ManagementProtocol.REQUEST_END);
                } catch (Exception e) {
                    error = e;
                    throw e;
                } finally {
                    writeResponseHeader(header, output, error);
                    if (error != null) {
                        output.writeByte(ManagementProtocol.RESPONSE_END);
                    }
                }
                requestHandler.writeResponse(output);
                output.writeByte(ManagementProtocol.RESPONSE_END);
            } catch (Exception e) {
                throwFormattedException(e);
            } finally {
                log.tracef("%s finished request %d", ManagementChannel.this, header.getBatchId());
                IoUtils.safeClose(output);
            }
        }

        private ManagementRequestHandler getRequestHandler(final ManagementRequestHeader header) throws IOException {
            try {
                ManagementOperationHandler operationHandler = this.operationHandler;
                if (operationHandler == null) {
                    throw new IOException("No operation handler set");
                }
                ManagementRequestHandler requestHandler = operationHandler.getRequestHandler(header.getOperationId());
                if (requestHandler == null) {
                    throw new IOException("No request handler found with id " + header.getOperationId() + " in operation handler " + operationHandler);
                }
                return requestHandler;
            } catch (IOException e) {
                e.printStackTrace();//TODO remove
                throw e;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        private void writeResponseHeader(final ManagementRequestHeader header, DataOutput output, Exception exception) throws IOException {
            final int workingVersion = Math.min(ManagementProtocol.VERSION, header.getVersion());
            try {
                // Now write the response header
                final ManagementResponseHeader responseHeader = new ManagementResponseHeader(workingVersion, header.getRequestId(), formatException(exception));
                responseHeader.write(output);
            } catch (IOException e) {
                throw e;
            } catch (Throwable t) {
                throw new IOException("Failed to write management response headers", t);
            }
        }

        private void setOperationHandler(final ManagementOperationHandler operationHandler) {
            this.operationHandler = operationHandler;
        }

        private String formatException(Exception exception) {
            if (exception == null) {
                return null;
            }
            return exception.getMessage();
        }
    }

    private class ResponseReceiver {

        private final Map<Integer, ManagementResponseHandler<?>> responseHandlers = Collections.synchronizedMap(new HashMap<Integer, ManagementResponseHandler<?>>());

        private void registerResponseHandler(final int requestId, final ManagementResponseHandler<?> handler) throws IOException {
            if (responseHandlers.put(requestId, handler) != null) {
                throw new IOException("Response handler already registered for request");
            }
        }

        private void handleResponse(ManagementResponseHeader header, DataInput input) throws IOException {
            log.tracef("%s handling response %d", ManagementChannel.this, header.getResponseId());
            ManagementResponseHandler<?> responseHandler = responseHandlers.get(header.getResponseId());
            if (responseHandler == null) {
                throw new IOException("No response handler for request " + header.getResponseId());
            }
            responseHandler.setResponseContext(new ManagementResponseContext(header, ManagementChannel.this));

            try {
                responseHandler.readResponse(input);
                expectHeader(input, ManagementProtocol.RESPONSE_END);
            } catch (Exception e) {
                throwFormattedException(e);
            } finally {
                log.tracef("%s handled response %d", ManagementChannel.this, header.getResponseId());
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1341.java