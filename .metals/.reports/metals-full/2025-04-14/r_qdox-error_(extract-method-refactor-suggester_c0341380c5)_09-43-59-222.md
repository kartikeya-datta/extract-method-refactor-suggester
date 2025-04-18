error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1670.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1670.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1670.java
text:
```scala
c@@ontext.stepCompleted();

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

package org.jboss.as.domain.controller.operations.coordination;


import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CALLER_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DOMAIN_RESULTS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.IGNORED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_OPERATIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.USER;
import static org.jboss.as.domain.controller.DomainControllerLogger.HOST_CONTROLLER_LOGGER;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.domain.controller.ServerIdentity;
import org.jboss.dmr.ModelNode;

/**
 * Adds to the localResponse the server-level operations needed to effect the given domain/host operation on the
 * servers controlled by this host.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class ServerOperationsResolverHandler implements OperationStepHandler {

    public static final String DOMAIN_PUSH_TO_SERVERS = "push-to-servers";
    public static final String OPERATION_NAME = "server-operation-resolver";

    private static final HostControllerExecutionSupport.ServerOperationProvider NO_OP_PROVIDER =
        new HostControllerExecutionSupport.ServerOperationProvider() {
            @Override
            public Map<Set<ServerIdentity>, ModelNode> getServerOperations(ModelNode domainOp, PathAddress address) {

                return Collections.emptyMap();
            }
        };

    private final ServerOperationResolver resolver;
    private final HostControllerExecutionSupport hostControllerExecutionSupport;
    private final PathAddress originalAddress;
    private final ImmutableManagementResourceRegistration originalRegistration;
    private final ModelNode localResponse;

    ServerOperationsResolverHandler(final ServerOperationResolver resolver,
                                    final HostControllerExecutionSupport hostControllerExecutionSupport,
                                    final PathAddress originalAddress,
                                    final ImmutableManagementResourceRegistration originalRegistration,
                                    final ModelNode response) {
        this.resolver = resolver;
        this.hostControllerExecutionSupport = hostControllerExecutionSupport;
        this.originalAddress = originalAddress;
        this.originalRegistration = originalRegistration;
        this.localResponse = response;
    }

    @Override
    public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {

        if (context.hasFailureDescription()) {
            localResponse.get(FAILURE_DESCRIPTION).set(context.getFailureDescription());
            // We do not allow failures on the host controllers
            context.setRollbackOnly();
        } else {
            boolean nullDomainOp = hostControllerExecutionSupport.getDomainOperation() == null;

            // Transformed operations might need to simulate certain behavior, so allow read-only operations to be pushed as well
            final boolean pushToServers;
            if(operation.hasDefined(OPERATION_HEADERS)) {
                pushToServers = operation.get(OPERATION_HEADERS, DOMAIN_PUSH_TO_SERVERS).asBoolean(false);
            } else {
                pushToServers = false;
            }
            HostControllerExecutionSupport.ServerOperationProvider provider = nullDomainOp
                ? NO_OP_PROVIDER
                : new HostControllerExecutionSupport.ServerOperationProvider() {
                    @Override
                    public Map<Set<ServerIdentity>, ModelNode> getServerOperations(ModelNode domainOp, PathAddress address) {

                        Map<Set<ServerIdentity>, ModelNode> ops = ServerOperationsResolverHandler.this.getServerOperations(context, domainOp, address, pushToServers);
                        for (Map.Entry<Set<ServerIdentity>, ModelNode> entry : ops.entrySet()) {
                            ModelNode op = entry.getValue();
                            //Remove the caller-type=user header
                            if (op.hasDefined(OPERATION_HEADERS) && op.get(OPERATION_HEADERS).hasDefined(CALLER_TYPE) && op.get(OPERATION_HEADERS, CALLER_TYPE).asString().equals(USER)) {
                                op.get(OPERATION_HEADERS).remove(CALLER_TYPE);
                            }
                        }
                        return ops;
                    }
                };
            Map<ServerIdentity, ModelNode> serverOps = hostControllerExecutionSupport.getServerOps(provider);

            ModelNode domainOpResult = nullDomainOp ? new ModelNode(IGNORED) : (context.hasResult() ? context.getResult() : new ModelNode());

            ModelNode overallResult = localResponse == null ? context.getResult() : localResponse.get(RESULT);
            createOverallResult(serverOps, domainOpResult, overallResult);

            if (HOST_CONTROLLER_LOGGER.isTraceEnabled()) {
                HOST_CONTROLLER_LOGGER.tracef("%s responseNode is %s", getClass().getSimpleName(), overallResult);
            }
        }

        context.completeStep();
    }

    private Map<Set<ServerIdentity>, ModelNode> getServerOperations(OperationContext context, ModelNode domainOp, PathAddress domainOpAddress, boolean pushToServers) {
        Map<Set<ServerIdentity>, ModelNode> result = null;
        final PathAddress relativeAddress = domainOpAddress.subAddress(originalAddress.size());
        if(! pushToServers) {
            Set<OperationEntry.Flag> flags = originalRegistration.getOperationFlags(relativeAddress, domainOp.require(OP).asString());
            if (flags.contains(OperationEntry.Flag.READ_ONLY) && !flags.contains(OperationEntry.Flag.DOMAIN_PUSH_TO_SERVERS)) {
                result = Collections.emptyMap();
            }
        }
        if (result == null) {
            result = resolver.getServerOperations(context, domainOp, domainOpAddress);
        }
        return result;
    }

    private void createOverallResult(final Map<ServerIdentity, ModelNode> serverOps,
                                     final ModelNode localResult, final ModelNode overallResult) {

        ModelNode domainResult = hostControllerExecutionSupport.getFormattedDomainResult(localResult);
        overallResult.setEmptyObject();
        overallResult.get(DOMAIN_RESULTS).set(domainResult);

        ModelNode serverOpsNode = overallResult.get(SERVER_OPERATIONS);

        // Group servers with the same ops together to save bandwidth
        final Map<ModelNode, Set<ServerIdentity>> bundled = new HashMap<ModelNode, Set<ServerIdentity>>();
        for (Map.Entry<ServerIdentity, ModelNode> entry : serverOps.entrySet()) {
            Set<ServerIdentity> idSet = bundled.get(entry.getValue());
            if (idSet == null) {
                idSet = new HashSet<ServerIdentity>();
                bundled.put(entry.getValue(), idSet);
            }
            idSet.add(entry.getKey());
        }
        for (Map.Entry<ModelNode, Set<ServerIdentity>> entry : bundled.entrySet()) {
            ModelNode setNode = serverOpsNode.add();
            ModelNode serverNode = setNode.get("servers");
            serverNode.setEmptyList();
            for (ServerIdentity server : entry.getValue()) {
                serverNode.add(server.getServerName(), server.getServerGroupName());
            }
            setNode.get(OP).set(entry.getKey());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1670.java