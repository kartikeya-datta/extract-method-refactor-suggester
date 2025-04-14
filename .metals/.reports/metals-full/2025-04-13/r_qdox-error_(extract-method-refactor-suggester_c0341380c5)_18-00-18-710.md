error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3807.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3807.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3807.java
text:
```scala
t@@his.serverCommunicationHandler = ServerCommunicationHandlerFactory.getInstance().getServerCommunicationHandler(environment, messageHandler);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 *
 */
package org.jboss.as.server;

import org.jboss.as.deployment.DeploymentServiceListener;
import org.jboss.as.model.Standalone;
import org.jboss.as.server.manager.ServerMessage;
import org.jboss.logging.Logger;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceActivatorContextImpl;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;


/**
 * An actual JBoss Application Server instance.
 *
 * @author Brian Stansberry
 * @author John E. Bailey
 */
public class Server {
    private static final Logger logger = Logger.getLogger("org.jboss.as.server");
    private final ServerEnvironment environment;
    private ServerCommunicationHandler serverCommunicationHandler;
    private final MessageHandler messageHandler = new MessageHandler(this);
    private Standalone config;
    private ServiceContainer serviceContainer;

    public Server(ServerEnvironment environment) {
        if (environment == null) {
            throw new IllegalArgumentException("bootstrapConfig is null");
        }
        this.environment = environment;
        launchCommunicationHandler();
        sendMessage("AVAILABLE");
        logger.info("Server Available to start");
    }

    public void start(Standalone config) throws ServerStartException {
        this.config = config;
        logger.info("Starting server with config: " + config.getServerName());
        serviceContainer = ServiceContainer.Factory.create();
        final BatchBuilder batchBuilder = serviceContainer.batchBuilder();
        final DeploymentServiceListener listener = new DeploymentServiceListener(new DeploymentServiceListener.Callback() {
            @Override
            public void run(Map<ServiceName, StartException> serviceFailures, long elapsedTime, int numberServices) {
                if(serviceFailures.isEmpty()) {
                    logger.infof("JBoss AS started [%d services in %dms]", numberServices, elapsedTime);
                    sendMessage("STARTED");
                } else {
                    sendMessage("START FAILED");
                    final StringBuilder buff = new StringBuilder(String.format("JBoss AS server start failed.  Attempted to start %d services in %dms", numberServices, elapsedTime));
                    buff.append("\nThe following services failed to start:\n");
                    for(Map.Entry<ServiceName, StartException> entry : serviceFailures.entrySet()) {
                        buff.append(String.format("\t%s => %s\n", entry.getKey(), entry.getValue().getMessage()));
                    }
                    logger.error(buff.toString());
                }
            }
        });
        batchBuilder.addListener(listener);

        try {
            listener.startBatch();
            final ServiceActivatorContext serviceActivatorContext = new ServiceActivatorContextImpl(batchBuilder);
            config.activate(serviceActivatorContext);
            batchBuilder.install();
            listener.finishBatch();
            listener.finishDeployment();
        } catch (Throwable t) {
            sendMessage("START FAILED");
            throw new ServerStartException("Failed to start server", t);
        }
    }

    public void stop() {
        serviceContainer.shutdown();
        sendMessage("STOPPED");
    }

    private void launchCommunicationHandler() {
        this.serverCommunicationHandler = ServerCommunicationHandlerFactory.getInstance().getProcessManagerSlave(environment, messageHandler);
        Thread t = new Thread(this.serverCommunicationHandler.getController(), "Server Process");
        t.start();
    }

    private void sendMessage(final String message) {
        try {
            final ServerMessage serverMessage = new ServerMessage(message);

            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            Checksum chksum = new Adler32();
            CheckedOutputStream cos = new CheckedOutputStream(baos, chksum);
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(cos);
                oos.writeObject(serverMessage);
                oos.close();
                oos = null;
                serverCommunicationHandler.sendMessage(baos.toByteArray(), chksum.getValue());
            }
            finally {
                if (oos != null) {
                    try {
                        oos.close();
                    }
                    catch (IOException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed to send message to Server Manager [" + message + "]", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3807.java