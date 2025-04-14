error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4559.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4559.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4559.java
text:
```scala
c@@ontext.removeService(ChannelService.getStackServiceName(ChannelService.DEFAULT));

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
package org.jboss.as.clustering.jgroups.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.util.ServiceLoader;

import org.jboss.as.clustering.dmr.ModelNodes;
import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.service.ServiceName;
import org.wildfly.clustering.spi.ClusteredGroupServiceInstaller;
import org.wildfly.clustering.spi.GroupServiceInstaller;

/**
 * Handler for JGroups subsystem remove operations.
 *
 * @author Kabir Khan
 */
public class JGroupsSubsystemRemoveHandler extends AbstractRemoveStepHandler {

    private final boolean allowRuntimeOnlyRegistration;

    JGroupsSubsystemRemoveHandler(boolean allowRuntimeOnlyRegistration) {
        this.allowRuntimeOnlyRegistration = allowRuntimeOnlyRegistration;
    }

    @Override
    protected void performRemove(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        PathAddress subsystemAddress = PathAddress.pathAddress(operation.require(OP_ADDR));

        if (model.hasDefined(ChannelResourceDefinition.WILDCARD_PATH.getKey())) {
            ModelNode channels = model.get(ChannelResourceDefinition.WILDCARD_PATH.getKey());
            if (channels.isDefined()) {
                for (Property channel: channels.asPropertyList()) {
                    PathAddress address = subsystemAddress.append(ChannelResourceDefinition.pathElement(channel.getName()));
                    context.addStep(Util.createRemoveOperation(address), new ChannelRemoveHandler(this.allowRuntimeOnlyRegistration), OperationContext.Stage.MODEL);
                }
            }
        }

        if (model.hasDefined(StackResourceDefinition.WILDCARD_PATH.getKey())) {
            ModelNode stacks = model.get(StackResourceDefinition.WILDCARD_PATH.getKey());
            if (stacks.isDefined()) {
                for (Property stack: stacks.asPropertyList()) {
                    PathAddress address = subsystemAddress.append(StackResourceDefinition.pathElement(stack.getName()));
                    context.addStep(Util.createRemoveOperation(address), new StackRemoveHandler(), OperationContext.Stage.MODEL);
                }
            }
        }

        super.performRemove(context, operation, model);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        // remove the ProtocolDefaultsService
        context.removeService(ProtocolDefaultsService.SERVICE_NAME);

        String defaultStack = ModelNodes.asString(JGroupsSubsystemResourceDefinition.DEFAULT_STACK.resolveModelAttribute(context, model));
        if ((defaultStack != null) && !defaultStack.equals(ChannelFactoryService.DEFAULT)) {
            context.removeService(ChannelFactoryService.getServiceName(ChannelFactoryService.DEFAULT));
        }

        String defaultChannel = ModelNodes.asString(JGroupsSubsystemResourceDefinition.DEFAULT_CHANNEL.resolveModelAttribute(context, model));
        if ((defaultChannel != null) && !defaultChannel.equals(ChannelService.DEFAULT)) {
            context.removeService(ChannelService.getServiceName(ChannelService.DEFAULT));
            context.removeService(ConnectedChannelService.getServiceName(ChannelService.DEFAULT));
            context.removeService(ChannelService.getFactoryServiceName(ChannelService.DEFAULT));

            for (GroupServiceInstaller installer : ServiceLoader.load(ClusteredGroupServiceInstaller.class, ClusteredGroupServiceInstaller.class.getClassLoader())) {
                for (ServiceName name : installer.getServiceNames(ChannelService.DEFAULT)) {
                    context.removeService(name);
                }
            }
        }
    }

    @Override
    protected void recoverServices(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        JGroupsSubsystemAddHandler.installRuntimeServices(context, operation, model, new ServiceVerificationHandler());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4559.java