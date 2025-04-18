error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7210.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7210.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7210.java
text:
```scala
.@@setElementValidator(new StringLengthValidator(1))

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

package org.jboss.as.ejb3.subsystem.deployment;

import java.util.Map;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ListAttributeDefinition;
import org.jboss.as.controller.ObjectTypeAttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PrimitiveListAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.ejb3.component.EJBComponent;
import org.jboss.as.ejb3.component.invocationmetrics.InvocationMetrics;
import org.jboss.as.ejb3.component.stateful.StatefulSessionComponent;
import org.jboss.as.ejb3.subsystem.EJB3Extension;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Base class for {@link org.jboss.as.controller.ResourceDefinition}s describing runtime {@link EJBComponent}s.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public abstract class AbstractEJBComponentResourceDefinition extends SimpleResourceDefinition {

    public static final SimpleAttributeDefinition COMPONENT_CLASS_NAME = new SimpleAttributeDefinitionBuilder("component-class-name", ModelType.STRING)
            .setValidator(new StringLengthValidator(1))
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    private static final AttributeDefinition EXECUTION_TIME = new SimpleAttributeDefinitionBuilder("execution-time", ModelType.LONG)
            .setAllowNull(false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    private static final AttributeDefinition INVOCATIONS = new SimpleAttributeDefinitionBuilder("invocations", ModelType.LONG)
            .setAllowNull(false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    private static final AttributeDefinition PEAK_CONCURRENT_INVOCATIONS = new SimpleAttributeDefinitionBuilder("peak-concurrent-invocations", ModelType.LONG)
            .setAllowNull(false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    public static final SimpleAttributeDefinition SECURITY_DOMAIN = new SimpleAttributeDefinitionBuilder("security-domain", ModelType.STRING, true)
            .setValidator(new StringLengthValidator(1, true))
            .build();

    private static final AttributeDefinition WAIT_TIME = new SimpleAttributeDefinitionBuilder("wait-time", ModelType.LONG)
            .setAllowNull(false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    private static final AttributeDefinition METHODS = ObjectTypeAttributeDefinition.Builder.of("methods", EXECUTION_TIME, INVOCATIONS, WAIT_TIME)
            .setAllowNull(false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    public static final SimpleAttributeDefinition RUN_AS_ROLE = new SimpleAttributeDefinitionBuilder("run-as-role", ModelType.STRING, true)
            .setValidator(new StringLengthValidator(1, true))
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    public static final ListAttributeDefinition DECLARED_ROLES = new PrimitiveListAttributeDefinition.Builder("declared-roles", ModelType.STRING)
            .setAllowNull(true)
            .setValidator(new StringLengthValidator(1))
            .setStorageRuntime()
            .build();

    private static final AttributeDefinition CACHE_SIZE = new SimpleAttributeDefinitionBuilder("cache-size", ModelType.LONG)
            .setAllowNull(false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    private static final AttributeDefinition PASSIVATED_SIZE = new SimpleAttributeDefinitionBuilder("passivated-count",
            ModelType.LONG).setAllowNull(false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    private static final AttributeDefinition TOTAL_SIZE = new SimpleAttributeDefinitionBuilder("total-size", ModelType.LONG)
            .setAllowNull(false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    // Pool attributes

    public static final SimpleAttributeDefinition POOL_AVAILABLE_COUNT = new SimpleAttributeDefinitionBuilder("pool-available-count", ModelType.INT, false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();
    public static final SimpleAttributeDefinition POOL_CREATE_COUNT = new SimpleAttributeDefinitionBuilder("pool-create-count", ModelType.INT, false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final SimpleAttributeDefinition POOL_CURRENT_SIZE = new SimpleAttributeDefinitionBuilder("pool-current-size", ModelType.INT, false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final SimpleAttributeDefinition POOL_NAME = new SimpleAttributeDefinitionBuilder("pool-name", ModelType.STRING, true)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final SimpleAttributeDefinition POOL_REMOVE_COUNT = new SimpleAttributeDefinitionBuilder("pool-remove-count", ModelType.INT, false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final SimpleAttributeDefinition POOL_MAX_SIZE = new SimpleAttributeDefinitionBuilder("pool-max-size", ModelType.INT, false)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();

    private final EJBComponentType componentType;

    public AbstractEJBComponentResourceDefinition(final EJBComponentType componentType) {
        super(PathElement.pathElement(componentType.getResourceType()),
                EJB3Extension.getResourceDescriptionResolver(componentType.getResourceType()));
        this.componentType = componentType;
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        final AbstractEJBComponentRuntimeHandler<?> handler = componentType.getRuntimeHandler();
        resourceRegistration.registerReadOnlyAttribute(COMPONENT_CLASS_NAME, handler);
        resourceRegistration.registerReadOnlyAttribute(SECURITY_DOMAIN, handler);
        resourceRegistration.registerReadOnlyAttribute(RUN_AS_ROLE, handler);
        resourceRegistration.registerReadOnlyAttribute(DECLARED_ROLES, handler);
        if (componentType.hasTimer()) {
            resourceRegistration.registerReadOnlyAttribute(TimerAttributeDefinition.INSTANCE, handler);
        }

        if (componentType.hasPool()) {
            resourceRegistration.registerReadOnlyAttribute(POOL_AVAILABLE_COUNT, handler);
            resourceRegistration.registerReadOnlyAttribute(POOL_CREATE_COUNT, handler);
            resourceRegistration.registerReadOnlyAttribute(POOL_NAME, handler);
            resourceRegistration.registerReadOnlyAttribute(POOL_REMOVE_COUNT, handler);
            resourceRegistration.registerReadOnlyAttribute(POOL_CURRENT_SIZE, handler);
            resourceRegistration.registerReadWriteAttribute(POOL_MAX_SIZE, handler, handler);
        }

        if (componentType.equals(EJBComponentType.STATEFUL)) {
            resourceRegistration.registerMetric(CACHE_SIZE, new AbstractRuntimeMetricsHandler() {
                @Override
                protected void executeReadMetricStep(final OperationContext context, final ModelNode operation, final EJBComponent component) throws OperationFailedException {
                    context.getResult().set(((StatefulSessionComponent)component).getCache().getCacheSize());
                }
            });
            resourceRegistration.registerMetric(PASSIVATED_SIZE, new AbstractRuntimeMetricsHandler() {
                @Override
                protected void executeReadMetricStep(final OperationContext context, final ModelNode operation, final EJBComponent component) throws OperationFailedException {
                    context.getResult().set(((StatefulSessionComponent)component).getCache().getPassivatedCount());
                }
            });
            resourceRegistration.registerMetric(TOTAL_SIZE, new AbstractRuntimeMetricsHandler() {
                @Override
                protected void executeReadMetricStep(final OperationContext context, final ModelNode operation, final EJBComponent component) throws OperationFailedException {
                    context.getResult().set(((StatefulSessionComponent)component).getCache().getTotalSize());
                }
            });
        }

        resourceRegistration.registerMetric(EXECUTION_TIME, new AbstractRuntimeMetricsHandler() {
            @Override
            protected void executeReadMetricStep(final OperationContext context, final ModelNode operation, final EJBComponent component) throws OperationFailedException {
                context.getResult().set(component.getInvocationMetrics().getExecutionTime());
            }
        });
        resourceRegistration.registerMetric(INVOCATIONS, new AbstractRuntimeMetricsHandler() {
            @Override
            protected void executeReadMetricStep(final OperationContext context, final ModelNode operation, final EJBComponent component) throws OperationFailedException {
                context.getResult().set(component.getInvocationMetrics().getInvocations());
            }
        });
        resourceRegistration.registerMetric(PEAK_CONCURRENT_INVOCATIONS, new AbstractRuntimeMetricsHandler() {
            @Override
            protected void executeReadMetricStep(final OperationContext context, final ModelNode operation, final EJBComponent component) throws OperationFailedException {
                context.getResult().set(component.getInvocationMetrics().getPeakConcurrent());
            }
        });
        resourceRegistration.registerMetric(WAIT_TIME, new AbstractRuntimeMetricsHandler() {
            @Override
            protected void executeReadMetricStep(final OperationContext context, final ModelNode operation, final EJBComponent component) throws OperationFailedException {
                context.getResult().set(component.getInvocationMetrics().getWaitTime());
            }
        });
        resourceRegistration.registerMetric(METHODS, new AbstractRuntimeMetricsHandler() {
            @Override
            protected void executeReadMetricStep(final OperationContext context, final ModelNode operation, final EJBComponent component) throws OperationFailedException {
                context.getResult().setEmptyObject();
                for (final Map.Entry<String, InvocationMetrics.Values> entry : component.getInvocationMetrics().getMethods().entrySet()) {
                    final InvocationMetrics.Values values = entry.getValue();
                    final ModelNode result = new ModelNode();
                    result.get("execution-time").set(values.getExecutionTime());
                    result.get("invocations").set(values.getInvocations());
                    result.get("wait-time").set(values.getWaitTime());
                    context.getResult().get(entry.getKey()).set(result);
                }
            }
        });
    }

    /* (non-Javadoc)
     * @see org.jboss.as.controller.SimpleResourceDefinition#registerChildren(org.jboss.as.controller.registry.ManagementResourceRegistration)
     */
    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {
        super.registerChildren(resourceRegistration);
        // /deployment=DU/**/subsystem=ejb3/*=EJBName/service=timer-service
        final AbstractEJBComponentRuntimeHandler<?> handler = componentType.getRuntimeHandler();
        resourceRegistration.registerSubModel(new TimerServiceResourceDefinition(handler));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7210.java