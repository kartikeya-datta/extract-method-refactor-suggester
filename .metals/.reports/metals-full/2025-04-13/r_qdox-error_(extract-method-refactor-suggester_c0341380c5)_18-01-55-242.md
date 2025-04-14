error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10102.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10102.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10102.java
text:
```scala
v@@oid getHandlers(final ListIterator<PathElement> iterator, final String child, final Map<String, OperationEntry> providers, final boolean inherited) {

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

package org.jboss.as.controller.registry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import org.jboss.as.controller.OperationHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProxyController;
import org.jboss.as.controller.descriptions.DescriptionProvider;

/**
 * A registry of values within a specific key type.
 */
final class NodeSubregistry {

    private final String keyName;
    private final ConcreteNodeRegistration parent;
    @SuppressWarnings( { "unused" })
    private volatile Map<String, AbstractNodeRegistration> childRegistries;

    private static final AtomicMapFieldUpdater<NodeSubregistry, String, AbstractNodeRegistration> childRegistriesUpdater = AtomicMapFieldUpdater.newMapUpdater(AtomicReferenceFieldUpdater.newUpdater(NodeSubregistry.class, Map.class, "childRegistries"));

    NodeSubregistry(final String keyName, final ConcreteNodeRegistration parent) {
        this.keyName = keyName;
        this.parent = parent;
        childRegistriesUpdater.clear(this);
    }

    Set<String> getChildNames(){
        final Map<String, AbstractNodeRegistration> snapshot = this.childRegistries;
        if (snapshot == null) {
            return Collections.emptySet();
        }
        return new HashSet<String>(snapshot.keySet());
    }

    ModelNodeRegistration register(final String elementValue, final DescriptionProvider provider) {
        final AbstractNodeRegistration newRegistry = new ConcreteNodeRegistration(elementValue, this, provider);
        final AbstractNodeRegistration appearingRegistry = childRegistriesUpdater.putIfAbsent(this, elementValue, newRegistry);
        if (appearingRegistry == null) {
            return newRegistry;
        } else {
            throw new IllegalArgumentException("A node is already registered at '" + getLocationString() + elementValue + ")'");
        }
    }

    void registerProxyController(final String elementValue, final ProxyController proxyController) {
        final AbstractNodeRegistration newRegistry = new ProxyControllerRegistration(elementValue, this, proxyController);
        final AbstractNodeRegistration appearingRegistry = childRegistriesUpdater.putIfAbsent(this, elementValue, newRegistry);
        if (appearingRegistry != null) {
            throw new IllegalArgumentException("A node is already registered at '" + getLocationString() + elementValue + ")'");
        }
    }

    void unregisterProxyController(final String elementValue) {
        childRegistriesUpdater.remove(this, elementValue);
    }

    OperationHandler getHandler(final ListIterator<PathElement> iterator, final String child, final String operationName) {
        final Map<String, AbstractNodeRegistration> snapshot = childRegistriesUpdater.get(this);
        final AbstractNodeRegistration childRegistry = snapshot.get(child);
        if (childRegistry != null) {
            return childRegistry.getHandler(iterator, operationName);
        } else {
            final AbstractNodeRegistration wildcardRegistry = snapshot.get("*");
            if (wildcardRegistry != null) {
                return wildcardRegistry.getHandler(iterator, operationName);
            } else {
                return null;
            }
        }
    }

    void getHandlers(final ListIterator<PathElement> iterator, final String child, final Map<String, DescriptionProvider> providers, final boolean inherited) {
        final Map<String, AbstractNodeRegistration> snapshot = childRegistriesUpdater.get(this);
        final AbstractNodeRegistration childRegistry = snapshot.get(child);
        final AbstractNodeRegistration wildcardRegistry = snapshot.get("*");
        if (wildcardRegistry == null) {
            if (childRegistry == null) {
                return;
            } else {
                childRegistry.getOperationDescriptions(iterator, providers, inherited);
                return;
            }
        } else {
            if (childRegistry == null) {
                wildcardRegistry.getOperationDescriptions(iterator, providers, inherited);
                return;
            } else {
                wildcardRegistry.getOperationDescriptions(iterator, providers, inherited);
                childRegistry.getOperationDescriptions(iterator, providers, inherited);
            }
        }
    }

    String getLocationString() {
        return parent.getLocationString() + "(" + keyName + " => ";
    }

    DescriptionProvider getOperationDescription(final Iterator<PathElement> iterator, final String child, final String operationName) {
        final Map<String, AbstractNodeRegistration> snapshot = childRegistries;
        AbstractNodeRegistration childRegistry = snapshot.get(child);
        if (childRegistry == null) {
            childRegistry = snapshot.get("*");
            if (childRegistry == null) {
                return null;
            }
        }
        return childRegistry.getOperationDescription(iterator, operationName);
    }

    DescriptionProvider getModelDescription(final Iterator<PathElement> iterator, final String child) {
        final Map<String, AbstractNodeRegistration> snapshot = childRegistries;
        AbstractNodeRegistration childRegistry = snapshot.get(child);
        if (childRegistry == null) {
            childRegistry = snapshot.get("*");
            if (childRegistry == null) {
                return null;
            }
        }
        return childRegistry.getModelDescription(iterator);
    }

    Set<String> getChildNames(final Iterator<PathElement> iterator, final String child){
        final Map<String, AbstractNodeRegistration> snapshot = childRegistries;
        AbstractNodeRegistration childRegistry = snapshot.get(child);
        if (childRegistry == null) {
            childRegistry = snapshot.get("*");
            if (childRegistry == null) {
                return null;
            }
        }
        return childRegistry.getChildNames(iterator);
    }

    Set<String> getAttributeNames(final Iterator<PathElement> iterator, final String child){
        final Map<String, AbstractNodeRegistration> snapshot = childRegistries;
        AbstractNodeRegistration childRegistry = snapshot.get(child);
        if (childRegistry == null) {
            childRegistry = snapshot.get("*");
            if (childRegistry == null) {
                return null;
            }
        }
        return childRegistry.getAttributeNames(iterator);
    }

    AttributeAccess getAttributeAccess(final ListIterator<PathElement> iterator, final String child, final String attributeName) {
        final Map<String, AbstractNodeRegistration> snapshot = childRegistriesUpdater.get(this);
        final AbstractNodeRegistration childRegistry = snapshot.get(child);
        if (childRegistry != null) {
            return childRegistry.getAttributeAccess(iterator, attributeName);
        } else {
            final AbstractNodeRegistration wildcardRegistry = snapshot.get("*");
            if (wildcardRegistry != null) {
                return wildcardRegistry.getAttributeAccess(iterator, attributeName);
            } else {
                return null;
            }
        }
    }


    Set<PathElement> getChildAddresses(final Iterator<PathElement> iterator, final String child){
        final Map<String, AbstractNodeRegistration> snapshot = childRegistries;
        AbstractNodeRegistration childRegistry = snapshot.get(child);
        if (childRegistry == null) {
            childRegistry = snapshot.get("*");
            if (childRegistry == null) {
                return null;
            }
        }
        return childRegistry.getChildAddresses(iterator);
    }

    ProxyController getProxyController(final Iterator<PathElement> iterator, final String child) {
        final Map<String, AbstractNodeRegistration> snapshot = childRegistries;
        AbstractNodeRegistration childRegistry = snapshot.get(child);
        if (childRegistry == null) {
            //Don't handle '*' for now
            return null;
        }
        return childRegistry.getProxyController(iterator);
    }

    void getProxyControllers(final Iterator<PathElement> iterator, final String child, Set<ProxyController> controllers) {
        final Map<String, AbstractNodeRegistration> snapshot = childRegistries;
        if (child != null) {
            AbstractNodeRegistration childRegistry = snapshot.get(child);
            if (childRegistry == null) {
                //Don't handle '*' for now
                return;
            }
            childRegistry.getProxyControllers(iterator, controllers);
        } else {
            for (AbstractNodeRegistration childRegistry : snapshot.values()) {
                childRegistry.getProxyControllers(iterator, controllers);
            }
        }
    }

    void resolveAddress(final PathAddress address, final PathAddress base, final PathElement current, Set<PathAddress> addresses) {
        final Map<String, AbstractNodeRegistration> snapshot = childRegistries;
        final AbstractNodeRegistration childRegistry = snapshot.get(current.getValue());
        if(childRegistry == null) {
            final AbstractNodeRegistration wildcardRegistry = snapshot.get("*");
            if(wildcardRegistry == null) {
                return;
            }
            wildcardRegistry.resolveAddress(address, base.append(current), addresses);
        } else {
            childRegistry.resolveAddress(address, base.append(current), addresses);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10102.java