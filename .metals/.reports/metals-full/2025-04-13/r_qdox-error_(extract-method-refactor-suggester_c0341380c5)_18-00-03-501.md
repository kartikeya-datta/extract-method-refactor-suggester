error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4977.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4977.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4977.java
text:
```scala
private R@@esource delegate = Resource.Factory.create(true);

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

import java.util.Set;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.ejb3.subsystem.EJB3SubsystemModel;
import org.jboss.dmr.ModelNode;

/**
 * @author baranowb
 *
 */
public class TimerServiceResource implements Resource {

    private Resource delegate = Resource.Factory.create();

    /**
     * @return
     * @see org.jboss.as.controller.registry.Resource#getModel()
     */
    public ModelNode getModel() {
        return delegate.getModel();
    }

    /**
     * @param newModel
     * @see org.jboss.as.controller.registry.Resource#writeModel(org.jboss.dmr.ModelNode)
     */
    public void writeModel(ModelNode newModel) {
        delegate.writeModel(newModel);
    }

    /**
     * @return
     * @see org.jboss.as.controller.registry.Resource#isModelDefined()
     */
    public boolean isModelDefined() {
        return delegate.isModelDefined();
    }

    /**
     * @param element
     * @return
     * @see org.jboss.as.controller.registry.Resource#hasChild(org.jboss.as.controller.PathElement)
     */
    public boolean hasChild(PathElement element) {
        return delegate.hasChild(element);
    }

    /**
     * @param element
     * @return
     * @see org.jboss.as.controller.registry.Resource#getChild(org.jboss.as.controller.PathElement)
     */
    public Resource getChild(PathElement element) {
        return delegate.getChild(element);
    }

    /**
     * @param element
     * @return
     * @see org.jboss.as.controller.registry.Resource#requireChild(org.jboss.as.controller.PathElement)
     */
    public Resource requireChild(PathElement element) {
        return delegate.requireChild(element);
    }

    /**
     * @param childType
     * @return
     * @see org.jboss.as.controller.registry.Resource#hasChildren(java.lang.String)
     */
    public boolean hasChildren(String childType) {
        return delegate.hasChildren(childType);
    }

    /**
     * @param address
     * @return
     * @see org.jboss.as.controller.registry.Resource#navigate(org.jboss.as.controller.PathAddress)
     */
    public Resource navigate(PathAddress address) {
        return delegate.navigate(address);
    }

    /**
     * @return
     * @see org.jboss.as.controller.registry.Resource#getChildTypes()
     */
    public Set<String> getChildTypes() {
        return delegate.getChildTypes();
    }

    /**
     * @param childType
     * @return
     * @see org.jboss.as.controller.registry.Resource#getChildrenNames(java.lang.String)
     */
    public Set<String> getChildrenNames(String childType) {
        return delegate.getChildrenNames(childType);
    }

    /**
     * @param childType
     * @return
     * @see org.jboss.as.controller.registry.Resource#getChildren(java.lang.String)
     */
    public Set<ResourceEntry> getChildren(String childType) {
        return delegate.getChildren(childType);
    }

    /**
     * @param address
     * @param resource
     * @see org.jboss.as.controller.registry.Resource#registerChild(org.jboss.as.controller.PathElement,
     *      org.jboss.as.controller.registry.Resource)
     */
    public void registerChild(PathElement address, Resource resource) {
        delegate.registerChild(address, resource);
    }

    /**
     * @param address
     * @return
     * @see org.jboss.as.controller.registry.Resource#removeChild(org.jboss.as.controller.PathElement)
     */
    public Resource removeChild(PathElement address) {
        return delegate.removeChild(address);
    }

    /**
     * @return
     * @see org.jboss.as.controller.registry.Resource#isRuntime()
     */
    public boolean isRuntime() {
        return delegate.isRuntime();
    }

    /**
     * @return
     * @see org.jboss.as.controller.registry.Resource#isProxy()
     */
    public boolean isProxy() {
        return delegate.isProxy();
    }

    /**
     * @return
     * @see org.jboss.as.controller.registry.Resource#clone()
     */
    public Resource clone() {
        return this;
    }

    public void timerCreated(String id) {
        PathElement address = PathElement.pathElement(EJB3SubsystemModel.TIMER, id);
        this.delegate.registerChild(address, Resource.Factory.create());
    }

    public void timerRemoved(String id) {
        PathElement address = PathElement.pathElement(EJB3SubsystemModel.TIMER, id);
        this.delegate.removeChild(address);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4977.java