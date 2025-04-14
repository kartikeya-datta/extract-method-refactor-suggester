error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14019.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14019.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14019.java
text:
```scala
r@@eturn (S) view.createInstance(Collections.<Object, Object>singletonMap(SessionID.class, id)).getInstance();

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.weld.ejb;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.NoSuchEJBException;

import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ejb3.component.stateful.StatefulSessionComponent;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.ejb.client.SessionID;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.weld.ejb.api.SessionObjectReference;
import org.jboss.weld.ejb.spi.BusinessInterfaceDescriptor;

/**
 * Implementation for SFSB's
 *
 * @author Stuart Douglas
 */
public class StatefulSessionObjectReferenceImpl implements SessionObjectReference, Serializable {

    private volatile boolean removed = false;

    private final Map<String, ServiceName> viewServices;
    private final ServiceName createServiceName;
    private final SessionID id;
    private final StatefulSessionComponent ejbComponent;

    public StatefulSessionObjectReferenceImpl(final SessionID id, final ServiceName createServiceName, final Map<String, ServiceName> viewServices) {
        this.id = id;
        this.createServiceName = createServiceName;
        this.viewServices = viewServices;
        final ServiceController<?> controller = CurrentServiceContainer.getServiceContainer().getRequiredService(createServiceName);
        ejbComponent = (StatefulSessionComponent) controller.getValue();
    }

    public StatefulSessionObjectReferenceImpl(EjbDescriptorImpl<?> descriptor) {
        this.createServiceName = descriptor.getCreateServiceName();
        final ServiceController<?> controller = CurrentServiceContainer.getServiceContainer().getRequiredService(createServiceName);
        ejbComponent = (StatefulSessionComponent) controller.getValue();
        this.id = ejbComponent.createSession();
        this.viewServices = buildViews(descriptor);

    }

    private static Map<String, ServiceName> buildViews(final EjbDescriptorImpl<?> descriptor) {
        final Map<String, ServiceName> viewServices = new HashMap<String, ServiceName>();
        final Map<String, Class<?>> views = new HashMap<String, Class<?>>();
        for (BusinessInterfaceDescriptor<?> view : descriptor.getRemoteBusinessInterfaces()) {
            views.put(view.getInterface().getName(), view.getInterface());
        }
        for (BusinessInterfaceDescriptor<?> view : descriptor.getLocalBusinessInterfaces()) {
            views.put(view.getInterface().getName(), view.getInterface());
        }

        for (Map.Entry<Class<?>, ServiceName> entry : descriptor.getViewServices().entrySet()) {
            final Class<?> viewClass = entry.getKey();
            if (viewClass != null) {
                //see WELD-921
                //this is horrible, but until it is fixed there is not much that can be done

                final Set<Class<?>> seen = new HashSet<Class<?>>();
                final Set<Class<?>> toProcess = new HashSet<Class<?>>();

                toProcess.add(viewClass);

                while (!toProcess.isEmpty()) {
                    Iterator<Class<?>> it = toProcess.iterator();
                    final Class<?> clazz = it.next();
                    it.remove();
                    seen.add(clazz);
                    viewServices.put(clazz.getName(), entry.getValue());
                    final Class<?> superclass = clazz.getSuperclass();
                    if (superclass != Object.class && superclass != null && !seen.contains(superclass)) {
                        toProcess.add(superclass);
                    }
                    for (Class<?> iface : clazz.getInterfaces()) {
                        if (!seen.contains(iface)) {
                            toProcess.add(iface);
                        }
                    }
                }
            }
        }
        return viewServices;
    }


    @Override
    @SuppressWarnings({"unchecked"})
    public synchronized <S> S getBusinessObject(Class<S> businessInterfaceType) {
        if (isRemoved()) {
            throw new NoSuchEJBException("Bean has been removed");
        }
        if (viewServices.containsKey(businessInterfaceType.getName())) {
            final ServiceController<?> serviceController = CurrentServiceContainer.getServiceContainer().getRequiredService(viewServices.get(businessInterfaceType.getName()));
            final ComponentView view = (ComponentView) serviceController.getValue();
            try {
                return (S) view.createInstance(Collections.<Object, Object>singletonMap(SessionID.SESSION_ID_KEY, id)).getInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("View of type " + businessInterfaceType + " not found on bean " + ejbComponent);
        }
    }

    protected Object writeReplace() throws IOException {
        return new SerializedStatefulSessionObject(createServiceName, id, viewServices);
    }

    @Override
    public void remove() {
        if (!isRemoved()) {
            ejbComponent.removeSession(id);
            removed = true;
        }
    }

    @Override
    public boolean isRemoved() {
        if (!removed) {
            return ejbComponent.getCache().get(id) == null;
        }
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14019.java