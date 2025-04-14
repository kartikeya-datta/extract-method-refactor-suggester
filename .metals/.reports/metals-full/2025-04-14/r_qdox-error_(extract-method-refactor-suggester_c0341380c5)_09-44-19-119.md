error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14142.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14142.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14142.java
text:
```scala
public B@@atchServiceTarget addListener(@SuppressWarnings("unchecked") ServiceListener<Object>... listeners) {

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

import org.jboss.msc.service.BatchServiceTarget;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StabilityMonitor;

/**
 * @author Paul Ferraro
 */
public class DelegatingBatchServiceTarget extends DelegatingServiceTarget implements BatchServiceTarget {
    private final BatchServiceTarget target;

    public DelegatingBatchServiceTarget(BatchServiceTarget target, ServiceTargetFactory factory, BatchServiceTargetFactory batchFactory, ServiceBuilderFactory builderFactory) {
        super(target, factory, batchFactory, builderFactory);
        this.target = target;
    }

    @Override
    public BatchServiceTarget addMonitor(StabilityMonitor monitor) {
        this.target.addMonitor(monitor);
        return this;
    }

    @Override
    public BatchServiceTarget addMonitors(StabilityMonitor... monitors) {
        this.target.addMonitors(monitors);
        return this;
    }

    @Override
    public BatchServiceTarget removeMonitor(StabilityMonitor monitor) {
        this.target.removeMonitor(monitor);
        return this;
    }

    @Override
    public BatchServiceTarget addListener(ServiceListener<Object> listener) {
        this.target.addListener(listener);
        return this;
    }

    @Override
    public BatchServiceTarget addListener(ServiceListener<Object>... listeners) {
        this.target.addListener(listeners);
        return this;
    }

    @Override
    public BatchServiceTarget addListener(Collection<ServiceListener<Object>> listeners) {
        this.target.addListener(listeners);
        return this;
    }

    @Override
    public BatchServiceTarget removeListener(ServiceListener<Object> listener) {
        this.target.removeListener(listener);
        return this;
    }

    @Override
    public BatchServiceTarget addDependency(ServiceName dependency) {
        this.target.addDependency(dependency);
        return this;
    }

    @Override
    public BatchServiceTarget addDependency(ServiceName... dependencies) {
        this.target.addDependency(dependencies);
        return this;
    }

    @Override
    public BatchServiceTarget addDependency(Collection<ServiceName> dependencies) {
        this.target.addDependency(dependencies);
        return this;
    }

    @Override
    public BatchServiceTarget removeDependency(ServiceName dependency) {
        this.target.removeDependency(dependency);
        return this;
    }

    @Override
    public void removeServices() {
        this.target.removeServices();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14142.java