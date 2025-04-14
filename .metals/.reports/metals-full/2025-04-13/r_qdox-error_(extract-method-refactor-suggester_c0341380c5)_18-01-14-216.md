error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/215.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/215.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/215.java
text:
```scala
r@@unningModeControl.setReloaded();

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.domain.controller.operations.deployment;

import java.util.Locale;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationDefinition;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleOperationDefinition;
import org.jboss.as.controller.SimpleOperationDefinitionBuilder;
import org.jboss.as.controller.descriptions.DefaultOperationDescriptionProvider;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.ProcessReloadHandler;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.domain.controller.LocalHostControllerInfo;
import org.jboss.as.host.controller.HostModelUtil;
import org.jboss.as.host.controller.HostRunningModeControl;
import org.jboss.as.host.controller.RestartMode;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.msc.service.ServiceName;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class HostProcessReloadHandler extends ProcessReloadHandler<HostRunningModeControl>{

    private static final AttributeDefinition RESTART_SERVERS = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.RESTART_SERVERS, ModelType.BOOLEAN, true)
    .setDefaultValue(new ModelNode(true)).build();

    private static final AttributeDefinition USE_CURRENT_DOMAIN_CONFIG = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.USE_CURRENT_DOMAIN_CONFIG, ModelType.BOOLEAN, true)
    .setDefaultValue(new ModelNode(true)).build();

    private static final AttributeDefinition USE_CURRENT_HOST_CONFIG = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.USE_CURRENT_HOST_CONFIG, ModelType.BOOLEAN, true)
    .setDefaultValue(new ModelNode(true)).build();


    private static final AttributeDefinition[] MASTER_ATTRIBUTES = new AttributeDefinition[] {ADMIN_ONLY, RESTART_SERVERS, USE_CURRENT_DOMAIN_CONFIG, USE_CURRENT_HOST_CONFIG};

    private static final AttributeDefinition[] SLAVE_ATTRIBUTES = new AttributeDefinition[] {ADMIN_ONLY, RESTART_SERVERS, USE_CURRENT_HOST_CONFIG};

    public static OperationDefinition getDefinition(final LocalHostControllerInfo hostControllerInfo) {
        return new DeferredParametersOperationDefinitionBuilder(hostControllerInfo, OPERATION_NAME, HostModelUtil.getResourceDescriptionResolver())
            .setParameters(hostControllerInfo.isMasterDomainController() ? MASTER_ATTRIBUTES : SLAVE_ATTRIBUTES)
            .withFlag(OperationEntry.Flag.HOST_CONTROLLER_ONLY)
            .build();
    }

    public HostProcessReloadHandler(final ServiceName rootService, final HostRunningModeControl runningModeControl, final ControlledProcessState processState) {
        super(rootService, runningModeControl, processState);
    }

    @Override
    protected ProcessReloadHandler.ReloadContext<HostRunningModeControl> initializeReloadContext(final OperationContext context, final ModelNode operation) throws OperationFailedException {
        final boolean adminOnly = ADMIN_ONLY.resolveModelAttribute(context, operation).asBoolean(false);
        final boolean restartServers = RESTART_SERVERS.resolveModelAttribute(context, operation).asBoolean(true);
        final boolean useCurrentHostConfig = USE_CURRENT_HOST_CONFIG.resolveModelAttribute(context, operation).asBoolean(true);
        final boolean useCurrentDomainConfig = USE_CURRENT_DOMAIN_CONFIG.resolveModelAttribute(context, operation).asBoolean(true);

        return new ReloadContext<HostRunningModeControl>() {

            @Override
            public void reloadInitiated(HostRunningModeControl runningModeControl) {
                runningModeControl.setRestartMode(restartServers ? RestartMode.SERVERS : RestartMode.HC_ONLY);
            }

            @Override
            public void doReload(HostRunningModeControl runningModeControl) {
                runningModeControl.setRunningMode(adminOnly ? RunningMode.ADMIN_ONLY : RunningMode.NORMAL);
                runningModeControl.setReloaded(true);
                runningModeControl.setUseCurrentConfig(useCurrentHostConfig);
                runningModeControl.setUseCurrentDomainConfig(useCurrentDomainConfig);
            }
        };
    }

    /**
     * The host controller info does not know if it is master or not until later in the bootup process
     */
    private static class DeferredParametersOperationDefinitionBuilder extends SimpleOperationDefinitionBuilder {
        private final LocalHostControllerInfo hostControllerInfo;

        public DeferredParametersOperationDefinitionBuilder(LocalHostControllerInfo hostControllerInfo, String name, ResourceDescriptionResolver resolver) {
            super(name, resolver);
            this.hostControllerInfo = hostControllerInfo;
        }

        @Override
        public SimpleOperationDefinition internalBuild(final ResourceDescriptionResolver resolver, final ResourceDescriptionResolver attributeResolver) {
            return new SimpleOperationDefinition(name, resolver, attributeResolver, entryType, flags, replyType, replyValueType, false, deprecationData, replyParameters, parameters) {
                @Override
                public DescriptionProvider getDescriptionProvider() {
                    return new DescriptionProvider() {
                        @Override
                        public ModelNode getModelDescription(Locale locale) {
                            AttributeDefinition[] params = hostControllerInfo.isMasterDomainController() ? MASTER_ATTRIBUTES : SLAVE_ATTRIBUTES;
                            return new DefaultOperationDescriptionProvider(getName(), resolver, attributeResolver, replyType, replyValueType, deprecationData, replyParameters, params).getModelDescription(locale);
                        }
                    };
                }
            };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/215.java