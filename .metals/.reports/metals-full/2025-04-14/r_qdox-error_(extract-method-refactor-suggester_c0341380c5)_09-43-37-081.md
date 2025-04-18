error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13525.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13525.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13525.java
text:
```scala
private static final S@@tring TOPIC = "jms-topic";

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

package org.jboss.as.messaging.jms;

import static org.jboss.as.messaging.CommonAttributes.*;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;

import java.util.EnumSet;
import java.util.Locale;

import org.hornetq.api.core.management.ResourceNames;
import org.hornetq.api.jms.management.TopicControl;
import org.hornetq.core.server.HornetQServer;
import org.jboss.as.controller.AbstractRuntimeOnlyHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.validation.ModelTypeValidator;
import org.jboss.as.controller.operations.validation.ParametersValidator;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.messaging.MessagingDescriptions;
import org.jboss.as.messaging.MessagingServices;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;
import org.jboss.msc.service.ServiceController;

/**
 * Handler for runtime operations that invoke on a HornetQ {@link org.hornetq.api.jms.management.TopicControl}.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class JMSTopicControlHandler extends AbstractRuntimeOnlyHandler {

    public static final JMSTopicControlHandler INSTANCE = new JMSTopicControlHandler();

    public static final String LIST_ALL_SUBSCRIPTIONS = "list-all-subscriptions";
    public static final String LIST_ALL_SUBSCRIPTIONS_AS_JSON = "list-all-subscriptions-as-json";
    public static final String LIST_DURABLE_SUBSCRIPTIONS = "list-durable-subscriptions";
    public static final String LIST_DURABLE_SUBSCRIPTIONS_AS_JSON = "list-durable-subscriptions-as-json";
    public static final String LIST_NON_DURABLE_SUBSCRIPTIONS = "list-non-durable-subscriptions";
    public static final String LIST_NON_DURABLE_SUBSCRIPTIONS_AS_JSON = "list-non-durable-subscriptions-as-json";
    public static final String LIST_MESSAGES_FOR_SUBSCRIPTION = "list-messages-for-subscription";
    public static final String LIST_MESSAGES_FOR_SUBSCRIPTION_AS_JSON = "list-messages-for-subscription-as-json";
    public static final String COUNT_MESSAGES_FOR_SUBSCRIPTION = "count-messages-for-subscription";
    public static final String SUBSCRIPTION_NAME = "subscription-name";
    public static final String DROP_DURABLE_SUBSCRIPTION = "drop-durable-subscription";
    public static final String DROP_ALL_SUBSCRIPTIONS = "drop-all-subscriptions";
    public static final String REMOVE_MESSAGES = "remove-messages";

    private static final String TOPIC = "topic";

    private final ParametersValidator listMessagesForSubscriptionValidator = new ParametersValidator();
    private final ParametersValidator countMessagesForSubscriptionValidator = new ParametersValidator();
    private final ParametersValidator dropDurableSubscriptionValidator = new ParametersValidator();
    private final ParametersValidator removeMessagesValidator = new ParametersValidator();

    private JMSTopicControlHandler() {
        listMessagesForSubscriptionValidator.registerValidator(QUEUE_NAME.getName(), new StringLengthValidator(1));

        countMessagesForSubscriptionValidator.registerValidator(CLIENT_ID.getName(), new StringLengthValidator(1));
        countMessagesForSubscriptionValidator.registerValidator(SUBSCRIPTION_NAME, new StringLengthValidator(1));
        countMessagesForSubscriptionValidator.registerValidator(FILTER.getName(), new ModelTypeValidator(ModelType.STRING, true, false));

        dropDurableSubscriptionValidator.registerValidator(CLIENT_ID.getName(), new StringLengthValidator(1));
        dropDurableSubscriptionValidator.registerValidator(SUBSCRIPTION_NAME, new StringLengthValidator(1));

        removeMessagesValidator.registerValidator(FILTER.getName(), new ModelTypeValidator(ModelType.STRING, true, false));
    }

    public void registerOperations(final ManagementResourceRegistration registry) {

        final EnumSet<OperationEntry.Flag> readOnly = EnumSet.of(OperationEntry.Flag.READ_ONLY);

        registry.registerOperationHandler(LIST_ALL_SUBSCRIPTIONS, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListSubscriptionsOperation(locale,  LIST_ALL_SUBSCRIPTIONS);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_ALL_SUBSCRIPTIONS_AS_JSON, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getNoArgSimpleReplyOperation(locale, LIST_ALL_SUBSCRIPTIONS_AS_JSON,
                        TOPIC, ModelType.STRING, false);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_DURABLE_SUBSCRIPTIONS, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListSubscriptionsOperation(locale,  LIST_DURABLE_SUBSCRIPTIONS);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_DURABLE_SUBSCRIPTIONS_AS_JSON, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getNoArgSimpleReplyOperation(locale, LIST_DURABLE_SUBSCRIPTIONS_AS_JSON,
                        TOPIC, ModelType.STRING, false);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_NON_DURABLE_SUBSCRIPTIONS, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListSubscriptionsOperation(locale,  LIST_NON_DURABLE_SUBSCRIPTIONS);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_NON_DURABLE_SUBSCRIPTIONS_AS_JSON, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getNoArgSimpleReplyOperation(locale, LIST_NON_DURABLE_SUBSCRIPTIONS_AS_JSON,
                        TOPIC, ModelType.STRING, false);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_MESSAGES_FOR_SUBSCRIPTION, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListMessagesForSubscription(locale);
            }
        }, readOnly);

        registry.registerOperationHandler(LIST_MESSAGES_FOR_SUBSCRIPTION_AS_JSON, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getListMessagesForSubscriptionAsJSON(locale);
            }
        }, readOnly);

        registry.registerOperationHandler(COUNT_MESSAGES_FOR_SUBSCRIPTION, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getCountMessagesForSubscription(locale);
            }
        }, readOnly);

        registry.registerOperationHandler(DROP_DURABLE_SUBSCRIPTION, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getDropDurableSubscription(locale);
            }
        });

        registry.registerOperationHandler(DROP_ALL_SUBSCRIPTIONS, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getDescriptionOnlyOperation(locale,  DROP_ALL_SUBSCRIPTIONS, TOPIC);
            }
        });

        registry.registerOperationHandler(REMOVE_MESSAGES, this, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return MessagingDescriptions.getRemoveMessages(locale);
            }
        });
    }

    @Override
    protected void executeRuntimeStep(OperationContext context, ModelNode operation) throws OperationFailedException {

        final String operationName = operation.require(ModelDescriptionConstants.OP).asString();
        final String topicName = PathAddress.pathAddress(operation.require(ModelDescriptionConstants.OP_ADDR)).getLastElement().getValue();
        ServiceController<?> hqService = context.getServiceRegistry(false).getService(MessagingServices.JBOSS_MESSAGING);
        HornetQServer hqServer = HornetQServer.class.cast(hqService.getValue());
        TopicControl control = TopicControl.class.cast(hqServer.getManagementService().getResource(ResourceNames.JMS_TOPIC + topicName));

        try {
            if (LIST_ALL_SUBSCRIPTIONS.equals(operationName)) {
                String json = control.listAllSubscriptionsAsJSON();
                ModelNode jsonAsNode = ModelNode.fromJSONString(json);
                context.getResult().set(jsonAsNode);
            } else if (LIST_ALL_SUBSCRIPTIONS_AS_JSON.equals(operationName)) {
                context.getResult().set(control.listAllSubscriptionsAsJSON());
            } else if (LIST_DURABLE_SUBSCRIPTIONS.equals(operationName)) {
                String json = control.listDurableSubscriptionsAsJSON();
                ModelNode jsonAsNode = ModelNode.fromJSONString(json);
                context.getResult().set(jsonAsNode);
            } else if (LIST_DURABLE_SUBSCRIPTIONS_AS_JSON.equals(operationName)) {
                context.getResult().set(control.listDurableSubscriptionsAsJSON());
            } else if (LIST_NON_DURABLE_SUBSCRIPTIONS.equals(operationName)) {
                String json = control.listNonDurableSubscriptionsAsJSON();
                ModelNode jsonAsNode = ModelNode.fromJSONString(json);
                context.getResult().set(jsonAsNode);
            } else if (LIST_NON_DURABLE_SUBSCRIPTIONS_AS_JSON.equals(operationName)) {
                context.getResult().set(control.listNonDurableSubscriptionsAsJSON());
            } else if (LIST_MESSAGES_FOR_SUBSCRIPTION.equals(operationName)) {
                listMessagesForSubscriptionValidator.validate(operation);
                final String queueName = operation.require(QUEUE_NAME.getName()).asString();
                String json = control.listMessagesForSubscriptionAsJSON(queueName);
                context.getResult().set(ModelNode.fromJSONString(json));
            } else if (LIST_MESSAGES_FOR_SUBSCRIPTION_AS_JSON.equals(operationName)) {
                final String queueName = operation.require(QUEUE_NAME.getName()).asString();
                context.getResult().set(control.listMessagesForSubscriptionAsJSON(queueName));
            } else if (COUNT_MESSAGES_FOR_SUBSCRIPTION.equals(operationName)) {
                countMessagesForSubscriptionValidator.validate(operation);
                String clientId = operation.require(CLIENT_ID.getName()).asString();
                String subscriptionName = operation.require(SUBSCRIPTION_NAME).asString();
                String filter = operation.hasDefined(FILTER.getName()) ? operation.get(FILTER.getName()).asString() : null;
                context.getResult().set(control.countMessagesForSubscription(clientId, subscriptionName, filter));
            } else if (DROP_DURABLE_SUBSCRIPTION.equals(operationName)) {
                dropDurableSubscriptionValidator.validate(operation);
                String clientId = operation.require(CLIENT_ID.getName()).asString();
                String subscriptionName = operation.require(SUBSCRIPTION_NAME).asString();
                control.dropDurableSubscription(clientId, subscriptionName);
                context.getResult();
            } else if (DROP_ALL_SUBSCRIPTIONS.equals(operationName)) {
                control.dropAllSubscriptions();
                context.getResult();
            } else if (REMOVE_MESSAGES.equals(operationName)) {
                removeMessagesValidator.validate(operation);
                String filter = operation.hasDefined(FILTER.getName()) ? operation.get(FILTER.getName()).asString() : null;
                context.getResult().set(control.removeMessages(filter));
            } else {
                // Bug
                throw MESSAGES.unsupportedOperation(operationName);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            context.getFailureDescription().set(e.getLocalizedMessage());
        }

        context.completeStep();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13525.java