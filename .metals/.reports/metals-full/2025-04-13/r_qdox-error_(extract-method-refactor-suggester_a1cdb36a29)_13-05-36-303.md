error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12019.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12019.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12019.java
text:
```scala
t@@hrow new OperationFailedException(getFallbackMessage() + ": " + e.getMessage());

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
package org.jboss.as.webservices.dmr;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.AccessController;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.as.webservices.logging.WSLogger;
import org.jboss.as.webservices.service.ServerConfigService;
import org.jboss.as.webservices.util.WSServices;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.wsf.spi.deployment.Endpoint;
import org.jboss.wsf.spi.management.EndpointMetrics;

/**
 * Provides WS endpoint metrics.
 *
 * @author <a href="mailto:ema@redhat.com">Jim Ma</a>
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
final class WSEndpointMetrics implements OperationStepHandler {

    static final WSEndpointMetrics INSTANCE = new WSEndpointMetrics();


    static final AttributeDefinition MIN_PROCESSING_TIME = new SimpleAttributeDefinitionBuilder("min-processing-time", ModelType.INT, true)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition MAX_PROCESSING_TIME = new SimpleAttributeDefinitionBuilder("max-processing-time", ModelType.INT, true)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition AVERAGE_PROCESSING_TIME = new SimpleAttributeDefinitionBuilder("average-processing-time", ModelType.INT, true)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition TOTAL_PROCESSING_TIME = new SimpleAttributeDefinitionBuilder("total-processing-time", ModelType.INT, true)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition REQUEST_COUNT = new SimpleAttributeDefinitionBuilder("request-count", ModelType.INT, true)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition RESPONSE_COUNT = new SimpleAttributeDefinitionBuilder("response-count", ModelType.INT, true)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition FAULT_COUNT = new SimpleAttributeDefinitionBuilder("fault-count", ModelType.INT, true)
            .setStorageRuntime()
            .build();


    static final AttributeDefinition[] ATTRIBUTES = {MIN_PROCESSING_TIME, MAX_PROCESSING_TIME, AVERAGE_PROCESSING_TIME,
            TOTAL_PROCESSING_TIME, REQUEST_COUNT, RESPONSE_COUNT, FAULT_COUNT};


    private WSEndpointMetrics() {
    }

    /**
     * {@inheritDoc}
     */
    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
        if (context.isNormalServer()) {
            context.addStep(new OperationStepHandler() {
                public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                    final ServiceRegistry registry = context.getServiceRegistry(false);
                    if (registry != null) {
                        try {
                            context.getResult().set(getEndpointMetricsFragment(operation, registry));
                        } catch (Exception e) {
                            throw new OperationFailedException(new ModelNode(getFallbackMessage() + ": " + e.getMessage()));
                        }
                    } else {
                        context.getResult().set(getFallbackMessage());
                    }
                    context.stepCompleted();
                }
            }, OperationContext.Stage.RUNTIME);
        } else {
            context.getResult().set(getFallbackMessage());
        }
        context.stepCompleted();
    }

    @SuppressWarnings("unchecked")
    private ModelNode getEndpointMetricsFragment(final ModelNode operation, final ServiceRegistry registry) throws OperationFailedException {
        final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        String endpointId;
        try {
            endpointId = URLDecoder.decode(address.getLastElement().getValue(), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        final String metricName = operation.require(NAME).asString();
        final String webContext = endpointId.substring(0, endpointId.indexOf(":"));
        final String endpointName = endpointId.substring(endpointId.indexOf(":") + 1);
        ServiceName endpointServiceName = WSServices.ENDPOINT_SERVICE.append("context="+webContext).append(endpointName);
        ServiceController<Endpoint> service = (ServiceController<Endpoint>) currentServiceContainer().getService(endpointServiceName);
        ServerConfigService serverConfigService = (ServerConfigService)currentServiceContainer().getService(WSServices.CONFIG_SERVICE).getService();
        Endpoint endpoint= service.getValue();
        final ModelNode result = new ModelNode();
        if (endpoint != null && serverConfigService.getValue().isStatisticsEnabled()) {
            final EndpointMetrics endpointMetrics = endpoint.getEndpointMetrics();
            if (MIN_PROCESSING_TIME.getName().equals(metricName)) {
                result.set(String.valueOf(endpointMetrics.getMinProcessingTime()));
            } else if (MAX_PROCESSING_TIME.getName().equals(metricName)) {
                result.set(String.valueOf(endpointMetrics.getMaxProcessingTime()));
            } else if (AVERAGE_PROCESSING_TIME.getName().equals(metricName)) {
                result.set(String.valueOf(endpointMetrics.getAverageProcessingTime()));
            } else if (TOTAL_PROCESSING_TIME.getName().equals(metricName)) {
                result.set(String.valueOf(endpointMetrics.getTotalProcessingTime()));
            } else if (REQUEST_COUNT.getName().equals(metricName)) {
                result.set(String.valueOf(endpointMetrics.getRequestCount()));
            } else if (RESPONSE_COUNT.getName().equals(metricName)) {
                result.set(String.valueOf(endpointMetrics.getResponseCount()));
            } else if (FAULT_COUNT.getName().equals(metricName)) {
                result.set(String.valueOf(endpointMetrics.getFaultCount()));
            }
        } else {
            result.set(getFallbackMessage());
        }
        return result;
    }

    private static String getFallbackMessage() {
        return WSLogger.ROOT_LOGGER.noMetricsAvailable();
    }

    private static ServiceContainer currentServiceContainer() {
        if(System.getSecurityManager() == null) {
            return CurrentServiceContainer.getServiceContainer();
        }
        return AccessController.doPrivileged(CurrentServiceContainer.GET_ACTION);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12019.java