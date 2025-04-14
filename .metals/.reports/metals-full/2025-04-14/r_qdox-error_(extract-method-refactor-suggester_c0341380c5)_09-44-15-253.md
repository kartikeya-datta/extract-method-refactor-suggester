error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10715.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10715.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10715.java
text:
```scala
i@@f (flags.contains(OperationEntry.Flag.READ_ONLY) && !flags.contains(OperationEntry.Flag.DOMAIN_PUSH_TO_SERVERS)) {

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


import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.domain.controller.ServerIdentity;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Adds to the localResponse the server-level operations needed to effect the given domain/host operation on the
 * servers controlled by this host.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class ServerOperationsResolverHandler implements OperationStepHandler {

    public static final String OPERATION_NAME = "server-operation-resolver";

    private final String localHostName;
    private final ServerOperationResolver resolver;
    private final ParsedOp parsedOp;
    private final PathAddress originalAddress;
    private final ImmutableManagementResourceRegistration originalRegistration;
    private final ModelNode localResponse;
    private final boolean recordResponse;

    ServerOperationsResolverHandler(final String localHostName, final ServerOperationResolver resolver, final ParsedOp parsedOp,
                                    final PathAddress originalAddress, final ImmutableManagementResourceRegistration originalRegistration,
                                    final ModelNode response, final boolean recordResponse) {
        this.localHostName = localHostName;
        this.resolver = resolver;
        this.parsedOp = parsedOp;
        this.originalAddress = originalAddress;
        this.originalRegistration = originalRegistration;
        this.localResponse = response;
        this.recordResponse = recordResponse;
    }

    @Override
    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {

        // Read out what was added by any MODEL/RUNTIME/VERIFY handlers and publish it to the overall context
        if (context.hasResult()) {
            localResponse.get(RESULT).set(context.getResult());
        }
        if (context.hasFailureDescription()) {
            localResponse.get(FAILURE_DESCRIPTION).set(context.getFailureDescription());
            // We do not allow failures on the host controllers
            context.setRollbackOnly();
        } else {

            final ModelNode domainModel = Resource.Tools.readModel(context.getRootResource());
            ParsedOp.ServerOperationProvider provider = new ParsedOp.ServerOperationProvider() {

                @Override
                public Map<Set<ServerIdentity>, ModelNode> getServerOperations(ModelNode domainOp, PathAddress address) {
                    return ServerOperationsResolverHandler.this.getServerOperations(domainOp, address, domainModel, domainModel.get(HOST).get(localHostName));
                }
            };

            ModelNode localResult = localResponse.get(RESULT);
            localResponse.remove(RESULT);  // We're going to replace it
            ModelNode responseNode = recordResponse ? context.getResult() : localResponse.get(RESULT);
            if (ModelType.STRING == localResult.getType() && IGNORED.equals(localResult.asString())) {
                // Just pass the IGNORED along
                responseNode.set(localResult);
            } else {
                Map<Set<ServerIdentity>, ModelNode> serverOps = parsedOp.getServerOps(provider);
                createOverallResult(serverOps, localResult, responseNode);
            }

            if (PrepareStepHandler.isTraceEnabled()) {
                PrepareStepHandler.log.trace(getClass().getSimpleName() + " responseNode is " + responseNode);
            }
        }

        context.completeStep();
    }

    private Map<Set<ServerIdentity>, ModelNode> getServerOperations(ModelNode domainOp, PathAddress domainOpAddress, ModelNode domainModel, ModelNode hostModel) {
        Map<Set<ServerIdentity>, ModelNode> result = null;
        final PathAddress relativeAddress = domainOpAddress.subAddress(originalAddress.size());
        Set<OperationEntry.Flag> flags = originalRegistration.getOperationFlags(relativeAddress, domainOp.require(OP).asString());
        if (flags.contains(OperationEntry.Flag.READ_ONLY)) {
            result = Collections.emptyMap();
        }
        if (result == null) {
            result = resolver.getServerOperations(domainOp, domainOpAddress, domainModel, hostModel);
        }
        return result;
    }

    private void createOverallResult(Map<Set<ServerIdentity>, ModelNode> serverOps, final ModelNode localResult, final ModelNode overallResult) {

        ModelNode domainResult = parsedOp.getFormattedDomainResult(localResult);
        overallResult.get(DOMAIN_RESULTS).set(domainResult);
        ModelNode serverOpsNode = overallResult.get(SERVER_OPERATIONS);
        for (Map.Entry<Set<ServerIdentity>, ModelNode> entry : serverOps.entrySet()) {
            ModelNode setNode = serverOpsNode.add();
            ModelNode serverNode = setNode.get("servers");
            serverNode.setEmptyList();
            for (ServerIdentity server : entry.getKey()) {
                serverNode.add(server.getServerName(), server.getServerGroupName());
            }
            setNode.get(OP).set(entry.getValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10715.java