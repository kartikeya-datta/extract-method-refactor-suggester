error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10106.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10106.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10106.java
text:
```scala
static O@@perationTransformerRegistry.ResourceTransformerEntry DISCARD = new OperationTransformerRegistry.ResourceTransformerEntry(ResourceTransformer.DISCARD, true);

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

package org.jboss.as.controller.registry;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.PathAddressTransformer;
import org.jboss.as.controller.transform.ResourceTransformer;

/**
 * Versioned operation transformer registry.
 *
 * @author Emanuel Muckenhuber
 */
public class GlobalTransformerRegistry {

    private volatile Map<String, SubRegistry> subRegistries;
    private volatile Map<ModelVersion, OperationTransformerRegistry> versionedRegistries;

    private static final AtomicMapFieldUpdater<GlobalTransformerRegistry, String, SubRegistry> subRegistriesUpdater = AtomicMapFieldUpdater.newMapUpdater(AtomicReferenceFieldUpdater.newUpdater(GlobalTransformerRegistry.class, Map.class, "subRegistries"));
    private static final AtomicMapFieldUpdater<GlobalTransformerRegistry, ModelVersion, OperationTransformerRegistry> registryUpdater = AtomicMapFieldUpdater.newMapUpdater(AtomicReferenceFieldUpdater.newUpdater(GlobalTransformerRegistry.class, Map.class, "versionedRegistries"));

    public GlobalTransformerRegistry() {
        registryUpdater.clear(this);
        subRegistriesUpdater.clear(this);
    }

    /**
     * Discard an operation.
     *
     * @param address the operation handler address
     * @param major the major version
     * @param minor the minor version
     * @param operationName the operation name
     */
    public void discardOperation(final PathAddress address, int major, int minor, final String operationName) {
        registerTransformer(address.iterator(), ModelVersion.create(major, minor), operationName, OperationTransformerRegistry.DISCARD);
    }

    /**
     * Discard an operation.
     *
     * @param address the operation handler address
     * @param version the model version
     * @param operationName the operation name
     */
    public void discardOperation(final PathAddress address, ModelVersion version, final String operationName) {
        registerTransformer(address.iterator(), version, operationName, OperationTransformerRegistry.DISCARD);
    }

    /**
     * Register an operation transformer.
     *
     * @param address the operation handler address
     * @param major the major version
     * @param minor the minor version
     * @param operationName the operation name
     * @param transformer the operation transformer
     */
    public void registerTransformer(final PathAddress address, int major, int minor, String operationName, OperationTransformer transformer) {
        registerTransformer(address.iterator(), ModelVersion.create(major, minor), operationName, new OperationTransformerRegistry.OperationTransformerEntry(transformer, false));
    }

    public void createDiscardingChildRegistry(final PathAddress address, final ModelVersion version) {
        createChildRegistry(address.iterator(), version, PathAddressTransformer.DEFAULT, DISCARD, OperationTransformerRegistry.DISCARD);
    }

    public void createChildRegistry(final PathAddress address, final ModelVersion version, OperationTransformer transformer) {
        createChildRegistry(address.iterator(), version, PathAddressTransformer.DEFAULT, RESOURCE_TRANSFORMER, new OperationTransformerRegistry.OperationTransformerEntry(transformer, false));
    }

    public void createChildRegistry(final PathAddress address, final ModelVersion version, ResourceTransformer resourceTransformer, boolean inherited) {
        createChildRegistry(address.iterator(), version, PathAddressTransformer.DEFAULT, new OperationTransformerRegistry.ResourceTransformerEntry(resourceTransformer, inherited), OperationTransformerRegistry.FORWARD);
    }

    public void createChildRegistry(final PathAddress address, final ModelVersion version, ResourceTransformer resourceTransformer, OperationTransformer operationTransformer) {
        createChildRegistry(address, version, PathAddressTransformer.DEFAULT, resourceTransformer, operationTransformer);
    }

    public void createChildRegistry(final PathAddress address, final ModelVersion version, PathAddressTransformer pathAddressTransformer, ResourceTransformer resourceTransformer, OperationTransformer operationTransformer) {
        createChildRegistry(address.iterator(), version, pathAddressTransformer, new OperationTransformerRegistry.ResourceTransformerEntry(resourceTransformer, false), new OperationTransformerRegistry.OperationTransformerEntry(operationTransformer, false));
    }

    public void createChildRegistry(final PathAddress address, final ModelVersion version, PathAddressTransformer pathAddressTransformer, ResourceTransformer resourceTransformer, OperationTransformer operationTransformer, boolean inherited) {
        createChildRegistry(address.iterator(), version, pathAddressTransformer, new OperationTransformerRegistry.ResourceTransformerEntry(resourceTransformer, false), new OperationTransformerRegistry.OperationTransformerEntry(operationTransformer, inherited));
    }


    /**
     * Register an operation transformer.
     *
     * @param address the operation handler address
     * @param version the model version
     * @param operationName the operation name
     * @param transformer the operation transformer
     */
    public void registerTransformer(final PathAddress address, final ModelVersion version, String operationName, OperationTransformer transformer) {
        registerTransformer(address.iterator(), version, operationName, new OperationTransformerRegistry.OperationTransformerEntry(transformer, false));
    }

    public OperationTransformerRegistry mergeSubtree(final OperationTransformerRegistry parent, final PathAddress address, final Map<PathAddress, ModelVersion> subTree) {
        final OperationTransformerRegistry target = parent.createChildRegistry(address.iterator(), PathAddressTransformer.DEFAULT, RESOURCE_TRANSFORMER, OperationTransformerRegistry.FORWARD);
        mergeSubtree(target, subTree);
        return target;
    }

    /**
     * Merge a subtree.
     *
     * @param targetRegistry the target registry
     * @param subTree the subtree
     */
    public void mergeSubtree(final OperationTransformerRegistry targetRegistry, final Map<PathAddress, ModelVersion> subTree) {
        for(Map.Entry<PathAddress, ModelVersion> entry: subTree.entrySet()) {
            mergeSubtree(targetRegistry, entry.getKey(), entry.getValue());
        }
    }

    protected void mergeSubtree(final OperationTransformerRegistry targetRegistry, final PathAddress address, final ModelVersion version) {
        final GlobalTransformerRegistry child = navigate(address.iterator());
        if(child != null) {
            child.process(targetRegistry, address, version, Collections.<PathAddress, ModelVersion>emptyMap());
        }
    }

    public OperationTransformerRegistry create(final ModelVersion version, final Map<PathAddress, ModelVersion> versions) {
        final OperationTransformerRegistry registry = new OperationTransformerRegistry(PathAddressTransformer.DEFAULT, RESOURCE_TRANSFORMER, null);
        process(registry, PathAddress.EMPTY_ADDRESS, version, versions);
        return registry;
    }

    protected void process(final OperationTransformerRegistry registry, final PathAddress address, final ModelVersion version, Map<PathAddress, ModelVersion> versions) {
        final OperationTransformerRegistry current = registryUpdater.get(this, version);
        if(current != null) {
            final OperationTransformerRegistry.ResourceTransformerEntry resourceTransformer = current.getResourceTransformer();
            final OperationTransformerRegistry.OperationTransformerEntry defaultTransformer = current.getDefaultTransformer();
            registry.createChildRegistry(address.iterator(), current.getPathAddressTransformer(), resourceTransformer, defaultTransformer);
            final Map<String, OperationTransformerRegistry.OperationTransformerEntry> transformers = current.getTransformers();
            for(final Map.Entry<String, OperationTransformerRegistry.OperationTransformerEntry> transformer : transformers.entrySet()) {
                registry.registerTransformer(address, transformer.getKey(), transformer.getValue().getTransformer());
            }
        }
        final Map<String, SubRegistry> snapshot = subRegistriesUpdater.get(this);
        if(snapshot != null) {
            for(final Map.Entry<String, SubRegistry> registryEntry : snapshot.entrySet()) {
                //
                final String key = registryEntry.getKey();
                final SubRegistry subRegistry = registryEntry.getValue();
                final Map<String, GlobalTransformerRegistry> children = SubRegistry.childrenUpdater.get(subRegistry);
                for(final Map.Entry<String, GlobalTransformerRegistry> childEntry : children.entrySet()) {
                    //
                    final String value = childEntry.getKey();
                    final GlobalTransformerRegistry child = childEntry.getValue();
                    final PathAddress childAddress = address.append(PathElement.pathElement(key, value));
                    final ModelVersion childVersion = versions.containsKey(childAddress) ? versions.get(childAddress) : version;
                    child.process(registry, childAddress, childVersion, versions);
                }
            }
        }
    }

    protected void createChildRegistry(final Iterator<PathElement> iterator, ModelVersion version, PathAddressTransformer pathAddressTransformer, OperationTransformerRegistry.ResourceTransformerEntry resourceTransformer, OperationTransformerRegistry.OperationTransformerEntry entry) {
        if(! iterator.hasNext()) {
            getOrCreate(version, pathAddressTransformer, resourceTransformer, entry);
        } else {
            final PathElement element = iterator.next();
            getOrCreate(element.getKey()).getOrCreate(element.getValue()).createChildRegistry(iterator, version, pathAddressTransformer, resourceTransformer, entry);
        }
    }

    protected void registerTransformer(final Iterator<PathElement> iterator, ModelVersion version, String operationName, OperationTransformerRegistry.OperationTransformerEntry entry) {
        if(! iterator.hasNext()) {
            // by default skip the default transformer
            getOrCreate(version, PathAddressTransformer.DEFAULT, null, null).registerTransformer(PathAddress.EMPTY_ADDRESS.iterator(), operationName, entry);
        } else {
            final PathElement element = iterator.next();
            final SubRegistry subRegistry = getOrCreate(element.getKey());
            subRegistry.registerTransformer(iterator, element.getValue(), version, operationName,   entry);
        }
    }

    protected OperationTransformerRegistry.OperationTransformerEntry resolveTransformer(final Iterator<PathElement> iterator, ModelVersion version, String operationName) {
        if(!iterator.hasNext()) {
            final OperationTransformerRegistry registry = registryUpdater.get(this, version);
            if(registry == null) {
                return null;
            }
            return registry.resolveOperationTransformer(PathAddress.EMPTY_ADDRESS, operationName);
        } else {
            final PathElement element = iterator.next();
            final SubRegistry registry = subRegistriesUpdater.get(this, element.getKey());
            if(registry == null) {
                return null;
            }
            return registry.resolveTransformer(iterator, element.getValue(), version, operationName);
        }
    }

    GlobalTransformerRegistry navigate(final Iterator<PathElement> iterator) {
        if(! iterator.hasNext()) {
            return this;
        } else {
            final PathElement element = iterator.next();
            final SubRegistry registry = subRegistriesUpdater.get(this, element.getKey());
            if(registry == null) {
                return null;
            }
            GlobalTransformerRegistry other = SubRegistry.childrenUpdater.get(registry, element.getValue());
            if(other != null) {
                return other.navigate(iterator);
            }
            return null;
        }
    };

    SubRegistry getOrCreate(final String key) {
        for (;;) {
            final Map<String, SubRegistry> subRegistries = subRegistriesUpdater.get(this);
            SubRegistry registry = subRegistries.get(key);
            if(registry == null) {
                registry = new SubRegistry();
                SubRegistry existing = subRegistriesUpdater.putAtomic(this, key, registry, subRegistries);
                if(existing == null) {
                    return registry;
                } else if (existing != registry) {
                    return existing;
                }
            }
            return registry;
        }
    }

    OperationTransformerRegistry getOrCreate(final ModelVersion version, PathAddressTransformer pathAddressTransformer, OperationTransformerRegistry.ResourceTransformerEntry resourceTransformer, final OperationTransformerRegistry.OperationTransformerEntry defaultTransformer) {
        for(;;) {
            final Map<ModelVersion, OperationTransformerRegistry> snapshot = registryUpdater.get(this);
            OperationTransformerRegistry registry = snapshot.get(version);
            if(registry == null) {
                registry = new OperationTransformerRegistry(pathAddressTransformer, resourceTransformer, defaultTransformer);
                OperationTransformerRegistry existing = registryUpdater.putAtomic(this, version, registry, snapshot);
                if(existing == null) {
                    return registry;
                } else if (existing != registry) {
                    return existing;
                }
            }
            return registry;
        }
    }

    static class SubRegistry {

        private static final AtomicMapFieldUpdater<SubRegistry, String, GlobalTransformerRegistry> childrenUpdater = AtomicMapFieldUpdater.newMapUpdater(AtomicReferenceFieldUpdater.newUpdater(SubRegistry.class, Map.class, "children"));
        private volatile Map<String, GlobalTransformerRegistry> children;

        SubRegistry() {
            childrenUpdater.clear(this);
        }

        GlobalTransformerRegistry getOrCreate(final String value) {
            for(;;) {
                final Map<String, GlobalTransformerRegistry> entries = childrenUpdater.get(this);
                GlobalTransformerRegistry entry = entries.get(value);
                if(entry != null) {
                    return entry;
                } else {
                    entry = new GlobalTransformerRegistry();
                    final GlobalTransformerRegistry existing = childrenUpdater.putAtomic(this, value, entry, entries);
                    if(existing == null) {
                        return entry;
                    } else if(existing != entry) {
                        return existing;
                    }
                }
            }
        }

        public OperationTransformerRegistry.OperationTransformerEntry resolveTransformer(Iterator<PathElement> iterator, String value, ModelVersion version, String operationName) {
            final GlobalTransformerRegistry registry = childrenUpdater.get(this, value);
            if(registry == null) {
                return null;
            }
            return registry.resolveTransformer(iterator, version, operationName);
        }

        public void registerTransformer(Iterator<PathElement> iterator, String value, ModelVersion version, String operationName, OperationTransformerRegistry.OperationTransformerEntry entry) {
            getOrCreate(value).registerTransformer(iterator, version, operationName, entry);
        }
    }

    static OperationTransformerRegistry.ResourceTransformerEntry RESOURCE_TRANSFORMER = new OperationTransformerRegistry.ResourceTransformerEntry(ResourceTransformer.DEFAULT, false);
    static OperationTransformerRegistry.ResourceTransformerEntry DISCARD = new OperationTransformerRegistry.ResourceTransformerEntry(ResourceTransformer.DISCARD, false);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10106.java