error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2475.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2475.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2475.java
text:
```scala
public static final A@@ttributeDefinition[] ATTRIBUTES_WITH_EXPRESSION_ALLOWED_IN_1_2_0 = new AttributeDefinition[]{ DEAD_LETTER_ADDRESS,

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

package org.jboss.as.messaging;

import static org.jboss.as.controller.SimpleAttributeDefinitionBuilder.create;
import static org.jboss.as.controller.client.helpers.MeasurementUnit.BYTES;
import static org.jboss.as.controller.client.helpers.MeasurementUnit.DAYS;
import static org.jboss.as.controller.client.helpers.MeasurementUnit.MILLISECONDS;
import static org.jboss.as.messaging.CommonAttributes.DEAD_LETTER_ADDRESS;
import static org.jboss.as.messaging.CommonAttributes.EXPIRY_ADDRESS;

import org.hornetq.core.settings.impl.AddressFullMessagePolicy;
import org.hornetq.core.settings.impl.AddressSettings;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Address setting resource definition
 *
 * @author <a href="http://jmesnil.net">Jeff Mesnil</a> (c) 2012 Red Hat Inc.
 */
public class AddressSettingDefinition extends SimpleResourceDefinition {

    private final boolean registerRuntimeOnly;

    public static final PathElement PATH = PathElement.pathElement(CommonAttributes.ADDRESS_SETTING);

    public static final SimpleAttributeDefinition ADDRESS_FULL_MESSAGE_POLICY = create("address-full-policy", ModelType.STRING)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_ADDRESS_FULL_MESSAGE_POLICY.toString()))
            .setValidator(new EnumValidator<AddressFullMessagePolicy>(AddressFullMessagePolicy.class, true, false))
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition LAST_VALUE_QUEUE = create("last-value-queue", ModelType.BOOLEAN)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_LAST_VALUE_QUEUE))
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition MAX_DELIVERY_ATTEMPTS = create("max-delivery-attempts", ModelType.INT)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_MAX_DELIVERY_ATTEMPTS))
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition MAX_SIZE_BYTES = create("max-size-bytes", ModelType.LONG)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_MAX_SIZE_BYTES))
            .setMeasurementUnit(BYTES)
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition MESSAGE_COUNTER_HISTORY_DAY_LIMIT = create("message-counter-history-day-limit", ModelType.INT)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_MESSAGE_COUNTER_HISTORY_DAY_LIMIT))
            .setMeasurementUnit(DAYS)
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition PAGE_MAX_CACHE_SIZE = create("page-max-cache-size", ModelType.INT)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_PAGE_MAX_CACHE))
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition PAGE_SIZE_BYTES = create("page-size-bytes", ModelType.LONG)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_PAGE_SIZE))
            .setMeasurementUnit(BYTES)
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition REDELIVERY_DELAY = create("redelivery-delay", ModelType.LONG)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_REDELIVER_DELAY))
            .setMeasurementUnit(MILLISECONDS)
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition REDISTRIBUTION_DELAY = create("redistribution-delay", ModelType.LONG)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_REDISTRIBUTION_DELAY))
            .setMeasurementUnit(MILLISECONDS)
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final SimpleAttributeDefinition SEND_TO_DLA_ON_NO_ROUTE = create("send-to-dla-on-no-route", ModelType.BOOLEAN)
            .setDefaultValue(new ModelNode(AddressSettings.DEFAULT_SEND_TO_DLA_ON_NO_ROUTE))
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    public static final AttributeDefinition[] REJECTED_EXPRESSION_ATTRIBUTES = new AttributeDefinition[]{ DEAD_LETTER_ADDRESS,
            EXPIRY_ADDRESS, REDELIVERY_DELAY, MAX_DELIVERY_ATTEMPTS, MAX_SIZE_BYTES,
            PAGE_SIZE_BYTES, PAGE_MAX_CACHE_SIZE, ADDRESS_FULL_MESSAGE_POLICY, MESSAGE_COUNTER_HISTORY_DAY_LIMIT,
            LAST_VALUE_QUEUE, REDISTRIBUTION_DELAY, SEND_TO_DLA_ON_NO_ROUTE
    };

    /**
     * Attributes are defined in the <em>same order than in the XSD schema</em>
     */
    static final SimpleAttributeDefinition[] ATTRIBUTES = new SimpleAttributeDefinition[] {
        DEAD_LETTER_ADDRESS,
        EXPIRY_ADDRESS,
        REDELIVERY_DELAY,
        MAX_DELIVERY_ATTEMPTS,
        MAX_SIZE_BYTES,
        PAGE_SIZE_BYTES,
        PAGE_MAX_CACHE_SIZE,
        ADDRESS_FULL_MESSAGE_POLICY,
        MESSAGE_COUNTER_HISTORY_DAY_LIMIT,
        LAST_VALUE_QUEUE,
        REDISTRIBUTION_DELAY,
        SEND_TO_DLA_ON_NO_ROUTE
    };

    public AddressSettingDefinition(final boolean registerRuntimeOnly) {
        super(PATH,
                MessagingExtension.getResourceDescriptionResolver(CommonAttributes.ADDRESS_SETTING),
                AddressSettingAdd.INSTANCE,
                AddressSettingRemove.INSTANCE);
        this.registerRuntimeOnly = registerRuntimeOnly;
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration registry) {
        super.registerAttributes(registry);
        for (AttributeDefinition attr : ATTRIBUTES) {
            if (registerRuntimeOnly || !attr.getFlags().contains(AttributeAccess.Flag.STORAGE_RUNTIME)) {
                registry.registerReadWriteAttribute(attr, null, AddressSettingsWriteHandler.INSTANCE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2475.java