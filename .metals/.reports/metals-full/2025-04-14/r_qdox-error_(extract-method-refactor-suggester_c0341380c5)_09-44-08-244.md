error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12662.java
text:
```scala
M@@odelNode actionResultNode = planResultNode.get("step-" + i);

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
package org.jboss.as.server.client.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.server.client.api.deployment.ServerDeploymentActionResult;
import org.jboss.as.server.client.api.deployment.ServerDeploymentPlanResult;
import org.jboss.as.server.client.api.deployment.ServerUpdateActionResult.Result;
import org.jboss.as.server.client.api.deployment.SimpleServerDeploymentActionResult;
import org.jboss.as.server.client.impl.deployment.DeploymentActionImpl;
import org.jboss.as.server.client.impl.deployment.DeploymentPlanImpl;
import org.jboss.as.server.client.impl.deployment.DeploymentPlanResultImpl;
import org.jboss.dmr.ModelNode;

/**
 * Adapts from Future<ModelNode> to Future<ServerDeploymentPlanResult>.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
class ServerDeploymentPlanResultFuture implements Future<ServerDeploymentPlanResult> {

    private final Future<ModelNode> nodeFuture;
    private final DeploymentPlanImpl plan;

    ServerDeploymentPlanResultFuture(final DeploymentPlanImpl plan, final Future<ModelNode> nodeFuture) {
        this.plan = plan;
        this.nodeFuture = nodeFuture;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return nodeFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return nodeFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return nodeFuture.isDone();
    }

    @Override
    public ServerDeploymentPlanResult get() throws InterruptedException, ExecutionException {
        ModelNode node = nodeFuture.get();
        return getResultFromNode(node);
    }

    @Override
    public ServerDeploymentPlanResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
            TimeoutException {
        ModelNode node = nodeFuture.get(timeout, unit);
        return getResultFromNode(node);
    }

    private ServerDeploymentPlanResult getResultFromNode(ModelNode planResultNode) {
        UUID planId = plan.getId();
        Map<UUID, ServerDeploymentActionResult> actionResults = new HashMap<UUID, ServerDeploymentActionResult>();
        List<DeploymentActionImpl> actions = plan.getDeploymentActionImpls();
        for (int i = 0; i < actions.size(); i++) {
            DeploymentActionImpl action = actions.get(i);
            UUID actionId = action.getId();
            ModelNode actionResultNode = planResultNode.get(i);
            actionResults.put(actionId, getActionResult(actionId, actionResultNode));
        }
        return new DeploymentPlanResultImpl(planId, actionResults);
    }

    private ServerDeploymentActionResult getActionResult(UUID actionId, ModelNode actionResultNode) {
        ServerDeploymentActionResult actionResult = null;
        String outcome = actionResultNode.get("outcome").asString();
        if ("cancelled".equals(outcome)) {
            actionResult = new SimpleServerDeploymentActionResult(actionId, Result.NOT_EXECUTED);
        } else if ("failed".equals(outcome)) {
            OperationFailedException e = actionResultNode.hasDefined("failure-description") ? new OperationFailedException(
                    actionResultNode.get("failure-description")) : null;
            if (actionResultNode.hasDefined("rolled-back") && actionResultNode.get("rolled-back").asBoolean()) {
                if (e == null) {
                    actionResult = new SimpleServerDeploymentActionResult(actionId, Result.ROLLED_BACK);
                } else {
                    actionResult = new SimpleServerDeploymentActionResult(actionId, Result.ROLLED_BACK, e);
                }
            } else {
                actionResult = new SimpleServerDeploymentActionResult(actionId, e);
            }
        } else {
            actionResult = new SimpleServerDeploymentActionResult(actionId, Result.EXECUTED);
        }
        // FIXME deal with shutdown possibilities
        return actionResult;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12662.java