error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13246.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13246.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13246.java
text:
```scala
r@@eturn classIntrospectorInjectedValue.getValue().createInstance(instance);

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

package org.jboss.as.ee.component;

import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.value.InjectedValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that can be used to create a fully injected class instance. If there is an appropriate component regiestered
 * an instance of the component will be created. Otherwise the default class introspector will be used to create an instance.
 * <p/>
 * This can be problematic in theory, as it is possible to have multiple components for a single class, however it does
 * not seem to be an issue in practice.
 * <p/>
 * This registry only contains simple component types that have at most 1 view
 *
 * @author Stuart Douglas
 */
public class ComponentRegistry {

    private static ServiceName SERVICE_NAME = ServiceName.of("ee", "ComponentRegistry");

    private final Map<Class<?>, ComponentManagedReferenceFactory> componentsByClass = new ConcurrentHashMap<Class<?>, ComponentManagedReferenceFactory>();
    private final ServiceRegistry serviceRegistry;
    private final InjectedValue<EEClassIntrospector> classIntrospectorInjectedValue = new InjectedValue<>();

    public static ServiceName serviceName(final DeploymentUnit deploymentUnit) {
        return deploymentUnit.getServiceName().append(SERVICE_NAME);
    }

    public ComponentRegistry(final ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public void addComponent(final ComponentConfiguration componentConfiguration) {
        if(componentConfiguration.getViews().size() > 1) {

        }
        componentsByClass.put(componentConfiguration.getComponentClass(), new ComponentManagedReferenceFactory(componentConfiguration.getComponentDescription().getStartServiceName()));
    }

    public ManagedReferenceFactory createInstanceFactory(final Class<?> componentClass) {
        final ManagedReferenceFactory factory = componentsByClass.get(componentClass);
        if (factory == null) {
            return classIntrospectorInjectedValue.getValue().createFactory(componentClass);
        }
        return factory;
    }

    public ManagedReference createInstance(final Object instance) {

        final ComponentManagedReferenceFactory factory = componentsByClass.get(instance.getClass());
        if (factory == null) {
            return null;
        }
        return factory.getReference(instance);
    }

    public InjectedValue<EEClassIntrospector> getClassIntrospectorInjectedValue() {
        return classIntrospectorInjectedValue;
    }

    private static class ComponentManagedReference implements ManagedReference {

        private final ComponentInstance instance;
        private boolean destroyed;

        public ComponentManagedReference(final ComponentInstance component) {
            instance = component;
        }

        @Override
        public synchronized void release() {
            if (!destroyed) {
                instance.destroy();
                destroyed = true;
            }
        }

        @Override
        public Object getInstance() {
            return instance.getInstance();
        }
    }

    public class ComponentManagedReferenceFactory implements ManagedReferenceFactory {

        private final ServiceName serviceName;
        private volatile ServiceController<Component> component;

        private ComponentManagedReferenceFactory(final ServiceName serviceName) {
            this.serviceName = serviceName;
        }

        @Override
        public ManagedReference getReference() {
            if (component == null) {
                synchronized (this) {
                    if (component == null) {
                        component = (ServiceController<Component>) serviceRegistry.getService(serviceName);
                    }
                }
            }
            if (component == null) {
                return null;
            }
            return new ComponentManagedReference(component.getValue().createInstance());
        }


        public ManagedReference getReference(final Object instance) {
            if (component == null) {
                synchronized (this) {
                    if (component == null) {
                        component = (ServiceController<Component>) serviceRegistry.getService(serviceName);
                    }
                }
            }
            if (component == null) {
                return null;
            }
            return new ComponentManagedReference(component.getValue().createInstance(instance));
        }

        public ServiceName getServiceName() {
            return serviceName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13246.java