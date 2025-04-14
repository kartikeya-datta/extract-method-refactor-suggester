error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14144.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14144.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14144.java
text:
```scala
public S@@erviceTarget addListener(@SuppressWarnings("unchecked") ServiceListener<Object>... listeners) {

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

package org.jboss.as.clustering.msc;

import java.util.Collection;
import java.util.Set;

import org.jboss.msc.service.BatchServiceTarget;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StabilityMonitor;
import org.jboss.msc.value.Value;

/**
 * @author Paul Ferraro
 */
public class DelegatingServiceTarget implements ServiceTarget {
    private final ServiceTarget target;
    private final ServiceTargetFactory factory;
    private final BatchServiceTargetFactory batchFactory;
    private final ServiceBuilderFactory builderFactory;

    public DelegatingServiceTarget(ServiceTarget target, ServiceTargetFactory factory, BatchServiceTargetFactory batchFactory, ServiceBuilderFactory builderFactory) {
        this.target = target;
        this.builderFactory = builderFactory;
        this.batchFactory = batchFactory;
        this.factory = factory;
    }

    @Override
    public <T> ServiceBuilder<T> addServiceValue(ServiceName name, Value<? extends Service<T>> value) {
        return this.builderFactory.createServiceBuilder(this.target.addServiceValue(name, value));
    }

    @Override
    public <T> ServiceBuilder<T> addService(ServiceName name, Service<T> service) {
        return this.builderFactory.createServiceBuilder(this.target.addService(name, service));
    }

    @Override
    public ServiceTarget addListener(ServiceListener<Object> listener) {
        this.target.addListener(listener);
        return this;
    }

    @Override
    public ServiceTarget addListener(ServiceListener<Object>... listeners) {
        this.target.addListener(listeners);
        return this;
    }

    @Override
    public ServiceTarget addListener(Collection<ServiceListener<Object>> listeners) {
        this.target.addListener(listeners);
        return this;
    }

    @Override
    public ServiceTarget removeListener(ServiceListener<Object> listener) {
        this.target.removeListener(listener);
        return this;
    }

    @Override
    public Set<ServiceListener<Object>> getListeners() {
        return this.getListeners();
    }

    @Override
    public ServiceTarget addDependency(ServiceName dependency) {
        this.target.addDependency(dependency);
        return this;
    }

    @Override
    public ServiceTarget addDependency(ServiceName... dependencies) {
        this.target.addDependency(dependencies);
        return this;
    }

    @Override
    public ServiceTarget addDependency(Collection<ServiceName> dependencies) {
        this.target.addDependency(dependencies);
        return this;
    }

    @Override
    public ServiceTarget removeDependency(ServiceName dependency) {
        this.target.removeDependency(dependency);
        return this;
    }

    @Override
    public Set<ServiceName> getDependencies() {
        return this.target.getDependencies();
    }

    @Override
    public ServiceTarget subTarget() {
        return this.factory.createServiceTarget(this.target.subTarget());
    }

    @Override
    public BatchServiceTarget batchTarget() {
        return this.batchFactory.createBatchServiceTarget(this.target.batchTarget());
    }

    @Override
    public ServiceTarget addMonitor(StabilityMonitor monitor) {
        target.addMonitor(monitor);
        return this;
    }

    @Override
    public ServiceTarget addMonitors(StabilityMonitor... monitors) {
        target.addMonitors(monitors);
        return this;
    }

    @Override
    public ServiceTarget removeMonitor(StabilityMonitor monitor) {
        target.removeMonitor(monitor);
        return this;
    }

    @Override
    public Set<StabilityMonitor> getMonitors() {
        return target.getMonitors();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14144.java