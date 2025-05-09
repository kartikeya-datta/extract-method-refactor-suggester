error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9966.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9966.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9966.java
text:
```scala
public static final O@@bjectName OBJECT_NAME = ObjectNameFactory.create("jboss.internal", "mbean", "ServiceContainer");

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

package org.jboss.as.jmx.mbean;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.jboss.as.jmx.MBeanServerService;
import org.jboss.as.jmx.ObjectNameFactory;
import org.jboss.logging.Logger;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.BatchServiceBuilder;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceController.State;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * Basic service managing the {@link ServiceContainer} instance.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 21-Oct-2010
 */
public class ManagedServiceContainerService implements Service<Void> {
    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("mbean", "service", "container");
    public static final ObjectName OBJECT_NAME = ObjectNameFactory.create("jboss", "mbean", "ServiceContainer");

    private final Logger log = Logger.getLogger(ManagedServiceContainerService.class);
    private final InjectedValue<MBeanServer> injectedMBeanServer = new InjectedValue<MBeanServer>();
    private ServiceContainer serviceContainer;

    public static void addService(final BatchBuilder batchBuilder) {
        ManagedServiceContainerService service = new ManagedServiceContainerService();
        BatchServiceBuilder<?> serviceBuilder = batchBuilder.addService(SERVICE_NAME, service);
        serviceBuilder.addDependency(MBeanServerService.SERVICE_NAME, MBeanServer.class, service.injectedMBeanServer);
        serviceBuilder.setInitialMode(Mode.ACTIVE);
    }

    /** {@inheritDoc} */
    public synchronized void start(final StartContext context) throws StartException {
        serviceContainer = context.getController().getServiceContainer();
        ManagedServiceContainer mbean = new ManagedServiceContainer() {

            @Override
            public List<String> listServices() {
                return getServiceList(null);
            }

            @Override
            public List<String> listServicesByMode(String mode) {
                if (mode == null)
                    return getServiceList(null);
                String pattern = "mode " + Mode.valueOf(mode.trim().toUpperCase());
                return getServiceList(pattern);
            }

            @Override
            public List<String> listServicesByState(String state) {
                if (state == null)
                    return getServiceList(null);
                String pattern = "state=" + State.valueOf(state.trim().toUpperCase());
                return getServiceList(pattern);
            }

            @Override
            public void setMode(String name, String mode) {
                if (name == null)
                    throw new IllegalArgumentException("Null name");
                if (mode == null)
                    throw new IllegalArgumentException("Null mode");
                ServiceName serviceName = ServiceName.parse(name.trim());
                ServiceController<?> controller = serviceContainer.getService(serviceName);
                if (controller == null)
                    throw new IllegalStateException("Cannot obtain service: " + serviceName);
                controller.setMode(Mode.valueOf(mode.trim().toUpperCase()));
            }

            private List<String> getServiceList(String pattern) {
                List<String> entries = new ArrayList<String>();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                serviceContainer.dumpServices(new PrintStream(baos));
                BufferedReader reader = new BufferedReader(new StringReader(new String(baos.toByteArray())));
                try {
                    String line = reader.readLine();
                    while (line != null) {
                        if (pattern == null || line.contains(pattern))
                            entries.add(line);
                        line = reader.readLine();
                    }
                } catch (IOException ex) {
                    log.errorf(ex, "Cannot process service list");
                }
                Collections.sort(entries);
                return entries;
            }
        };
        try {
            MBeanServer mbeanServer = injectedMBeanServer.getValue();
            mbeanServer.registerMBean(new StandardMBean(mbean, ManagedServiceContainer.class), OBJECT_NAME);
        } catch (Exception ex) {
            new StartException("Cannot register: " + OBJECT_NAME, ex);
        }
    }

    /** {@inheritDoc} */
    public synchronized void stop(final StopContext context) {
        try {
            MBeanServer mbeanServer = injectedMBeanServer.getValue();
            mbeanServer.unregisterMBean(OBJECT_NAME);
        } catch (Exception ex) {
            log.errorf(ex, "Cannot unregister: " + OBJECT_NAME);
        }
    }

    /** {@inheritDoc} */
    public synchronized Void getValue() throws IllegalStateException {
        return null;
    }

    public interface ManagedServiceContainer {
        List<String> listServices();

        List<String> listServicesByMode(String mode);

        List<String> listServicesByState(String state);

        void setMode(String serviceName, String mode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9966.java