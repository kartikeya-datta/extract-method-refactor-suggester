error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14143.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14143.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14143.java
text:
```scala
public S@@erviceBuilder<T> addListener(@SuppressWarnings("unchecked") ServiceListener<? super T>... listeners) {

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

import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.StabilityMonitor;
import org.jboss.msc.value.Value;

/**
 * @author Paul Ferraro
 */
public class DelegatingServiceBuilder<T> implements ServiceBuilder<T> {
    private final ServiceBuilder<T> builder;
    private final ServiceControllerFactory factory;

    public DelegatingServiceBuilder(ServiceBuilder<T> builder, ServiceControllerFactory factory) {
        this.builder = builder;
        this.factory = factory;
    }

    @Override
    public ServiceBuilder<T> addAliases(ServiceName... aliases) {
        this.builder.addAliases(aliases);
        return this;
    }

    @Override
    public ServiceBuilder<T> setInitialMode(Mode mode) {
        this.builder.setInitialMode(mode);
        return this;
    }

    @Override
    public ServiceBuilder<T> addDependencies(ServiceName... dependencies) {
        this.builder.addDependencies(dependencies);
        return this;
    }

    @Override
    public ServiceBuilder<T> addDependencies(ServiceBuilder.DependencyType dependencyType, ServiceName... dependencies) {
        this.builder.addDependencies(dependencyType, dependencies);
        return this;
    }

    @Override
    public ServiceBuilder<T> addDependencies(Iterable<ServiceName> dependencies) {
        this.builder.addDependencies(dependencies);
        return this;
    }

    @Override
    public ServiceBuilder<T> addDependencies(ServiceBuilder.DependencyType dependencyType, Iterable<ServiceName> dependencies) {
        this.builder.addDependencies(dependencyType, dependencies);
        return this;
    }

    @Override
    public ServiceBuilder<T> addDependency(ServiceName dependency) {
        this.builder.addDependency(dependency);
        return this;
    }

    @Override
    public ServiceBuilder<T> addDependency(ServiceBuilder.DependencyType dependencyType, ServiceName dependency) {
        this.builder.addDependency(dependencyType, dependency);
        return this;
    }

    @Override
    public ServiceBuilder<T> addDependency(ServiceName dependency, Injector<Object> target) {
        this.builder.addDependency(dependency, target);
        return this;
    }

    @Override
    public ServiceBuilder<T> addDependency(ServiceBuilder.DependencyType dependencyType, ServiceName dependency, Injector<Object> target) {
        this.builder.addDependency(dependencyType, dependency, target);
        return this;
    }

    @Override
    public <I> ServiceBuilder<T> addDependency(ServiceName dependency, Class<I> type, Injector<I> target) {
        this.builder.addDependency(dependency, type, target);
        return this;
    }

    @Override
    public <I> ServiceBuilder<T> addDependency(org.jboss.msc.service.ServiceBuilder.DependencyType dependencyType, ServiceName dependency, Class<I> type, Injector<I> target) {
        this.builder.addDependency(dependencyType, dependency, type, target);
        return this;
    }

    @Override
    public <I> ServiceBuilder<T> addInjection(Injector<? super I> target, I value) {
        this.builder.addInjection(target, value);
        return this;
    }

    @Override
    public <I> ServiceBuilder<T> addInjectionValue(Injector<? super I> target, Value<I> value) {
        this.builder.addInjectionValue(target, value);
        return this;
    }

    @Override
    public ServiceBuilder<T> addInjection(Injector<? super T> target) {
        this.builder.addInjection(target);
        return this;
    }

    @Override
    public ServiceBuilder<T> addMonitor(StabilityMonitor monitor) {
        this.builder.addMonitor(monitor);
        return this;
    }

    @Override
    public ServiceBuilder<T> addMonitors(StabilityMonitor... monitors) {
        this.builder.addMonitors(monitors);
        return this;
    }

    @Override
    public ServiceBuilder<T> addListener(ServiceListener<? super T> listener) {
        this.builder.addListener(listener);
        return this;
    }

    @Override
    public ServiceBuilder<T> addListener(ServiceListener<? super T>... listeners) {
        this.builder.addListener(listeners);
        return this;
    }

    @Override
    public ServiceBuilder<T> addListener(Collection<? extends ServiceListener<? super T>> listeners) {
        this.builder.addListener(listeners);
        return this;
    }

    @Override
    public ServiceController<T> install() {
        return this.factory.createServiceController(this.builder.install());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14143.java