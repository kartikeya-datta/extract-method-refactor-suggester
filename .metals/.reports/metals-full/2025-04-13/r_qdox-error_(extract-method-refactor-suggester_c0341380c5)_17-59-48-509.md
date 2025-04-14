error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4374.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4374.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4374.java
text:
```scala
l@@og.infof("Starting server '%s'", environment.getProcessName());

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

package org.jboss.as.server;

import org.jboss.as.model.ServerModel;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceActivatorContextImpl;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceRegistryException;

/**
 * The server base class.
 *
 * @author Emanuel Muckenhuber
 */
public abstract class AbstractServer {

    static final Logger log = Logger.getLogger("org.jboss.as.server");

    private ServerModel config;
    private ServiceContainer serviceContainer;
    private final ServerEnvironment environment;

    protected AbstractServer(final ServerEnvironment environment) {
        if (environment == null) {
            throw new IllegalArgumentException("bootstrapConfig is null");
        }
        this.environment = environment;
    }

    /**
     * Get the server environment.
     *
     * @return the server environment
     */
    public ServerEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Get the standalone configuration.
     *
     * @return the standalone configuration
     */
    public ServerModel getConfig() {
        if(config == null) {
            throw new IllegalStateException("null configuration");
        }
        return config;
    }

    /**
     * Start the server.
     *
     * @throws ServerStartException
     */
    public abstract void start() throws ServerStartException;

    /**
     * Start the server.
     *
     * @param config the server
     * @throws ServerStartException
     */
    void start(final ServerModel config) throws ServerStartException {
        if(config == null)  {
            throw new IllegalArgumentException("null standalone config");
        }
        this.config = config;
        log.infof("Starting server '%s'", config.getServerName());
        serviceContainer = ServiceContainer.Factory.create();

        final ServerStartupListener listener = new ServerStartupListener(createListenerCallback());

        try {
            // Activate subsystems
            final ServerStartBatchBuilder subsystemBatchBuilder = new ServerStartBatchBuilder(serviceContainer.batchBuilder(), listener);
            subsystemBatchBuilder.addListener(listener);
            final ServiceActivatorContext subsystemActivatorContext = new ServiceActivatorContextImpl(subsystemBatchBuilder);
            config.activateSubsystems(subsystemActivatorContext);
            listener.startBatch(new Runnable() {
                @Override
                public void run() {
                    // Activate deployments once the first batch is complete.
                    final ServerStartBatchBuilder deploymentBatchBuilder = new ServerStartBatchBuilder(serviceContainer.batchBuilder(), listener);
                    deploymentBatchBuilder.addListener(listener);
                    final ServiceActivatorContext deploymentActivatorContext = new ServiceActivatorContextImpl(deploymentBatchBuilder);
                    listener.startBatch(null);
                    config.activateDeployments(deploymentActivatorContext);
                    listener.finish(); // We have finished adding everything for the server start
                    try {
                        deploymentBatchBuilder.install();
                        listener.finishBatch();
                    } catch (ServiceRegistryException e) {
                        throw new RuntimeException(e); // TODO: better exception handling.
                    }
                }
            });
            subsystemBatchBuilder.install();
            listener.finishBatch();
        } catch (Throwable t) {
            throw new ServerStartException("Failed to start server", t);
        }
    }

    /**
     * Stop the server.
     *
     */
    public void stop() {
        log.infof("Stopping server '%s'", config.getServerName());
        final ServiceContainer container = this.serviceContainer;
        if(container != null) {
            container.shutdown();
        }
        this.config = null;
        this.serviceContainer = null;
    }

    abstract ServerStartupListener.Callback createListenerCallback();

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4374.java