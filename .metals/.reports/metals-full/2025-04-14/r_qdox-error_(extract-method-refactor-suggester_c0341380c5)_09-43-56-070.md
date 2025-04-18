error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2332.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2332.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2332.java
text:
```scala
static final A@@ttributeDefinition[] ATTRIBUTES = { CommonAttributes.DESTINATION_ENTRIES };

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
import static org.jboss.dmr.ModelType.INT;
import static org.jboss.dmr.ModelType.STRING;

import java.util.EnumSet;
import java.util.Locale;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleOperationDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.messaging.CommonAttributes;
import org.jboss.as.messaging.MessagingDescriptions;
import org.jboss.as.messaging.MessagingExtension;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;


/**
 * JMS Topic resource definition
 *
 * @author <a href="http://jmesnil.net">Jeff Mesnil</a> (c) 2012 Red Hat Inc.
 */
public class JMSTopicDefinition extends SimpleResourceDefinition {

    static final AttributeDefinition[] ATTRIBUTES = { JndiEntriesAttribute.DESTINATION };

    static final AttributeDefinition TOPIC_ADDRESS = create(CommonAttributes.TOPIC_ADDRESS, STRING)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition[] READONLY_ATTRIBUTES = { TOPIC_ADDRESS, CommonAttributes.TEMPORARY };

    static final AttributeDefinition DURABLE_MESSAGE_COUNT = create(CommonAttributes.DURABLE_MESSAGE_COUNT, INT)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition NON_DURABLE_MESSAGE_COUNT = create(CommonAttributes.NON_DURABLE_MESSAGE_COUNT, INT)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition SUBSCRIPTION_COUNT = create(CommonAttributes.SUBSCRIPTION_COUNT, INT)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition DURABLE_SUBSCRIPTION_COUNT = create(CommonAttributes.DURABLE_SUBSCRIPTION_COUNT, INT)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition NON_DURABLE_SUBSCRIPTION_COUNT = create(CommonAttributes.NON_DURABLE_SUBSCRIPTION_COUNT, INT)
            .setStorageRuntime()
            .build();

    static final AttributeDefinition[] METRICS = { CommonAttributes.DELIVERING_COUNT, CommonAttributes.MESSAGES_ADDED,
        CommonAttributes.MESSAGE_COUNT, DURABLE_MESSAGE_COUNT, NON_DURABLE_MESSAGE_COUNT,
        SUBSCRIPTION_COUNT, DURABLE_SUBSCRIPTION_COUNT, NON_DURABLE_SUBSCRIPTION_COUNT};

    public static final String REMOVE_MESSAGES = "remove-messages";

    public static final String DROP_ALL_SUBSCRIPTIONS = "drop-all-subscriptions";

    public static final String DROP_DURABLE_SUBSCRIPTION = "drop-durable-subscription";

    public static final String SUBSCRIPTION_NAME = "subscription-name";

    public static final String COUNT_MESSAGES_FOR_SUBSCRIPTION = "count-messages-for-subscription";

    public static final String LIST_MESSAGES_FOR_SUBSCRIPTION_AS_JSON = "list-messages-for-subscription-as-json";

    public static final String LIST_MESSAGES_FOR_SUBSCRIPTION = "list-messages-for-subscription";

    public static final String LIST_NON_DURABLE_SUBSCRIPTIONS_AS_JSON = "list-non-durable-subscriptions-as-json";

    public static final String LIST_NON_DURABLE_SUBSCRIPTIONS = "list-non-durable-subscriptions";

    public static final String LIST_DURABLE_SUBSCRIPTIONS_AS_JSON = "list-durable-subscriptions-as-json";

    public static final String LIST_DURABLE_SUBSCRIPTIONS = "list-durable-subscriptions";

    public static final String LIST_ALL_SUBSCRIPTIONS_AS_JSON = "list-all-subscriptions-as-json";

    public static final String LIST_ALL_SUBSCRIPTIONS = "list-all-subscriptions";

    private final boolean registerRuntimeOnly;

    private final boolean deployed;

    public static JMSTopicDefinition newDeployedJMSTopicDefinition() {
        return new JMSTopicDefinition(true, true, null, null);
    }

    public JMSTopicDefinition(final boolean registerRuntimeOnly) {
        this(registerRuntimeOnly, false, JMSTopicAdd.INSTANCE, JMSTopicRemove.INSTANCE);
    }

    private JMSTopicDefinition(final boolean registerRuntimeOnly, final boolean deployed, final OperationStepHandler addHandler, final OperationStepHandler removeHandler) {
        super(PathElement.pathElement(CommonAttributes.JMS_TOPIC),
                MessagingExtension.getResourceDescriptionResolver(CommonAttributes.JMS_TOPIC),
                addHandler,
                removeHandler);
        this.registerRuntimeOnly = registerRuntimeOnly;
        this.deployed = deployed;
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration registry) {
        super.registerAttributes(registry);

        for (AttributeDefinition attr : ATTRIBUTES) {
            if (registerRuntimeOnly || !attr.getFlags().contains(AttributeAccess.Flag.STORAGE_RUNTIME)) {
                if (deployed) {
                    registry.registerReadOnlyAttribute(attr, JMSTopicConfigurationRuntimeHandler.INSTANCE);
                } else {
                    registry.registerReadWriteAttribute(attr, null, JMSTopicConfigurationWriteHandler.INSTANCE);
                }
            }
        }

        if (registerRuntimeOnly) {
            for (AttributeDefinition attr : READONLY_ATTRIBUTES) {
                registry.registerReadOnlyAttribute(attr, JMSTopicReadAttributeHandler.INSTANCE);
            }

            for (AttributeDefinition metric : METRICS) {
                registry.registerMetric(metric, JMSTopicReadAttributeHandler.INSTANCE);
            }
        }
    }

    @Override
    public void registerOperations(ManagementResourceRegistration registry) {
        super.registerOperations(registry);

        if (registerRuntimeOnly && !deployed) {
            SimpleOperationDefinition op = new SimpleOperationDefinition(ConnectionFactoryAddJndiHandler.ADD_JNDI,
                    getResourceDescriptionResolver(),
                    ConnectionFactoryAddJndiHandler.JNDI_BINDING);
            registry.registerOperationHandler(op,JMSTopicAddJndiHandler.INSTANCE);
        }

        final EnumSet<OperationEntry.Flag> readOnly = EnumSet.of(OperationEntry.Flag.READ_ONLY, OperationEntry.Flag.RUNTIME_ONLY);
        final EnumSet<OperationEntry.Flag> runtimeOnly = EnumSet.of(OperationEntry.Flag.RUNTIME_ONLY);

        registry.registerOperationHandler(LIST_ALL_SUBSCRIPTIONS, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListSubscriptionsOperation(locale,  LIST_ALL_SUBSCRIPTIONS);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_ALL_SUBSCRIPTIONS_AS_JSON, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getNoArgSimpleReplyOperation(locale, LIST_ALL_SUBSCRIPTIONS_AS_JSON,
                        CommonAttributes.JMS_TOPIC, ModelType.STRING, false);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_DURABLE_SUBSCRIPTIONS, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListSubscriptionsOperation(locale,  LIST_DURABLE_SUBSCRIPTIONS);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_DURABLE_SUBSCRIPTIONS_AS_JSON, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getNoArgSimpleReplyOperation(locale, LIST_DURABLE_SUBSCRIPTIONS_AS_JSON,
                        CommonAttributes.JMS_TOPIC, STRING, false);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_NON_DURABLE_SUBSCRIPTIONS, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListSubscriptionsOperation(locale,  LIST_NON_DURABLE_SUBSCRIPTIONS);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_NON_DURABLE_SUBSCRIPTIONS_AS_JSON, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getNoArgSimpleReplyOperation(locale, LIST_NON_DURABLE_SUBSCRIPTIONS_AS_JSON,
                        CommonAttributes.JMS_TOPIC, STRING, false);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_MESSAGES_FOR_SUBSCRIPTION, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListMessagesForSubscription(locale);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_MESSAGES_FOR_SUBSCRIPTION_AS_JSON, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListMessagesForSubscriptionAsJSON(locale);
            }
        }, readOnly);

        registry.registerOperationHandler(COUNT_MESSAGES_FOR_SUBSCRIPTION, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getCountMessagesForSubscription(locale);
            }
        }, readOnly);

        registry.registerOperationHandler(DROP_DURABLE_SUBSCRIPTION, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getDropDurableSubscription(locale);
            }
        }, runtimeOnly);

        registry.registerOperationHandler(DROP_ALL_SUBSCRIPTIONS, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getDescriptionOnlyOperation(locale, DROP_ALL_SUBSCRIPTIONS, CommonAttributes.JMS_TOPIC);
            }
        }, runtimeOnly);

        registry.registerOperationHandler(REMOVE_MESSAGES, JMSTopicControlHandler.INSTANCE, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getRemoveMessages(locale);
            }
        }, runtimeOnly);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2332.java