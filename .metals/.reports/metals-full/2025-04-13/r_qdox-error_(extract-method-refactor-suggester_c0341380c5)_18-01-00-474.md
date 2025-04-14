error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1091.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1091.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1091.java
text:
```scala
S@@erverStartBatchBuilder.this.serverStartupListener.unexpectOnDemand(serviceName);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.server;

import java.util.Collection;

import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.BatchServiceBuilder;
import org.jboss.msc.service.Location;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.value.Value;

/**
 * Super-hack batch builder used to ignore on-demand services for the server startup listener.
 *
 * @author John E. Bailey
 */
public class ServerStartBatchBuilder implements BatchBuilder {

    private final BatchBuilder delegate;
    private final ServerStartupListener serverStartupListener;

    public ServerStartBatchBuilder(final BatchBuilder delegate, final ServerStartupListener serverStartupListener) {
        this.delegate = delegate;
        this.serverStartupListener = serverStartupListener;
    }

    @Override
    public <T> BatchServiceBuilder<T> addServiceValue(ServiceName name, Value<? extends Service<T>> value) throws IllegalArgumentException {
        return new ServerStartBatchServiceBuilder<T>(name, delegate.addServiceValue(name, value));
    }

    @Override
    public <T> BatchServiceBuilder<T> addService(ServiceName name, Service<T> service) throws IllegalArgumentException {
        return new ServerStartBatchServiceBuilder<T>(name, delegate.addService(name, service));
    }

    @Override
    public <T> BatchServiceBuilder<T> addServiceValueIfNotExist(ServiceName name, Value<? extends Service<T>> value) throws IllegalArgumentException {
        return new ServerStartBatchServiceBuilder<T>(name, delegate.addServiceValueIfNotExist(name, value));
    }

    @Override
    public BatchBuilder addListener(ServiceListener<Object> listener) {
        delegate.addListener(listener);
        return this;
    }

    @Override
    public BatchBuilder addListener(ServiceListener<Object>... listeners) {
        delegate.addListener(listeners);
        return this;
    }

    @Override
    public BatchBuilder addListener(Collection<ServiceListener<Object>> listeners) {
        delegate.addListener(listeners);
        return this;
    }

    @Override
    public BatchBuilder addDependency(ServiceName dependency) {
        delegate.addDependency(dependency);
        return this;
    }

    @Override
    public BatchBuilder addDependency(ServiceName... dependencies) {
        delegate.addDependency(dependencies);
        return this;
    }

    @Override
    public BatchBuilder addDependency(Collection<ServiceName> dependencies) {
        delegate.addDependency(dependencies);
        return this;
    }

    @Override
    public void install() throws ServiceRegistryException {
        delegate.install();
    }

    @Override
    public BatchBuilder subBatchBuilder() {
        return new ServerStartBatchBuilder(delegate.subBatchBuilder(), serverStartupListener);
    }

    private class ServerStartBatchServiceBuilder<T> implements BatchServiceBuilder<T> {
        private final ServiceName serviceName;
        final BatchServiceBuilder<T> delegate;

        private ServerStartBatchServiceBuilder(ServiceName serviceName, BatchServiceBuilder<T> delegate) {
            this.serviceName = serviceName;
            this.delegate = delegate;
        }

        @Override
        public BatchServiceBuilder addAliases(ServiceName... aliases) {
            delegate.addAliases(aliases);
            return this;
        }

        @Override
        public BatchServiceBuilder setLocation() {
            delegate.setLocation();
            return this;
        }

        @Override
        public BatchServiceBuilder setLocation(Location location) {
            delegate.setLocation(location);
            return this;
        }

        @Override
        public BatchServiceBuilder setInitialMode(ServiceController.Mode mode) {
            if(mode.equals(ServiceController.Mode.ON_DEMAND)) {
                ServerStartBatchBuilder.this.serverStartupListener.expectOnDemand(serviceName);
            } else {
                ServerStartBatchBuilder.this.serverStartupListener.expectOnDemand(serviceName);
            }
            delegate.setInitialMode(mode);
            return this;
        }

        @Override
        public BatchServiceBuilder addDependencies(ServiceName... dependencies) {
            delegate.addDependencies(dependencies);
            return this;
        }

        @Override
        public BatchServiceBuilder addOptionalDependencies(ServiceName... dependencies) {
            delegate.addOptionalDependencies(dependencies);
            return this;
        }

        @Override
        public BatchServiceBuilder addDependencies(Iterable<ServiceName> dependencies) {
            delegate.addDependencies(dependencies);
            return this;
        }

        @Override
        public BatchServiceBuilder addOptionalDependencies(Iterable<ServiceName> dependencies) {
            delegate.addOptionalDependencies(dependencies);
            return this;
        }

        @Override
        public BatchServiceBuilder addDependency(ServiceName dependency) {
            delegate.addDependency(dependency);
            return this;
        }

        @Override
        public BatchServiceBuilder addOptionalDependency(ServiceName dependency) {
            delegate.addOptionalDependency(dependency);
            return this;
        }

        @Override
        public BatchServiceBuilder addDependency(ServiceName dependency, Injector<Object> target) {
            delegate.addDependency(dependency, target);
            return this;
        }

        @Override
        public BatchServiceBuilder addOptionalDependency(ServiceName dependency, Injector<Object> target) {
            delegate.addOptionalDependency(dependency, target);
            return this;
        }

        @Override
        public BatchServiceBuilder addDependency(ServiceName dependency, Class type, Injector target) {
            delegate.addDependency(dependency, type, target);
            return this;
        }

        @Override
        public BatchServiceBuilder addOptionalDependency(ServiceName dependency, Class type, Injector target) {
            delegate.addOptionalDependency(dependency, type, target);
            return this;
        }

        @Override
        public BatchServiceBuilder addInjection(Injector target, Object value) {
            delegate.addInjection(target, value);
            return this;
        }

        @Override
        public BatchServiceBuilder addInjectionValue(Injector target, Value value) {
            delegate.addInjection(target, value);
            return this;
        }

        @Override
        public BatchServiceBuilder addListener(ServiceListener serviceListener) {
            delegate.addListener(serviceListener);
            return this;
        }

        @Override
        public BatchServiceBuilder addListener(ServiceListener... serviceListeners) {
            delegate.addListener(serviceListeners);
            return this;
        }

        @Override
        public BatchServiceBuilder addListener(Collection<? extends ServiceListener<? super T>> collection) {
            delegate.addListener(collection);
            return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1091.java