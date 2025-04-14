error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10683.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10683.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10683.java
text:
```scala
private static final P@@athElement transportPath = PathElement.pathElement(ModelKeys.TRANSPORT, ModelKeys.TRANSPORT_NAME);

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
package org.jboss.as.clustering.infinispan.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;

import java.util.EnumSet;
import java.util.List;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry.EntryType;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;

/**
 * Defines the Infinispan subsystem and its addressable resources.
 *
 * @author Paul Ferraro
 * @author Richard Achmatowicz
 */
public class InfinispanExtension implements Extension {

    static final String SUBSYSTEM_NAME = "infinispan";

    private static final PathElement containerPath = PathElement.pathElement(ModelKeys.CACHE_CONTAINER);
    private static final PathElement localCachePath = PathElement.pathElement(ModelKeys.LOCAL_CACHE);
    private static final PathElement invalidationCachePath = PathElement.pathElement(ModelKeys.INVALIDATION_CACHE);
    private static final PathElement replicatedCachePath = PathElement.pathElement(ModelKeys.REPLICATED_CACHE);
    private static final PathElement distributedCachePath = PathElement.pathElement(ModelKeys.DISTRIBUTED_CACHE);
    private static final PathElement transportPath = PathElement.pathElement(ModelKeys.SINGLETON, ModelKeys.TRANSPORT);

    private static final PathElement lockingPath = PathElement.pathElement(ModelKeys.SINGLETON, ModelKeys.LOCKING);
    private static final PathElement transactionPath = PathElement.pathElement(ModelKeys.SINGLETON, ModelKeys.TRANSACTION);
    private static final PathElement evictionPath = PathElement.pathElement(ModelKeys.SINGLETON, ModelKeys.EVICTION);
    private static final PathElement expirationPath = PathElement.pathElement(ModelKeys.SINGLETON, ModelKeys.EXPIRATION);
    private static final PathElement stateTransferPath = PathElement.pathElement(ModelKeys.SINGLETON, ModelKeys.STATE_TRANSFER);
    private static final PathElement storePropertyPath = PathElement.pathElement(ModelKeys.PROPERTY);

    /**
     * {@inheritDoc}
     * @see org.jboss.as.controller.Extension#initialize(org.jboss.as.controller.ExtensionContext)
     */
    @Override
    public void initialize(ExtensionContext context) {
        SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, Namespace.CURRENT.getMajorVersion(), Namespace.CURRENT.getMinorVersion());
        subsystem.registerXMLElementWriter(new InfinispanSubsystemXMLWriter());

        ManagementResourceRegistration registration = subsystem.registerSubsystemModel(InfinispanSubsystemProviders.SUBSYSTEM);
        registration.registerOperationHandler(ADD, InfinispanSubsystemAdd.INSTANCE, InfinispanSubsystemProviders.SUBSYSTEM_ADD, false);
        registration.registerOperationHandler(DESCRIBE, InfinispanSubsystemDescribe.INSTANCE, InfinispanSubsystemProviders.SUBSYSTEM_DESCRIBE, false, EntryType.PRIVATE);
        registration.registerOperationHandler(REMOVE, ReloadRequiredRemoveStepHandler.INSTANCE, InfinispanSubsystemProviders.SUBSYSTEM_REMOVE, false);

        ManagementResourceRegistration container = registration.registerSubModel(containerPath, InfinispanSubsystemProviders.CACHE_CONTAINER);
        container.registerOperationHandler(ADD, CacheContainerAdd.INSTANCE, InfinispanSubsystemProviders.CACHE_CONTAINER_ADD, false);
        container.registerOperationHandler(REMOVE, CacheContainerRemove.INSTANCE, InfinispanSubsystemProviders.CACHE_CONTAINER_REMOVE, false);
        container.registerOperationHandler("add-alias", AddAliasCommand.INSTANCE, InfinispanSubsystemProviders.ADD_ALIAS, false);
        container.registerOperationHandler("remove-alias", RemoveAliasCommand.INSTANCE, InfinispanSubsystemProviders.REMOVE_ALIAS, false);
        CacheContainerWriteAttributeHandler.INSTANCE.registerAttributes(container);

        // add /subsystem=infinispan/cache-container=*/singleton=transport:write-attribute
        final ManagementResourceRegistration transport = container.registerSubModel(transportPath,InfinispanSubsystemProviders.TRANSPORT);
        transport.registerOperationHandler(ADD, TransportAdd.INSTANCE, InfinispanSubsystemProviders.TRANSPORT_ADD, false);
        transport.registerOperationHandler(REMOVE, TransportRemove.INSTANCE, InfinispanSubsystemProviders.TRANSPORT_REMOVE, false);
        TransportWriteAttributeHandler.INSTANCE.registerAttributes(transport);

        // add /subsystem=infinispan/cache-container=*/local-cache=*
        ManagementResourceRegistration local = container.registerSubModel(localCachePath, InfinispanSubsystemProviders.LOCAL_CACHE);
        local.registerOperationHandler(ADD, LocalCacheAdd.INSTANCE, InfinispanSubsystemProviders.LOCAL_CACHE_ADD, false);
        local.registerOperationHandler(REMOVE, CacheRemove.INSTANCE, InfinispanSubsystemProviders.CACHE_REMOVE, false);
        registerCommonCacheAttributeHandlers(local);

        // add /subsystem=infinispan/cache-container=*/invalidation-cache=*
        ManagementResourceRegistration invalidation = container.registerSubModel(invalidationCachePath, InfinispanSubsystemProviders.INVALIDATION_CACHE);
        invalidation.registerOperationHandler(ADD, InvalidationCacheAdd.INSTANCE, InfinispanSubsystemProviders.INVALIDATION_CACHE_ADD, false);
        invalidation.registerOperationHandler(REMOVE, CacheRemove.INSTANCE, InfinispanSubsystemProviders.CACHE_REMOVE, false);
        registerCommonCacheAttributeHandlers(invalidation);

        // add /subsystem=infinispan/cache-container=*/replicated-cache=*
        ManagementResourceRegistration replicated = container.registerSubModel(replicatedCachePath, InfinispanSubsystemProviders.REPLICATED_CACHE);
        replicated.registerOperationHandler(ADD, ReplicatedCacheAdd.INSTANCE, InfinispanSubsystemProviders.REPLICATED_CACHE_ADD, false);
        replicated.registerOperationHandler(REMOVE, CacheRemove.INSTANCE, InfinispanSubsystemProviders.CACHE_REMOVE, false);
        registerCommonCacheAttributeHandlers(replicated);

        // add /subsystem=infinispan/cache-container=*/distributed-cache=*
        ManagementResourceRegistration distributed = container.registerSubModel(distributedCachePath, InfinispanSubsystemProviders.DISTRIBUTED_CACHE);
        distributed.registerOperationHandler(ADD, DistributedCacheAdd.INSTANCE, InfinispanSubsystemProviders.DISTRIBUTED_CACHE_ADD, false);
        distributed.registerOperationHandler(REMOVE, CacheRemove.INSTANCE, InfinispanSubsystemProviders.CACHE_REMOVE, false);
        registerCommonCacheAttributeHandlers(distributed);
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.as.controller.Extension#initializeParsers(org.jboss.as.controller.parsing.ExtensionParsingContext)
     */
    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        for (Namespace namespace: Namespace.values()) {
            XMLElementReader<List<ModelNode>> reader = namespace.getXMLReader();
            if (reader != null) {
                context.setSubsystemXmlMapping(SUBSYSTEM_NAME, namespace.getUri(), reader);
            }
        }
    }

    private void registerCommonCacheAttributeHandlers(ManagementResourceRegistration resource) {
        // register the singleton=locking handlers
        final ManagementResourceRegistration locking = resource.registerSubModel(lockingPath, InfinispanSubsystemProviders.LOCKING);
        locking.registerOperationHandler(ADD, CacheConfigOperationHandlers.LOCKING_ADD, InfinispanSubsystemProviders.LOCKING_ADD);
        locking.registerOperationHandler(REMOVE, CacheConfigOperationHandlers.REMOVE, InfinispanSubsystemProviders.LOCKING_REMOVE);
        CacheConfigOperationHandlers.LOCKING_ATTR.registerAttributes(locking);

        // register the singleton=transaction handlers
        final ManagementResourceRegistration transaction = resource.registerSubModel(transactionPath, InfinispanSubsystemProviders.TRANSACTION);
        transaction.registerOperationHandler(ADD, CacheConfigOperationHandlers.TRANSACTION_ADD, InfinispanSubsystemProviders.TRANSACTION_ADD);
        transaction.registerOperationHandler(REMOVE, CacheConfigOperationHandlers.REMOVE, InfinispanSubsystemProviders.TRANSACTION_REMOVE);
        CacheConfigOperationHandlers.TRANSACTION_ATTR.registerAttributes(transaction);

        // register the singleton=eviction handlers
        final ManagementResourceRegistration eviction = resource.registerSubModel(evictionPath, InfinispanSubsystemProviders.EVICTION);
        eviction.registerOperationHandler(ADD, CacheConfigOperationHandlers.EVICTION_ADD, InfinispanSubsystemProviders.EVICTION_ADD);
        eviction.registerOperationHandler(REMOVE, CacheConfigOperationHandlers.REMOVE, InfinispanSubsystemProviders.EVICTION_REMOVE);
        CacheConfigOperationHandlers.EVICTION_ATTR.registerAttributes(eviction);

        // register the singleton=expiration handlers
        final ManagementResourceRegistration expiration = resource.registerSubModel(expirationPath, InfinispanSubsystemProviders.EXPIRATION);
        expiration.registerOperationHandler(ADD, CacheConfigOperationHandlers.EXPIRATION_ADD, InfinispanSubsystemProviders.EXPIRATION_ADD);
        expiration.registerOperationHandler(REMOVE, CacheConfigOperationHandlers.REMOVE, InfinispanSubsystemProviders.EXPIRATION_REMOVE);
        CacheConfigOperationHandlers.LOCKING_ATTR.registerAttributes(expiration);

        // register the singleton=state-transfer handlers
        final ManagementResourceRegistration stateTransfer = resource.registerSubModel(stateTransferPath, InfinispanSubsystemProviders.STATE_TRANSFER);
        stateTransfer.registerOperationHandler(ADD, CacheConfigOperationHandlers.STATE_TRANSFER_ADD, InfinispanSubsystemProviders.STATE_TRANSFER_ADD);
        stateTransfer.registerOperationHandler(REMOVE, CacheConfigOperationHandlers.REMOVE, InfinispanSubsystemProviders.STATE_TRANSFER_REMOVE);
        CacheConfigOperationHandlers.STATE_TRANSFER_ATTR.registerAttributes(stateTransfer);
    }

    static void createPropertyRegistration(final ManagementResourceRegistration parent) {
        final ManagementResourceRegistration registration = parent.registerSubModel(storePropertyPath, InfinispanSubsystemProviders.STORE_PROPERTY);
        registration.registerOperationHandler(ADD, CacheConfigOperationHandlers.STORE_PROPERTY_ADD, InfinispanSubsystemProviders.STORE_PROPERTY_ADD);
        registration.registerOperationHandler(REMOVE, CacheConfigOperationHandlers.REMOVE, InfinispanSubsystemProviders.STORE_PROPERTY_REMOVE);
        registration.registerReadWriteAttribute("value", null, CacheConfigOperationHandlers.STORE_PROPERTY_ATTR, EnumSet.of(AttributeAccess.Flag.RESTART_ALL_SERVICES));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10683.java