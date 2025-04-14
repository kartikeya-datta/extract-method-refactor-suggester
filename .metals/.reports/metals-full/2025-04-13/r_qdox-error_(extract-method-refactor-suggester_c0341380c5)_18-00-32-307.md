error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2583.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2583.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2583.java
text:
```scala
P@@athElement.pathElement(ModelKeys.CACHE_CONTAINER, containerName),

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
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import java.util.List;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

/**
 * Remove a cache container, taking care to remove any child cache resources as well.
 *
 * @author Paul Ferraro
 * @author Richard Achmatowicz (c) 2011 Red Hat, Inc.
 */
public class CacheContainerRemove extends AbstractRemoveStepHandler {

    public static final CacheContainerRemove INSTANCE = new CacheContainerRemove();

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {

        final PathAddress address = PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR));
        final String containerName = address.getLastElement().getValue();

        // remove any existing cache entries
        removeExistingCacheServices(context, model, containerName);

        // remove the cache container services
        CacheContainerAdd.INSTANCE.removeRuntimeServices(context, operation, model);
    }

    /**
     * Method to re-install any services associated with existing local caches.
     *
     * @param context
     * @param operation
     * @param model
     * @throws OperationFailedException
     */
    @Override
    protected void recoverServices(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {

        final PathAddress address = PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR));
        final String containerName = address.getLastElement().getValue();
        // used by service installation
        final ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler() ;

        // re-install the cache container services
        CacheContainerAdd.INSTANCE.installRuntimeServices(context, operation, model, verificationHandler);

        // re-install any existing cache services
        reinstallExistingCacheServices(context, model, containerName, verificationHandler);
    }


    /**
     * Method to reinstall any services associated with existing caches.
     *
     * @param context
     * @param containerModel
     * @param containerName
     * @throws OperationFailedException
     */
    private static void reinstallExistingCacheServices(OperationContext context, ModelNode containerModel, String containerName, ServiceVerificationHandler verificationHandler) throws OperationFailedException {

        for (String cacheType: CacheRemove.INSTANCE.getCacheTypes()) {
            CacheAdd addHandler = CacheRemove.INSTANCE.getAddHandler(cacheType);
            List<Property> caches = getCachesFromParentModel(cacheType, containerModel);
            if (caches != null) {
                for (Property cache: caches) {
                    String cacheName = cache.getName();
                    ModelNode cacheModel = cache.getValue();
                    ModelNode operation = createCacheAddOperation(cacheType, containerName, cacheName);
                    addHandler.installRuntimeServices(context, operation, containerModel, cacheModel, verificationHandler);
                }
            }
        }
    }

    /**
     * Method to remove any services associated with existing caches.
     *
     * @param context
     * @param containerModel
     * @param containerName
     * @throws OperationFailedException
     */
    private static void removeExistingCacheServices(OperationContext context, ModelNode containerModel, String containerName) throws OperationFailedException {

        for (String cacheType: CacheRemove.INSTANCE.getCacheTypes()) {
            CacheAdd addHandler = CacheRemove.INSTANCE.getAddHandler(cacheType);
            List<Property> caches = getCachesFromParentModel(cacheType, containerModel);
            if (caches != null) {
                for (Property cache : caches) {
                    String cacheName = cache.getName();
                    ModelNode cacheModel = cache.getValue();
                    ModelNode operation = createCacheRemoveOperation(cacheType, containerName, cacheName);
                    addHandler.removeRuntimeServices(context, operation, containerModel, cacheModel);
                }
            }
        }
    }

    private static List<Property> getCachesFromParentModel(String cacheType, ModelNode model) {
        // get the caches of a type
        List<Property> cacheList = null;
        ModelNode caches = model.get(cacheType);
        if (caches.isDefined() && caches.getType() == ModelType.OBJECT) {
            cacheList = caches.asPropertyList();
            return cacheList;
        }
        return null;
    }

    private static ModelNode createCacheRemoveOperation(String cacheType, String containerName, String cacheName) {
        // create the address of the cache
        PathAddress cacheAddr = getCacheAddress(containerName, cacheName, cacheType);
        ModelNode removeOp = new ModelNode();
        removeOp.get(OP).set(REMOVE);
        removeOp.get(OP_ADDR).set(cacheAddr.toModelNode());

        return removeOp;
    }

    private static ModelNode createCacheAddOperation(String cacheType, String containerName, String cacheName) {
        // create the address of the cache
        PathAddress cacheAddr = getCacheAddress(containerName, cacheName, cacheType);
        ModelNode addOp = new ModelNode();
        addOp.get(OP).set(ADD);
        addOp.get(OP_ADDR).set(cacheAddr.toModelNode());

        return addOp;
    }

    private static PathAddress getCacheAddress(String containerName, String cacheName, String cacheType) {
        // create the address of the cache
        PathAddress cacheAddr = PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, InfinispanExtension.SUBSYSTEM_NAME),
                PathElement.pathElement("cache-container", containerName),
                PathElement.pathElement(cacheType, cacheName));
        return cacheAddr;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2583.java