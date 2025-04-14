error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2532.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2532.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2532.java
text:
```scala
m@@beanTracker.open(true);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.jmx.whiteboard;

import javax.management.MBeanServer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private JmxWhiteboardSupport jmxWhiteBoard;

    private ServiceTracker mbeanServerTracker;

    private ServiceTracker mbeanTracker;

    public void start(BundleContext context) throws Exception {
        jmxWhiteBoard = new JmxWhiteboardSupport();

        mbeanServerTracker = new MBeanServerTracker(context);
        mbeanServerTracker.open();

        mbeanTracker = new MBeanTracker(context);
        mbeanTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        if (mbeanTracker != null) {
            mbeanTracker.close();
            mbeanTracker = null;
        }

        if (mbeanServerTracker != null) {
            mbeanServerTracker.close();
            mbeanServerTracker = null;
        }

        jmxWhiteBoard = null;
    }

    private class MBeanServerTracker extends ServiceTracker {

        public MBeanServerTracker(BundleContext context) {
            super(context, MBeanServer.class.getName(), null);
        }

        @Override
        public Object addingService(ServiceReference reference) {
            MBeanServer mbeanServer = (MBeanServer) super.addingService(reference);
            jmxWhiteBoard.addMBeanServer(mbeanServer);
            return mbeanServer;
        }

        @Override
        public void removedService(ServiceReference reference, Object service) {
            if (service instanceof MBeanServer) {
                jmxWhiteBoard.removeMBeanServer((MBeanServer) service);
            }
            super.removedService(reference, service);
        }
    }

    private class MBeanTracker extends ServiceTracker {

        /**
         * Listens for any services registered with an interface whose name ends
         * with "MBean". This matches all simple MBeans which have to implement
         * an interface named after the class with a suffix of MBean. It also
         * matches DynamicMBeans and all its extensions like open MBeans, model
         * MBeans and StandardMBeans.
         */
        private static final String SIMPLE_MBEAN_FILTER = "("
            + Constants.OBJECTCLASS + "=*MBean)";

        public MBeanTracker(BundleContext context)
                throws InvalidSyntaxException {
            super(context, context.createFilter(SIMPLE_MBEAN_FILTER), null);
        }

        @Override
        public Object addingService(ServiceReference reference) {
            Object mbean = super.addingService(reference);
            jmxWhiteBoard.registerMBean(mbean, reference);
            return mbean;
        }

        @Override
        public void removedService(ServiceReference reference, Object service) {
            jmxWhiteBoard.unregisterMBean(service);
            super.removedService(reference, service);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2532.java