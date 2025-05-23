error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6790.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6790.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6790.java
text:
```scala
r@@esult.get(SERVER_GROUPS, groupName).set(groupNode);

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

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DOMAIN_FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST_FAILURE_DESCRIPTIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.IGNORED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESPONSE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUPS;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.domain.controller.ServerIdentity;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Assembles the overall result for a domain operation from individual host and server results.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class DomainFinalResultHandler implements OperationStepHandler {

    private final DomainOperationContext domainOperationContext;

    public DomainFinalResultHandler(DomainOperationContext domainOperationContext) {
        this.domainOperationContext = domainOperationContext;
    }

    @Override
    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {

        context.completeStep();

        // On the way out, fix up the response
        final boolean isDomain = isDomainOperation(operation);
        boolean shouldContinue = !collectDomainFailure(context, isDomain);
        shouldContinue = shouldContinue && !collectContextFailure(context, isDomain);
        shouldContinue = shouldContinue && !collectHostFailures(context, isDomain);
        if(shouldContinue){
            if (domainOperationContext.getServerResults().size() == 0) {
                context.getResult().set(getSingleHostResult());
            } else {
                populateServerGroupResults(context, context.getResult());
            }
        }
    }

    private boolean collectDomainFailure(OperationContext context, final boolean isDomain) {
        final ModelNode coordinator = domainOperationContext.getCoordinatorResult();
        ModelNode domainFailure = null;
        if (isDomain &&  coordinator != null && coordinator.has(FAILURE_DESCRIPTION)) {
            domainFailure = coordinator.hasDefined(FAILURE_DESCRIPTION) ? coordinator.get(FAILURE_DESCRIPTION) : new ModelNode().set("Unexplained failure");
        }
        if (domainFailure != null) {
            context.getFailureDescription().get(DOMAIN_FAILURE_DESCRIPTION).set(domainFailure);
            return true;
        }
        return false;
    }

    private boolean collectContextFailure(OperationContext context, final boolean isDomain) {
        // We ignore a context failure description if the request failed on all servers, as the
        // DomainRolloutStepHandler would have had to set that to trigger model rollback
        // but we still want to record the server results so the user can see the problem
        if (!domainOperationContext.isFailureReported() && context.hasFailureDescription()) {
            ModelNode formattedFailure = new ModelNode();
            if (isDomain) {
                ModelNode failure = context.getFailureDescription();
                if (failure.isDefined())
                    formattedFailure.get(DOMAIN_FAILURE_DESCRIPTION).set(failure);
                else
                    formattedFailure.get(DOMAIN_FAILURE_DESCRIPTION).set("Unexplained failure");
            } else {
                ModelNode hostFailureProperty = new ModelNode();
                ModelNode contextFailure = context.getFailureDescription();
                ModelNode hostFailure = contextFailure.isDefined() ? contextFailure : new ModelNode().set("Unexplained failure");
                hostFailureProperty.add(domainOperationContext.getLocalHostInfo().getLocalHostName(), hostFailure);

                formattedFailure.get(HOST_FAILURE_DESCRIPTIONS).set(hostFailureProperty);
            }

            context.getFailureDescription().set(formattedFailure);

            return true;
        }
        return false;
    }

    private boolean collectHostFailures(final OperationContext context, final boolean isDomain) {
        ModelNode hostFailureResults = null;
        for (Map.Entry<String, ModelNode> entry : domainOperationContext.getHostControllerResults().entrySet()) {
            ModelNode hostResult = entry.getValue();
            if (hostResult.has(FAILURE_DESCRIPTION)) {
                if (hostFailureResults == null) {
                    hostFailureResults = new ModelNode();
                }
                final ModelNode desc = hostResult.hasDefined(FAILURE_DESCRIPTION) ? hostResult.get(FAILURE_DESCRIPTION) : new ModelNode().set("Unexplained failure");
                hostFailureResults.add(entry.getKey(), desc);
            }
        }

        final ModelNode coordinator = domainOperationContext.getCoordinatorResult();
        if (!isDomain && coordinator != null && coordinator.has(FAILURE_DESCRIPTION)) {
            if (hostFailureResults == null) {
                hostFailureResults = new ModelNode();
            }
            final ModelNode desc = coordinator.hasDefined(FAILURE_DESCRIPTION) ? coordinator.get(FAILURE_DESCRIPTION) : new ModelNode().set("Unexplained failure");
            hostFailureResults.add(domainOperationContext.getLocalHostInfo().getLocalHostName(), desc);
        }

        if (hostFailureResults != null) {
            context.getFailureDescription().get(HOST_FAILURE_DESCRIPTIONS).set(hostFailureResults);
            return true;
        }
        return false;
    }

    private ModelNode getSingleHostResult() {
        ModelNode singleHost = domainOperationContext.getCoordinatorResult();
        if (singleHost != null
                && (!singleHost.hasDefined(RESULT)
 (ModelType.STRING == singleHost.get(RESULT).getType()
                        && IGNORED.equals(singleHost.get(RESULT).asString())))) {
            singleHost = null;
        }
        if (singleHost == null) {
            for (ModelNode node : domainOperationContext.getHostControllerResults().values()) {
                if (node.hasDefined(RESULT) && !IGNORED.equals(node.get(RESULT).asString())) {
                    singleHost = node;
                    break;
                }
            }
        }

        return singleHost == null ? new ModelNode() : singleHost.get(RESULT);
    }

    private void populateServerGroupResults(final OperationContext context, final ModelNode result) {
        final Set<String> groupNames = new TreeSet<String>();
        final Map<String, Set<HostServer>> groupToServerMap = new HashMap<String, Set<HostServer>>();
        for (Map.Entry<ServerIdentity, ModelNode> entry : domainOperationContext.getServerResults().entrySet()) {
            final String serverGroup = entry.getKey().getServerGroupName();
            groupNames.add(serverGroup);
            final String hostName = entry.getKey().getHostName();
            final String serverName = entry.getKey().getServerName();
            if (!groupToServerMap.containsKey(serverGroup)) {
                groupToServerMap.put(serverGroup, new TreeSet<HostServer>());
            }
            groupToServerMap.get(serverGroup).add(new HostServer(hostName, serverName, entry.getValue()));
        }

        boolean serverGroupSuccess = false;
        for (String groupName : groupNames) {
            final ModelNode groupNode = new ModelNode();
            if (domainOperationContext.isServerGroupRollback(groupName)) {
                // TODO revisit if we should report this for the whole group, since the result might not be accurate
                // groupNode.get(ROLLED_BACK).set(true);
            } else {
                serverGroupSuccess = true;
            }
            for (HostServer hostServer : groupToServerMap.get(groupName)) {
                final ModelNode serverNode = new ModelNode();
                serverNode.get(HOST).set(hostServer.hostName);
                serverNode.get(RESPONSE).set(hostServer.result);
                groupNode.get(hostServer.serverName).set(serverNode);
            }
            result.get(SERVER_GROUPS).add(groupName, groupNode);
        }
        if(!serverGroupSuccess) {
            context.getFailureDescription().set("Operation failed or was rolled back on all servers.");
        }
    }

    private boolean isDomainOperation(final ModelNode operation) {
        final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        return address.size() == 0 || !address.getElement(0).getKey().equals(HOST);
    }

    private class HostServer implements Comparable<HostServer> {
        private final String hostName;
        private final String serverName;
        private final ModelNode result;

        private HostServer(String hostName, String serverName, ModelNode result) {
            this.hostName = hostName;
            this.serverName = serverName;
            this.result = result;
        }

        public int compareTo(HostServer hostServer) {
            int hostCompare = hostName.compareTo(hostServer.hostName);
            if (hostCompare != 0) {
                return hostCompare;
            }
            return serverName.compareTo(hostServer.serverName);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6790.java