error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12509.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12509.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12509.java
text:
```scala
m@@essageHandler = new SessionOpenRequestHandler(this.deploymentRepository, this.marshallingStrategy, this.executorService);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.ejb3.remote.protocol.versionone;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.jboss.as.ejb3.deployment.DeploymentModuleIdentifier;
import org.jboss.as.ejb3.deployment.DeploymentRepository;
import org.jboss.as.ejb3.deployment.DeploymentRepositoryListener;
import org.jboss.as.ejb3.deployment.ModuleDeployment;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.CloseHandler;
import org.jboss.remoting3.MessageInputStream;
import org.xnio.IoUtils;

/**
 * User: jpai
 */
public class VersionOneProtocolChannelReceiver implements Channel.Receiver, DeploymentRepositoryListener {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(VersionOneProtocolChannelReceiver.class);

    private static final byte HEADER_SESSION_OPEN_REQUEST = 0x01;
    private static final byte HEADER_INVOCATION_REQUEST = 0x03;

    private final ServiceContainer serviceContainer;

    private final Channel channel;

    private final DeploymentRepository deploymentRepository;

    private final String marshallingStrategy;

    private final ExecutorService executorService;

    public VersionOneProtocolChannelReceiver(final Channel channel, final ServiceContainer serviceContainer, final String marshallingStrategy, final ExecutorService executorService) {
        this.serviceContainer = serviceContainer;
        this.marshallingStrategy = marshallingStrategy;
        this.channel = channel;
        this.executorService = executorService;

        this.deploymentRepository = this.getDeploymentRepository();
    }

    public void startReceiving() {
        this.channel.addCloseHandler(new ChannelCloseHandler());

        this.channel.receiveMessage(this);
        // listen to module availability/unavailability events
        this.deploymentRepository.addListener(this);
    }

    @Override
    public void handleError(Channel channel, IOException error) {
        try {
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.deploymentRepository.removeListener(this);
        }
        throw new RuntimeException("NYI: .handleError");
    }

    @Override
    public void handleEnd(Channel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            // ignore
        } finally {
            this.deploymentRepository.removeListener(this);
        }
    }

    @Override
    public void handleMessage(Channel channel, MessageInputStream messageInputStream) {
        try {
            // read the first byte to see what type of a message it is
            final int header = messageInputStream.read();
            if (logger.isTraceEnabled()) {
                logger.trace("Got message with header 0x" + Integer.toHexString(header) + " on channel " + channel);
            }
            MessageHandler messageHandler = null;
            switch (header) {
                case HEADER_INVOCATION_REQUEST:
                    messageHandler = new MethodInvocationMessageHandler(this.deploymentRepository, this.marshallingStrategy, this.executorService);
                    messageHandler.processMessage(channel, messageInputStream);
                    break;
                case HEADER_SESSION_OPEN_REQUEST:
                    messageHandler = new SessionOpenRequestHandler(this.deploymentRepository, this.marshallingStrategy);
                    messageHandler.processMessage(channel, messageInputStream);
                    break;
                default:
                    logger.warn("Received unsupported message header 0x" + Integer.toHexString(header) + " on channel " + channel);
            }
            // enroll for next message (whenever it's available)
            channel.receiveMessage(this);

        } catch (IOException e) {
            // log it
            logger.errorf(e, "Exception on channel %s from message %s", channel, messageInputStream);
            try {
                // no more messages can be sent or received on this channel
                channel.close();
            } catch (IOException e1) {
                // ignore
            }
        } finally {
            IoUtils.safeClose(messageInputStream);
        }
    }

    @Override
    public void listenerAdded(DeploymentRepository repository) {
        // get the initial available modules and send a message to the client
        final Map<DeploymentModuleIdentifier, ModuleDeployment> availableModules = this.deploymentRepository.getModules();
        if (availableModules != null && !availableModules.isEmpty()) {
            try {
                logger.debug("Sending initial module availabilty message, containing " + availableModules.size() + " module(s) to channel " + this.channel);
                this.sendModuleAvailability(availableModules.keySet().toArray(new DeploymentModuleIdentifier[availableModules.size()]));
            } catch (IOException e) {
                logger.warn("Could not send initial module availability report to channel " + this.channel, e);
            }
        }
    }

    @Override
    public void deploymentAvailable(DeploymentModuleIdentifier deploymentModuleIdentifier, ModuleDeployment moduleDeployment) {
        try {
            this.sendModuleAvailability(new DeploymentModuleIdentifier[]{deploymentModuleIdentifier});
        } catch (IOException e) {
            logger.warn("Could not send module availability notification of module " + deploymentModuleIdentifier + " to channel " + this.channel, e);
        }
    }

    @Override
    public void deploymentRemoved(DeploymentModuleIdentifier deploymentModuleIdentifier) {
        try {
            this.sendModuleUnAvailability(new DeploymentModuleIdentifier[]{deploymentModuleIdentifier});
        } catch (IOException e) {
            logger.warn("Could not send module un-availability notification of module " + deploymentModuleIdentifier + " to channel " + this.channel, e);
        }
    }

    private DeploymentRepository getDeploymentRepository() {
        final ServiceController<DeploymentRepository> serviceServiceController = (ServiceController<DeploymentRepository>) this.serviceContainer.getRequiredService(DeploymentRepository.SERVICE_NAME);
        return serviceServiceController.getValue();
    }


    private void sendModuleAvailability(DeploymentModuleIdentifier[] availableModules) throws IOException {
        final DataOutputStream outputStream = new DataOutputStream(this.channel.writeMessage());
        final ModuleAvailabilityWriter moduleAvailabilityWriter = new ModuleAvailabilityWriter(this.marshallingStrategy);
        try {
            moduleAvailabilityWriter.writeModuleAvailability(outputStream, availableModules);
        } finally {
            outputStream.close();
        }
    }

    private void sendModuleUnAvailability(DeploymentModuleIdentifier[] availableModules) throws IOException {
        final DataOutputStream outputStream = new DataOutputStream(this.channel.writeMessage());
        final ModuleAvailabilityWriter moduleAvailabilityWriter = new ModuleAvailabilityWriter(this.marshallingStrategy);
        try {
            moduleAvailabilityWriter.writeModuleUnAvailability(outputStream, availableModules);
        } finally {
            outputStream.close();
        }
    }

    private class ChannelCloseHandler implements CloseHandler<Channel> {

        @Override
        public void handleClose(Channel closedChannel, IOException exception) {
            logger.debug("Channel " + closedChannel + " closed. removing deployment listener " + this);
            VersionOneProtocolChannelReceiver.this.deploymentRepository.removeListener(VersionOneProtocolChannelReceiver.this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12509.java