error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2481.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2481.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2481.java
text:
```scala
public static final A@@ttributeDefinition[] ATTRIBUTES_WITH_EXPRESSION_ALLOWED_IN_1_2_0 = { CommonAttributes.DESTINATION_ENTRIES, CommonAttributes.SELECTOR, CommonAttributes.DURABLE};

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.messaging.jms;

import static org.jboss.as.controller.SimpleAttributeDefinitionBuilder.create;
import static org.jboss.as.messaging.CommonAttributes.PAUSED;
import static org.jboss.as.messaging.CommonAttributes.TEMPORARY;
import static org.jboss.dmr.ModelType.STRING;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PrimitiveListAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleOperationDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.messaging.CommonAttributes;
import org.jboss.as.messaging.MessagingExtension;

/**
 * JMS Queue resource definition
 *
 * @author <a href="http://jmesnil.net">Jeff Mesnil</a> (c) 2012 Red Hat Inc.
 */
public class JMSQueueDefinition extends SimpleResourceDefinition {

    public static final PathElement PATH = PathElement.pathElement(CommonAttributes.JMS_QUEUE);

    public static final AttributeDefinition[] ATTRIBUTES = { CommonAttributes.DESTINATION_ENTRIES, CommonAttributes.SELECTOR, CommonAttributes.DURABLE };

    public static final AttributeDefinition[] ATTRIBUTES_WITH_EXPRESSION_AFTER_1_1_0 = { CommonAttributes.DESTINATION_ENTRIES, CommonAttributes.SELECTOR, CommonAttributes.DURABLE};

    /**
     * Attributes for deployed JMS queue are stored in runtime
     */
    private static AttributeDefinition[] getDeploymentAttributes() {
        return new AttributeDefinition[] {
                new PrimitiveListAttributeDefinition.Builder(CommonAttributes.DESTINATION_ENTRIES).setStorageRuntime().build(),
                SimpleAttributeDefinitionBuilder.create(CommonAttributes.SELECTOR).setStorageRuntime().build(),
                SimpleAttributeDefinitionBuilder.create(CommonAttributes.DURABLE).setStorageRuntime().build()
        };
    }

    static final AttributeDefinition QUEUE_ADDRESS = create("queue-address", STRING)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition DEAD_LETTER_ADDRESS = create("dead-letter-address", STRING)
            .setAllowNull(true)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition EXPIRY_ADDRESS = create("expiry-address", STRING)
            .setAllowNull(true)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition[] READONLY_ATTRIBUTES = { QUEUE_ADDRESS,
        EXPIRY_ADDRESS, DEAD_LETTER_ADDRESS, PAUSED, TEMPORARY  };

    static final AttributeDefinition[] METRICS = { CommonAttributes.MESSAGE_COUNT, CommonAttributes.DELIVERING_COUNT, CommonAttributes.MESSAGES_ADDED,
            CommonAttributes.SCHEDULED_COUNT, CommonAttributes.CONSUMER_COUNT
            };

    private final boolean registerRuntimeOnly;

    private final boolean deployed;

    public static JMSQueueDefinition newDeployedJMSQueueDefinition() {
        return new JMSQueueDefinition(true, true, null, null);
    }

    public JMSQueueDefinition(final boolean registerRuntimeOnly) {
        this(registerRuntimeOnly, false, JMSQueueAdd.INSTANCE, JMSQueueRemove.INSTANCE);
    }

    private JMSQueueDefinition(final boolean registerRuntimeOnly, final boolean deployed, final OperationStepHandler addHandler, final OperationStepHandler removeHandler) {
        super(PATH,
                MessagingExtension.getResourceDescriptionResolver(CommonAttributes.JMS_QUEUE),
                addHandler,
                removeHandler);
        this.registerRuntimeOnly = registerRuntimeOnly;
        this.deployed = deployed;
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration registry) {
        super.registerAttributes(registry);

        AttributeDefinition[] attributes = deployed ? getDeploymentAttributes() : ATTRIBUTES;
        for (AttributeDefinition attr : attributes) {
            if (registerRuntimeOnly || !attr.getFlags().contains(AttributeAccess.Flag.STORAGE_RUNTIME)) {
                if (deployed) {
                    registry.registerReadOnlyAttribute(attr, JMSQueueConfigurationRuntimeHandler.INSTANCE);
                } else {
                    registry.registerReadWriteAttribute(attr, null, JMSQueueConfigurationWriteHandler.INSTANCE);
                }
            }
        }

        if (registerRuntimeOnly) {
            for (AttributeDefinition attr : READONLY_ATTRIBUTES) {
                registry.registerReadOnlyAttribute(attr, JMSQueueReadAttributeHandler.INSTANCE);
            }

            for (AttributeDefinition metric : METRICS) {
                registry.registerMetric(metric, JMSQueueReadAttributeHandler.INSTANCE);
            }
        }
    }


    @Override
    public void registerOperations(ManagementResourceRegistration registry) {
        super.registerOperations(registry);

        if (registerRuntimeOnly) {
            JMSQueueControlHandler.INSTANCE.registerOperations(registry);

            if (!deployed) {
                SimpleOperationDefinition op = new SimpleOperationDefinition(ConnectionFactoryAddJndiHandler.ADD_JNDI,
                        getResourceDescriptionResolver(),
                        ConnectionFactoryAddJndiHandler.JNDI_BINDING);
                registry.registerOperationHandler(op, JMSQueueAddJndiHandler.INSTANCE);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2481.java