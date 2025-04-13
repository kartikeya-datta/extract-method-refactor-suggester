error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4328.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4328.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4328.java
text:
```scala
private volatile S@@erviceController<HornetQServer> hornetQServerServiceController;

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

package org.jboss.as.messaging;

import static org.jboss.as.messaging.CommonAttributes.CORE_ADDRESS;
import static org.jboss.as.messaging.CommonAttributes.RUNTIME_QUEUE;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hornetq.api.core.management.AddressControl;
import org.hornetq.api.core.management.QueueControl;
import org.hornetq.api.core.management.ResourceNames;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.management.ManagementService;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.registry.PlaceholderResource;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

/**
 * Resource representing a HornetQ server.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class HornetQServerResource implements Resource {

    private final Resource delegate;
    private ServiceController<HornetQServer> hornetQServerServiceController;

    public HornetQServerResource() {
        this(Resource.Factory.create());
    }

    public HornetQServerResource(final Resource delegate) {
        this.delegate = delegate;
    }

    public ServiceController<HornetQServer> getHornetQServerServiceController() {
        return hornetQServerServiceController;
    }

    public void setHornetQServerServiceController(ServiceController<HornetQServer> hornetQServerServiceController) {
        this.hornetQServerServiceController = hornetQServerServiceController;
    }

    @Override
    public ModelNode getModel() {
        return delegate.getModel();
    }

    @Override
    public void writeModel(ModelNode newModel) {
        delegate.writeModel(newModel);
    }

    @Override
    public boolean isModelDefined() {
        return delegate.isModelDefined();
    }

    @Override
    public boolean hasChild(PathElement element) {
        if (CORE_ADDRESS.equals(element.getKey())) {
            return hasAddressControl(element);
        } else if (RUNTIME_QUEUE.equals(element.getKey())) {
            return hasQueueControl(element.getValue());
        } else {
            return delegate.hasChild(element);
        }
    }

    @Override
    public Resource getChild(PathElement element) {
        if (CORE_ADDRESS.equals(element.getKey())) {
            return hasAddressControl(element) ? new CoreAddressResource(element.getValue(), getManagementService()) : null;
        } else if (RUNTIME_QUEUE.equals(element.getKey())) {
            return hasQueueControl(element.getValue()) ? PlaceholderResource.INSTANCE : null;
        } else {
            return delegate.getChild(element);
        }
    }

    @Override
    public Resource requireChild(PathElement element) {
        if (CORE_ADDRESS.equals(element.getKey())) {
            if (hasAddressControl(element)) {
                return new CoreAddressResource(element.getValue(), getManagementService());
            }
            throw new NoSuchResourceException(element);
        } else if (RUNTIME_QUEUE.equals(element.getKey())) {
            if (hasQueueControl(element.getValue())) {
                return PlaceholderResource.INSTANCE;
            }
            throw new NoSuchResourceException(element);
        } else {
            return delegate.requireChild(element);
        }
    }

    @Override
    public boolean hasChildren(String childType) {
        if (CORE_ADDRESS.equals(childType)) {
            return getChildrenNames(CORE_ADDRESS).size() > 0;
        } else if (RUNTIME_QUEUE.equals(childType)) {
            return getChildrenNames(RUNTIME_QUEUE).size() > 0;
        } else {
            return delegate.hasChildren(childType);
        }
    }

    @Override
    public Resource navigate(PathAddress address) {
        if (address.size() > 0 && CORE_ADDRESS.equals(address.getElement(0).getKey())) {
            if (address.size() > 1) {
                throw new NoSuchResourceException(address.getElement(1));
            }
            return new CoreAddressResource(address.getElement(0).getValue(), getManagementService());
        } else if (address.size() > 0 && RUNTIME_QUEUE.equals(address.getElement(0).getKey())) {
            if (address.size() > 1) {
                throw new NoSuchResourceException(address.getElement(1));
            }
            return PlaceholderResource.INSTANCE;
        } else {
            return delegate.navigate(address);
        }
    }

    @Override
    public Set<String> getChildTypes() {
        Set<String> result = new HashSet<String>(delegate.getChildTypes());
        result.add(CORE_ADDRESS);
        result.add(RUNTIME_QUEUE);
        return result;
    }

    @Override
    public Set<String> getChildrenNames(String childType) {
        if (CORE_ADDRESS.equals(childType)) {
            return getCoreAddressNames();
        } else if (RUNTIME_QUEUE.equals(childType)) {
            return getCoreQueueNames();
        } else {
            return delegate.getChildrenNames(childType);
        }
    }

    @Override
    public Set<ResourceEntry> getChildren(String childType) {
        if (CORE_ADDRESS.equals(childType)) {
            Set<ResourceEntry> result = new HashSet<ResourceEntry>();
            for (String name : getCoreAddressNames()) {
                result.add(new CoreAddressResource.CoreAddressResourceEntry(name, getManagementService()));
            }
            return result;
        } else if (RUNTIME_QUEUE.equals(childType)) {
            Set<ResourceEntry> result = new LinkedHashSet<ResourceEntry>();
            for (String name : getCoreQueueNames()) {
                result.add(new PlaceholderResource.PlaceholderResourceEntry(RUNTIME_QUEUE, name));
            }
            return result;
        } else {
            return delegate.getChildren(childType);
        }
    }

    @Override
    public void registerChild(PathElement address, Resource resource) {
        String type = address.getKey();
        if (CORE_ADDRESS.equals(type) ||
                RUNTIME_QUEUE.equals(type)) {
            throw MESSAGES.canNotRegisterResourceOfType(type);
        } else {
            delegate.registerChild(address, resource);
        }
    }

    @Override
    public Resource removeChild(PathElement address) {
        String type = address.getKey();
        if (CORE_ADDRESS.equals(type) ||
                RUNTIME_QUEUE.equals(type)) {
            throw MESSAGES.canNotRemoveResourceOfType(type);
        } else {
            return delegate.removeChild(address);
        }
    }

    @Override
    public boolean isRuntime() {
        return delegate.isRuntime();
    }

    @Override
    public boolean isProxy() {
        return delegate.isProxy();
    }

    @Override
    public Resource clone() {
        HornetQServerResource clone = new HornetQServerResource(delegate.clone());
        clone.setHornetQServerServiceController(hornetQServerServiceController);
        return clone;
    }

    private boolean hasAddressControl(PathElement element) {
        final ManagementService managementService = getManagementService();
        return managementService == null ? false : managementService.getResource(ResourceNames.CORE_ADDRESS + element.getValue()) != null;
    }

    private boolean hasQueueControl(String name) {
        final ManagementService managementService = getManagementService();
        return managementService == null ? false : managementService.getResource(ResourceNames.CORE_QUEUE + name) != null;
    }

    private Set<String> getCoreAddressNames() {
        final ManagementService managementService = getManagementService();
        if (managementService == null) {
            return Collections.emptySet();
        } else {
            Set<String> result = new HashSet<String>();
            for (Object obj : managementService.getResources(AddressControl.class)) {
                AddressControl ac = AddressControl.class.cast(obj);
                result.add(ac.getAddress());
            }
            return result;
        }
    }

    private Set<String> getCoreQueueNames() {
        final ManagementService managementService = getManagementService();
        if (managementService == null) {
            return Collections.emptySet();
        } else {
            Set<String> result = new HashSet<String>();
            for (Object obj : managementService.getResources(QueueControl.class)) {
                QueueControl qc = QueueControl.class.cast(obj);
                result.add(qc.getName());
            }
            return result;
        }
    }

    private ManagementService getManagementService() {
        if (hornetQServerServiceController == null
 hornetQServerServiceController.getState() != ServiceController.State.UP) {
            return null;
        } else {
            return hornetQServerServiceController.getValue().getManagementService();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4328.java